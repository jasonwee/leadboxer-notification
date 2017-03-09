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

import static play.test.Helpers.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.avaje.ebean.PagedList;
import com.google.common.collect.ImmutableMap;

import models.NotificationSpecification;
import play.Application;
import play.Environment;
import play.Mode;
import play.db.Database;
import play.db.Databases;
import play.inject.guice.GuiceApplicationBuilder;
import play.test.Helpers;
import play.test.WithApplication;


/**
 *
 * @author jason
 *
 */
public class NotificationSpecificationTest extends WithApplication {

    @Test
     public void testMock() {

      // Create and train mock
      List<String> mockedList = mock(List.class);
      when(mockedList.get(0)).thenReturn("first");

      // check value
      assertEquals("first", mockedList.get(0));

      // verify interaction
      verify(mockedList).get(0);
     }

     public void testMock1() {

      // Create and train mock
      List<NotificationSpecification> nss = new ArrayList<>();
      NotificationSpecification mockedList = mock(NotificationSpecification.class);
      when(mockedList.getNotificationSpecification("1", "2", "3")).thenReturn(nss);


      List<NotificationSpecification> mytest = mockedList.getNotificationSpecification("1", "2", "3");
      System.out.println(mytest.toString());

     }



    @Test
    public void testIsAdmin() {
       /*
      // Create and train mock repository
      NotificationSpecification repositoryMock = mock(NotificationSpecification.class);
      Set<Role> roles = new HashSet<Role>();
      roles.add(new Role("ADMIN"));
      when(repositoryMock.findUserRoles(any(User.class))).thenReturn(roles);

      // Test Service
      UserService userService = new UserService(repositoryMock);
      User user = new User(1, "Johnny Utah");
      assertTrue(userService.isAdmin(user));
      verify(repositoryMock).findUserRoles(user);
      */
    }


    /*
        @Test
    public void getNotificationSpecification1() {

       Database database = Databases.createFrom(
             "com.mysql.jdbc.Driver",
              "jdbc:mysql://localhost/test"
             );

       Database inMem = Databases.inMemory(
                 "mydatabase",
                 ImmutableMap.of(
                         "MODE", "MYSQL"
                 ),
                 ImmutableMap.of(
                         "logStatements", true
                 )
         );

       database.shutdown();
       */

         /*
         ClassLoader classLoader = classLoader();
         Application application = new GuiceApplicationBuilder()
               //.in(new Environment(new File("/home/jason/work/svn/leadboxer-notification/"), classLoader, Mode.TEST))
               .in(new Environment(new File("path/to/app"), classLoader, Mode.DEV))
               .build();

         running(application, () -> {
            NotificationSpecification ns = NotificationSpecification.getNotificationSpecification("1", "2", "3");
            System.out.println("here " + ns.toString());
         });

    }

       private ClassLoader classLoader() {
           return new URLClassLoader(new URL[0]);
       }
              */

   @Test
   public void testPagination() {
      running(fakeApplication(inMemoryDatabase()), new Runnable() {
         public void run() {
            PagedList<NotificationSpecification> nss = NotificationSpecification.page(1, 20, "nKey", "ASC", "");
            assertEquals(0, nss.getTotalPageCount());
            assertEquals(0, nss.getList().size());
         }
      });
   }

   @Test
   public void testByDatataset() {
      running(fakeApplication(inMemoryDatabase()), () -> {
         List<NotificationSpecification> lists = NotificationSpecification.byDataset("a1d90dccc04df83f26553dc753ed41f2");
         assertEquals(0, lists.size());
      });
   }

   @Test
   public void testDistinctDatasets() {
      running(fakeApplication(inMemoryDatabase()), () -> {
         List<NotificationSpecification> lists = NotificationSpecification.distinctDatasets();
         assertEquals(0, lists.size());
      });
   }

   @Test
   public void testGetNotificationSpecification() {

      running(fakeApplication(inMemoryDatabase()), () -> {
         List<NotificationSpecification> ns = NotificationSpecification.getNotificationSpecification("a1d90dccc04df83f26553dc753ed41f2", "foo", "bar");
         assertEquals(0, ns.size());
      });
   }

    @Test
    public void findById() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
           public void run() {
               NotificationSpecification ns = NotificationSpecification.find.byId(1l);
               assertNull(ns);
           }
        });
    }

}
