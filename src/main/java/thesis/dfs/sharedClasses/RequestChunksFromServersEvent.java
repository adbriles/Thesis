package thesis.dfs.sharedClasses;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import thesis.dfs.messages.Message;
import thesis.dfs.transport.TCPSender;

public class RequestChunksFromServersEvent implements Runnable{

	Message message;
	
	public RequestChunksFromServersEvent(Message message) {
		this.message = message;
		System.out.println(message.getContent());
		
		
	}
	
	@Override
	public void run() {
	String hostName = "";	
	try {
		hostName = InetAddress.getLocalHost().getHostName();
	} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
		e1.printStackTrace();
	}	
		
		
		
		// TODO Auto-generated method stub
	String[] splitContent = message.getContent().split(" ");
	if(splitContent[0].equals("invalid")) {
		System.out.println("The requested file, " + splitContent[1] +", cannot be found within the system.");
		return;
	} else {
		
		//message list content order: host, port, chunkname
		//message content: invalid or valid followed by file name		
		//Pass in size of list to get total number of chunks.
		//System.out.println("The list size is actually: "+ message.getList().size());
		//System.out.println("The content is actually: " + splitContent[1]);
		
		EventFactory.clientGets.addFileToGet(splitContent[1], message.getList().size());
		for(String s: message.getList()) {
			String[] splitChunkInfo = s.split(" ");
			Message requestMessage = new Message("SendChunkToClient");
			requestMessage.setContent(splitChunkInfo[2]);

			requestMessage.setSenderHostName(hostName);

			requestMessage.setSenderPort(EventFactory.getClientPort());
			try {
				TCPSender sender = new TCPSender(new Socket(splitChunkInfo[0], Integer.parseInt(splitChunkInfo[1])));
				sender.sendData(requestMessage);
			} catch (NumberFormatException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
		}
		
		
		
	}
	
	}
	
	

}
