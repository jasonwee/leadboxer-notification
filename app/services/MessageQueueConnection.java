package services;

import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;
import javax.inject.Singleton;

import play.Logger;
import play.inject.ApplicationLifecycle;

@Singleton
public class MessageQueueConnection {
	
	private final MessageQueue connection;
	
	/**
	 * sample stop hook
	 * 
	 * @param lifecycle
	 */
	@Inject public MessageQueueConnection(ApplicationLifecycle lifecycle) {
		connection = MessageQueue.connect();
		
		lifecycle.addStopHook(() -> {
			connection.stop();
			Logger.info("stopping MessageQueueConnection");
			return CompletableFuture.completedFuture(null);
			
		});
		// dont work?
		Logger.info("starting MessageQueueConnection");
		
		
	}

}
