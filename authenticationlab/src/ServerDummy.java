import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.codec.digest.DigestUtils;


public class ServerDummy {

	public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException, AlreadyBoundException, SQLException
	{
		int port = 1099;
		if (args.length > 0)
			port = new Integer(args[0]);
		
		Registry reg = LocateRegistry.createRegistry(port);
		
		final String salt = "0";
		
		PrinterLogger logger = new PrinterLogger() {
			
			@Override
			public void Write(String text) {
				System.out.println(text);
				
			}
		};
		
		MessageAuthenticator msgAuth = new MessageAuthenticator() {
			@Override
			public String signMessage(String username, String MessageHash) {
				return AuthMsgHasher.generateUserHash("admin", "1234", salt, MessageHash);
			}
		};
		
		SaltContainer salter = new SaltContainer() {
			@Override
			public String getSalt(String username) {
				return salt;
			}
		};
		AuthenticatedReceiverPrinter authenticatedService = new AuthenticatedReceiverPrinter(msgAuth,salter,new PrinterService(),logger);
		
		
		reg.rebind("Printer", authenticatedService);
		
	}
}
