package thesis.dfs.sharedClasses;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

import thesis.dfs.messages.Message;
import thesis.dfs.transport.TCPSender;

public class FixCorruptedChunkEvent implements Runnable {

	private Message message;
	
	public FixCorruptedChunkEvent(Message message) {
		this.message = message;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		String goodChunkLocation = EventFactory.getInstance().hostToFiles.findBackupChunk(message);
		String[] hostInformationSplit = goodChunkLocation.split(" ");
		
		System.out.println("Requesting this chunk server to forward a good chunk: " + goodChunkLocation);
		
		Message fixCorruptionMessage = new Message("FixCorruption");
		fixCorruptionMessage.setContent(message.getContent() + " " + goodChunkLocation);
		
		try {
			TCPSender sender = new TCPSender(new Socket(hostInformationSplit[0], Integer.parseInt(hostInformationSplit[1])));
			sender.sendData(fixCorruptionMessage);
			
		} catch(IOException e) {
			e.printStackTrace();
		}
		
	}

}
