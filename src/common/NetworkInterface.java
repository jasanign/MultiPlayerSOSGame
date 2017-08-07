package common;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


/**
 * This class is responsible for sending messages to and receiving 
 * messages from remote hosts.
 * @author GJ (Gajjan Jasani)
 * @version 11/18/2015
 */
public class NetworkInterface extends MessageSource implements Runnable {
	/** for reading input stream */
	private BufferedReader in;
	/** for writing to the output stream */
	private PrintWriter out;
	/** for closing the game */
	private boolean done = false;
	/** socket for connection */
	private Socket socket;

	/**
	 * Constructor of the NetworkInterface class
	 * @param socket socket
	 */
	public NetworkInterface(Socket socket) {
		try {
			this.socket = socket;
			in = new BufferedReader(new InputStreamReader
								(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			System.out.println("Error in connection socket");
		}

	}

	@Override
	public void run() {
		String msg = "";
		try {
			while (!done) {
				msg = in.readLine();
				if (msg.equalsIgnoreCase("/quit")) {
					closeMessageSource();
					done = true;
				} else {
					notifyReceipt(msg);
				}
			}
		} catch (Exception e) {
			done = true;
			System.err.println("Error occured during proccess client request");
			try {
				// close the socket
				socket.close();
			} catch (Exception se) {
				System.out.println("ERROR CLOSING SOCKET " + se);
			}
		}
	}

	/**
	 * Print the message to the output stream
	 * @param message message
	 */
	public void sendMessage(String message) {
		out.println(message);
		out.flush();
	}

}
