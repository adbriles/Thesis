package thesis.dfs.sharedClasses;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

import thesis.dfs.messages.Message;
import thesis.dfs.transport.TCPSender;

public class ChunkRegistrationEvent extends Event{
	private Message message;
	
	public ChunkRegistrationEvent(Message message) {
		this.message = message;
	}
	
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Registering the Chunk Server.");
		EventFactory eventFactory = EventFactory.getInstance();
		try {
			TCPSender sender = new TCPSender(new Socket(message.getSenderHostName(), message.getSenderPort()));
			
			long chunksSpace = Long.parseLong(message.getContent());

			String serverName = message.getSenderHostName() + " " + message.getSenderPort();
			eventFactory.hostToFiles.addChunkServer(serverName, chunksSpace);
			Message responseMessage = new Message("RegistrationConfirmation");
			sender.sendData(responseMessage);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("A socket could not be re-opened between the controller and the client");
		}
		
	}
	
	

}
