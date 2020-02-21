package thesis.dfs.sharedClasses;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;


import thesis.dfs.messages.Message;
import thesis.dfs.transport.TCPSender;

public class GetRequestEvent implements Runnable{
	private Message message;
	
	public GetRequestEvent(Message message) {
		this.message = message;
	}
	//!!!!!!YOU NEED TO ADD A WAY FOR THE CHUNKSERVER TO SEND SOMETHING TO A CLIENT
	@Override
	public void run() {
		// TODO Auto-generated method stub
		//Get a list of pairs. The pair will be a chunkname with a chunkserver 
		/*for(Pair<String, String> p: EventFactory.hostToFiles.getChunkLocations(message.getContent())){
			System.out.println("The location is: " + p.getKey() + " and the chunkname is: " + p.getValue());
			
			//actual code will contact chunk servers and tell them to read and forward the file to the client
			Message forwardToClient = new Message("ForwardChunkToClient");
			//chunkname, servername, port
			forwardToClient.setContent(p.getKey());
			
			try {
				TCPSender sender = new TCPSender(new Socket(p.getValue().split(" ")[0], Integer.parseInt(p.getValue().split(" ")[1])));
				sender.sendData(forwardToClient);
			
			
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}*/
		//Send the list to the client so it can request the chunks from chunkServers
		//Any other way seems like it would be beyond a pain. 
		LinkedList<String> fileAndHosts = new LinkedList<String>();
		for(PairStrings p: EventFactory.hostToFiles.getChunkLocations(message.getContent())) {
			String fileAndHost = p.getValue() + " " + p.getKey();
			fileAndHosts.add(fileAndHost);
		}
		
		Message getFromChunkServers = new Message("RequestChunksFromServers");
		
		//If the list is empy, the no file was found, report invalid.
		if(fileAndHosts.size() == 0) {
			getFromChunkServers.setList(fileAndHosts);
			getFromChunkServers.setContent("invalid" + " " + message.getContent());
		} else {
			getFromChunkServers.setList(fileAndHosts);
			getFromChunkServers.setContent("valid" + " " + message.getContent());
		}
		
		try {
			TCPSender sender = new TCPSender(new Socket(message.getSenderHostName(), message.getSenderPort()));
			sender.sendData(getFromChunkServers);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
		
}
