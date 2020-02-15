package thesis.dfs.sharedClasses;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;

import thesis.dfs.messages.Message;
import thesis.dfs.transport.TCPSender;

public class ForwardGoodChunkEvent implements Runnable{
	Message message;
	
	public ForwardGoodChunkEvent(Message message) {
		this.message = message;
		//Use my forwarding protocol develop
		
	}
	//Runs on chunk server with a valid chunk
	@Override
	public void run() {
		// TODO Auto-generated method stub
		EventFactory eventFactory = EventFactory.getInstance();
		System.out.println("Sending over a good chunk.");
		System.out.println(message.getContent());
		//file to send, host name, host port
		String[] messageSplit = message.getContent().split(" ");

		String[] chunkNameParts = messageSplit[1].split("_");
		
		try {
			TCPSender sender = new TCPSender(new Socket(messageSplit[1], Integer.parseInt(messageSplit[2])));
			Message storeChunk = new Message("StoreChunk", chunkNameParts[0] + messageSplit[0], new LinkedList<String>());
			storeChunk.setReadFile(true);
			//sender.sendData(storeChunk);
			sender.sendFile(new File(message.getContent().split("\\s+")[1]), storeChunk);
			
		} catch(IOException e) {
			e.printStackTrace();
		}
			
		
	}

}
