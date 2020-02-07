package thesis.dfs.sharedClasses;

import thesis.dfs.messages.Message;

public class HandleMinorHeartBeat implements Runnable{

	private Message message;
	
	public HandleMinorHeartBeat(Message message) {
		this.message = message;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Handling a heartbeat");
		
		for(String s: message.getList()) {
			EventFactory.hostToFiles.addChunk(s, s.split("_")[0], (message.getSenderHostName() + " " + message.getSenderPort()));
		}
		for(String s: message.getSecondList()) {
			System.out.println("Corruped chunk: " + s);
		}
		
		
		
		for(String s: message.getList()) {
			System.out.println("Adding" + s);
		}
		
	}

	
}
