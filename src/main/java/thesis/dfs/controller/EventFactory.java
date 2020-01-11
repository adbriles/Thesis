package thesis.dfs.controller;

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
}
