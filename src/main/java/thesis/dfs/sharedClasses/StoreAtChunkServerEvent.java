package thesis.dfs.sharedClasses;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;

import thesis.dfs.messages.Message;
import thesis.dfs.transport.TCPSender;

public class StoreAtChunkServerEvent implements Runnable{
	private Message message;
	private EventFactory eventFactory;
	
	public StoreAtChunkServerEvent(Message message) {
		this.message = message;
		eventFactory = EventFactory.getInstance();
	}
	

	//This event should run on the client. Upon 
	public void run() {
		EventFactory eventFactory = EventFactory.getInstance();
		String[] messageSplit = message.getContent().split(" ");
		/*System.out.println();
		
		for(String s: message.getList()) {
			System.out.println(s);
		}
		System.out.println();
		*/
		
		String[] forwardingChunk = message.getList().get(0).split("\\s+");
		LinkedList<String> newList = message.getList();
		newList.remove(0);
		try {
			TCPSender sender = new TCPSender(new Socket(forwardingChunk[0], Integer.parseInt(forwardingChunk[1])));
			Message storeChunk = new Message("StoreChunk", message.getContent(), newList);
			storeChunk.setReadFile(true);
			//sender.sendData(storeChunk);
			sender.sendFile(new File(message.getContent().split("\\s+")[1]), storeChunk);
			
		} catch(IOException e) {
			e.printStackTrace();
		}
			
		

	}
}
