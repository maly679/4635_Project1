
/**
 * Title: COMP4635 Assignment 1
 * This class is used for the Client that entails the user interface activities.
 * Usage: java Client [localhost] [5599] 
 * @author Mohamed Aly
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

//Class invocation 
public class Client {
	public static int numWords = 0;
	public static int failedAttemptsFactor = 0;
	private static final String USAGE = "java Client [host] [5599]";
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
		//instantiation of game.	
		String line = "";
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("If you would like to start a new game, enter yes: ");
		String userGeneratedRequest = in.readLine();
		if (userGeneratedRequest.equals("yes")) {
			numWords = processNumberofWords();
			failedAttemptsFactor = processFailedAttemptsFactor();
			
			line = "start " +  numWords + " " + failedAttemptsFactor;
		} else {
			getUserGeneratedRequest();
		}
		
		return line;
	}
	
	
	int processNumberofWords() throws IOException {
		//process number of words for game
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Please enter the number of words for the game: ");
		int numWords = Integer.parseInt(in.readLine());
		return numWords;
		
	}
	
	int processFailedAttemptsFactor() throws IOException {
		//process number of failed attempts for game
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Please enter the failed attempts factor: ");
		int failedAttempts =  Integer.parseInt(in.readLine());
		return failedAttempts;
	}
	

	void writeRequest(String UserGeneratedRequest) {
		//writing request to server
		System.out.println("\nSending the request: " + UserGeneratedRequest + " to the server!");
		try {
			PrintStream out = new PrintStream(clientSocket.getOutputStream());
			out.println(UserGeneratedRequest);
			out.println();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	void readAndProcessResponse(Client client) {
		System.out.println("\nWaiting for reply from the server!");
		try {
			//reading and processing next request
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			clientSocket.setSoTimeout(10000);
		
			String line = in.readLine();

			while (!line.equals("Quit")) {
				//print response from server
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
			//starts a new game
			String userGeneratedRequest = client.getUserGeneratedRequest();

			while(failedAttemptsFactor > 0)
			{
			client.writeRequest(userGeneratedRequest);
			client.readAndProcessResponse(client);
			}
	
		} catch (NumberFormatException e) {
			System.err.println("Invalid port number: " + args[1] + ".");
			System.exit(1);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}

	}
}