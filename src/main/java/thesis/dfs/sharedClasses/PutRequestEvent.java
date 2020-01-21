package thesis.dfs.sharedClasses;

import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import thesis.dfs.messages.Message;
import thesis.dfs.transport.TCPSender;

public class PutRequestEvent extends Event{
	private Message message;
	private EventFactory eventFactory;
	
	public PutRequestEvent(Message message) {
		this.message = message;
		eventFactory = EventFactory.getInstance();
	}
	

	
	public void run() {
		EventFactory eventFactory = EventFactory.getInstance();
		String[] messageSplit = message.getContent().split(" ");
		try {
			TCPSender sender = new TCPSender(new Socket(message.getSenderHostName(), message.getSenderPort()));
			//.eventFactory.chunkServerToStore
			//find the nodes that can store this. Since this is a new file,
			LinkedList<Integer> chunkServersToStore;
			
			if(eventFactory.hostToFiles.getUnsafeNumberChunks() < 3) {//!!!!!!!!!!!!!!!!This is a race condition. It's only currently in here for testing!!!!!!!!!!!!!!!!!!!!!!!!
				chunkServersToStore = eventFactory.hostToFiles.getChunkServersForStorageLessThanReplication();
				//Just leave whats in the else statement. 
			} else {
				chunkServersToStore = eventFactory.hostToFiles.getChunkServersForStorage();
			}
			
			//The next line makes a call that records where a chunk will be stored.
			System.out.println(messageSplit[0] + " " + messageSplit[1] + message.getContent());
			Message newMessage = new Message("StoreChunkOnChunkServers", message.getContent(), eventFactory.hostToFiles.findWhereToPlaceChunks(chunkServersToStore, messageSplit[0], messageSplit[1]));
			sender.sendData(newMessage);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("A socket could not be re-opened between the controller and the client");
		}
		
	}

}
