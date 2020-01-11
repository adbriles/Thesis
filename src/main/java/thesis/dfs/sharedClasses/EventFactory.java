package thesis.dfs.sharedClasses;

import thesis.dfs.messages.Message;

public class EventFactory {
	private static EventFactory instance;
	
	private EventFactory() {
		
	}
	
	//Thread safe singleton get instance
	//synchronize only the block of code creating 
	//an instance for improved performance.
	//Initial startup is still slow while threads wait
	//for one to create an instance. 
	public static EventFactory getInstance() {		
		if (instance == null) {
			synchronized (EventFactory.class) {
				if(instance == null) {
					instance = new EventFactory();
				}
			}
		}
		return instance;
	}
	
	//Return a runnable event that a reveiver thread will toss on a thread pool.
	public static Runnable createEvent(Message message) {
		//Event event = new Event();
		//System.out.println(message.getMessageType());
		if(message.getMessageType().equals("PutRequest")) {
			System.out.println("putrequestevent being make");
			return new PutRequestEvent(message); 
		} else if(message.getMessageType().equals("ResponseFromController")) {
			System.out.println("A whole exchange took place");
		}
		
		return null;		
	}
}
