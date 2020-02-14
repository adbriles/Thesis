package thesis.dfs.sharedClasses;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import thesis.dfs.messages.Message;

public class ControllerRecordStructure {//This is built on top of concurrent structures, but some compound actions need to happen
	
	//Server name mapped to a hashtable of files to their chunks
	private HashMap<String, HashMap<String, LinkedList<String>>> chunkServerToStoredFiles;
	private LinkedList<String> chunkServerNames;
	private HashMap<String, Long> chunkToAvailableSpace;
	private HashMap<String, Integer> fileToReplicationCount;

	public synchronized void printChunkServers() {
		for(String s: chunkServerNames) {
			System.out.println(s);
			System.out.flush();
		}
	}
	
	//This is a garbage method. Should've just made another map, but I didn't want to complicate this
	//Record structure and its maintenance any further.
	public synchronized String findBackupChunk(Message corruptionMessage) {
		String serverName = corruptionMessage.getSenderHostName() + " " + corruptionMessage.getSenderPort();
		String uncorruptedServer = "";
		for(Map.Entry<String, HashMap<String, LinkedList<String>>> m: chunkServerToStoredFiles.entrySet()) {
			if(serverName.equals(m.getKey())) {
				continue;
			}
			for(Map.Entry<String, LinkedList<String>> mm: m.getValue().entrySet()) {
				for(String s: mm.getValue()) {
					if(s.equals(corruptionMessage.getContent())) {
						uncorruptedServer = m.getKey();
					}
				}
			}
			
		}
		return uncorruptedServer;
	}
	
	public synchronized void printRecordStructure() {
		
		for(Map.Entry<String, HashMap<String, LinkedList<String>>> m: chunkServerToStoredFiles.entrySet()) {
			System.out.println(m.getKey());
			for(Map.Entry<String, LinkedList<String>> fileToChunksStored: m.getValue().entrySet()) {
				System.out.println("----" + fileToChunksStored.getKey());
				for(String s: fileToChunksStored.getValue()) {
					System.out.println("--------" + s);
				}
			}
		}
	}
	
	
	public ControllerRecordStructure() {
		chunkServerToStoredFiles = new HashMap<String, HashMap<String, LinkedList<String>>>();
		chunkServerNames = new LinkedList<String>();
		chunkToAvailableSpace = new HashMap<String, Long>();
		fileToReplicationCount = new HashMap<String, Integer>();
	}
	//Adding a file will happen on a put request
	//Add a file to the replication count, but actually add the file to the structure on an add chunk call
	/*public synchronized void addFile(String fileName, String chunkServerName) {

	}*/
	
	//adding a chunk will happen at heartbeat responses.
	public synchronized void addChunk(String chunkName, String fileName, String chunkServerName) {
		System.out.println(chunkServerName);
		if(!chunkServerToStoredFiles.get(chunkServerName).containsKey(fileName)) {
			chunkServerToStoredFiles.get(chunkServerName).put(fileName, new LinkedList<String>());
		}
		chunkServerToStoredFiles.get(chunkServerName).get(fileName).add(chunkName);
		
		//Update replication count
		if(!fileToReplicationCount.containsKey(chunkName)) {
			fileToReplicationCount.put(chunkName, 1);
		} else {
			int originalVal = fileToReplicationCount.get(chunkName);
			fileToReplicationCount.put(chunkName, originalVal + 1);
		}
		System.out.println("The total replication count is: " + fileToReplicationCount.get(chunkName));
		
		
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
			System.out.println(s);
		}
		System.out.println();
		
		return chunkNames;
		

	}
	public int getUnsafeNumberChunks() {
		return chunkServerToStoredFiles.size();
	}
	
	
	
	public synchronized void addChunkServer(String name, long availableSpace) {
		chunkServerToStoredFiles.put(name, new HashMap<String, LinkedList<String>>());
		chunkToAvailableSpace.put(name, availableSpace);
		chunkServerNames.add(name);
	}
	
	public synchronized void updateChunksSpace(String name, long availableSpace) {
		chunkToAvailableSpace.put(name, availableSpace);
	}
	
	public synchronized LinkedList<String> getChunkServersForStorage(){
		LinkedList<String> chunkServersToStore = new LinkedList<String>();
		/*
		while(chunkServersToStore.size() != 3) {//Get a list of chunk servers to store this chunk on
			Random r = new Random(System.currentTimeMillis());
			Integer possibleChunkServer = r.nextInt(chunkServerToStoredFiles.size());
			if(!chunkServersToStore.contains(possibleChunkServer)) {
				chunkServersToStore.add(possibleChunkServer);
			}
		}
		*/
		//I'm entirely aware that this is a terrible way to do this, but Java doesn't have 
		//A nice way to keep a sorted hashmap by value, and it has to be by value
		
		while(chunkServersToStore.size() != 3) {
			Long currentMin = Long.MAX_VALUE;
			String nameMin = "";
			for(Map.Entry<String, Long> m: chunkToAvailableSpace.entrySet()) {
				if(m.getValue() < currentMin && !chunkServersToStore.contains(m.getKey())) {
					currentMin = m.getValue();
					nameMin = m.getKey();
				}
			}
			chunkServersToStore.add(nameMin);
			
		}

		
		return chunkServersToStore;
	}
	
	public synchronized LinkedList<String> getChunkServersForStorageLessThanReplication(){
		LinkedList<String> chunkServersToStore = new LinkedList<String>();
		for(Map.Entry<String, Long> m: chunkToAvailableSpace.entrySet()) {
			chunkServersToStore.add(m.getKey());
		}
		return chunkServersToStore;
	}

	
}
