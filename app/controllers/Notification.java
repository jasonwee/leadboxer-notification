package controllers;

import java.util.List;

import models.NotificationSpecification;
import play.mvc.Controller;
import play.mvc.Result;

import static play.libs.Json.toJson;

public class Notification extends Controller {
	
	// list all notification specification
	public Result list() {
		return ok("list content");
	}
	
	public Result listAll() {
		List<NotificationSpecification> notificationSpecifications = NotificationSpecification.findAll();
		return ok(toJson(notificationSpecifications));
	}

}
