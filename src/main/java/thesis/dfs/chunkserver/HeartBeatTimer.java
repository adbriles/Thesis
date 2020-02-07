package thesis.dfs.chunkserver;

import java.util.Date;
import java.util.concurrent.ExecutorService;

import thesis.dfs.sharedClasses.MajorHeartBeat;
import thesis.dfs.sharedClasses.MinorHeartBeat;

public class HeartBeatTimer implements Runnable{
	ExecutorService threadPool;
	private boolean continueTimer = true;
	private String hostName;
	private Integer portnum;
	private String controllerName;
	private Integer controllerPort;
	
	//Pass in the threadpool reference so I can toss on send tasks. 
	public HeartBeatTimer(ExecutorService threadPool, String hostName, Integer portnum, String controllerName, Integer controllerPort) {
		this.threadPool = threadPool;
		this.hostName = hostName;
		this.portnum = portnum;
		this.controllerName = controllerName;
		this.controllerPort = controllerPort;
	}
	//Just going to create my own timer to submit jobs on my threadpool. No need to use the timer class.
	public void killTimer() {
		continueTimer = false;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		int minorHeartBeatCount = 0;
		while(continueTimer) {
			long startTime = System.currentTimeMillis();
			long elapsedTime = 0L;

			while(elapsedTime < 30 * 1000) {
				elapsedTime = (new Date()).getTime() - startTime;
			}
			//Execute minor heartbeat
			if(minorHeartBeatCount > 8) {
				System.out.println("A major heartBeat occured");
				minorHeartBeatCount = 0;
				threadPool.execute(new MajorHeartBeat());
			} else {
				System.out.println("A minor heartbeat occured.");
				threadPool.execute(new MinorHeartBeat(hostName, portnum, controllerName, controllerPort));
				minorHeartBeatCount++;
				
			}
			
		}
		
		
	}
	
	
	
	
}
