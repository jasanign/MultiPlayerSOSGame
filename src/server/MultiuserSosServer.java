package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import common.MessageListener;
import common.MessageSource;
import common.NetworkInterface;

/**
 * MultiuserSosServer is one of the classes that implement the 
 * server-side logic of this client-server application. It is 
 * responsible for accepting incoming connections, creating 
 * NetworkInterfaces, and passing the Network-Interface off to 
 * threads for processing.
 * @author GJ (Gajjan Jasani)
 * @version 11/18/2015
 */
public class MultiuserSosServer implements MessageListener {
	/** Game board size*/
	private int size;
	/** server socket for accepting connections */
	ServerSocket serverSocket;
	/** creating a game on server */
	private Game game;
	/** clients connected to server */
	private Map<String, MessageSource> clients;
	
	/**
	 * constructor for the MultiuserSosServer class
	 * @param port port number
	 * @param size board size
	 */
	public MultiuserSosServer(int port, int size) {
		this.size = size;
		clients = new HashMap<String, MessageSource>();
		try {
			serverSocket = new ServerSocket(port);

		} catch (IOException e) {
			System.out.println("Exception caught: Input-output exception");
			System.exit(1);
		}

	}

	/**
	 * Server listening for the client connections
	 */
	public void listen() {
		System.out.println("MultiuserSosServer is running...");
		while (serverSocket != null) {
			try {
				Socket client = serverSocket.accept();
				NetworkInterface networkInterface = new NetworkInterface(client);
				networkInterface.addMessageListener(this);
				new Thread(networkInterface).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	/**
	 * Message received from the clients as per the private protocol
	 * @param message message
	 * @param source message source
	 */
	@Override
	public void messageReceived(String message, MessageSource source) {
		String command = message.split(" ")[0];
		String paramsStr = message.replaceFirst(command, "").trim();

		switch (command) {
		case "/connect":
			connectUser(paramsStr, source);
			break;
		case "/play":
			play(source);
			break;
		case "/move":
			move(paramsStr, source);
			break;
		}

	}
	
	/**
	 * command to connect the client to server
	 * @param nickname player name
	 * @param source message source
	 */
	private void connectUser(String nickname, MessageSource source) {

		if (clients.containsKey(nickname)) {
			((NetworkInterface) source).sendMessage("User " + nickname
					+ " already connected. Choose another nickname.");
		} else if (game != null && game.isInProgress()) {
			((NetworkInterface) source).sendMessage("Play in progress.");
			return;
		} else {
			clients.put(nickname, source);
			broadcastMessage(nickname + " has joined");
		}
	}
	
	/**
	 * Command to start the game when enough players are connected
	 * @param source message source
	 */
	private void play(MessageSource source) {

		// ignore command if game is in progress
		if (game != null && game.isInProgress()) {
			((NetworkInterface) source).sendMessage("Play already in"
					+ " progress.");
			return;
		}

		if (clients.size() < 2) {
			((NetworkInterface) source).sendMessage("Not enough players"
					+ " to play the game.");
		} else {
			// start the game
			game = new Game(size, clients.keySet().toArray(
					new String[clients.keySet().size()]));
			String message = System.lineSeparator() + game.getBoardStr();
			message += System.lineSeparator() + game.getPlayerTurn() 
					+ " it is your turn";
			broadcastMessage(message);

		}
	}

	/**
	 * command for a player to make a move in the game
	 * @param paramsStr parameters for move
	 * @param source message source
	 */
	private void move(String paramsStr, MessageSource source) {
		// ignore command if game is not in progress
		if (game == null || !game.isInProgress()) {
			((NetworkInterface) source).sendMessage("Play not in progress.");
			return;
		}

		String client = getClientFromValue(source);
		// ignore command if it is not player turn
		if (!game.isPlayerTurn(client)) {
			((NetworkInterface) source).sendMessage("Not your turn.");
			return;
		}

		String[] params = paramsStr.split(" ");
		if (params.length < 3) {
			broadcastMessage("Invalid move:" + game.getPlayerTurn() 
					+ " it is your turn");
		}

		try {
			int row = Integer.parseInt(params[1]);
			int col = Integer.parseInt(params[2]);
			if (!game.isValidMove(row, col) || (!params[0].equalsIgnoreCase(
					"S") && !params[0].equalsIgnoreCase("O"))) {
				broadcastMessage("Invalid move:" + game.getPlayerTurn() 
						+ " it is your turn");
				return;
			}

			String messStatus = game.move(params[0].charAt(0), row, col);
			broadcastMessage(messStatus);

		} catch (NumberFormatException e) {
			broadcastMessage("Invalid move:" + game.getPlayerTurn() 
					+ " it is your turn");
		}

	}

	/**
	 * find and return the current player
	 * @param source message source
	 * @return current player
	 */
	private String getClientFromValue(MessageSource source) {
		for (String client : clients.keySet()) {
			if (clients.get(client).equals(source)) {
				return client;
			}
		}
		return null;
	}

	/**
	 * Sent a message to each player in the game
	 * @param message message
	 */
	private void broadcastMessage(String message) {
		for (MessageSource client : clients.values()) {
			((NetworkInterface) client).sendMessage("--->"+message);

		}
	}

	/**
	 * handles the even of a player leaving the game, broadcasts who left
	 * and current score to each player
	 * @param source message source
	 */
	@Override
	public void sourceClosed(MessageSource source) {

		String client = getClientFromValue(source);
		clients.remove(client);
		((NetworkInterface) source).sendMessage("/quit");
		broadcastMessage(client + " has quit");
		if (game.isInProgress()) {
			broadcastMessage(game.gameOver());
		}
	}

}
