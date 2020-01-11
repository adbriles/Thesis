package thesis.dfs.sharedClasses;

import java.io.IOException;
import java.net.Socket;

import thesis.dfs.messages.Message;
import thesis.dfs.transport.TCPSender;

public class PutRequestEvent extends Event{
	private Message message;
	
	public PutRequestEvent(Message message) {
		this.message = message;
	}
	
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Put request response sending");
		try {
			TCPSender sender = new TCPSender(new Socket(message.getSenderHostName(), message.getSenderPort()));
			Message message = new Message("ResponseFromController");
			sender.sendData(message);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("A socket could not be re-opened between the controller and the client");
		}
		
	}

}
