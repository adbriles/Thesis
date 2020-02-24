package thesis.dfs.sharedClasses;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import thesis.dfs.messages.Message;

public class ControllerRecordStructure {//This is built on top of concurrent structures, but some compound actions need to happen
	
	//Server name mapped to a hashtable of files to the chunks
	private HashMap<String, HashMap<String, LinkedList<String>>> chunkServerToStoredFiles;
	private LinkedList<String> chunkServerNames;//
	private HashMap<String, Long> chunkToAvailableSpace;//
	private HashMap<String, Integer> fileToReplicationCount;
	
	//This data structure is using a set to make sure I'm not constantly adding redundant chunks
	//I can't really be sure a chunk server has a file unless they tell the controller
	private HashMap<String, HashSet<String>> fileToAllChunks; 
	private HashMap<String, LinkedList<String>> chunkFileToServersStoring;
	
	//Map structure for tracking if servers are alive.
	//If the servers int value gets above two, its pretty likely dead.
	private HashMap<String, Integer> serverToPingCount;
	
	
	public synchronized void printChunkServers() {
		for(String s: chunkServerNames) {
			System.out.println(s);
			System.out.flush();
		}
	}
	//In the case of a node failure, find another server to replicate the chunk on. 
	public synchronized LinkedList<String> getServerToStore(String chunk, int neededServers) {
		LinkedList<String> availableServers = new LinkedList<String>();
		for(int i = 0; i < neededServers; i++) {
			for(String server: chunkServerNames) {
				//if the server isn't listed under the chunk
				if(!chunkFileToServersStoring.get(chunk).contains(server)) {
					availableServers.add(server);
				}
			}
		}
		return availableServers;
	}
	
	//If a server goes down, handle all the book keeping
	public synchronized void removeAnyTrace(String serverName) {
		// TODO Auto-generated method stub
		chunkServerNames.removeFirstOccurrence(serverName);
		chunkToAvailableSpace.remove(serverName);
		serverToPingCount.remove(serverName);
		//figure out what files the chunks was storing
		HashMap<String, LinkedList<String>> filesServerStored = chunkServerToStoredFiles.get(serverName);
		
		for(Map.Entry<String, LinkedList<String>> m: filesServerStored.entrySet()) {
			for(String chunkName: m.getValue()) {
				int currentReplicationCount = fileToReplicationCount.get(chunkName);
				fileToReplicationCount.put(chunkName, currentReplicationCount - 1);
				System.out.println("The replication count has been reduced to: " + (currentReplicationCount - 1));
				chunkFileToServersStoring.get(chunkName).removeFirstOccurrence(serverName);
			}
		}
		chunkServerToStoredFiles.remove(serverName);
		
	}
	//finds anythign with a replication lower than 3 and fixes it
	public synchronized LinkedList<String> getReplicationUp(int desiredReplication) {
		LinkedList<String> filesBelowReplication = new LinkedList<String>();
		for(Map.Entry<String, Integer> m: fileToReplicationCount.entrySet()) {
			if(m.getValue() < desiredReplication) {
				filesBelowReplication.add(m.getKey());
			}
		}
		return filesBelowReplication;
	}
	
	public synchronized void decrementReplicationCount(String chunkFileName) {
		Integer currentCount =  fileToReplicationCount.get(chunkFileName);
		fileToReplicationCount.put(chunkFileName, (currentCount - 1));
	}
	
