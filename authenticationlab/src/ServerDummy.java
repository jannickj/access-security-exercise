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
		//user = 3
		//puser = 39
		//tech = 504
		//manag = 511
		long user = 	
				PrinterMessages.PRINT.getBinaryCode() |
				PrinterMessages.QUEUE.getBinaryCode();
		long puser = user |
				PrinterMessages.TOP_QUEUE.getBinaryCode() |
				PrinterMessages.RESTART.getBinaryCode();
		long tech = 
				PrinterMessages.SET_CONFIG.getBinaryCode() |
				PrinterMessages.STOP.getBinaryCode() |
				PrinterMessages.RESTART.getBinaryCode() |
				PrinterMessages.READ_CONFIG.getBinaryCode() |
				PrinterMessages.STATUS.getBinaryCode() |
				PrinterMessages.START.getBinaryCode();
		long manag = puser | tech;
		
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
		PrinterAuthenticatedReceiver authenticatedService = new PrinterAuthenticatedReceiver(msgAuth,salter,new PrinterService(),logger);
		
		
		reg.rebind("Printer", authenticatedService);
		
	}
}
