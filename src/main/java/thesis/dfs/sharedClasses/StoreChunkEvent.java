package thesis.dfs.sharedClasses;

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
		System.out.println("I hope this is running on the chunk server");
		
		/*for(String s: message.getList()) {
			System.out.println(s);
		}
		System.out.println();
		
		String[] forwardingChunk = message.getList().get(0).split("\\s+");
		LinkedList<String> newList = message.getList();
		newList.remove(0);*/
		/*try {
			TCPSender sender = new TCPSender(new Socket(forwardingChunk[0], Integer.parseInt(forwardingChunk[1])));
			Message storeChunk = new Message("StoreChunk", message.getContent(), newList);
			
		} catch(IOException e) {
			e.printStackTrace();
		}*/
			
		
		
	}

}
