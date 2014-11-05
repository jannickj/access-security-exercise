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
	public Serializable sendMessage(int msgId, Serializable[] load, String username,
			long Nonce, String hash) throws RemoteException {
		if (awaitingNonces.containsKey(username) && awaitingNonces.get(username) == Nonce)
		{
			String msgHash = AuthMsgHasher.generateMsgHash(msgId, Nonce, load);
			if(msgAuth.checkHash(username, msgHash, hash))
			{
				awaitingNonces.remove(username);
				return respond(msgId, load);
			}
			
		}
		return null;
	}
	
	
	public abstract Serializable respond(int msgId, Serializable[] load);
}
