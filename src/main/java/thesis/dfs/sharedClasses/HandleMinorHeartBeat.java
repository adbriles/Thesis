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
		//System.out.println("Handling a heartbeat");
		
		for(String s: message.getList()) {
			EventFactory.hostToFiles.addChunk(s, s.split("_")[0], (message.getSenderHostName() + " " + message.getSenderPort()));
		}
		
		EventFactory.hostToFiles.updateChunksSpace((message.getSenderHostName() + " " + message.getSenderPort()), Long.parseLong(message.getContent()));
		
	}

	
}
