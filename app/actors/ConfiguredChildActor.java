package actors;

import javax.inject.Inject;

import com.google.inject.assistedinject.Assisted;

import akka.actor.UntypedActor;
import play.Configuration;

public class ConfiguredChildActor extends UntypedActor {
	
	private final Configuration configuration;
	private final String key;
	
	@Inject
	public ConfiguredChildActor(Configuration configuration, @Assisted String key) {
		this.configuration = configuration;
		this.key = key;
	}

	@Override
	public void onReceive(Object message) throws Throwable {
		if (message instanceof ConfiguredChildActorProtocol.GetConfig) {
			sender().tell(configuration.getString(key), self());
		}

	}

}
