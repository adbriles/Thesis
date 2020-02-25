package thesis.dfs.sharedClasses;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;

import thesis.dfs.messages.Message;
import thesis.dfs.transport.TCPSender;

public class ForwardChunkAfterServerFailureEvent implements Runnable{
	Message message;
	
	public ForwardChunkAfterServerFailureEvent(Message message) {
		this.message = message;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		EventFactory eventFactory = EventFactory.getInstance();
		//System.out.println("Sending over a good chunk.");
		//System.out.println(message.getContent());
		//file to send, host name, host port
		String[] messageSplit = message.getContent().split(" ");

		String[] chunkNameParts = messageSplit[1].split("_");
		System.out.println("I'm sending a file to: " + message.getContent());
		
		try {
			TCPSender sender = new TCPSender(new Socket(messageSplit[1], Integer.parseInt(messageSplit[2])));
			Message storeChunk = new Message("StoreChunk", chunkNameParts[0] + " " + messageSplit[0], new LinkedList<String>());
			storeChunk.setReplacementChunk(false);//Make sure a heartbeat updates the controller
			sender.sendFile(new File(message.getContent().split("\\s+")[0]), storeChunk);
			
		} catch(IOException e) {
			e.printStackTrace();
		}
			
	}

}
