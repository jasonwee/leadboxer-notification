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
	
	// c
	public Result create() {
		Form<NotificationSpecification> ndForm = formFactory.form(NotificationSpecification.class);
		return ok(views.html.ndCreateForm.render(ndForm));
	}
	
	// c
	public Result save() {
		Form<NotificationSpecification> ndForm = formFactory.form(NotificationSpecification.class).bindFromRequest();
		if (ndForm.hasErrors()) {
			return badRequest(views.html.ndCreateForm.render(ndForm));
		}
		ndForm.get().save();
		flash("success", "Notification Specification " + ndForm.get().nKey + " has been created");
		return ok();
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
