package controllers;

import java.util.List;

import javax.inject.Inject;

import models.Computer;
import models.NotificationSpecification;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;

import static play.libs.Json.toJson;

public class Notification extends Controller {
	
	FormFactory formFactory;
	
	@Inject
	public Notification(FormFactory formFactory) {
		this.formFactory = formFactory;
	}
	
	// create, show the form
	public Result create() {
		Form<NotificationSpecification> nsForm = formFactory.form(NotificationSpecification.class);
		return ok(views.html.nsCreateForm.render(nsForm));
	}
	
	// create, save the form
	public Result save() {
		Form<NotificationSpecification> nsForm = formFactory.form(NotificationSpecification.class).bindFromRequest();
		if (nsForm.hasErrors()) {
			return badRequest(views.html.nsCreateForm.render(nsForm));
		}
		nsForm.get().save();
		flash("success", "Notification Specification " + nsForm.get().nKey + " has been created");
		return ok(views.html.nsCreateForm.render(nsForm));
	}
	
	
	// list all notification specification
	// r
	public Result list() {
		return ok("list content");
	}
	
	// r
	public Result listAll() {
		List<NotificationSpecification> notificationSpecifications = NotificationSpecification.findAll();
		return ok(toJson(notificationSpecifications));
	}
	
	// u
	
	
	// d

}
