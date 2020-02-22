package thesis.dfs.sharedClasses;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
		

		PrintWriter pw = new PrintWriter(fileName);

		
		for(String chunkName: chunkNames) {
			int chunkNumber = Integer.parseInt(chunkName.split("_chunk")[1]) - 1;
			inOrderChunks[chunkNumber] = chunkName;
			File f = new File(chunkName);
			
			BufferedReader br = new BufferedReader(new FileReader(chunkName));
			String line = br.readLine();
			while(line != null) {
				pw.println();
				line = br.readLine();
			}
			pw.flush();
         
			
		}

	}
	

}
