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

import javax.inject.Inject;

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
	
	public String sendEmail(List<String> recipients, String message) {
		Email email = new Email();
		email.setSubject("test email");
		email.setFrom("<noreply@leadboxer.com>");
		recipients.forEach((e) -> email.addTo(e));
		email.setBodyText(message);
		String id = mailerClient.send(email);
		return id;
	}
	

}
