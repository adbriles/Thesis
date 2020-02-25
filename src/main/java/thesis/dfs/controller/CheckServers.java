package thesis.dfs.controller;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.LinkedList;

import thesis.dfs.messages.Message;
import thesis.dfs.sharedClasses.EventFactory;
import thesis.dfs.sharedClasses.MinorHeartBeat;
import thesis.dfs.transport.TCPSender;

public class CheckServers implements Runnable {
	private boolean continueTimer = true;
	private int controllerPort;
	private String controllerHostName;
	private int desiredReplication = 3;
	
	public void killTimer() {
		continueTimer = false;
	}
	
	public CheckServers(String controllerHostName, int controllerPort) {
		this.controllerHostName = controllerHostName;
		this.controllerPort = controllerPort;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Message message = new Message("AreYouAlive");
		message.setSenderHostName(controllerHostName);
		message.setSenderPort(controllerPort);
		
		while(continueTimer) {
			long startTime = System.currentTimeMillis();
			long elapsedTime = 0L;

			while(elapsedTime < 45 * 1000) {
				elapsedTime = (new Date()).getTime() - startTime;
			}
			
			//Execute minor heartbeat
			for(String serverName : EventFactory.hostToFiles.getAllChunkServers()) {
				String[] splitName = serverName.split(" ");
				try {
					//if true returned, the server is dead.
					if(EventFactory.hostToFiles.incrementServerChecks(serverName)) {
						System.out.println("The controller detects a dead chunk server with a name of: " + serverName);
						//Remove all traces of the dead server from records and make sure replication makes it to three.
						EventFactory.hostToFiles.removeAnyTrace(serverName);
						LinkedList<String> chunksToReplicate = EventFactory.hostToFiles.getReplicationUp(desiredReplication);
						for(String chunk: chunksToReplicate) {
							LinkedList<String> newServers = EventFactory.hostToFiles.getServerToStore(chunk, 1);
							if(newServers.size() != 1) {
								//too many servers have died, and replication needs to drop
								desiredReplication--;
							}
							for(String s: newServers) {
								requestReplication(chunk, s);
							}

							
						}
						
						
					} else {
					message.setContent(serverName);	
					TCPSender sender = new TCPSender(new Socket(splitName[0], Integer.parseInt(splitName[1])));
					sender.sendData(message);
					}
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					System.out.println("The chunk server, " + serverName + ", has refused one connection.");
					
				}
			}
			
		}
	}

	private void requestReplication(String chunkName, String serverToSendTo) throws NumberFormatException, UnknownHostException, IOException {
		// TODO Auto-generated method stub
		System.out.println("About to store the chunk, " + chunkName + ", from the server: " + serverToSendTo);
		String getValidServer = EventFactory.hostToFiles.getGoodChunk(chunkName);
		String[] serverParts = getValidServer.split(" ");
		TCPSender sender = new TCPSender(new Socket(serverParts[0], Integer.parseInt(serverParts[1])));
		Message forwardGoodChunk = new Message("ForwardChunkAfterServerFailure");
		forwardGoodChunk.setContent(chunkName + " " + serverToSendTo);
		sender.sendData(forwardGoodChunk);
		
		
	}

}
