package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import common.MessageListener;
import common.MessageSource;
import common.NetworkInterface;

/**
 * It is responsible for creating a NetworkInterface, reading input from 
 * the user, and sending that input to the server via the NetworkInterface. 
 * @author GJ (Gajjan Jasani)
 * @version 11/18/2015
 */
public class MultiuserSosClient extends MessageSource 
									implements MessageListener {

	/** Client socket */
	Socket s;
	/** network interface for exchanging messages */
	private NetworkInterface networkInterface;

	
	/**
	 * Constructor for MultiuseSosClient class
	 * @param hostname	Host address
	 * @param port Port number
	 * @param nickname user name
	 */
	public MultiuserSosClient(String hostname, int port, String nickname) {
		
		try {
			connect(hostname, port);
		} catch (IOException e) {
			System.err.println("Error while connecting to server!!!");
			System.exit(1);
		}
		networkInterface.sendMessage("/connect "+nickname);
		play();
	}

	/**
	 * Print the message received from the source
	 * @param message received message
	 * @param source message source
	 */
	@Override
	public void messageReceived(String message, MessageSource source) {
		System.out.println(message);

	}

	/**
	 * to close the source when message source is closed and no more
	 * messages are expected from the message source
	 * @param source message source
	 */
	@Override
	public void sourceClosed(MessageSource source) {
		System.exit(0);
	}

	/**
	 * to read the client input and send it to the server
	 */
	private void play() {
		BufferedReader input = new BufferedReader(
						new InputStreamReader(System.in));
		String userInput;
		try {
			while ((userInput = input.readLine()) != null) {
				networkInterface.sendMessage(userInput);
			}
		} catch (IOException e) {
			System.out.println("Input-Output Exception Caught");
		}
	}

	/**
	 * Connect to server and initialize the network interface and start 
	 * listening to the subject(message source)
	 * @param hostname host address
	 * @param port port number
	 * @throws IOException input output exception
	 */
	protected void connect(String hostname, int port) throws IOException {
		s = new Socket(hostname, port);
		networkInterface = new NetworkInterface(s);
		networkInterface.addMessageListener(this);
		new Thread(networkInterface).start();
		
	}

}
