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

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import models.NotificationData;
import play.api.libs.mailer.MailerClient;
import play.libs.mailer.Email;
import play.mvc.Controller;



/**
 * A email controller allow you to inject and send email from another service. For example
 *
 * <pre>
 * {@code
 * @Inject
 * private EmailController emailer;
 *
 * emailer.sendEmail(recipients, message);
 * }
 * </pre>
 *
 * @author jason
 *
 */
public class EmailController extends Controller {
	
	@Inject MailerClient mailerClient;
	
	public String sendEmail(List<String> recipients, NotificationData nd, Map<String, String> extra) {
		Email email = new Email();
		email.setSubject(String.format(String.format("LeadBoxer Notification Hit - %s - %s", nd.key, nd.value)));
		email.setFrom("<noreply@leadboxer.com>");
		recipients.forEach((e) -> email.addTo(e));
		String message = String.format("%s Hit%n%s : %s%n%nHit Timestamp : %s%nLeadcard URL : https://product.leadboxer.com/leadcard/index.jsp?dataset_id=%s&use_id=%s", extra.getOrDefault("isInitial", "Recurrent"), nd.key, nd.value, nd.hitTimestamp, nd.datasetId, extra.get("use_id"));
		email.setBodyText(message);
		String id = mailerClient.send(email);
		return id;

		/*
		 *  Initial/Recurrent Hit
		 *  nd.key : nd.value
		 *
		 *  hit timestamp : nd.hitTimestamp
		 *  leadcard url : https://product.leadboxer.com/leadcard/index.jsp?dataset_id=0909f23883cccc11c97aac54408b364b&use_id=extra.get("use_id")
		 */
	}

}
