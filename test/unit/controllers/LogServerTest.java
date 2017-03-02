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
