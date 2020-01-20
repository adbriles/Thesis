package thesis.dfs.messages;

public class PutRequestMessage extends Message{

	public PutRequestMessage() {
		
	}
	
	public PutRequestMessage(String messageType) {
		super.setMessageType(messageType);
	}
	
	public PutRequestMessage(String messageType, String content, String senderHostName, int senderPort) {
		super.setMessageType(messageType);
		super.setContent(content);
		super.setSenderHostName(senderHostName);
		super.setSenderPort(senderPort);
	}
	
	

}
