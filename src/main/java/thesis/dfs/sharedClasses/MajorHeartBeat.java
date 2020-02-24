package thesis.dfs.sharedClasses;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import thesis.dfs.messages.Message;
import thesis.dfs.transport.TCPSender;

public class MajorHeartBeat implements Runnable{
	private String hostName;
	private Integer portnum;
	private String controllerHostName;
	private Integer controllerPort;
	
	public MajorHeartBeat(String hostName, Integer portnum, String controllerHostName, Integer controllerPort) {
		this.controllerHostName = hostName;
		this.portnum = portnum;
		this.controllerHostName = controllerHostName;
		this.controllerPort = controllerPort;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("A major heart beat occured.");
		//Message to totally reset the system and ensure everything is up to date
		Message message = new Message();
		try {
			message.setSenderHostName(InetAddress.getLocalHost().getHostName());
			message.setSenderPort(portnum);
			TCPSender sender = new TCPSender(new Socket(controllerHostName, controllerPort));
			message.setList(EventFactory.chunkRecords.getMajorHeartBeatInformation());
			
			//The second list contains newly added chunks, like a minor heartbeat, just to make things easier
			message.setSecondList(EventFactory.chunkRecords.getRecentlyAddedChunks());
			
			message.setContent(Long.toString((new File("/")).getFreeSpace()));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
