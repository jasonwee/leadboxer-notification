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

import play.Application;
import play.ApplicationLoader;
import play.Environment;
import play.inject.guice.GuiceApplicationBuilder;
import play.inject.guice.GuiceApplicationLoader;
import play.mvc.Result;
import play.test.*;

import static org.junit.Assert.*;

import javax.inject.Inject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Module;

/**
 *
 * @author jason
 *
 */
public class FunctionalTest {

   /*
   @Inject Application application;

   @Before
   public void setup() {
     Module testModule = new AbstractModule() {
       @Override
       public void configure() {
         // Install custom test binding here
       }
     };

     GuiceApplicationBuilder builder = new GuiceApplicationLoader()
         .builder(new ApplicationLoader.Context(Environment.simple()))
         .overrides(testModule);
     Guice.createInjector(builder.applicationModule()).injectMembers(this);

     Helpers.start(application);
   }

   @After
   public void teardown() {
     Helpers.stop(application);
   }
   */









   /*
   @Test
   public void findById() {
       running(application, () -> {
          NotificationSpecification ns = NotificationSpecification.getNotificationSpecification("", "", "");
          System.out.println("here : " + ns.toString());
       });
   }
   */
}
