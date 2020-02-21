package thesis.dfs.sharedClasses;

import thesis.dfs.messages.Message;

public class GetFileFromChunkServerEvent implements Runnable{

	private Message message;
	
	public GetFileFromChunkServerEvent(Message message) {
		this.message = message;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("A chunk server sent over a chunk: " + message.getContent());
	}

}
