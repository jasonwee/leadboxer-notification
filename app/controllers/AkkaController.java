/*
 * Copyright (c) 2014 - 2017, LeadBoxer and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  LeadBoxer designates this
 * particular file as subject to the "Classpath" exception as provided
 * by LeadBoxer in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact LeadBoxer, Herengracht 182 Amsterdam, Noord-Holland 1016 BR
 * Netherlands or visit www.leadboxer.com if you need additional information or
 * have any questions.
 */

package controllers;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

import actors.HelloActor;
import actors.HelloActorProtocol.SayHello;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.pattern.Patterns;
import play.libs.concurrent.HttpExecution;
import play.mvc.Controller;
import play.mvc.Result;
import scala.compat.java8.FutureConverters;

/**
 * This is akka controller, we should try to implement for notification hit
 * @author jason
 *
 */
@Singleton
public class AkkaController extends Controller {

      final ActorRef helloActor;

      // this is akka
      @Inject
      public AkkaController(ActorSystem system) {
         helloActor = system.actorOf(HelloActor.props);
      }

      // this is akka
      // this is non block action, means the client will be block but not this server.
      public CompletionStage<Result> sayHello(String name) {
         return FutureConverters.toJava(Patterns.ask(helloActor, new SayHello(name), 1000))
               .thenApply(response -> ok((String) response));
      }

      // ------------------------------------------------------------------------------------------------------------------------



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
         } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
         return new Integer(1);
      }

}
