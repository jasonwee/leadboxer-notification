package services;

public class MessageQueue {
	
	public static boolean stopped = false;
	
	
	public static MessageQueue connect() {
		return new MessageQueue();
	}
	
	public void stop() {
		stopped = true;
	}
}
