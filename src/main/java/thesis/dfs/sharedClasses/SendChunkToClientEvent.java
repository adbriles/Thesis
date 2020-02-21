package thesis.dfs.sharedClasses;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;

import thesis.dfs.messages.Message;
import thesis.dfs.transport.TCPSender;

public class SendChunkToClientEvent implements Runnable{

	private Message message;
	
	public SendChunkToClientEvent(Message message) {
		this.message = message;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		EventFactory eventFactory = EventFactory.getInstance();
		//System.out.println(message.getContent());
		//file to send, host name, host port

		try {
			TCPSender sender = new TCPSender(new Socket(message.getSenderHostName(), message.getSenderPort()));
			Message storeChunk = new Message("GetFileFromChunkServer");
			
			storeChunk.setContent(message.getContent().split("_")[0] + " " + message.getContent());
			
			storeChunk.setReadFile(true);
			storeChunk.setReplacementChunk(true);
			//sender.sendData(storeChunk);
			//System.out.println("The message content is: " + message.getContent());
			
			sender.sendFile(new File(message.getContent()), storeChunk);
			
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

}
