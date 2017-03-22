package controllers;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import com.avaje.ebean.PagedList;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import models.NotificationSpecification;
import models.RetryQueue;
import play.Logger;
import play.inject.ApplicationLifecycle;
import play.mvc.Controller;
import scala.concurrent.duration.Duration;

@Singleton
public class RetryQueueController extends Controller {
   
   public static boolean stop = false;

   
   @Inject
   public RetryQueueController(ActorSystem system, @Named("retry-queue-actor") ActorRef retryQueueActor, ApplicationLifecycle lifecycle) {
      /*
      system.scheduler().schedule(
            Duration.create(0, TimeUnit.SECONDS), 
            Duration.create(5, TimeUnit.SECONDS),
            retryQueueActor,
            "start-retry-queue",
            system.dispatcher(),
            null);
      */
      lifecycle.addStopHook(() -> {
         stop = true;
         return CompletableFuture.completedFuture(null);
      });
      system.scheduler().scheduleOnce(Duration.create(0, TimeUnit.SECONDS), new CustomRetry(), system.dispatcher());
   }
   
   static class CustomRetry implements Runnable {

      boolean inProgress = false;

      @Override
      public void run() {
         while (!stop) {
            if (!inProgress) {
               runNow();
            }
            try {
               Thread.sleep(5000);
            } catch (InterruptedException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
            }
         }
      }
      
      private void runNow() {
         System.out.println("running " + System.currentTimeMillis());
         
         PagedList<RetryQueue> retries = RetryQueue.page(0, 3, "id", "asc", "");
         retries.getList().forEach((q) -> { 
            System.out.println(q.id);
            NotificationSpecification ns = NotificationSpecification.find.byId(q.notificationId);
            if (ns == null) {
               Logger.warn("RetryQueue id {} reference to notification spec id {} not found, removing", q.id, q.notificationId);
               RetryQueue.find.ref(q.id).delete();
            }
         });
         
         int page = 0;
         while (retries.hasNext()) {
            page += 3;
            retries = RetryQueue.page(page, 3, "id", "asc", "");
            retries.getList().forEach((q) -> { 
               System.out.println(q.id);
               NotificationSpecification ns = NotificationSpecification.find.byId(q.notificationId);
               if (ns == null) {
                  Logger.warn("RetryQueue id {} reference to notification spec id {} not found, removing", q.id, q.notificationId);
                  RetryQueue.find.ref(q.id).delete();
               }
            });
            
            
         }
         
      }
      
   }

}
