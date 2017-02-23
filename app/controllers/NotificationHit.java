package controllers;

import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import actors.HelloActor;
import actors.HelloActorProtocol.SayHello;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.pattern.Patterns;
import models.NotificationSpecification;
import play.Logger;
import play.libs.Json;
import play.libs.concurrent.HttpExecution;
import play.mvc.Controller;
import play.mvc.Result;
import scala.compat.java8.FutureConverters;

@Singleton
public class NotificationHit extends Controller {

   final ActorRef helloActor;

   @Inject
   private EmailController emailer;


   // this is akka
   @Inject
   public NotificationHit(ActorSystem system) {
      helloActor = system.actorOf(HelloActor.props);
   }

   // this is akka
   // this is non block action, means the client will be block but not this server.
   public CompletionStage<Result> sayHello(String name) {
      return FutureConverters.toJava(Patterns.ask(helloActor, new SayHello(name), 1000))
            .thenApply(response -> ok((String) response));
   }

   // this is akka or http async
   // this is non block action, means the client will be block but not this server.
   public CompletionStage<Result> index(String name) {
      CompletableFuture f = CompletableFuture.supplyAsync(() -> longComputation(name));

      // how to cancel
      //f.cancel(true);

      //https://dzone.com/articles/implementing-java-8-0
      // http://www.deadcoderising.com/java8-writing-asynchronous-code-with-completablefuture/
      // should take a look
      f.exceptionally(ex -> new Result(400));

      return CompletableFuture.supplyAsync(() -> longComputation(name)).thenApply((Integer i ) -> ok ("Got " +i ));
   }

   public CompletionStage<Result> hit() {
      JsonNode hitJson = request().body().asJson();

      CompletionStage<Result> response = CompletableFuture.supplyAsync(() -> processNotificationSpecificationHit(hitJson)).thenApply((result) -> ok(Json.toJson(result)));

      return response;
   }

   /**
    * 23.Feb 7:02:32,336 INFO  HTTPRequest [post]: sending nd {"dbTimestamp":"","hitTimestamp":"2017-02-23 07:02:32",
     * "datasetId":"e2d1c24722e8a52390a42be6e89f7a65","notificationServer":null,"UUID":"",
     *   "logServer":"gl04.opentracker.net","value":"Bae Systems","key":"most_likely_company"}
     * 23.Feb 7:02:32,418 INFO  HTTPRequest [post]: result -> {"status":"ok"}
     *
     * curl -XPOST -H "Content-Type: application/json" --data @notification-hit.json 'http://localhost:9001/ns/hit/'
     *
    * @param hitJson
    * @return
    */
   public ObjectNode processNotificationSpecificationHit(JsonNode hitJson) {
      ObjectNode result = Json.newObject();

      Logger.info("bingo {}", hitJson.toString());

      String datasetId = hitJson.findPath("datasetId").asText();
      String key = hitJson.findPath("key").asText();
      String value = hitJson.findPath("value").asText();

      // check in ns if the object still available, if not available, log it and return
      // datasetid, key, value
      NotificationSpecification nsHit = NotificationSpecification.getNotificationSpecification(datasetId, key, value);

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
         emailer.sendEmail(Arrays.asList(emails), "send");
         NotificationSpecification savedNS = NotificationSpecification.find.ref(nsHit.getId());
         savedNS.setLastSend(new Date());
         savedNS.update();
         result.put("status", "email sent");
      } else { // recurrrent
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
            emailer.sendEmail(Arrays.asList(emails), "send recurrent");
            NotificationSpecification savedNS = NotificationSpecification.find.ref(nsHit.getId());
            savedNS.setLastSend(new Date());
            savedNS.update();
            result.put("status", "email sent");
         } else { // when the time now is less than or eq the last send + the condition
            result.put("status", "email not sent");
         }
      }


      // return a valid json response.

      return result;
   }

   // ------------------------------------------------------------------------------------------------------------------------

   private Executor myThreadPool = null;

   // this is http async becuase from httpexecution context, it supply an executor.
   public CompletionStage<Result> index2(String name) {
      // Wrap an existing thread pool, using the context from the current thread
      Executor myEc = HttpExecution.fromThread(myThreadPool);
      return CompletableFuture.supplyAsync(() -> longComputation(name), myEc)
            .thenApplyAsync(i -> ok("Got result: " + i), myThreadPool);
   }

   // timeout sample
   class MyClass implements play.libs.concurrent.Timeout {
       CompletionStage<Double> callWithOneSecondTimeout() {
           return timeout(computePIAsynchronously(), Duration.ofSeconds(1));
       }
   }

    private static CompletionStage<Double> computePIAsynchronously() {
       return CompletableFuture.completedFuture(Math.PI);
   }


   Integer longComputation(String name) {
      try {
         Thread.sleep(5000);
         //http://api.leadboxer.com/api/management/sendEmail.jsp?to=jason@opentracker.net&from=noreply@leadboxer.com&toName=Jason&replyTo=noreply@leadboxer.com&subject=test from notification server&text=this is a test
      } catch (InterruptedException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      return new Integer(1);
   }



}
