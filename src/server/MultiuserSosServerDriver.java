package server;

/**
 * It parses command line options, instantiates a MultiuserSosServer, 
 * and calls its listen() method. This takes two command line arguments, 
 * the port number for the server and the size of the board. (if the port
 * and/or board size are not provided, use 8091 as default port and 3x3 as
 * default board size).
 * @author GJ (Gajjan Jasnai)
 * @version 11/18/2015
 *
 */
public class MultiuserSosServerDriver {

	/**
	 * Entry point for the server
	 * @param args[0] port number
	 * @param args[1] board size
	 */
	public static void main(String[] args) {
		
		int port = 8091; // default port for server
		int size = 3;	 // default board size for the game

		if (args.length == 2) {
			try{
				port = Integer.parseInt(args[0]);
				size = Integer.parseInt(args[1]);
			} catch(NumberFormatException nfe){
				System.out.println("Number formate exception...");
				System.out.println("Please try again with a valid port"
						+ " number and valid board size");
				System.exit(0);
			}
			
		} else if (args.length == 1) {
			try{
				port = Integer.parseInt(args[0]);
			} catch(NumberFormatException nfe){
				System.out.println("Number formate exception...");
				System.out.println("Please try again with a valid port"
						+ " number");
				System.exit(0);
			}
		}
		MultiuserSosServer server = new MultiuserSosServer(port, size);
		server.listen();
	}

}
