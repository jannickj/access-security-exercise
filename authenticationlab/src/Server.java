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


public class Server {

	public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException, AlreadyBoundException, SQLException
	{
		Registry reg = LocateRegistry.createRegistry(1099);
//		MessageAuthenticator msgAuth = new MessageAuthenticator() {
//			
//			@Override
//			public boolean checkHash(String username, String MessageHash,
//					String FullHash) {
//				// TODO Auto-generated method stub
//				System.out.println(username);
//				System.out.println(MessageHash);
//				System.out.println(FullHash);
//				return AuthMsgHasher.generateUserHash("timmy", "123", 0, MessageHash).equals(FullHash);
//			}
//		};
//		
//		SaltContainer salter = new SaltContainer() {
//			
//			@Override
//			public long getSalt(String username) {
//				// TODO Auto-generated method stub
//				return 2827251440285755392L;
//			}
//		};
		try {
		    System.out.println("Loading mysql driver...");
		    Class.forName("com.mysql.jdbc.Driver");
		    System.out.println("Driver mysql loaded!");
		} catch (ClassNotFoundException e) {
		    throw new RuntimeException("Cannot find the driver in the classpath!", e);
		}
		
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/printer_authentication?noAccessToProcedureBodies=true", "printer_server", "password");

		MySqlConnector mysqlConn = new MySqlConnector(connection);
		AuthenticatedReceiverPrinter authenticatedService = new AuthenticatedReceiverPrinter(mysqlConn,mysqlConn,new PrinterService());
		reg.rebind("Printer", authenticatedService);
		
	}
}
