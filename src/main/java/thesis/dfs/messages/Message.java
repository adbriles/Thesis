package thesis.dfs.messages;

import java.io.File;
import java.io.Serializable;
import java.util.LinkedList;

public class Message implements Serializable{
	private String senderHostName;
	private int senderPort;
	private boolean isReplacementChunk = false;
	private String messageType;
	private String content;
	
	
	private boolean readFile = false;
	
	private File file;
	
	private LinkedList<String> list;
	private LinkedList<String> secondList;
	
	public Message() {
		
	}
	
	public Message(String messageType) {
		this.messageType = messageType;
	}
	
	public Message(String messageType, String content) {
		this.messageType = messageType;
		this.setContent(content);
	}
	
	public Message(String messageType, String content, LinkedList<String> list) {
		this.messageType = messageType;
		this.setContent(content);
		this.setList(list);
	}
	
	public Message(String messageType, String content, String senderHostName, int senderPort) {
		this.messageType = messageType;
		this.setContent(content);
		this.setSenderHostName(senderHostName);
		this.setSenderPort(senderPort);
	}
	
	public String getMessageType() {return messageType;}
	
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public int getSenderPort() {
		return senderPort;
	}

	public void setSenderPort(int senderPort) {
		this.senderPort = senderPort;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSenderHostName() {
		return senderHostName;
	}

	public void setSenderHostName(String senderHostName) {
		this.senderHostName = senderHostName;
	}

	public LinkedList<String> getList() {
		return new LinkedList<String>(list);
	}

	public void setList(LinkedList<String> list) {
		this.list = list;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public boolean isReadFile() {
		return readFile;
	}

	public void setReadFile(boolean readFile) {
		this.readFile = readFile;
	}

	public LinkedList<String> getSecondList() {
		return secondList;
	}

	public void setSecondList(LinkedList<String> secondList) {
		this.secondList = secondList;
	}

	public boolean isReplacementChunk() {
		return isReplacementChunk;
	}

	public void setReplacementChunk(boolean isReplacementChunk) {
		this.isReplacementChunk = isReplacementChunk;
	}
	
}
