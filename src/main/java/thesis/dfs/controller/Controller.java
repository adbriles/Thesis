package thesis.dfs.controller;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import thesis.*;

import java.lang.Number.*;
import thesis.dfs.transport.*;
import thesis.dfs.sharedClasses.*;



public class Controller {
	private static Integer portnum;
	private static EventFactory eventFactory;
	
	
	public static void main(String[] args) {
		portnum = Integer.parseInt(args[0]);
		eventFactory = EventFactory.getInstance();//make sure my singleton instance starts here
		
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

		
		System.out.println("Controller Online");
		boolean continueServerFunction = true;
		while(continueServerFunction) {
			
		}
	}
	
	private static Integer findOpenPort() throws IOException {
		try (ServerSocket socket = new ServerSocket(0);){
			return socket.getLocalPort();
		}
	}

}
