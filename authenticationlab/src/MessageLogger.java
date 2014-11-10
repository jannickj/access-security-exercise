import java.io.Serializable;


public interface MessageLogger {
	void LogMessage(String sender,int msgId, Serializable[] message, String comment);
}
