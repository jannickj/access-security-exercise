import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;


public interface Authenticator extends Remote  {

	long getNOnce(String username)  throws RemoteException;
	String getSalt(String username) throws RemoteException;
	Object sendMessage(int msgId, Serializable[] load, String username, String hash)  throws RemoteException;
}
