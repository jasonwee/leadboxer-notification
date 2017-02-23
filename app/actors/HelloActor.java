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

package actors;

import actors.HelloActorProtocol.SayHello;
import actors.HelloActorProtocol.SendEmail;

import akka.actor.Props;
import akka.actor.UntypedActor;

/**
 * Hello Actor
 *
 * @author jason
 *
 */
public class HelloActor extends UntypedActor {
	
	public static Props props = Props.create(HelloActor.class);

	// we should defined a method here that the actor should perform and give back result

	@Override
	public void onReceive(Object msg) throws Throwable {
		
		if (msg instanceof SayHello) {
			sender().tell("Hello, " + ((SayHello) msg).name, self());
		} else if (msg instanceof SendEmail) {
			sender().tell("done " + ((SendEmail)msg).recipients, self());
		}
	}
	
}
