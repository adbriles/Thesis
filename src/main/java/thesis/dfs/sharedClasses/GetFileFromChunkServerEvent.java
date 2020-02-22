package thesis.dfs.sharedClasses;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;

import thesis.dfs.messages.Message;

public class GetFileFromChunkServerEvent implements Runnable{

	private Message message;
	
	public GetFileFromChunkServerEvent(Message message) {
		this.message = message;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("A chunk server sent over a chunk: " + message.getContent());
		String chunkName = message.getContent().split(" ")[1];
		String fileName = message.getContent().split(" ")[0];
		boolean isLast = EventFactory.clientGets.recordNewChunk(chunkName);
		if(isLast) {//Stop waiting on file and make stitch it back togethor. 
			LinkedList<String> allChunks = EventFactory.clientGets.getAllChunks(chunkName);
			
			try {
				stitchBackTogethor(allChunks, fileName);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			EventFactory.clientGets.stopWaitingOnfile(message.getContent().split(" ")[0]);
		}
		
	}
	
	
	private void stitchBackTogethor(LinkedList<String> chunkNames, String fileName) throws IOException {
		String[] inOrderChunks = new String[chunkNames.size()];
		

		//Order the chunks properly
		for(String chunkName: chunkNames) {
			int chunkNumber = Integer.parseInt(chunkName.split("_chunk")[1]) - 1;
			inOrderChunks[chunkNumber] = chunkName;

		}
		

		FileOutputStream writer = new FileOutputStream(new File(fileName), true);		

		for(String chunk: inOrderChunks) {
			System.out.println("Getting ready to append the chunk: " + chunk);
			int sizeOfFiles = 1024 * 64;
			byte[] buffer = new byte[sizeOfFiles];			
			try(FileInputStream fileIn = new FileInputStream(chunk);
					BufferedInputStream bufferedIn = new BufferedInputStream(fileIn);) {
					
					Integer bytesAmount = 0;
					int sequenceNumber = 0;
					
					int amountRead = bufferedIn.read(buffer);
					System.out.println("The buffer read in: " + amountRead);	
					if(amountRead != 1024 * 64) {
						byte[] actuallyRead = new byte[amountRead];
						System.arraycopy(buffer, 0, actuallyRead,0, amountRead);
						buffer = actuallyRead;
					}
					writer.write(buffer);
					
					
				} 
		}
		
		writer.close();
		System.out.println("Finished getting the file: " + fileName);
	}
	

}
