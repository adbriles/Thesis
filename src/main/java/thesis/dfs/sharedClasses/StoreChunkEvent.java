package thesis.dfs.sharedClasses;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;

import thesis.dfs.messages.Message;
import thesis.dfs.transport.TCPSender;

public class StoreChunkEvent implements Runnable{
	private Message message;
	private EventFactory eventFactory;
	
	public StoreChunkEvent(Message message) {
		this.message = message;
		eventFactory = EventFactory.getInstance();
	}
	

	//Runs on chunk server. Reads 
	public void run() {
		EventFactory eventFactory = EventFactory.getInstance();
		String[] messageSplit = message.getContent().split(" ");
		LinkedList<String> newList = message.getList();		
		
		if(newList.size() > 0) {
			String[] nextChunk = newList.remove(0).split("\\s+");
			try {
				TCPSender sender = new TCPSender(new Socket(nextChunk[0], Integer.parseInt(nextChunk[1])));
				Message storeChunk = new Message("StoreChunk", message.getContent(), newList);
				storeChunk.setReadFile(true);
				sender.sendFile(new File(message.getContent().split("\\s+")[1]), storeChunk);
			} catch(IOException e) {
				e.printStackTrace();
			}
			
		
		}
		eventFactory.chunkRecords.addChunkFile(message.getContent().split("\\s+")[1]);
	}

}

