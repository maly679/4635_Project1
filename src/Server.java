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
	private static ServerSocket serverSocket;
	private int counter;
	private static String gameWord = "";
	private static String gameWordFixed = "";
	private static int failedAttemptsFactor;
	private static String blanks;
	private static String userSelection = "";
	
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
	//	System.out.println(gameWord);
		for (int i = 0; i < phraseChar.length; i ++) {
			if(Character.isWhitespace(phraseChar[i])) {
				initialPlay+= " ";
			} else {
				initialPlay+= "-";
			}
		}
		return initialPlay; //+"C" + failedAttemptsFactor;//Integer.toString(counter);	

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

				failedAttemptsFactor = factorAttempts  * numWords;
				
				//counter = numWords * factorAttempts;
				//System.out.println("fail attempts: " + failedAttemptsFactor);
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
				blanks = initialGamePlay;
				out.println(initialGamePlay);
				
				  while (!in.readLine().equals(null)) {
				        userSelection = in.readLine();
					//	System.out.println(userSelection);
				       enterWord(userSelection);
					//System.out.println(userSelection);
					  }				 

				clientSocket.close();
			} catch (SocketException e) {
				System.out.println(e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
	}
	
	
	 public static void enterWord(String userSelection) throws IOException {
	        /*
	         * BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	         * System.out.print("Please guess a letter or solve the phrase: ");
	         * String userGeneratedRequest = in.readLine();
	         */
		 
//			DatagramSocket socket = new DatagramSocket();
//		 byte[] buf = new byte[256];
//			byte[] inputbuf = new byte[256];
////			buf = Integer.toString(numWords).getBytes();
//			InetAddress address = InetAddress.getByName("localhost");
//			DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 5599);
////			socket.send(packet);
//			DatagramPacket udpReplyPacket = 
//					new DatagramPacket(inputbuf, inputbuf.length, address, 5599);
//			socket.receive(udpReplyPacket);
//		String userSelection = new String(udpReplyPacket.getData());
////			String initialGamePlay = setInitialGamePlay(numWords);
////			blanks = initialGamePlay;
//			System.out.println(userSelection);
////			clientSocket.close();
			boolean win = false;
	        if (failedAttemptsFactor > 0) {
	            
	            if (userSelection.length() > 1) {

					gameWordFixed = gameWord.substring(0,userSelection.length());
	                
					if (userSelection.equals(gameWordFixed)) {
	                    System.out.println("You are correct!");
	                    win = true;
	                } else {
	                    System.out.println("Incorrect Guess");
	                    failedAttemptsFactor -=1;
	                }
	            } else {
	                Character userChar = userSelection.charAt(0);

	                if (userSelection.equals("*")) 
	                {

	                    System.out.println("Creating new game");
	                    //loop to top
	                }

	                else if (userSelection.equals(".")) {
	                    System.out.println("Ending game");
	                    return;
	                } else {
	                    if (gameWord.contains(userSelection)) {
	                        for (int i = 0; i < gameWord.length(); i++) {

	                            char[] blankChar = blanks.toCharArray();
	                            if (gameWord.charAt(i) == userChar) {
	                                blankChar[i] = userChar;
	                                blanks = String.valueOf(blankChar);
	                            }
	                        }
	                    } else {

	                        failedAttemptsFactor = failedAttemptsFactor - 1;
	                        System.out.println("Letter not found");
	                    }

	                }
	            }
	            if(failedAttemptsFactor == 0)
	            {
	                System.out.println("You lose");
	            }
	         }
			
			 if(win)
			 
				 System.out.println(gameWord + "C" + failedAttemptsFactor);
			 
			 else
			 {
			 System.out.println(blanks + "C" + failedAttemptsFactor);
			
			 }
	        return;
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