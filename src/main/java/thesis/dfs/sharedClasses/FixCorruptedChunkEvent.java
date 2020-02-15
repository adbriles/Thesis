package thesis.dfs.sharedClasses;

import thesis.dfs.messages.Message;

public class FixCorruptedChunkEvent implements Runnable {

	private Message message;
	
	public FixCorruptedChunkEvent(Message message) {
		this.message = message;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		String goodChunkLocation = EventFactory.getInstance().hostToFiles.findBackupChunk(message);
		
		System.out.println("Requesting this chunk server to forward a good chunk: " + goodChunkLocation);
		
		Message fixCorruptionMessage = new Message("FixCorruption");
		fixCorruptionMessage.setContent(message.getContent() + goodChunkLocation);
	}

}
