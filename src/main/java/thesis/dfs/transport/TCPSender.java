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
	
	public void sendFile(File file, Message message) throws IOException {
		
		oos.writeObject(message);
		
		System.out.println("Tried to send a file");
		DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
		FileInputStream fis = new FileInputStream(file);
		byte[] buffer = new byte[4096];
		
		//Make sure you write the same number of bytes fis read, or you end up duplicating data. 
		int br = -1;
		while(/*fis.read(buffer) > 0*/ (br = fis.read(buffer, 0, buffer.length)) != -1) {
			
			dos.write(buffer, 0, br);
		}
		
		fis.close();
		dos.close();	
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
