package thesis.dfs.sharedClasses;

import java.io.Serializable;
import java.security.Timestamp;
import java.util.Date;

public class ChunkMetadata implements Serializable{

	private int version = 0;
	private int sequenceNumber;
	private long timeLastUpdated;
	
	public ChunkMetadata(int sequenceNumber) {
		setSequenceNumber(sequenceNumber);
		updateTimestamp();
	}
	public int getSequenceNumber() {
		return this.sequenceNumber;
	}
	private void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
	
	public void updateTimestamp() {
		Date date = new Date();
		this.timeLastUpdated = date.getTime();	
	}
	
	public long getTimeLastUpdated() {
		return this.timeLastUpdated;
	}

	public int getVersion() {
		return version;
	}

	public void incrementVersion() {
		this.version++;
	}
}
