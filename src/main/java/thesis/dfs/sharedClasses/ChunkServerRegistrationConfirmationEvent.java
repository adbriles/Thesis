package thesis.dfs.sharedClasses;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

import thesis.dfs.messages.Message;
import thesis.dfs.transport.TCPSender;

public class ChunkServerRegistrationConfirmationEvent implements Runnable{
	private Message message;
	
	public ChunkServerRegistrationConfirmationEvent(Message message) {
		this.message = message;
	}
	
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("This Chunk Server successfully registered with the controller.");
		
	}
}
