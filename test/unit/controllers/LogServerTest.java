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

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableMap;

import static play.mvc.Http.Status.OK;
import static org.junit.Assert.*;
import static play.test.Helpers.*;

import controllers.LogServer;
import play.Application;
import play.mvc.Result;
import play.test.WithApplication;

/**
 *
 * @author jason
 *
 */
public class LogServerTest extends WithApplication {

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

   @Override
   public void startPlay() {
      System.out.println("starting fake application");
      super.startPlay();
   }

   @Override
   public void stopPlay() {
      System.out.println("stopping fake application");
      super.stopPlay();
   }

     @Test
     public void testListAllisEmpty() {
       Result result = new LogServer().listAll();
       assertEquals(OK, result.status());
       assertEquals("application/json", result.contentType().get());
       assertEquals("UTF-8", result.charset().get());
       assertTrue(contentAsString(result).equals("{}"));
     }

     @Test
     public void testGetDatasetsFromDSIsEmpty() {
        JsonNode result = new LogServer().getDatasetsFromDS();
        assertEquals("{}", result.toString());
     }

   @Test
     public void testUpdateDataset() {
      // TODO how to test this?
      //new LogServer().updateDataset("123");
     }

     /**
      * output of the assertion should match the following
      * {
       * "dataset" : {
       *    "datasetId" : "1234",
       *    "enable" : false,
       *    "notifications" : { }
       *  }
       *}
       *
      */
     @Test
     public void testGetDatasetFromDSIsEmpty() {
        JsonNode jsonNode = new LogServer().getDatasetFromDS("1234");
        assertEquals("{\"dataset\":{\"datasetId\":\"1234\",\"enable\":false,\"notifications\":{}}}", jsonNode.toString());
     }

}
