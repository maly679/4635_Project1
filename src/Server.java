/**
 * Title: COMP4635 Task 2. Basic Socket Communication. Clients and Servers
 * Usage: java BasicEchoServer [port] 
 */
 
import java.net.*;
import java.io.*;

public class Server {
	private static final String USAGE = "Usage: java BasicEchoServer [port]";
	private ServerSocket serverSocket;
	public Server() throws IOException {
		this(5599);
	}
	
	public Server(int port) throws IOException {
		serverSocket = new ServerSocket(port);
	}
	
	public void serve() {
		while(true) {
			try {
				System.out.println("Listening for incoming requests...");
				Socket clientSocket = serverSocket.accept();
				PrintStream out =
					new PrintStream(clientSocket.getOutputStream());
				BufferedReader in = new BufferedReader(
					new InputStreamReader(clientSocket.getInputStream()));
				// Read the request and echo it back
				String inputLine = in.readLine();
				int numWords = Integer.parseInt(inputLine.split(" ")[1]); 
				System.out.println(numWords);
			
				   DatagramSocket socket = new DatagramSocket();

//			        
//			        while (!request.equals("QUIT")) {
//						System.out.println("\nSending the request: " 
//								+ request + " to the server!" );	        // send request
			        byte[] buf = new byte[1000];
			        buf = Integer.toString(numWords).getBytes();
			        InetAddress address = InetAddress.getByName("localhost");
			        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 5599);
			        socket.send(packet);
			        // get response
			        packet = new DatagramPacket(buf, buf.length);
			        socket.receive(packet);
			        
			        
				
				while (inputLine != null) {
					out.println(inputLine);
					System.out.println(inputLine);
					inputLine = in.readLine();
				}
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