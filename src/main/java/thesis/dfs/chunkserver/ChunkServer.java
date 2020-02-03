package thesis.dfs.chunkserver;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import thesis.dfs.sharedClasses.EventFactory;
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
		
		
		EventFactory eventFactory = EventFactory.getInstance();
		boolean getUserInput = true;
		Scanner scanner = new Scanner(System.in);
		System.out.println("The client can now recieve user input:");
		while(getUserInput) {
            String input = scanner.nextLine();
            if(!input.isEmpty()){
            	String[] inputSplit = input.split("\\s+");
            	
            	if(inputSplit[0].equals("getChunkServers")) {
            		eventFactory.hostToFiles.printChunkServers();
            	}
            }
		}
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
			registration.sendData(new Message("ChunkServerRegistration", "", ip.getHostName(), portnum));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
