package controllers;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.avaje.ebean.PagedList;

import akka.actor.ActorSystem;
import models.NotificationData;
import models.NotificationSpecification;
import models.RetryQueue;
import play.Logger;
import play.inject.ApplicationLifecycle;
import play.mvc.Controller;
import scala.concurrent.duration.Duration;

@Singleton
public class RetryQueueController extends Controller {
   
   public boolean stop = false;
   
   public static int wakeup_duration = 60000;

   @Inject
   private EmailController emailer;
   
   @Inject
   public RetryQueueController(ActorSystem system, ApplicationLifecycle lifecycle) {
   
      lifecycle.addStopHook(() -> {
         stop = true;
         return CompletableFuture.completedFuture(null);
      });
      system.scheduler().scheduleOnce(Duration.create(0, TimeUnit.SECONDS), new DelayRetry(), system.dispatcher());
   }
   
   class DelayRetry implements Runnable {

      boolean inProgress = false;

      @Override
      public void run() {
         while (!stop) {
            if (!inProgress) {
               runNow();
            }
            try {
               Thread.sleep(wakeup_duration);
            } catch (InterruptedException e) {
               Logger.error("interrupted", e);
            }
         }
      }
      
        private void runNow() {
            try {
                inProgress = true;

                int page = 0;
                PagedList<RetryQueue> retries = RetryQueue.page(page, 3, "id", "asc", "");
                do {
                    Logger.info("running => " + System.currentTimeMillis());

                    retries.getList().forEach((RetryQueue q) -> {
                        Logger.info("processing queue id " + q.id);
                        NotificationSpecification nsHit = NotificationSpecification.find.byId(q.notificationId);
                        if (nsHit == null) {
                            Logger.warn("RetryQueue id {} reference to notification spec id {} not found, removing", q.id,
                                    q.notificationId);
                            RetryQueue.find.ref(q.id).delete();
                        }

                        if (q.retry >= q.maxRetry) {
                            Logger.warn("retry {} greater than or equal to maxRetry {}, removing", q.retry, q.maxRetry);
                            RetryQueue.find.ref(q.id).delete();
                        }

                        String email = nsHit.getEmailRecipients();
                        Date lastSend = nsHit.getLastSend();
                        String condition = nsHit.getSendCondition();
                        // check sending condition. like N minute send one. // if
                        // email sent, update ns last sent
                        Logger.info("email {} lastSend {} condition {}", email, lastSend, condition);
                        if (lastSend == null) { // first time
                            String emails[] = email.split(",");
                            NotificationData nd = null;
                            try {
                                nd = NotificationData.toNotificationData(q.getHitJson().toString());
                            } catch (IOException e1) {
                                Logger.error("unexpected json " + q.getHitJson().toString(), e1);
                            }
                            Map<String, String> extra = new HashMap<>();
                            extra.put("isInitial", "Initial");
                            extra.putAll(nd.extras);
                            String emailId = null;
                            try {
                                emailId = emailer.sendEmail(Arrays.asList(emails), nd, extra);

                                // we check it here and throw it here because we
                                // want to catch handle all email cases here, dont
                                // want to the same
                                // purpose outside of this try catch block.
                                if (emailId == null || emailId.isEmpty()) {
                                    throw new Exception("cannot be empty " + emailId);
                                }
                            } catch (Exception e) {
                                Logger.error("fail to retry to send initial email", e);
                                RetryQueue newQueue = RetryQueue.find.byId(q.id);
                                newQueue.retry = q.retry++;
                                newQueue.update();
                            }
                            NotificationSpecification savedNS = NotificationSpecification.find.ref(nsHit.getId());
                            savedNS.setLastSend(new Date());
                            savedNS.update();
                            // result.put("status", String.format("email %s sent",
                            // emailId));
                        } else {
                            Date now = new Date();
                            String conditions[] = condition.split("=");
                            // TODO simple way of getting based on splitting equal
                            // sign. it should more robust:
                            // * in the form, validate input
                            // * check, checks if conditions not null and length >
                            // length % 2 == 0 and length > 2
                            // * get based on n.
                            int duration = Integer.parseInt(conditions[1]);

                            long padLastSend = lastSend.getTime() + duration * 1000L;
                            if (now.getTime() > padLastSend) { // when the time now
                                                                // is greater than
                                                                // the last send +
                                                                // the condition
                                String emails[] = email.split(",");
                                NotificationData nd = null;
                                try {
                                    nd = NotificationData.toNotificationData(q.getHitJson().toString());
                                } catch (IOException e1) {
                                    Logger.error("unexpected json " + q.getHitJson().toString(), e1);
                                }
                                Map<String, String> extra = new HashMap<>();
                                extra.put("isInitial", "Recurrent");
                                extra.putAll(nd.extras);
                                String emailId = null;
                                try {
                                    emailId = emailer.sendEmail(Arrays.asList(emails), nd, extra);

                                    // we check it here and throw it here because we
                                    // want to catch handle all email cases here,
                                    // dont want to the same
                                    // purpose outside of this try catch block.
                                    if (emailId == null || emailId.isEmpty()) {
                                        throw new Exception("cannot be empty " + emailId);
                                    }
                                } catch (Exception e) {
                                    Logger.error("fail to retry to send recurrent email", e);
                                    RetryQueue newQueue = RetryQueue.find.byId(q.id);
                                    newQueue.retry = q.retry++;
                                    newQueue.update();
                                }
                                NotificationSpecification savedNS = NotificationSpecification.find.ref(nsHit.getId());
                                savedNS.setLastSend(new Date());
                                savedNS.update();
                                // result.put("status", String.format("email %s sent", emailId));
                                RetryQueue.find.ref(q.id).delete();
                            } else { // when the time now is less than or eq the last send + the condition
                                // result.put("status", "email not sent as recurrent condition not satisfy");
                                RetryQueue.find.ref(q.id).delete();
                            }
                        }
                    });
                    page += 3;
                } while (retries.hasNext());
            } finally {
                inProgress = false;
            }
        }
   }

}
