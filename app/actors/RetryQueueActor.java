package actors;

import akka.actor.UntypedActor;

public class RetryQueueActor extends UntypedActor {
   
   private static boolean inProgress = false;

   @Override
   public void onReceive(Object message) throws Throwable {
      if (message instanceof String) {
         if (message != null && message.equals("start-retry-queue")) {
            if (!inProgress) {
               System.out.println("start the job");
               startRetry();
            } else {
               System.out.println("still running " + inProgress);
            }
         }
      }
   }
   
   private void startRetry() {
      try {
         System.out.println("running " + System.currentTimeMillis());
         inProgress = true;
         
         Thread.sleep(20000);
         
         inProgress = false;
      } catch (Exception e) {
         inProgress = false;
      }
   }
}
