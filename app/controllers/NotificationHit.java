/*
 * Copyright (c) 2014 - 2017, LeadBoxer and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  LeadBoxer designates this
 * particular file as subject to the "Classpath" exception as provided
 * by LeadBoxer in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact LeadBoxer, Herengracht 182 Amsterdam, Noord-Holland 1016 BR
 * Netherlands or visit www.leadboxer.com if you need additional information or
 * have any questions.
 */

package controllers;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import models.NotificationData;
import models.NotificationSpecification;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * NotificationHit controller managed hits from logserver and process the hit accordingly.
 *
 * @author jason
 *
 */
public class NotificationHit extends Controller {

	@Inject
	private EmailController emailer;

	public CompletionStage<Result> hit() {
		JsonNode hitJson = request().body().asJson();

		CompletionStage<Result> response = CompletableFuture
				.supplyAsync(() -> processNotificationSpecificationHit(hitJson))
				.thenApply((result) -> ok(Json.toJson(result)));

		return response;
	}

   /**
    * TODO
    * * email fail send 1 time, we should put into a queue and let another service to pick up and email out again.
    * * timeout?
    *
     *
     * curl -XPOST -H "Content-Type: application/json" --data @notification-hit.json 'http://localhost:9001/ns/hit/'
     *
    * @param hitJson
    * @return
    */
   public ObjectNode processNotificationSpecificationHit(JsonNode hitJson) {
      ObjectNode result = Json.newObject();
      NotificationData nd = new NotificationData();

      Logger.info("notification hit receive {}", hitJson.toString());

      try {
         nd = NotificationData.toNotificationData(hitJson.toString());
      } catch (IOException e) {
         Logger.error("unexpected json");
         return result.put("status", "unexpected json");
      }

      // check in ns if the object still available, if not available, log it and return
      // datasetid, key, value
      NotificationSpecification nsHit = NotificationSpecification.getNotificationSpecification(nd.datasetId, nd.key, nd.value);

      if (nsHit == null) {
         Logger.warn("ns has been removed? stale hit {}", hitJson.toString());
         result.put("status", String.format("ns has been removed? stale hit %s", hitJson.toString()));
         return result;
      }

      // get the email and lastSent from ns.
      String email = nsHit.getEmailRecipients();
      Date lastSend = nsHit.getLastSend();
      String condition = nsHit.getSendCondition();

      // check sending condition. like N minute send one. // if email sent, update ns last sent
      Logger.info("email {} lastSend {} condition {}", email, lastSend, condition);
      if (lastSend == null) {  // first time
         String emails[] = email.split(",");
         Map<String, String> extra = new HashMap<>();
         extra.put("isInitial", "Initial");
         String emailId = null;
         try {
            emailId = emailer.sendEmail(Arrays.asList(emails), nd, extra);

            // for test
            emailId = null;

            // we check it here and throw it here because we want to catch handle all email cases here, dont want to the same
            // purpose outside of this try catch block.
            if (emailId == null || emailId.isEmpty()) {
               throw new Exception("cannot be empty");
            }
         } catch (Exception e) {
            Logger.error("fail to send initial email", e);
            putIntoRetryQueue(email, nd, extra);
         }
         NotificationSpecification savedNS = NotificationSpecification.find.ref(nsHit.getId());
         savedNS.setLastSend(new Date());
         savedNS.update();
         result.put("status", String.format("email %s sent", emailId));
      } else { // Recurrent
         Date now = new Date();
         String conditions[] = condition.split("=");
         // TODO simple way of getting based on splitting equal sign. it should more robust:
         // * in the form, validate input
         // * check, checks if conditions not null and length > length % 2 == 0 and length > 2
         // * get based on n.
         int duration = Integer.parseInt(conditions[1]);

         long padLastSend = lastSend.getTime() + duration * 1000L;
         if (now.getTime() > padLastSend) { // when the time now is greater than the last send + the condition
            String emails[] = email.split(",");
            Map<String, String> extra = new HashMap<>();
            extra.put("isInitial", "Recurrent");
            String emailId = null;
            try {
               emailId = emailer.sendEmail(Arrays.asList(emails), nd, extra);

               // for test
               emailId = null;

               // we check it here and throw it here because we want to catch handle all email cases here, dont want to the same
               // purpose outside of this try catch block.
               if (emailId == null || emailId.isEmpty()) {
                  throw new Exception("cannot be empty");
               }
            } catch (Exception e) {
                 Logger.error("fail to send recurrent email", e);
                 putIntoRetryQueue(email, nd, extra);
            }
            NotificationSpecification savedNS = NotificationSpecification.find.ref(nsHit.getId());
            savedNS.setLastSend(new Date());
            savedNS.update();
            result.put("status", String.format("email %s sent", emailId));
         } else { // when the time now is less than or eq the last send + the condition
            result.put("status", "email not sent as recurrent condition not satisfy");
         }
      }

      // return a valid json response.
      return result;
   }
   
   private boolean putIntoRetryQueue(String email, NotificationData nd, Map<String, String> extra) {
      Logger.info("putting into queue");
      return true;
   }

}
