package thesis.dfs.sharedClasses;

import thesis.dfs.messages.Message;

public class HandleMajorHeartBeatEvent implements Runnable{
	private Message message;
	
	public HandleMajorHeartBeatEvent(Message message) {
		this.message = message;
	}
	@Override
	public void run() {
		System.out.println("Handling a major heartbeat");
		for(String s: message.getSecondList()) {//Update any newly added chunks.
			EventFactory.hostToFiles.addChunk(s, s.split("_")[0], (message.getSenderHostName() + " " + message.getSenderPort()));
		}//Not sure what the point of the rest is. Everythign is here, but I'm not just dropping chunks
		//I don't see a situation where a chunk just ceases to exist at a chunk server 
		//If chunks did cease to exist, it would be useful to totally rebuild a servers records
		//but since that can't really happen in the current system, a total rebuild seems useless. 
		
	}

}
