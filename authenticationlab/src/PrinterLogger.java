import java.io.Serializable;
import java.sql.Time;
import java.util.Date;


public abstract class PrinterLogger implements MessageLogger {

	@Override
	public void LogMessage(String sender, int msgId, Serializable[] message,
			String comment) {
		PrinterMessages msgType = PrinterMessages.values()[msgId];
		Date d = new Date();
		
		String input = "[";
		for(int i=0; i<message.length; i++)
			input += message[i]+", ";
		input = message.length > 0 ? input.substring(0, input.length()-2)+"]" : "";
		
		Write(d+": "+sender+" sent "+msgType.name()+" "+input+", Result: "+comment);
	}
	
	public abstract void Write(String text);

}
