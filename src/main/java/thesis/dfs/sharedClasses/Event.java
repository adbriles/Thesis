package thesis.dfs.sharedClasses;

import thesis.dfs.messages.Message;

public class Event implements Runnable{

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("The superclass ran and something needs to be debugged.");
	}

	public void sendResponse(Message message) {
		
	}
	
}
