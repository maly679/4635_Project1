/**
 * Title: COMP4635 Task 2. Basic Socket Communication. Clients and Servers
 * Usage: java SingleRequestClient [host] [port] [request]
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class App {
	private static final String USAGE = 
	"java SingleRequestClient [host] [port] [request]";
	private Socket clientSocket;

	public App(String host, int port) {
		try {
			clientSocket = new Socket(host, port);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	void writeRequest(String request) {
		System.out.println("\nSending the request: " 
								+ request + " to the server!" );
		try {
			// Create output streams & write the request to the server
			PrintStream out = 
				new PrintStream(clientSocket.getOutputStream());
			out.println(request);
			out.println();	
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	void readAndPrintResponse() {
		System.out.println("\nWaiting for reply from the server!");
		try {
			BufferedReader in = new BufferedReader(
					new InputStreamReader(clientSocket.getInputStream()));
			clientSocket.setSoTimeout(10000);
			String line = in.readLine();
			while (line != null) {
				System.out.println(line);
				line = in.readLine();
			}
			in.close();
			clientSocket.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
	}

	public static void main(String[] args) throws IOException {
		App client = null;
		
        if (args.length != 3) {
			System.out.println(USAGE);
			System.exit(1);
		}

		try {
			client = new App(
								args[0], 
								Integer.parseInt(args[1])
								);
			client.writeRequest(args[2]);
			client.readAndPrintResponse();
		} catch (NumberFormatException e) {
			System.err.println("Invalid port number: " + args[1] + ".");
			System.exit(1);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
	}
}