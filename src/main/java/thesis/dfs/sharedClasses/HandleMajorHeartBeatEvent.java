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
		}
		
	}

}
