package controllers;

import static play.libs.Json.toJson;

import java.util.List;

import models.NotificationSpecification;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;

public class LogServer extends Controller {

	// for logserver to get all the notification
	public Result listAll() {
		List<NotificationSpecification> notificationSpecifications = NotificationSpecification.findAll();
		notificationSpecifications.forEach((ns) -> Logger.info("notificationSpecifications={}", ns));
		return ok(toJson(notificationSpecifications));
	}

	// for notificaiton server to push to logserver
	// TODO implement me
	public void updateDataset(String datasetId) {
		Logger.info("test " + datasetId);
	}
	
	// TODO maybe do a form for writing logservers hostname?
	// log01.leadboxer.com, log02.leadboxer.com	
	 

}
