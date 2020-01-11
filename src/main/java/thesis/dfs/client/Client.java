package thesis.dfs.client;

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
import java.util.regex.Pattern;

import thesis.*;

import java.lang.Number.*;
import thesis.dfs.transport.*;
import thesis.dfs.controller.EventFactory;
import thesis.dfs.messages.Message;
import thesis.dfs.sharedClasses.*;


//Clients take user inputs. Clients consist of a server thread, 
//and a thread to handle ui. 

public class Client {
	private static Integer portnum;
	private static EventFactory eventFactory;
	private static Socket controllerSocket;
	private static InetAddress ip;
	private static String controllerHostName;
	private static int controllerPort;
	
	public static void main(String[] args) {
		try {//Get portnum to run on. 
			portnum = findOpenPort();
			ip = InetAddress.getLocalHost();
		} catch(IOException e) {
			System.out.println("A usable port was not found. The client is exiting.");
			return;
		}
		controllerHostName = args[0];
		controllerPort = Integer.parseInt(args[1]);
		//Setup a port for communication with the controller
		//setControllerSocket(args[0], Integer.parseInt(args[1]));
		
		ExecutorService threadPool = Executors.newCachedThreadPool();
		
		
		eventFactory = EventFactory.getInstance();//make sure my singleton instance starts here
		
		TCPServerThread server = new TCPServerThread(portnum, threadPool);
		threadPool.execute(server);
		
		boolean getUserInput = true;
		Scanner scanner = new Scanner(System.in);
		System.out.println("The client can now recieve user input:");
		while(getUserInput) {
            String input = scanner.nextLine();
            if(!input.isEmpty()){
            	String[] inputSplit = input.split("\\s+");
            	
            	/*if(input.equals("sendTest")) {
					//TCPSender sender = new TCPSender(new Socket(args[0], Integer.parseInt(args[1])));//This is test garbage. Clean it up
					System.out.println("what is even happening.");
            		sendTest();
            	}*/
            	if(inputSplit[0].equals("put") && inputSplit.length == 2) {
            		System.out.println("Sending request to store the file.");
            		String fileName = inputSplit[1];
            		splitFile(fileName);
            		requestPut(inputSplit[1]);
            	}
            }
		}
	}
	//Split the file before sending.
	private static void splitFile(String fileName) {
		
	}
	
	private static void requestPut(String fileName) {
		try {
			TCPSender sender = new TCPSender(createSocket(controllerHostName, controllerPort));
			sender.sendData(new Message("PutRequest", "what what what", ip.getHostName(), portnum));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	
	private static Integer findOpenPort() throws IOException {
		try (ServerSocket socket = new ServerSocket(0);){
			return socket.getLocalPort();
		}
	}
	
	private static void sendTest() {
		try {
			TCPSender sender = new TCPSender(controllerSocket);//This is test garbage. Clean it up
			sender.sendData(new Message("Test"));
			System.out.println("Sent a test message.");
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
	}
	
}