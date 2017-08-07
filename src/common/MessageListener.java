package common;
/**
 * MessageListener defines the interface to objects that can observe 
 * other objects that receive messages. When the subject receives a 
 * message, the message is forwarded to all registered observers.
 */
public interface MessageListener {
	
	/**
	 * Different implementation for client and server
	 * Client simply prints the message and server interprets 
	 * these messages as commands and responds accordingly
	 * @param message message
	 * @param source message source
	 */
	public void messageReceived(String message, MessageSource source);

	/**
	 * Again different implementations for client and server
	 * client closes itself and server removes the client who quit and
	 * broadcast who left  and the score of the game and close the game
	 * @param source message source
	 */
	public void sourceClosed(MessageSource source);
}