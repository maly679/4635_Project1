	import java.io.BufferedReader;
	import java.io.File;
	import java.io.FileReader;
	import java.io.IOException;
	import java.io.InputStreamReader;
	import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
	import java.net.SocketException;
	import java.net.SocketTimeoutException;
	import java.net.UnknownHostException;
	import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
	
	public class WordRepository {
		private static final String USAGE = "Usage: java BasicUDPTimeServer [port]";
		DatagramSocket socket;
		public WordRepository() throws IOException {
			this(5599);
			
		}
		
		public WordRepository(int port) throws IOException {
			socket = new DatagramSocket(port);
		}
		
		public void serve() {
			while(true) {
				try {
					System.out.println("Listening for incoming requests...");
	                byte[] inputbuf = new byte[256];
	                byte[] outputbuf = new byte[256];
	                
	                // receive request
	                DatagramPacket udpRequestPacket = new DatagramPacket(inputbuf, inputbuf.length);
	                socket.receive(udpRequestPacket);
	                
//	                // create response (This is a date server, regardless of what the request is, send the date!)
//	                String dateandLastAccessString = new Date().toString() + lastAccess;
//	                if (lastAccess != null) {
//	                outputbuf = dateandLastAccessString.getBytes();
//	                } else {
//	     
//	                	outputbuf = "yes".getBytes();
//
//	                }
//	        		// Send the response to the client.
//	                // Address and port are extracted from client request message. 
//	                InetAddress address = udpRequestPacket.getAddress();
//	                int port = udpRequestPacket.getPort();
//	                DatagramPacket udpReplyPacket = 
//	                		new DatagramPacket(outputbuf, outputbuf.length, address, port);
//
//	                socket.send(udpReplyPacket);
	                
	                System.out.println(new String(udpRequestPacket.getData()));
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
//			BufferedReader bufReader = new BufferedReader(new FileReader("words.txt")); 
//			ArrayList<String> words = new ArrayList<>();
//			String line = bufReader.readLine(); 
//			while (line != null) { words.add(line); 
//			line = bufReader.readLine();
//			}
//			bufReader.close();
			
			server.serve();
			server.socket.close();
 
		}
	
}
