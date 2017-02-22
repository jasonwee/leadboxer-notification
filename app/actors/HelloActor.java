package actors;

import actors.HelloActorProtocol.SayHello;
import actors.HelloActorProtocol.SendEmail;

import akka.actor.Props;
import akka.actor.UntypedActor;

public class HelloActor extends UntypedActor {
	
	public static Props props = Props.create(HelloActor.class);

	@Override
	public void onReceive(Object msg) throws Throwable {
		
		if (msg instanceof SayHello) {
			sender().tell("Hello, " + ((SayHello) msg).name, self());
		} else if (msg instanceof SendEmail) {
			sender().tell("done " + ((SendEmail)msg).recipients, self());
		}
	}
	
}