	public synchronized void updateFromMajorHeartBeat(LinkedList<String> allChunks, String serverID) {
		LinkedList<String> chunksAtServer = new LinkedList<String>();
		
		//for each controller known chunk, check if its in the reported list. If not, remove it from controller records.
		//
		for(Map.Entry<String, LinkedList<String>> fileName: chunkServerToStoredFiles.get(serverID).entrySet()) {
			
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
		fileToAllChunks = new HashMap<String, HashSet<String>>();
		chunkFileToServersStoring = new HashMap<String, LinkedList<String>>();
		serverToPingCount = new HashMap<String, Integer>();
	}
	//Adding a file will happen on a put request
	//Add a file to the replication count, but actually add the file to the structure on an add chunk call
	/*public synchronized void addFile(String fileName, String chunkServerName) {

	}*/
	
	//adding a chunk will happen at heartbeat responses.
	public synchronized void addChunk(String chunkName, String fileName, String chunkServerName) {
		//System.out.println(chunkServerName);
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
		
		
		if(!fileToAllChunks.containsKey(fileName)) {
			fileToAllChunks.put(fileName, new HashSet<String>());
		} 
		fileToAllChunks.get(fileName).add(chunkName);
		
		
		if(!chunkFileToServersStoring.containsKey(chunkName)) {
			chunkFileToServersStoring.put(chunkName, new LinkedList<String>());
		}
		chunkFileToServersStoring.get(chunkName).add(chunkServerName);
		
		
		System.out.println("The total replication count is: " + fileToReplicationCount.get(chunkName));
		
		
	}
	
	public synchronized LinkedList<PairStrings> getChunkLocations(String fileName) {
		LinkedList<PairStrings> chunksWithServer = new LinkedList<>();
		
		//If file doesn't exist send back that the file doesn't exist.
		if( !fileToAllChunks.containsKey(fileName)) {
			return chunksWithServer;
		}
		//Use a random number generator to make sure one chunk server doesn't get every request for a chunk.
		//It's a little janky, but I don't see why it's not a good solution.
		Random randomGenerator = new Random();
		HashSet<String> fileChunks = fileToAllChunks.get(fileName);
		for(String s: fileChunks) {
			LinkedList<String> chunkServerList = chunkFileToServersStoring.get(s);
			int indexOfServer = randomGenerator.nextInt(chunkServerList.size());
			String chunkServerToRequestFrom = chunkServerList.get(indexOfServer);
			//chunk name to server to get it
			chunksWithServer.add(new PairStrings(s, chunkServerToRequestFrom));
		}
		
		return chunksWithServer;
		
	}
	
	public synchronized void printAllMaps() {
		System.out.println("File to all chunks it contains: ");		
		for(Map.Entry<String, HashSet<String>> m: fileToAllChunks.entrySet()) {
			System.out.println("The file is: " + m.getKey());
			for(String s: m.getValue()) {
				System.out.println();
			}
		}
		
		System.out.println();
		
		System.out.println("Chunk file to all servers storing it: ");		
		for(Map.Entry<String, LinkedList<String>> m: chunkFileToServersStoring.entrySet()) {
			System.out.println("name of chunk file: " + m.getKey());
			for(String s: m.getValue()) {
				System.out.println(s);
			}
		}
		
	}
	
	
	public int getUnsafeNumberChunks() {
		return chunkServerToStoredFiles.size();
	}
	
	
	
	public synchronized void addChunkServer(String name, long availableSpace) {
		chunkServerToStoredFiles.put(name, new HashMap<String, LinkedList<String>>());
		chunkToAvailableSpace.put(name, availableSpace);
		chunkServerNames.add(name);
		serverToPingCount.put(name, 0);
	}
	
	public synchronized boolean incrementServerChecks(String serverName) {
		boolean isDead = false;
		int checks = serverToPingCount.get(serverName) + 1;
		if(checks == 2) {
			isDead = true;
		}
		serverToPingCount.put(serverName, checks);
		return isDead;
	}
	//Reset to zero instead of just decrementing. I want to give a chunk server a min and a half before 
	//I murder it.
	public synchronized void resetServerChecks(String serverName) {
		serverToPingCount.put(serverName, 0);
	}
	
	public synchronized void updateChunksSpace(String name, long availableSpace) {
		chunkToAvailableSpace.put(name, availableSpace);
	}
	
	public synchronized LinkedList<String> getChunkServersForStorage(){
		LinkedList<String> chunkServersToStore = new LinkedList<String>();
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
	
	public synchronized LinkedList<String> getAllChunkServers(){
		return new LinkedList<String>(chunkServerNames);
	}
	
	public synchronized LinkedList<String> getChunkServersForStorageLessThanReplication(){
		LinkedList<String> chunkServersToStore = new LinkedList<String>();
		for(Map.Entry<String, Long> m: chunkToAvailableSpace.entrySet()) {
			chunkServersToStore.add(m.getKey());
		}
		return chunkServersToStore;
	}




	
}
