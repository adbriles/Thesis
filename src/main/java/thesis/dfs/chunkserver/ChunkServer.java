package thesis.dfs.chunkserver;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import thesis.dfs.controller.EventFactory;
import thesis.dfs.messages.Message;
import thesis.dfs.transport.TCPSender;
import thesis.dfs.transport.TCPServerThread;

public class ChunkServer {
	private static Integer portnum;
	private static EventFactory eventFactory;
	private static InetAddress ip;
	private static String controllerHostName;
	private static int controllerPort;
	private static boolean isRegisteredWithController;
	
	public static void main(String[] args) {
		portnum = Integer.parseInt(args[0]);
		controllerHostName = args[1];
		controllerPort = Integer.parseInt(args[2]);
		ExecutorService threadPool = Executors.newCachedThreadPool();
		try {
			ip = InetAddress.getLocalHost();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		eventFactory = EventFactory.getInstance();//make sure my singleton instance starts here
		
		TCPServerThread server = new TCPServerThread(portnum, threadPool);
		threadPool.execute(server);
		
		//Sign up with Controller
		registerWithController();
		
		
		
		HeartBeatTimer heartBeater = new HeartBeatTimer(threadPool, ip.getHostName(), portnum, controllerHostName, controllerPort);
		threadPool.execute(heartBeater);
		
		threadPool.execute(new CheckForCorruption(threadPool, ip.getHostName(), portnum, controllerHostName, controllerPort));
		
		boolean continueFunction = true;
		while(continueFunction) {
			
		}
		heartBeater.killTimer();
	}
	

	private static Socket createSocket(String hostName, int controllerPort) {
		try {
			return new Socket(hostName, controllerPort);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	private static void registerWithController() {
		try {
			TCPSender registration = new TCPSender(createSocket(controllerHostName, controllerPort));
			
			//Get total Free space
			long totalFreeSpace = new File("/").getFreeSpace();
			
			registration.sendData(new Message("ChunkServerRegistration", "" + Long.toString(totalFreeSpace), ip.getHostName(), portnum));
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
