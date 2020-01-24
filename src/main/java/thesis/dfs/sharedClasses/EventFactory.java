package thesis.dfs.sharedClasses;

import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import thesis.dfs.chunkserver.ChunkServerFileRecords;
import thesis.dfs.messages.Message;

public class EventFactory {
	private static EventFactory instance;
	
	//port and hostname -> other hashtable with file name to list of chunks
	//Traversal is painful. A lot of locks must be acquired. 
	//public static ConcurrentHashMap<String, ConcurrentHashMap<String, LinkedList<String>>> chunkServerToStoredFiles;
	public static ControllerRecordStructure hostToFiles;
	
	public static ChunkServerFileRecords chunkRecords;
	
	
	private EventFactory() {
		hostToFiles = new ControllerRecordStructure();
		chunkRecords = new ChunkServerFileRecords();
	}
	
	//Thread safe singleton get instance
	//synchronize only the block of code creating 
	//an instance for improved performance.
	//Initial startup is still slow while threads wait
	//for one to create an instance. 
	public static EventFactory getInstance() {		
		if (instance == null) {
			synchronized (EventFactory.class) {
				if(instance == null) {
					instance = new EventFactory();
				}
			}
		}
		return instance;
	}
	
	//Return a runnable event that a receiver thread will toss on a thread pool.
	public static Runnable createEvent(Message message) {

		if(message.getMessageType().equals("PutRequest")) {
			System.out.println("why isn't this printing");
			return new PutRequestEvent(message); 
		} else if(message.getMessageType().equals("ResponseFromController")) {
			System.out.println("A whole exchange took place");
		} else if(message.getMessageType().equals("ChunkServerRegistration")) {
			return new ChunkRegistrationEvent(message);
		} else if(message.getMessageType().equals("RegistrationConfirmation")) {
			return new ChunkServerRegistrationConfirmationEvent(message);
		} else if(message.getMessageType().equals("StoreChunkOnChunkServers")) {
			return new StoreAtChunkServerEvent(message);
		} else if(message.getMessageType().equals("StoreChunk")) {
			return new StoreChunkEvent(message);
		}
		
		return null;		
	}

	
	
	
}
