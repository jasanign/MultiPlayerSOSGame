package client;

/**
 * Driver for the  MultiuserSosClient class
 * @author GJ (Gajjan Jasani)
 * @version 11/18/2015
 */
public class MultiuserSosClientDriver {

	/**
	 * Entry point for the new client (player)
	 * @param args[0] host name
	 * @param args[1] port number
	 * @param args[2] user nick name
	 */
	public static void main(String[] args) {
		String hostname;
		int port = 0;
		String nickname;
		
		if(args.length < 3) {
			System.err.println("Invalid command line arguments. "
					+ "<hostname> <portnumber> <user nickname>");
			System.exit(1);
		}
		
		hostname = args[0];
		try{
			port = Integer.parseInt(args[1]);
		} catch(NumberFormatException nfe){
			System.out.println("Number formate exception...");
			System.out.println("Please try again with a valid port"
					+ " number");
			System.exit(0);
		}
		nickname = args[2];
		MultiuserSosClient client = new MultiuserSosClient(
									hostname, port, nickname);

	}

}
