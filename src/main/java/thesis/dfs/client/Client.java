package thesis.dfs.client;

import java.awt.List;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
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
import thesis.dfs.sharedClasses.EventFactory;
import thesis.dfs.messages.Message;
import thesis.dfs.sharedClasses.*;


//Clients take user inputs. Clients consist of a server thread, 
//and a thread to handle ui. 


//to test 	put C:\Users\adambriles1216\eclipse-workspace\Thesis\TestOutput\TestWrite.txt
//to test   get C:\Users\adambriles1216\eclipse-workspace\Thesis\TestOutput\TestWrite.txt

//to test with linux	put /s/bach/d/under/adbriles/Thesis/TestFiles/TestWrite.txt
//						get /s/bach/d/under/adbriles/Thesis/TestFiles/TestWrite.txt
public class Client {
	private static boolean isWindows = false;
	
	
	
	private static Integer portnum;
	private static EventFactory eventFactory;
	private static Socket controllerSocket;
	private static InetAddress ip;
	private static String controllerHostName;
	private static int controllerPort;
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
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
		EventFactory.setClientPort(portnum);
		
		
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
            	
            	if(inputSplit[0].equals("put") && inputSplit.length == 2) {
            		System.out.println("Sending request to store the file.");
            		executePut(inputSplit[1]);
            	} else if(inputSplit[0].equals("get") && inputSplit.length == 2) {
            		System.out.println("Getting file: " + inputSplit[1]);
            		executeGet(inputSplit[1]);
            	}
            }
		}
	}
	private static void executeGet(String fileName) {
		try {
			TCPSender sender = new TCPSender(createSocket(controllerHostName, controllerPort));
			String messageContent = fileName;
			if(!isWindows) {
				messageContent = "/tmp" + fileName;
			}
			
			sender.sendData(new Message("GetRequest", messageContent, ip.getHostName(), portnum));
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	//Split the file before sending. All part files stored under tmp.
	private static void executePut(String fileName) throws FileNotFoundException, IOException {
		LinkedList<String> chunkNamesAndMetadata = splitFile(fileName);
		for(String p : chunkNamesAndMetadata) {
			requestPut(fileName, p);
		}
		
	}
	
	//Command to test: put C:\Users\adambriles1216\eclipse-workspace\Thesis\TestFiles\TestWrite.txt
	private static LinkedList<String> splitFile(String fileName) throws FileNotFoundException, IOException {
		File file = new File(fileName);
		
		LinkedList< String> partFileNames = new LinkedList<String>();
		Integer chunkNumber = 1;
		int sizeOfFiles = 1024 * 64;
		byte[] buffer = new byte[sizeOfFiles];
		
		try(FileInputStream fileIn = new FileInputStream(file);
			BufferedInputStream bufferedIn = new BufferedInputStream(fileIn);) {
			
			Integer bytesAmount = 0;
			int sequenceNumber = 0;
			while(( bytesAmount = bufferedIn.read(buffer)) > 0) {
				
				String chunkName;
				if(!isWindows) {//A check that lets me differentiate between my development environments.
					chunkName = "/tmp" + file + "_chunk" + chunkNumber.toString();
				} else {
					chunkName = file + "_chunk" + chunkNumber.toString();
				}
				
				File newChunkFile = new File(chunkName);	
				
				newChunkFile.getParentFile().mkdirs();
				newChunkFile.createNewFile();
				try(FileOutputStream outChunk = new FileOutputStream(newChunkFile)) {
					outChunk.write(buffer, 0, bytesAmount);
				}
				
				partFileNames.add(chunkName);
				sequenceNumber++;
				chunkNumber++;	
			}
			
		} 
		return partFileNames;
	}
	
	private static void requestPut(String fileName, String nameChunk) {
		try {
			TCPSender sender = new TCPSender(createSocket(controllerHostName, controllerPort));
			String messageContent = fileName + " " + nameChunk;
			sender.sendData(new Message("PutRequest", messageContent, ip.getHostName(), portnum));
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