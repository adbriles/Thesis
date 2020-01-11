package thesis.dfs.transport;

import java.io.*;
import java.net.Socket;
import thesis.dfs.*;
import thesis.dfs.messages.Message;
import java.io.ObjectOutputStream;
public class TCPSender {
	private Socket socket;
	//private DataOutputStream dout;
	private ObjectOutputStream oos;
	
	public TCPSender(Socket socket) throws IOException {
		this.socket = socket;
		//dout = new DataOutputStream(socket.getOutputStream());
		oos = new ObjectOutputStream(socket.getOutputStream());
	}
	
	public void sendData(Message message) throws IOException {
		
		oos.writeObject(message);
		oos.close();
	}
	
	public void shutDown() {
		try {
			oos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
