package thesis.dfs.chunkserver;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;

import thesis.dfs.messages.Message;
import thesis.dfs.sharedClasses.EventFactory;
import thesis.dfs.sharedClasses.MajorHeartBeat;
import thesis.dfs.sharedClasses.MinorHeartBeat;

import thesis.dfs.transport.TCPSender;

public class CheckForCorruption implements Runnable{

	ExecutorService threadPool;
	private boolean continueTimer = true;
	private String senderHostName;
	private int senderPort;
	private String controllerHostName;
	private int controllerPort;
	
	public CheckForCorruption(ExecutorService threadPool, String senderHostName, int senderPort, String controllerHostName, int controllerPort) {
		this.threadPool= threadPool; 
		this.senderHostName = senderHostName;
		this.senderPort = senderPort;
		this.controllerHostName = controllerHostName;
		this.controllerPort = controllerPort;
	}

	//Just going to create my own timer to submit jobs on my threadpool. No need to use the timer class.
	public void killTimer() {
		continueTimer = false;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(continueTimer) {
			long startTime = System.currentTimeMillis();
			long elapsedTime = 0L;

			//Every minute, scan for corruption
			while(elapsedTime < 60 * 1000) {
				elapsedTime = (new Date()).getTime() - startTime;
			}
			LinkedList<String> corruptedList = EventFactory.getInstance().chunkRecords.getCorruptedList();
			for(String s: corruptedList) {//For each corruption, ask controller for a replacement
				Message message = new Message("CorruptedFileDetected");
				message.setContent(s);
				message.setSenderHostName(senderHostName);
				message.setSenderPort(senderPort);
				
				try {
					TCPSender sender = new TCPSender(new Socket(controllerHostName, controllerPort));
					sender.sendData(message);
					
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			
			
		}
		
		
	}
	

}
