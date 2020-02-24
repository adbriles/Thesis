package thesis.dfs.controller;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import thesis.*;

import java.lang.Number.*;
import thesis.dfs.transport.*;
import thesis.dfs.sharedClasses.*;
import thesis.dfs.sharedClasses.EventFactory;



public class Controller {
	private static Integer portnum;
	private static EventFactory eventFactory;
	private static String hostName;
	
	public static void main(String[] args) {
		portnum = Integer.parseInt(args[0]);
		eventFactory = EventFactory.getInstance();//make sure my singleton instance starts here
		try {
			hostName = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//The use of java ExecutorService is mostly just for ease of use. 
		//I'm not too worried about too many threads constantly being created, because
		//there really aren't that many events to handle. Though, if things get too tense
		//a cached threadpool will make sure enough threads are created and resources are
		//--somewhat-- efficiently used. 
		ExecutorService threadPool = Executors.newCachedThreadPool();
		
		//The server threads creates a exactly what it sounds like. Upon receiving a message,
		//The message is read, and another thread is added to the thread pool handling the message. 
		TCPServerThread server = new TCPServerThread(portnum, threadPool);
		Thread serverThread = new Thread(server);
		serverThread.start();

		threadPool.execute(new CheckServers(hostName, portnum));
		
		
		System.out.println("Controller Online");
		boolean getUserInput = true;
		Scanner scanner = new Scanner(System.in);
		System.out.println("The Controller can now recieve user input:");
		while(getUserInput) {
            String input = scanner.nextLine();
            if(!input.isEmpty()){
            	String[] inputSplit = input.split("\\s+");
            	
            	if(inputSplit[0].equals("getChunkServers")) {
            		System.out.println("The chunk servers are: ");
            		EventFactory.hostToFiles.printChunkServers();
            	} else if(inputSplit[0].equals("printAll")) {
            		System.out.println("The structure of the system: ");
            		EventFactory.hostToFiles.printRecordStructure();
            	} else if(inputSplit[0].equals("printAllMaps")) {
            		EventFactory.hostToFiles.printAllMaps();
            	}
            	
            }
		}
	}
	

}
