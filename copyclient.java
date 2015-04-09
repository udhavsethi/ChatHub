import java.io.*;
import java.net.*;
import java.util.*;

class ClientThread implements Runnable
{
	private static Socket clientSocket = null;

	private static DataOutputStream outputstream = null;
	private static DataInputStream inputstream = null;

	private static BufferedReader reader = null;
	
	private static boolean flag = true;
	static String inputLine = null;
	static String userName = "Me";

	public static void main(String[] args) 
	{
		
		//setting default port and host name
		String host = "localhost";
		int port = 8080;

		// command line arguments 172.16.8.55
		if(args.length < 2)
		{
			System.out.println("Default port has been set: " + port);
		}
		else
		{
			host = args[0];
			port = Integer.parseInt(args[1]);
		}

		//Now open a socket on host and port names set above
		try
		{
			clientSocket = new Socket(host,port);

			inputstream = new DataInputStream(clientSocket.getInputStream());
			outputstream = new DataOutputStream(clientSocket.getOutputStream());

			reader = new BufferedReader(new InputStreamReader(System.in));
			
		} catch(IOException e)
		{
			System.err.println("IO exception");
		}

		if(clientSocket != null && inputstream != null && outputstream != null)
		{
			//String response;
			try
			{
				//creating thread to read from server
				ClientThread newclient = new ClientThread();
				Thread t = new Thread(newclient);
				t.start();	//executes a call to run () method

				//Scanner scan = new Scanner( System.in );
				System.out.print("Choose your NickName: ");
				userName = reader.readLine().trim();
				outputstream.writeUTF(userName + ": ");
				System.out.println("***Welcome " + userName + ", You are now online!***");
				System.out.println("***Type 'quit' to go offline; Type 'list' to see who's online***");

				while (flag)
				{
					System.out.print(userName+": ");
					inputLine = reader.readLine().trim();
					if(inputLine != null && !inputLine.isEmpty())
					{
						if(inputLine.equals("quit"))
						{						
							outputstream.writeUTF("***" + userName + " is now offline!***");
							flag = false;
							break;
						}
						outputstream.writeUTF(userName+": "+inputLine);	
					}	
				}

				// Close the output stream, input stream and socket in reverse order that they were created 
				outputstream.close();
				inputstream.close();
				clientSocket.close();

			} catch(IOException e)
			{
				e.printStackTrace();
			} 
		}
	}

	public void run()
	{
		try
		{
			
			while(true)
			{				
				System.out.println(inputstream.readUTF());
			}
					
		} catch(IOException e)
		{
			flag = false;
			System.err.println("***You are now offline!***");
		}
	}
}