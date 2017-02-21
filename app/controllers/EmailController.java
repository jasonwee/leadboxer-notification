package controllers;

import java.util.List;

import javax.inject.Inject;

import play.api.libs.mailer.MailerClient;
import play.libs.mailer.Email;
import play.mvc.Controller;

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
