package controllers;

import static play.libs.Json.toJson;

import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;

import models.NotificationSpecification;
import play.Logger;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import play.mvc.Result;

public class LogServer extends Controller {

	@Inject WSClient ws;

	// for logserver to get all the notification
	public Result listAll() {
		List<NotificationSpecification> notificationSpecifications = NotificationSpecification.findAll();
		notificationSpecifications.forEach((ns) -> Logger.info("notificationSpecifications={}", ns));
		return ok(toJson(notificationSpecifications));
	}

	// for notificaiton server to push to logserver
	// TODO implement me
	public void updateDataset(String datasetId) {
		Logger.info("pushing to " + datasetId);
		// TODO Change url later
		WSRequest request = ws.url("https://log-dev.leadboxer.com/notification")
				.setRequestTimeout(5000)
				.setContentType("application/json");

		JsonNode body = Json.newObject().put("key1", "value1");

		CompletionStage<WSResponse> responsePromise = request.post(body);
		// http://stackoverflow.com/questions/38387780/extract-results-from-wsresponse-playws-java
		// http://stackoverflow.com/questions/38428551/java-lang-illegalstateexception-closed-by-using-wsrequest-play-java?noredirect=1#comment64263909_38428551
		try {
			WSResponse response = responsePromise.toCompletableFuture().get();
			System.out.println("response from log server" + response.getBody());
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// TODO maybe do a form for writing logservers hostname?
	// log01.leadboxer.com, log02.leadboxer.com	
	 

}
