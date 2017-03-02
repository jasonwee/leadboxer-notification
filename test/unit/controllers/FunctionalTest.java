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
