package actors;

import akka.actor.Actor;

public class ConfiguredChildActorProtocol {
	
	public static class GetConfig {
		
	}
	
	public interface Factory {
		public Actor create(String key);
	}
}
