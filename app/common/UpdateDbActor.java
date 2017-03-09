package common;

import akka.actor.UntypedActor;

public class UpdateDbActor extends UntypedActor {

	@Override
	public void onReceive(Object message) throws Throwable {
		System.out.println(message);
		sender().tell("hi", self());
	}

}
