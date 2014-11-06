import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;


public abstract class AuthenticatedReceiver extends UnicastRemoteObject implements Authenticator {

	private Random random = new Random();
	private MessageAuthenticator msgAuth;
	private HashMap<String,Long> awaitingNonces = new HashMap<String, Long>();
	
	
	protected AuthenticatedReceiver(MessageAuthenticator msgAuth) throws RemoteException {
		super();
		this.msgAuth = msgAuth;
	}

	@Override
	public long getNOnce(String username) throws RemoteException {
		if (awaitingNonces.containsKey(username))
			return awaitingNonces.get(username);
		else
		{
			long ran = random.nextLong();
			awaitingNonces.put(username,ran);
			return ran;
		}
	}

	@Override
	public Serializable sendMessage(int msgId, Serializable[] load, String username, String hash) throws RemoteException {
		if (awaitingNonces.containsKey(username))
		{
			long nonce = awaitingNonces.get(username);
			String msgHash = AuthMsgHasher.generateMsgHash(msgId, load);
			if(msgAuth.checkHash(username, nonce, msgHash, hash))
			{
				awaitingNonces.remove(username);
				return respond(msgId, load);
			}
			
		}
		return null;
	}
	
	
	public abstract Serializable respond(int msgId, Serializable[] load);
}
