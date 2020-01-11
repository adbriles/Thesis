package thesis.dfs.transport;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;



public class TCPServerThread implements Runnable{
    private int portNum;
    private volatile int continueRunning = 0;
    private ArrayList<Socket> socketsConnected = new ArrayList<Socket>();
    private volatile int allDone = 0;
    private ServerSocket serverSocket;
    private final ExecutorService threadPool;
    
    public TCPServerThread(int portNum, ExecutorService threadPool){
        this.portNum = portNum;
        this.threadPool = threadPool;
    }
    
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
       	try {
            //set up a serverSocket and wait for incoming connections       		
			serverSocket = new ServerSocket(this.portNum);	
       		
			while(continueRunning == 0) {
                //accept incoming requests and spawn a thread to handle each one. 
				Socket socket = serverSocket.accept();
                threadPool.execute(new TCPReceiverThread(socket, threadPool));
                
			}
	
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			return;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return;
		}
		
	}

}
