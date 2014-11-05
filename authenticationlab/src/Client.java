import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;


public class Client {
	
	
	public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException
	{
		Authenticator authservice = (Authenticator) Naming.lookup("rmi://localhost:1099/Printer");
		AuthenticatedTransmitterPrinter service = new AuthenticatedTransmitterPrinter("admin", "1234", authservice);
		service.print("file1", "printer1");
		service.print("file2", "printer2");
		service.print("file3", "printer3");
		service.start();
	
		
	}
	
}
