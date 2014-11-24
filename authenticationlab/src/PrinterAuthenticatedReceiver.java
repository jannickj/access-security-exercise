import java.io.Serializable;
import java.rmi.RemoteException;


@SuppressWarnings("serial")
public class PrinterAuthenticatedReceiver extends AuthenticatedReceiver {

	private Printer service;
	private PrinterMessages[] msgTypes = PrinterMessages.values(); 
	private SaltContainer salter;
	
	protected PrinterAuthenticatedReceiver(MessageAuthenticator msgAuth, SaltContainer salter, Printer service, MessageLogger logger)
			throws RemoteException {
		super(msgAuth, logger);
		this.service = service;
		this.salter = salter;
	}



	@Override
	public Serializable respond(int msgId, Serializable[] load) {
		PrinterMessages msgType = msgTypes[msgId];
		switch (msgType) {
		case PRINT:
			service.print((String) load[0], (String) load[1]);
			break;
		case QUEUE:
			return (Serializable) service.queue();
		case READ_CONFIG:
			return service.readConfig((String) load[0]);
		case RESTART:
			service.restart();
			break;
		case SET_CONFIG:
			service.setConfig((String) load[0], (String) load[1]);
			break;
		case START:
			service.start();
			break;
		case STATUS:
			return service.status();
		case STOP:
			service.stop();
			break;
		case TOP_QUEUE:
			service.topQueue((Integer) load[0]);
			break;
		
			
		}
		return null;
	}



	@Override
	public String getSalt(String username) throws RemoteException {
		return salter.getSalt(username);
	}

}
