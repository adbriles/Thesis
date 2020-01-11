package thesis.dfs.chunkserver;

import java.io.IOException;
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
	public static void main(String[] args) {
		portnum = Integer.parseInt(args[0]);
		
		ExecutorService threadPool = Executors.newCachedThreadPool();
		
		
		eventFactory = EventFactory.getInstance();//make sure my singleton instance starts here
		
		TCPServerThread server = new TCPServerThread(portnum, threadPool);
		threadPool.execute(server);
		
		
		System.out.println("The Chunk Server is Online");
		boolean continueFunction = true;
		while(continueFunction) {
			
		}
	}
}
