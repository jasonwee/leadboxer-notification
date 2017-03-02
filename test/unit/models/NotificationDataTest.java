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

package unit.models;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

import models.NotificationData;

import static play.test.Helpers.*;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 *
 * @author jason
 *
 */
public class NotificationDataTest {

   @Test
   public void testToNotificationData() {
      try {
         String nsString = "{ \"dbTimestamp\":\"\", \"hitTimestamp\":\"2017-02-23 07:02:32\", \"datasetId\":\"e2d1c24722e8a52390a42be6e89f7a65\", \"notificationServer\":null, \"UUID\":\"\", \"logServer\":\"gl04.opentracker.net\", \"value\":\"Bae Systems\", \"key\":\"most_likely_company\" }";

         NotificationData nd = NotificationData.toNotificationData(nsString);

         assertEquals("2017-02-23 07:02:32", nd.hitTimestamp);
         assertEquals("{\"dbTimestamp\":\"\",\"hitTimestamp\":\"2017-02-23 07:02:32\",\"datasetId\":\"e2d1c24722e8a52390a42be6e89f7a65\",\"notificationServer\":null,\"UUID\":\"\",\"logServer\":\"gl04.opentracker.net\",\"value\":\"Bae Systems\",\"key\":\"most_likely_company\"}", nd.toJsonString());
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   @Test
   public void testToJsonString() {
      try {
         NotificationData nd = new NotificationData();
         assertEquals("{\"dbTimestamp\":null,\"hitTimestamp\":null,\"datasetId\":null,\"notificationServer\":null,\"UUID\":null,\"logServer\":null,\"value\":null,\"key\":null}", nd.toJsonString());
      } catch (JsonProcessingException e) {
         e.printStackTrace();
      }
   }

}
