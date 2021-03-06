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


package unit.controllers;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;

import controllers.EmailController;
import models.NotificationData;
import play.Application;
import play.test.WithApplication;

import static play.test.Helpers.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 *
 * @author jason
 *
 */
public class EmailControllerTest extends WithApplication {

   @Inject EmailController email_agent;


   @Override
   protected Application provideApplication() {
        return fakeApplication(ImmutableMap.of(
                "db.default.driver", "org.h2.Driver",
                "db.default.url", "jdbc:h2:mem:play;MODE=MYSQL",
                "db.default.username", "sa",
                "db.default.password", "",
                "play.mailer.mock", "yes"
            ));
   }

   // TODO why npe on line 48?
   //@Test
   public void testSendEmail() {

      List<String> recipients = Arrays.asList("jason@leadboxer.com");
      NotificationData nd = new NotificationData();
      nd.key = "foo";
      nd.value = "bar";
      nd.hitTimestamp = "2017-03-02 17:56:00";
      Map<String, String> extra = new HashMap<>();

      String res = email_agent.sendEmail(recipients, nd, extra);
      System.out.println(res);
   }

   @Test
   public void testEmailOutput() {
      boolean isInitial = false;

      String message = String.format("%s Hit%n%s : %s%n%nHit Timestamp : %s", isInitial ? "Initial" : "Recurrent",
            "most_likely_company", "amd", "2017 - 02 - 27 21:16:00");

      System.out.println(message);
   }

}
