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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import models.NotificationSpecification;
import play.Configuration;
import play.Logger;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * communication between logserver and notification server on retrieving and pushing notification specification
 *
 * @author jason
 *
 */
public class LogServer extends Controller {

   @Inject private WSClient ws;

   @Inject private Configuration config;

   // for logserver to get all the notification
   public Result listAll() {
      List<NotificationSpecification> notificationSpecifications = NotificationSpecification.findAll();
      notificationSpecifications.forEach((ns) -> Logger.info("notificationSpecifications={}", ns));
      return ok(getDatasetsFromDS());
   }

   public JsonNode getDatasetsFromDS() {
      // select datasetIds
      List<NotificationSpecification> datasetIds = NotificationSpecification.distinctDatasets();

      boolean enable = false;

      if (datasetIds == null | datasetIds.isEmpty()) {
         // return empty string
         return Json.newObject();
      }

      ObjectMapper om = new ObjectMapper();
      ObjectNode root = Json.newObject();
      ArrayNode datasets = root.putArray("datasets");

      // going through all datasets id one by one
      for (NotificationSpecification datasetId : datasetIds) {

         String dId = datasetId.getDatasetId();

         Logger.info("datasetId {}", dId);

         // get single dataset id
         List<NotificationSpecification> nss = NotificationSpecification.byDataset(dId);

         List<String> companies = new ArrayList<>();
         List<String> industries = new ArrayList<>();
         List<String> urls = new ArrayList<>();

         for (NotificationSpecification ns : nss) {
            switch (ns.getnKey()) {
            case "most_likely_company":
               companies.add(ns.getnValue());
               break;
            case "li_industry":
               industries.add(ns.getnValue());
               break;
            case "original_url":
               urls.add(ns.getnValue());
               break;
            }
         }

         // TODO we should really put this into a form where user can
         // specify.
         if (companies.size() > 0 || industries.size() > 0 || urls.size() > 0) {
            enable = true;
         }

         ObjectNode dataset = om.createObjectNode();
         dataset.put("datasetId", dId);
         dataset.put("enable", enable);
         ObjectNode notifications = dataset.putObject("notifications");

         if (companies.size() == 1) {
            notifications.put("most_likely_company", companies.get(0));
         } else if (companies.size() > 1) {
            ArrayNode an = notifications.putArray("most_likely_company");
            companies.forEach((c) -> an.add(c));
         }

         if (industries.size() == 1) {
            notifications.put("li_industry", industries.get(0));
         } else if (industries.size() > 1) {
            ArrayNode an = notifications.putArray("li_industry");
            industries.forEach(i -> an.add(i));
         }

         if (urls.size() == 1) {
            notifications.put("original_url", urls.get(0));
         } else if (urls.size() > 1) {
            ArrayNode an = notifications.putArray("original_url");
            urls.forEach(u -> an.add(u));
         }

         datasets.add(dataset);
      }

      try {
         ObjectMapper mapper = new ObjectMapper();
         mapper.enable(SerializationFeature.INDENT_OUTPUT);
         String ident = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(root);
         Logger.info(ident);
      } catch (JsonProcessingException e) {
         Logger.error("", e);
      }

      return root;
   }

   /**
    * For notification server to push to logserver
    * 
    * http://stackoverflow.com/questions/38387780/extract-results-from-wsresponse-playws-java
    * http://stackoverflow.com/questions/38428551/java-lang-illegalstateexception-closed-by-using-wsrequest-play-java?noredirect=1#comment64263909_38428551
    * 
    * @param datasetId
    */
    public void updateDataset(String datasetId) {
       List<String> urls = config.getStringList("leadboxer.logserver.notification_urls");

       urls.forEach(url -> {
          Logger.info("pushing {} to url {}", datasetId, url);
          WSRequest request = ws.url(url).setRequestTimeout(5000).setContentType("application/json");

          JsonNode body = getDatasetFromDS(datasetId);

          CompletionStage<WSResponse> responsePromise = request.post(body);

          try {
             WSResponse response = responsePromise.toCompletableFuture().get();
             Logger.info("response from log server {}", response.getBody());
          } catch (InterruptedException | ExecutionException e) {
             Logger.error("unable to push to logserver", e);
          }
       });

    }
   
   /**
    *  read from database for a given datasetId and format the output example of the following into JsonNode format
    *    {
    *     "dataset": {
    *       "datasetId": "a1d90dccc04df83f26553dc753ed41f2",
    *       "enable": false,
    *          "notifications": {
    *            "most_likely_company": [
    *              "google",
    *              "leadboxer"
    *            ],
    *            "li_industry": [
    *                "Defense & Space",
    *              "foo"
    *            ]
    *          }
    *        }
    *      }
    *   
    * @param datasetId
    * @return
    */
   public JsonNode getDatasetFromDS(String datasetId) {
      // no need meddle with jdbc, just use orm
      /*
      try (Connection con = DB.getConnection();) {
         try (PreparedStatement pStatment = con.prepareStatement("SELECT * FROM notification_specification where dataset_id = ?");) {
            pStatment.setString(1, dataset);
            try (ResultSet result = pStatment.executeQuery();) {
               result.getString("id");
               result.getString("nKey");
               result.getString("nKey");
            }
         }
         //results.forEach((ns) -> Logger.info("list from db {}", ns));
         
      } catch (SQLException e) {
         e.printStackTrace();
      }
      */
      List<NotificationSpecification> nss = NotificationSpecification.byDataset(datasetId);
      
      boolean enable = false;

      if (nss == null | nss.isEmpty()) {
         enable = false;
      }
      
      List<String> companies = new ArrayList<>();
      List<String> industries = new ArrayList<>();
      List<String> urls = new ArrayList<>();
      
      for (NotificationSpecification ns : nss) {
         switch (ns.getnKey()) {
         case "most_likely_company":
            companies.add(ns.getnValue());
            break;
         case "li_industry":
            industries.add(ns.getnValue());
            break;
         case "original_url":
            urls.add(ns.getnValue());
            break;
         }
      }
      
      // TODO we should really put this into a form where user can specify.
      if (companies.size() > 0 || industries.size() > 0 || urls.size() > 0) {
         enable = true;
      }
      
      ObjectNode root = Json.newObject();
      ObjectNode aDataset = root.putObject("dataset")
            .put("datasetId", datasetId)
            .put("enable", enable);
      ObjectNode notifications = aDataset.putObject("notifications");
      
      if (companies.size() == 1) {
         notifications.put("most_likely_company", companies.get(0));
      } else if (companies.size() > 1) {
         ArrayNode an = notifications.putArray("most_likely_company");
         companies.forEach((c) -> an.add(c));
      }
      
      if (industries.size() == 1) {
         notifications.put("li_industry", industries.get(0));
      } else if (industries.size() > 1) {
         ArrayNode an = notifications.putArray("li_industry");
         industries.forEach( i -> an.add(i));
      }
      
      if (urls.size() == 1) {
         notifications.put("original_url", urls.get(0));
      } else if (urls.size() > 1) {
         ArrayNode an = notifications.putArray("original_url");
         urls.forEach(u -> an.add(u));
      }
      
      if (Logger.isDebugEnabled()) {
         ObjectMapper mapper = new ObjectMapper();
         mapper.enable(SerializationFeature.INDENT_OUTPUT);
         try {
            String ident = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(root);
            Logger.debug("jsonnode {}", ident);
         } catch (JsonProcessingException e) {
            Logger.error("", e);
         }
      }

      return root;
   }
   
   // TODO maybe do a form for writing logservers hostname?
   // log01.leadboxer.com, log02.leadboxer.com
    

}
