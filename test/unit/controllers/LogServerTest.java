package unit.controllers;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;

import static play.mvc.Http.Status.OK;
import static org.junit.Assert.*;
import static play.test.Helpers.*;

import controllers.LogServer;
import play.mvc.Result;
import play.test.WithApplication;


public class LogServerTest extends WithApplication {
	
	  /* TODO this actually get data in the local database, we should use test database but we need to know how.
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
	  */
	  
	  @Test
	  public void testUpdateDataset() {
		  // TODO how to test?
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
