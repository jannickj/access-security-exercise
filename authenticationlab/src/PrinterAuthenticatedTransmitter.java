import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.List;


public class PrinterAuthenticatedTransmitter extends AuthenticatedTransmitter implements Printer {

	public PrinterAuthenticatedTransmitter(String username, String password,
			Authenticator auth) {
		super(username, password, auth);
	}

	@Override
	public void print(String filename, String printer) {
		try {
			this.SendMessage(PrinterMessages.PRINT.getValue(), new Serializable[]{filename, printer});
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Pair<Integer, Pair<String, String>>> queue() {
		try {
			return (List<Pair<Integer, Pair<String, String>>>) this.SendMessage(PrinterMessages.QUEUE.getValue(), new Serializable[0]);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void topQueue(int job) {
		try {
			this.SendMessage(PrinterMessages.TOP_QUEUE.getValue(), new Serializable[]{job});
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void start() {
		try {
			this.SendMessage(PrinterMessages.START.getValue(), new Serializable[]{});
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void stop() {
		try {
			this.SendMessage(PrinterMessages.STOP.getValue(), new Serializable[]{});
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void restart() {
		try {
			this.SendMessage(PrinterMessages.RESTART.getValue(), new Serializable[]{});
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public String status() {
		try {
			return (String) this.SendMessage(PrinterMessages.STATUS.getValue(), new Serializable[]{});
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String readConfig(String parameter) {
		try {
			return (String) this.SendMessage(PrinterMessages.READ_CONFIG.getValue(), new Serializable[]{parameter});
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return parameter;
	}

	@Override
	public void setConfig(String parameter, String value) {
		try {
			this.SendMessage(PrinterMessages.SET_CONFIG.getValue(), new Serializable[]{parameter, value});
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
