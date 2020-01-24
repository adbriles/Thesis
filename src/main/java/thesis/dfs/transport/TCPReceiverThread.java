package thesis.dfs.transport;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;

import thesis.dfs.messages.Message;
import thesis.dfs.sharedClasses.ChunkMetadata;
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
	
	public void createMetadata(String chunkName) {
		String metadataName = chunkName + ".metadata";
		System.out.println("Creating the metadata file: " + metadataName);
		try {
			FileOutputStream outMetadata = new FileOutputStream(metadataName);
			ObjectOutputStream outObject = new ObjectOutputStream(outMetadata);
			
			//This line gets the sequence number. It's not great, but it should work no matter what. 
			String[] fileNameSplit = chunkName.split("\\.");
			int sequenceNumber = Integer.parseInt(fileNameSplit[fileNameSplit.length - 1].replaceAll("[^0-9]", "")) - 1;
			
			outObject.writeObject(new ChunkMetadata(sequenceNumber));
			
			outObject.close();
			outMetadata.close();
			
			
			
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void readAndStoreFile(Message message) throws IOException {
		DataInputStream dis = new DataInputStream(socket.getInputStream());
		String chunkName = message.getContent().split("\\s+")[1];
		System.out.println("Trying to store: " + chunkName);
		
		File file = new File(chunkName);
		file.getParentFile().mkdirs();
		file.createNewFile();
		FileOutputStream fos = new FileOutputStream(file);

		byte[] buffer = new byte[4096];
		int filesize = 1024 * 64;//This can always be assumed
		int read = 0;
		int totalRead = 0;
		int remaining = filesize;
		while((read = dis.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
			totalRead += read;
			remaining -= read;
			fos.write(buffer, 0, read);
		}
		fos.close();
		dis.close();
		
		createMetadata(chunkName);

	}
	
	@Override
	public void run() {
		EventFactory eventFactory = EventFactory.getInstance();
		while(!socket.isClosed()) {
			try {

				Message message = (Message)ois.readObject();
				if(message.isReadFile()) {//Next block of code reads in a file and stores it. Metadata still needs to be collected. 
					readAndStoreFile(message);
				}
				Runnable event = EventFactory.createEvent(message);
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
