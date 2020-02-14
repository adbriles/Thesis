package thesis.dfs.messages;

import java.util.LinkedList;

public class FixCorruptedChunks implements Runnable{

	LinkedList<String> corruptedList;
	
	public FixCorruptedChunks(LinkedList<String> corruptedList) {
		this.corruptedList = corruptedList;
	}
	
	@Override
	public void run() {
		for(String s: corruptedList) {
			
		}
		
	}

}
