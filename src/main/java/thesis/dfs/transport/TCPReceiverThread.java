package thesis.dfs.transport;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;

import thesis.dfs.messages.Message;
import thesis.dfs.sharedClasses.Event;
import thesis.dfs.sharedClasses.EventFactory;
public class TCPReceiverThread implements Runnable{
	private Socket socket;
	//private DataInputStream din;
	private ObjectInputStream ois;
	private ExecutorService threadPool;
	private EventFactory eventFactory;
	
	public TCPReceiverThread (Socket socket, ExecutorService threadPool) throws IOException {
		this.socket = socket;
		//din = new DataInputStream(socket.getInputStream());
		ois = new ObjectInputStream(socket.getInputStream());
		this.threadPool = threadPool;
	}
	
	@Override
	public void run() {
		EventFactory eventFactory = EventFactory.getInstance();
		while(!socket.isClosed()) {
			try {

				Message message = (Message)ois.readObject();
				Runnable event = EventFactory.createEvent(message);
				
				System.out.println(message.getMessageType());
				
				threadPool.execute(event);				
				socket.close();

			} catch(SocketException se) {
				System.out.println(se.getMessage());
				break;
			} catch (IOException ioe) {
				System.out.println(ioe.getMessage());
				break;
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			}
		}

	}
	
	
	
}
