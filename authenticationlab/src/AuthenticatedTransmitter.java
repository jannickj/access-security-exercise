import java.io.Serializable;
import java.rmi.RemoteException;


public class AuthenticatedTransmitter {

		private String username;
		private String password;
		private Authenticator auth;
		private long salt;
		private boolean haveSalt = false;
	
		public AuthenticatedTransmitter(String username, String password, Authenticator auth) 
		{
			this.username = username;
			this.password = password;
			this.auth = auth;
		}
		
		public Object SendMessage(int msgId, Serializable[] msg) throws RemoteException
		{
			if(!haveSalt)
			{
				salt = auth.getSalt(username);
				haveSalt = true;
			}
			long nonce = auth.getNOnce(username);
			String hash = AuthMsgHasher.generateUserHash(username, password, salt, AuthMsgHasher.generateMsgHash(msgId, nonce, msg));
			return auth.sendMessage(msgId, msg, username, nonce, hash);
			
				
			
		}
		
}
