package controllers;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.avaje.ebean.enhance.agent.SysoutMessageOutput;

import actors.HelloActor;
import actors.HelloActorProtocol.SayHello;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.pattern.Patterns;
import play.libs.concurrent.HttpExecution;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import scala.compat.java8.FutureConverters;

@Singleton
public class NotificationHit extends Controller {
	
	final ActorRef helloActor;
	
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
