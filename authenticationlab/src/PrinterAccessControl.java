import java.io.Serializable;
import java.util.HashMap;


public class PrinterAccessControl implements AccessControlContainer {

	private PrinterMessages[] msgTypes = PrinterMessages.values(); 
	private HashMap<String, Long> mapping = new HashMap<String, Long>();
	
	public PrinterAccessControl(HashMap<String, Long> mapping)
	{
		this.mapping = mapping;		
	}
	
	@Override
	public boolean HasAccess(int msgId, Serializable[] load, String username) {
		PrinterMessages msgType = msgTypes[msgId];
		long accesses = (long) mapping.get(username);
		long accessGranted = accesses & msgType.getBinaryCode();
		return accessGranted > 0;
		
	}

}
