import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;


public class AccessControlAuthenticator extends UnicastRemoteObject implements Authenticator {

	private static final long serialVersionUID = 7369954102401318388L;
	
	private AccessControlContainer container;
	private Authenticator base;
	private MessageLogger logger;
	
	protected AccessControlAuthenticator(Authenticator base,AccessControlContainer container,MessageLogger logger) throws RemoteException {
		super();
		this.container = container;
		this.base = base;
		this.logger = logger;
		
	}

	@Override
	public long getNOnce(String username) throws RemoteException {
		return  this.base.getNOnce(username);
	}

	@Override
	public String getSalt(String username) throws RemoteException {
		return this.base.getSalt(username);
	}

	@Override
	public Object sendMessage(int msgId, Serializable[] load, String username,String hash) throws RemoteException {

		if (container.HasAccess(msgId, load, username))
			return this.base.sendMessage(msgId, load, username, hash);
		else
		{
			logger.LogMessage(username, msgId, load, "Command not accepted by the access of the user");
			return null;
		}
	}

}
