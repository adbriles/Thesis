package thesis.dfs.messages;

import java.io.Serializable;

public class Message implements Serializable{
	private String senderHostName;
	private int senderPort;
	
	private String messageType;
	private String content;
	
	
	
	public Message(String messageType) {
		this.messageType = messageType;
	}
	
	public Message(String messageType, String content, String senderHostName, int senderPort) {
		this.messageType = messageType;
		this.setContent(content);
		this.setSenderHostName(senderHostName);
		this.setSenderPort(senderPort);
	}
	
	public String getMessageType() {return messageType;}

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
	
}