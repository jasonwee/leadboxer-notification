package unit.models;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

import models.NotificationData;

import static play.test.Helpers.*;

import java.io.IOException;

import static org.junit.Assert.*;

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
