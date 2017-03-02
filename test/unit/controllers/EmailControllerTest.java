package unit.controllers;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;

import controllers.EmailController;
import models.NotificationData;
import play.Application;
import play.test.WithApplication;

import static play.test.Helpers.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class EmailControllerTest extends WithApplication {
	
	@Inject EmailController email_agent;
	

	@Override
	protected Application provideApplication() {
        return fakeApplication(ImmutableMap.of(
                "db.default.driver", "org.h2.Driver",
                "db.default.url", "jdbc:h2:mem:play;MODE=MYSQL",
                "db.default.username", "sa",
                "db.default.password", "",
                "play.mailer.mock", "yes"
            ));
	}
	
	// TODO why npe on line 48? 
	//@Test
	public void testSendEmail() {

		List<String> recipients = Arrays.asList("jason@leadboxer.com");
		NotificationData nd = new NotificationData();
		nd.key = "foo";
		nd.value = "bar";
		nd.hitTimestamp = "2017-03-02 17:56:00";
		Map<String, String> extra = new HashMap<>();

		String res = email_agent.sendEmail(recipients, nd, extra);
		System.out.println(res);
	}
	
	@Test
	public void testEmailOutput() {
		boolean isInitial = false;

		String message = String.format("%s Hit%n%s : %s%n%nHit Timestamp : %s", isInitial ? "Initial" : "Recurrent",
				"most_likely_company", "amd", "2017 - 02 - 27 21:16:00");

		System.out.println(message);
	}

}
