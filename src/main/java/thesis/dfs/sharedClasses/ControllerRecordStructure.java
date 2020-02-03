package thesis.dfs.sharedClasses;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class ControllerRecordStructure {//This is built on top of concurrent structures, but some compound actions need to happen
	private HashMap<String, HashMap<String, LinkedList<String>>> chunkServerToStoredFiles;
	
	public synchronized void printChunkServers() {
		System.out.println("If I'm getting here I'm failing to get the locks and act on the data structure.");
		for(Map.Entry<String, HashMap<String, LinkedList<String>>> m : chunkServerToStoredFiles.entrySet()) {
			System.out.println(m.getKey() + " wtf");
			System.out.flush();
		}
		System.out.println("Either nothing is in there or I can't get all the locks " + chunkServerToStoredFiles.size());
	}
	
	
	public ControllerRecordStructure() {
		chunkServerToStoredFiles = new HashMap<String, HashMap<String, LinkedList<String>>>();
	}


	public synchronized LinkedList<String> findWhereToPlaceChunks(LinkedList<Integer> chunkServerIndices, String fileName, String chunkName){
		LinkedList<String> chunkServerNames = new LinkedList<String>();
				
		for(Integer i: chunkServerIndices) {
			int counter = 0;
			for(Map.Entry<String, HashMap<String, LinkedList<String>>> m : chunkServerToStoredFiles.entrySet()) {
				if(counter == i) {
					chunkServerNames.add(m.getKey());
					//Add file name!
					HashMap<String, LinkedList<String>> fileToChunks = chunkServerToStoredFiles.get(m.getKey());
					LinkedList<String> chunkFileList = fileToChunks.get(fileName);
					if(chunkFileList == null) {
						chunkServerToStoredFiles.get(m.getKey()).put(fileName, new LinkedList<String>());
					}
					chunkServerToStoredFiles.get(m.getKey()).get(fileName).add(chunkName);	

					break;
				}
				counter++;
			}
		}		
		System.out.println("List of chunkServers: ");
		for(String s: chunkServerNames) {
			System.out.println();
		}
		System.out.println();
		return chunkServerNames;
		

	}
	public int getUnsafeNumberChunks() {
		return chunkServerToStoredFiles.size();
	}
	
	
	
	public synchronized void addChunkServer(String name) {
		chunkServerToStoredFiles.put(name, new HashMap<String, LinkedList<String>>());
		System.out.println("Chunk server was added with name: " + name);
	}
	
	public synchronized LinkedList<Integer> getChunkServersForStorage(){
		LinkedList<Integer> chunkServersToStore = new LinkedList<Integer>();
		
		while(chunkServersToStore.size() != 2) {//Get a list of chunk servers to store this chunk on
			Random r = new Random(System.currentTimeMillis());
			Integer possibleChunkServer = r.nextInt(chunkServerToStoredFiles.size());
			if(!chunkServersToStore.contains(possibleChunkServer)) {
				chunkServersToStore.add(possibleChunkServer);
			}
		}
		
		return chunkServersToStore;
	}
	
	public synchronized LinkedList<Integer> getChunkServersForStorageLessThanReplication(){
		LinkedList<Integer> chunkServersToStore = new LinkedList<Integer>();
		for(int i = 0; i < chunkServerToStoredFiles.size();i++) {
			chunkServersToStore.add(i);
		}
		return chunkServersToStore;
	}
	
}
