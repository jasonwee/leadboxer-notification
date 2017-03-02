package unit.controllers;

import play.test.WithApplication;

import static play.test.Helpers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import controllers.NotificationHit;
import play.Application;

public class NotificationHitTest extends WithApplication {
	
	@Override
	protected Application provideApplication() {
		// we override default db.default parameter because we want to test.
		 Map<String, String> config = new HashMap<String, String>();
		 config.put("db.default.driver", "org.h2.Driver");
		 config.put("db.default.url", "jdbc:h2:mem:play;MODE=MYSQL");
		 config.put("db.default.username", "sa");
		 config.put("db.default.password", "");
        return fakeApplication(config);
	}
	
	// TODO how to test
	@Test
	public void testHit() {
		
	}

}
