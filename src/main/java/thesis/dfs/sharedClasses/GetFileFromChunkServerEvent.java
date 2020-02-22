package thesis.dfs.sharedClasses;

import java.util.ArrayList;
import java.util.LinkedList;

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
		String chunkName = message.getContent().split(" ")[1];
		
		boolean isLast = EventFactory.clientGets.recordNewChunk(chunkName);
		if(isLast) {
			LinkedList<String> allChunks = EventFactory.clientGets.getAllChunks(chunkName);
			stitchBackTogethor(allChunks);
		}
		
	}
	
	
	private void stitchBackTogethor(LinkedList<String> chunkNames) {
		String[] inOrderChunks = new String[chunkNames.size()];
		for(String s: chunkNames) {
			int chunkNumber = Integer.parseInt(s.split("_chunk")[1]) - 1;
			inOrderChunks[chunkNumber] = s;
		}
		for(String s: inOrderChunks) {
			System.out.println("The chunkNumber is: " + s);
		}
	}
	

}
