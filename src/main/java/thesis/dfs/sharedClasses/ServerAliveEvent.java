package thesis.dfs.sharedClasses;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import thesis.dfs.messages.Message;
import thesis.dfs.transport.TCPSender;

public class ServerAliveEvent implements Runnable{

	private Message message;
	
	public ServerAliveEvent(Message message) {
		this.message = message;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("I'm alive!");
		Message aliveResponse = new Message("ImAlive");
		aliveResponse.setContent(message.getContent());//just pass the name of the server back to the controller. Easier
		try {
			TCPSender sender = new TCPSender(new Socket(message.getSenderHostName(), message.getSenderPort()));
			sender.sendData(aliveResponse);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
