package thesis.dfs.sharedClasses;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class ControllerRecordStructure {//This is built on top of concurrent structures, but some compound actions need to happen
	private HashMap<String, HashMap<String, LinkedList<String>>> chunkServerToStoredFiles;
	private LinkedList<String> chunkServerNames;
	
	public synchronized void printChunkServers() {
		System.out.println("What the mother fuck is going on");
		for(String s: chunkServerNames) {
			System.out.println(s);
			System.out.flush();
		}
	}
	
	
	public ControllerRecordStructure() {
		chunkServerToStoredFiles = new HashMap<String, HashMap<String, LinkedList<String>>>();
		chunkServerNames = new LinkedList<String>();
	}


	public synchronized LinkedList<String> findWhereToPlaceChunks(LinkedList<Integer> chunkServerIndices, String fileName, String chunkName){
		LinkedList<String> chunkNames = new LinkedList<String>();
				
		for(Integer i: chunkServerIndices) {
			int counter = 0;
			for(Map.Entry<String, HashMap<String, LinkedList<String>>> m : chunkServerToStoredFiles.entrySet()) {
				if(counter == i) {
					chunkNames.add(m.getKey());
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
		for(String s: chunkNames) {
			System.out.println();
		}
		System.out.println();
		return chunkNames;
		

	}
	public int getUnsafeNumberChunks() {
		return chunkServerToStoredFiles.size();
	}
	
	
	
	public synchronized void addChunkServer(String name) {
		chunkServerToStoredFiles.put(name, new HashMap<String, LinkedList<String>>());
		chunkServerNames.add(name);
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
