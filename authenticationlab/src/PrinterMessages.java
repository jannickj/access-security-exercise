import java.rmi.RemoteException;
import java.util.List;


public enum PrinterMessages {
	
	
	PRINT(0),
	QUEUE(1),
	TOP_QUEUE(2),
	START(3),
	STOP(4),
	RESTART(5),
	STATUS(6),
	READ_CONFIG(7),
	SET_CONFIG(8);

	private int value;
	private int binaryCode;
	
	public int getValue() {
        return value;
    }
	public long getBinaryCode() {
		return 1 << value;
    }
	
	private PrinterMessages(int val) {
		this.value = val;
	}

}
