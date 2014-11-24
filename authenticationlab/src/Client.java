import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;


public class Client {
	
	
	public static void main(String[] args) throws NotBoundException, IOException
	{
		String server = "localhost:1099";
		if (args.length >= 1)
			server = args[0];
		Authenticator authservice = (Authenticator) Naming.lookup("rmi://"+server+"/Printer");
		PrinterAuthenticatedTransmitter service = null;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("type \"help\" to see available commands");
		
		while(true)
		{
			System.out.print("> ");
			String input = br.readLine();
			try
			{
				String[] words = input.split(" ");
				
				switch(words[0].toLowerCase())
				{
				case "login":
					service = new PrinterAuthenticatedTransmitter(words[1], words[2], authservice);
					break;
				case "print":
					service.print(words[1], words[2]);
					break;
				case "queue":
					printQueue(service.queue());
					break;
				case "topqueue":
					service.topQueue(new Integer(words[1]));
					break;
				case "start":
					service.start();
					break;
				case "stop":
					service.stop();
					break;
				case "restart":
					service.restart();
					break;
				case "status":
					System.out.println(service.status());
					break;
				case "readconfig":
					System.out.println(words[1]+": "+ service.readConfig(words[1]));
					break;
				case "setconfig":
					service.setConfig(words[1], words[2]);
					break;
				case "help":
					System.out.println(
							"Available Commands:\n"+
					"login [username] [password] 	login to the print server\n"+
					"print [filename] [printer]	prints file filename on the specified printer \n"+
					"queue				lists the print queue on the user's display in lines of the form <job number>   <file name>\n"+
					"topqueue [job id]		moves job to the top of the queue\n"+
					"start				starts the print server\n"+
					"stop				stops the print server\n"+
					"restart				stops the print server, clears the print queue and starts the print server again\n"+
					"status				prints status of printer on the user's display\n"+
					"readConfig [parameter]		prints the value of the parameter on the user's display\n"+
					"setConfig [parameter] [value]	sets the value of the parameter\n");
					break;
				}
			} catch(Exception e)
			{
				if (service == null)
					System.out.println("not logged in!");
				else
					System.out.println("fatal error! "+e.getMessage());
			}
		}
		}
		
	
	private static void printQueue(List<Pair<Integer, Pair<String, String>>> list)
	{
		for(Pair<Integer, Pair<String, String>> elem : list)
		{
			System.out.println(elem.getVal1()+": "+elem.getVal2().getVal1()+ " on "+elem.getVal2().getVal2() );			
		}
	}
	
}
