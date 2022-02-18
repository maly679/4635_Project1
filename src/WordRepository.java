/**
 * Title: COMP4635 Assignment 1
 * This class is used as a word repository that is to store and process words and word changes during the game.
 * Usage: java WordRepository [5599] 
 * @author Mohamed Aly Erik Szilagyi
 */ 

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

//Class invocation
public class WordRepository {
	private static final String USAGE = "Usage: java WordRepository [5599]";
	DatagramSocket socket;
	static ArrayList<String> words = new ArrayList<>();
	public WordRepository() throws IOException {
		this(5599);
	}

	public WordRepository(int port) throws IOException {
		socket = new DatagramSocket(port);
	}

	public String generateRandomWord(int wordCount) {
	//Generate a random word based on server's request.

		String generatedWord = "";
		for (int i = 0; i < wordCount; i++) 
		{
			// generating the index using Math.random()
			int index = (int) ((Math.random() * (words.size() - 1)) + 1);

			generatedWord = generatedWord + words.get(index).toLowerCase()  + " " ;
		}
		return generatedWord;

	}

	public void serve() {
		//Process UDP based communication, implementing required logic.
		while(true) {
			try {
				System.out.println("Listening for incoming requests...");
				byte[] inputbuf = new byte[256];
				byte[] outputbuf = new byte[256];

				// receive request
				DatagramPacket udpRequestPacket = new DatagramPacket(inputbuf, inputbuf.length);
				socket.receive(udpRequestPacket);	              
				outputbuf =  generateRandomWord(Integer.parseInt(new String(udpRequestPacket.getData()).trim())).getBytes();
				InetAddress address = udpRequestPacket.getAddress();
				int port = udpRequestPacket.getPort();

				DatagramPacket udpReplyPacket = 
						new DatagramPacket(outputbuf, outputbuf.length, address, port);

				socket.send(udpReplyPacket);

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
		
		//location set for words text file
		BufferedReader bufReader = new BufferedReader(new FileReader("../words.txt")); 
		String line = bufReader.readLine(); 
		while (line != null) { words.add(line); 
		line = bufReader.readLine();
		}
		bufReader.close();
		int port = 0;
		WordRepository server = null;
		try {
			port = Integer.parseInt(args[0]);
			server = new WordRepository(port);

		} catch (NumberFormatException e) {
			System.err.println("Invalid port number: " + port + ".");
			System.exit(1);
		} catch (IOException e) {
			System.out.println("Exception caught when trying to listen on port "
					+ port);
			System.out.println(e.getMessage());
		}


		server.serve();
		server.socket.close();

	}

}