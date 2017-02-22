package common;

import com.google.inject.AbstractModule;

import play.Logger;
import services.MessageQueueConnection;

public class Module extends AbstractModule {

	@Override
	protected void configure() {
		Logger.info("testing start");
		bind(MessageQueueConnection.class).asEagerSingleton();

	}

}
