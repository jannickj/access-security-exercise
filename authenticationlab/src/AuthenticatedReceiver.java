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
	private MessageLogger logger;
	
	protected AuthenticatedReceiver(MessageAuthenticator msgAuth, MessageLogger logger) throws RemoteException {
		super();
		this.logger = logger;
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
		Serializable resp = null;
		if (awaitingNonces.containsKey(username))
		{
			long nonce = awaitingNonces.get(username);
			String msgHash = AuthMsgHasher.generateMsgHash(msgId, load);
			String signatureHash = msgAuth.signMessage(username, msgHash);
			if(signatureHash != null && AuthMsgHasher.generatedNoncedHash(nonce, signatureHash).equals(hash))
			{
				logger.LogMessage(username, msgId, load, "Was authenticated by "+hash+" using nonce "+nonce+" and message signature "+signatureHash);
				awaitingNonces.remove(username);
				resp = respond(msgId, load);
			}
			else
				logger.LogMessage(username, msgId, load, "Message could not be authenticated by "+hash);
			
		}
		
		return resp;
	}
	
	
	public abstract Serializable respond(int msgId, Serializable[] load);
}
