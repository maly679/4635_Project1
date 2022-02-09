
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

public class Client {
	private static final String USAGE = "java SingleRequestClient [host] [port] [UserGeneratedRequest]";
	private static Socket clientSocket;

	public Client(String host, int port) {
		try {
			clientSocket = new Socket(host, port);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	String getUserGeneratedRequest() throws IOException {
		
		String line = "";
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("If you would like to start a new game, enter yes: ");
		String userGeneratedRequest = in.readLine();
		
		if (userGeneratedRequest.equals("yes")) {
			int numWords = processNumberofWords();
			int failedAttemptsFactor = processFailedAttemptsFactor();
			System.out.println(numWords);
			System.out.println(failedAttemptsFactor);
			line =  "start " + numWords + " " + failedAttemptsFactor;
		} else {
			getUserGeneratedRequest();
		}
		
		return line;
	}
	
	int processNumberofWords() throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Please enter the number of words for the game: ");
		int numWords = Integer.parseInt(in.readLine());
		return numWords;
		
	}
	
	int processFailedAttemptsFactor() throws IOException {

		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Please enter the failed attempts factor: ");
		int failedAttempts =  Integer.parseInt(in.readLine());
		return failedAttempts;
	}
	
	//writes the request to the server!
	void writeRequest(String UserGeneratedRequest) {
		
		System.out.println("\nSending the request: " + UserGeneratedRequest + " to the server!");
		try {
			// Create output streams & write the request to the server
			PrintStream out = new PrintStream(clientSocket.getOutputStream());
			out.println(UserGeneratedRequest);
			out.println();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	void readAndPrintResponse(Client client) {
		System.out.println("\nWaiting for reply from the server!");
		try {
			//reading response from server..
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			clientSocket.setSoTimeout(10000);
			String line = in.readLine();

			while (!line.equals("Quit")) {
				//print response from server..
				System.out.println(line);
				line = client.getUserGeneratedRequest();
				client.writeRequest(line);
			}
			in.close();
			clientSocket.close();
			return;
		} catch (IOException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
	}

	public static void main(String[] args) throws IOException {
		Client client = null;

		if (args.length != 2) {
			System.out.println(USAGE);
			System.exit(1);
		}

		try {
			
			client = new Client(args[0], Integer.parseInt(args[1]));
			String userGeneratedRequest = client.getUserGeneratedRequest();
			client.writeRequest(userGeneratedRequest);
			client.readAndPrintResponse(client);
		
			
	
		} catch (NumberFormatException e) {
			System.err.println("Invalid port number: " + args[1] + ".");
			System.exit(1);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
	}
}