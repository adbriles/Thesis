package thesis.dfs.chunkserver;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.apache.commons.math3.util.Pair;

public class ChunkServerFileRecords {

	//File name to integrity information
	public class FileInformation{
		LinkedList<Integer> fileHashes;
		
		
		//When given chunkName, construct new object and create checksums
		public FileInformation(String chunkName) {
			fileHashes = new LinkedList<Integer>();
			
			//read in 8 kb chunks of file and create digests
			try{
				FileInputStream fileIn = new FileInputStream(chunkName);
				BufferedInputStream bufferedIn = new BufferedInputStream(fileIn);
				
				Integer bytesAmount = 0;
				int sizeOfSegments = 1024 * 8;
				byte[] buffer = new byte[sizeOfSegments];
				
				while((bytesAmount = bufferedIn.read(buffer, 0, buffer.length)) != -1) {
					//Compute hash of the read in buffer and add it to the file Hashes structure.
					fileHashes.add(java.util.Arrays.hashCode(buffer));
					System.out.println(java.util.Arrays.hashCode(buffer));
				}
				fileIn.close();
				bufferedIn.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	

	
	
	//Data structure that maps a chunkname to a class that contains integrity information about a chunk
	//This is a class just so I can synchronsize access to the internal datastructures. 
	private Map<String, FileInformation> fileToInformation;
	
	public ChunkServerFileRecords() {
		fileToInformation = new HashMap<String, FileInformation>();
	}
	
	public synchronized void addChunkFile(String chunkName) {
		fileToInformation.put(chunkName, new FileInformation(chunkName));
	}
	
	
}
