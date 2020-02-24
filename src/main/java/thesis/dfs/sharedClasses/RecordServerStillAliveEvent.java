package thesis.dfs.sharedClasses;

import thesis.dfs.messages.Message;

public class RecordServerStillAliveEvent implements Runnable{
	private Message message;
	
	public RecordServerStillAliveEvent(Message message) {
		this.message = message;
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		EventFactory.hostToFiles.resetServerChecks(message.getContent());
	}

}
