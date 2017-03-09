package services;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import scala.concurrent.duration.Duration;

/**
 * http://stackoverflow.com/questions/38129137/scheduling-asynchronus-tasks-in-playframework-2-5-x-java
 * 
 * @author jason
 *
 */
@Singleton
public class SchedulingTask {
	
	@Inject
	public SchedulingTask(final ActorSystem system, @Named("update-db-actor") ActorRef updateDbActor) {
		system.scheduler().schedule(
				Duration.create(0, TimeUnit.MILLISECONDS), 
				Duration.create(5, TimeUnit.SECONDS), 
				updateDbActor, 
				"update", 
				system.dispatcher(), 
				null
			);
	}

}
