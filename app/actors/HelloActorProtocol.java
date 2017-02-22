package actors;

import java.util.List;

public class HelloActorProtocol {
	
	public static class SayHello {
		
		public final String name;
		
		public SayHello(String name) {
			this.name = name;
		}
	}
	
	public static class SendEmail {
		
		public final List<String> recipients;
		
		public SendEmail(List<String>recipients) {
			this.recipients = recipients;
		}
	}

}
