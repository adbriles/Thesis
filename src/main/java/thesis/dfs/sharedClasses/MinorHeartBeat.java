package thesis.dfs.sharedClasses;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import thesis.dfs.messages.Message;
import thesis.dfs.transport.TCPSender;

public class MinorHeartBeat implements Runnable{
	private String hostName;
	private Integer portnum;
	private String controllerHostName;
	private Integer controllerPort;
	
	public MinorHeartBeat(String hostName, Integer portnum, String controllerHostName, Integer controllerPort) {
		this.controllerHostName = hostName;
		this.portnum = portnum;
		this.controllerHostName = controllerHostName;
		this.controllerPort = controllerPort;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Message heartBeatMessage = new Message("MinorHeartBeat");
		try {
			heartBeatMessage.setSenderHostName(InetAddress.getLocalHost().getHostName());
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		heartBeatMessage.setSenderPort(portnum.intValue());
		
		heartBeatMessage.setList(EventFactory.chunkRecords.getRecentlyAddedChunks());
		
		heartBeatMessage.setContent(Long.toString((new File("/")).getFreeSpace()));
		
		try {
			TCPSender sender = new TCPSender(new Socket(controllerHostName, controllerPort));
			sender.sendData(heartBeatMessage);
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
