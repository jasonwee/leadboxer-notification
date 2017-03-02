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

import play.test.WithApplication;

import static play.test.Helpers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import play.test.*;
import static org.junit.Assert.*;

import com.google.common.collect.ImmutableMap;

import play.Application;
import play.mvc.Result;

/**
 *
 * @author jason
 *
 */
public class NotificationTest extends WithApplication {

   @Override
   protected Application provideApplication() {
      // we override default db.default parameter because we want to test.
        return fakeApplication(ImmutableMap.of(
                "db.default.driver", "org.h2.Driver",
                "db.default.url", "jdbc:h2:mem:play;MODE=MYSQL",
                "db.default.username", "sa",
                "db.default.password", ""
            ));
   }

   @Test
   public void redirectHomePage() {
      Result result = Helpers.route(provideApplication(), controllers.routes.Notification.index());

      assertEquals(Helpers.SEE_OTHER, result.status());
      assertEquals("/ns", result.redirectLocation().get());
   }

   @Test
   public void listNotificationSpecificationOnTheFirstPage() {
      Result result = Helpers.route(provideApplication(), controllers.routes.Notification.list(0, "nKey", "asc", ""));

      assertEquals(Helpers.OK, result.status());
      assertTrue(Helpers.contentAsString(result), Helpers.contentAsString(result).contains("No notification specifications found"));
   }

   /**
    * TODO we can actually insert some sample value and so we can actually test actual filter data.
    *
    */
   @Test
   public void filterNotificationSpecificationByValue() {
      Result result = Helpers.route(provideApplication(), controllers.routes.Notification.list(0, "nValue", "asc", "amd"));

      assertEquals(Helpers.OK, result.status());
      assertTrue(Helpers.contentAsString(result), Helpers.contentAsString(result).contains("No notification specifications found"));
   }

   /* TODO this actually work but we need to change things like configuraiton file use, the lb url and database use. currently
    * using persistent database.
    *
    @Test
    public void createANotificationSpecification() {
        Result result = Helpers.route(provideApplication(), controllers.routes.Notification.save());

      assertEquals(Helpers.BAD_REQUEST, result.status());

        Map<String,String> data = new HashMap<>();
        data.put("datasetId", "a1d90dccc04df83f26553dc753ed41f2");
        data.put("nKey", "most_likely_company");
        data.put("nValue", "google");
        data.put("emailRecipients", "foo@bar.com");
        data.put("nsAdded", "2017-03-01 17:39:00");
        data.put("sendCondition", "n=600");

        String saveUrl = controllers.routes.Notification.save().url();

        //result = Helpers.route(application, Helpers.fakeRequest().bodyForm(data).method("POST").uri(saveUrl));
        // for the bad test
        //assertEquals(Helpers.BAD_REQUEST, result.status());
        //assertThat(Helpers.contentAsString(result), containsString("<option value=\"1\" selected >google</option>"));
        //  <input type="date" id="introduced" name="introduced" value="badbadbad" aria-describedby="introduced_info_0 introduced_error_0" aria-invalid="true" class="form-control">
        //assertThat(Helpers.contentAsString(result), containsString("<input type=\"date\" id=\"introduced\" name=\"introduced\" value=\"badbadbad\" "));
        // <input type="text" id="name" name="name" value="FooBar" aria-describedby="name_info_0" required="true" class="form-control">
        //assertThat(Helpers.contentAsString(result), containsString("<input type=\"text\" id=\"name\" name=\"name\" value=\"FooBar\" "));
        //data.put("nsAdded", "2017-03-01 17:39:00");

        result = Helpers.route(
            provideApplication(),
            Helpers.fakeRequest().bodyForm(data).method("POST").uri(saveUrl)
        );

        assertEquals(Helpers.SEE_OTHER, result.status());
        assertEquals("/ns", result.redirectLocation().get());
        assertEquals("Done! Notification Specification original_url has been created", result.flash().get("success"));

        result = Helpers.route(provideApplication(), controllers.routes.Notification.list(0, "nKey", "asc", "google"));
        assertEquals(Helpers.OK, result.status());
        assertTrue(Helpers.contentAsString(result), Helpers.contentAsString(result).contains("One notification specification found"));
    }
    */
}
