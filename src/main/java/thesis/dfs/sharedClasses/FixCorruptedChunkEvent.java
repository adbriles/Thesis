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
		System.out.println("Go to this chunk to find a replacement: " + EventFactory.getInstance().hostToFiles.findBackupChunk(message));
	}

}
