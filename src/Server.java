/**
 * Title: COMP4635 Assignment 1
 * This class is used for the Server that processes requests from clients and interfaces with WordRepository
 * micro-service.
 * Usage: java Server [5599] 
 * @author Mohamed Aly
 */ 

import java.net.*;
import java.io.*;

//Class invocation
public class Server {
	private static final String USAGE = "Usage: java Server [5599]";
	private ServerSocket serverSocket;
	private int counter;
	private String gameWord = "";

	public Server() throws IOException {
		this(5599);
	}

	public Server(int port) throws IOException {
		serverSocket = new ServerSocket(port);
	}

	public String setInitialGamePlay(int numWords) {
		//Setting initial stage of game based on assignment requirements.
		String initialPlay = "";
		char [] phraseChar =  gameWord.trim().toCharArray();
		for (int i = 0; i < phraseChar.length; i ++) {
			if(Character.isWhitespace(phraseChar[i])) {
				initialPlay+= " ";
			} else {
				initialPlay+= "-";
			}
		}
	
		return initialPlay+"C" + Integer.toString(counter);	

	}

	public void serve() {
		//Process the gameplay based on Client.java set parameters for word and factor attempts/assignment requirements.
		while(true) {
			try {
				System.out.println("Listening for incoming requests...");
				Socket clientSocket = serverSocket.accept();
				PrintStream out =
						new PrintStream(clientSocket.getOutputStream());
				BufferedReader in = new BufferedReader(
						new InputStreamReader(clientSocket.getInputStream()));
				String inputLine = in.readLine();
				int numWords = Integer.parseInt(inputLine.split(" ")[1]); 
				int factorAttempts = Integer.parseInt(inputLine.split(" ")[2]); 
				counter = numWords * factorAttempts;
				DatagramSocket socket = new DatagramSocket();
				byte[] buf = new byte[256];
				byte[] inputbuf = new byte[256];
				buf = Integer.toString(numWords).getBytes();
				InetAddress address = InetAddress.getByName("localhost");
				DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 5599);
				socket.send(packet);
				DatagramPacket udpReplyPacket = 
						new DatagramPacket(inputbuf, inputbuf.length, address, 5599);
				socket.receive(udpReplyPacket);
				gameWord = new String(udpReplyPacket.getData());
				String initialGamePlay = setInitialGamePlay(numWords);
				out.println(initialGamePlay);
				clientSocket.close();
			} catch (SocketException e) {
				System.out.println(e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
	}

	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			System.err.println(USAGE);
			System.exit(1);
		}
		
		int port = 0;
		Server server = null;
		try {
			port = Integer.parseInt(args[0]);
			server = new Server(port);
		} catch (IOException e) {
			System.out.println("Exception caught when trying to listen on port "
					+ port + " or listening for a connection");
			System.out.println(e.getMessage());
		}
		server.serve();
	}
}