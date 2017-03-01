package acceptance;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import static play.test.Helpers.*;

public class AcceptanceTest {

    /**
     * in this example we just check if the welcome page is being shown
     */
    @Test
    public void test() {
        running(testServer(9002, fakeApplication(inMemoryDatabase())), HTMLUNIT, browser -> {
            browser.goTo("http://localhost:9002");
            System.out.println(browser.pageSource());
            assertThat(browser.pageSource(), containsString("Notification"));
        });
    }
}
