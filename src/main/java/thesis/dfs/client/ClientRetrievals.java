package thesis.dfs.client;

import java.util.HashMap;
import java.util.LinkedList;

public class ClientRetrievals {	
	
	//private class to make my life a little easier. 
	private class FileInfo{
		private String fileName;
		private int numberChunksLeft;
		private LinkedList<String> chunks;
	
		public FileInfo(String fileName, int totalChunks) {
			numberChunksLeft = totalChunks;
			this.fileName = fileName;
			chunks = new LinkedList<String>();
		}
		
		public boolean decrementChunksLeft(String newlyRetrievedChunk) {
			boolean isNoneLeft = false;
			numberChunksLeft = numberChunksLeft--;
			if(numberChunksLeft == 0) {
				isNoneLeft = true;
			}
			chunks.add(newlyRetrievedChunk);
			return isNoneLeft;
		}
		
		//returns a copy of the underlying data structure so I don't accidentally mess with it later on
		public LinkedList<String> getAllChunks(){
			return new LinkedList<String>(chunks);
		}
		

	}

	
	
	private HashMap<String, FileInfo> fileData;
	
	public ClientRetrievals() {
		fileData = new HashMap<String, FileInfo>();
	}
	
	//This method gets called right before the client will request chunks from chunk servers
	public synchronized void addFileToGet(String fileName, int numberChunks ) {
		fileData.put(fileName, new FileInfo(fileName, numberChunks));
	}
	
	//This will be called as new chunks come in. will let code calling it know if everything has been retrieved
	public synchronized boolean recordNewChunk(String newChunkName) {
		String fileName = newChunkName.split("_")[0];
		boolean isLastChunk = fileData.get(fileName).decrementChunksLeft(newChunkName);
		return isLastChunk;
	}
	//Called when the last chunk is retrieved to stitch it get a list of all file names
	public synchronized LinkedList<String> getAllChunks(String newChunkName){
		String fileName = newChunkName.split("_")[0];
		return fileData.get(fileName).getAllChunks();
	}
	
	public synchronized void stopWaitingOnfile(String fileName) {
		fileData.remove(fileName);
	}
	
}
