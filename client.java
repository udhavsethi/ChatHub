import java.io.*;
import java.net.*;

class ClientThread implements Runnable
{
    private static Socket clientSocket = null;

	private static PrintStream outputstream = null;
	private static DataInputStream inputstream = null;
	
	private static BufferedReader inputLine = null;
	private static boolean flag = true;

	public static void main(String[] args) 
	{
		
		//setting default port and host name
		String host = "localhost";
		int port = 8080;

		// command line arguments
		if(args.length < 2)
		{
			System.out.println("Default port has been set: " + port);
		}
		else
		{
			host = args[0];
			port = Integer.parseInt(args[1]);
		}

		/*
		 * Now open a socket on host and port names set above
		*/
		try
		{
			clientSocket = new Socket(host,port);

			inputstream = new DataInputStream(clientSocket.getInputStream());
			outputstream = new PrintStream(clientSocket.getOutputStream());
			
			//inputLine reads lines from cmd
			inputLine = new BufferedReader(new InputStreamReader(System.in)); //default sized input buffer (can define size as well)

		} catch(IOException e)
		{
			System.err.println("IO exception");
			//e.printStackTrace();
		}

		if(clientSocket != null && inputstream != null && outputstream != null)
		{
			try
			{
				//creating thread to read from server
				ClientThread newclient = new ClientThread();
				Thread t = new Thread(newclient);
				t.start();	//executes a call to run () method

				while (flag == true)
				{
					outputstream.println(inputLine.readLine().trim());
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
		String response;
		try
		{
			while((response = inputstream.readLine()) != null)
			{
				if(response.indexOf("offline") != -1)
					break;
				System.out.println(response);
			}
			flag = false;		
		} catch(IOException e)
		{
			flag = false;
			System.err.println("IOException: " + e);
		}
	}
}