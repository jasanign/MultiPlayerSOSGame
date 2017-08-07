package server;

/**
 * Contains the logic for the game of S.O.S.
 * @author GJ (Gajjan Jasani)
 * @version 11/18/2015
 */
public class Game {
	/** game board */
	private SosBoard board;
	/** To keep track of ongoing game */
	private boolean inProgress;
	/** array of player names */
	private String[] players;
	/** array of player scores */
	private int[] scores;
	/** Keep track of the turn of players */
	private int currentPlayer;
	/** board size */
	private int size;

	/**
	 * Constructor of the Game class
	 * @param size board size
	 * @param players players currently in game
	 */
	public Game(int size, String[] players) {
		board = new SosBoard(size);
		inProgress = true;
		this.size = size;
		this.players = players;
		currentPlayer = 0;
		scores = new int[players.length];
		for (int i = 0; i < scores.length; i++)
			scores[i] = 0;

	}

	/**
	 * Update the board  and turn after player makes a move
	 * @param c player's input in game
	 * @param row row of board
	 * @param col column of board
	 * @return game board and whose turn it is now
	 */
	public String move(char c, int row, int col) {
		board.setElement(c, row, col);

		if (wordFound(row, col)) {
			scores[currentPlayer]++;
		}
		if (board.isFull()) {
			return gameOver();
		} else {
			// game continue with next player move;
			currentPlayer = (currentPlayer + 1) % players.length;
			return getBoardStr() + System.lineSeparator() + 
					players[currentPlayer] + " it is your turn";
		}
	}

	/**
	 * Current board of the game
	 * @return board
	 */
	public String getBoardStr() {
		return board.boardToStr();
	}

	/**
	 * checks if a game is going on
	 * @return true if game in progress, false otherwise
	 */
	public boolean isInProgress() {
		return inProgress;
	}

	/**
	 * check if player's move is valid or not
	 * @param row board row
	 * @param colboard column
	 * @return true if valid, false otherwise
	 */
	public boolean isValidMove(int row, int col) {
		return board.isValidMove(row, col);
	}

	/**
	 * keeps track of whose turn it is
	 * @return current player name
	 */
	public String getPlayerTurn() {
		return players[currentPlayer];
	}

	/**
	 * Makes sure only the current player's move is validated
	 * @param player current player
	 * @return true if move comes from current player, false otherwise
	 */
	public boolean isPlayerTurn(String player) {
		return getPlayerTurn().equalsIgnoreCase(player);
	}

	/**
	 * Check if the place where player is trying to make a move is 
	 * empty or not
	 * @param row
	 * @param col
	 * @return
	 */
	private boolean wordFound(int row, int col) {
		if (isSOS(row, col, row + 1, col, row + 2, col) 
				|| isSOS(row, col, row + 1, col - 1, row + 2, col - 2)
				|| isSOS(row, col, row, col - 1, row, col - 2) 
				|| isSOS(row, col, row - 1, col - 1, row - 2, col - 2)
				|| isSOS(row, col, row - 1, col, row - 2, col) 
				|| isSOS(row, col, row - 1, col + 1, row - 2, col + 2)
				|| isSOS(row, col, row, col + 1, row, col + 2) 
				|| isSOS(row, col, row + 1, col + 1, row + 2, col + 2)
				|| isSOS(row - 1, col, row, col, row + 1, col) 
				|| isSOS(row - 1, col - 1, row, col, row + 1, col - 1)
				|| isSOS(row, col - 1, row, col, row, col + 1) 
				|| isSOS(row - 1, col - 1, row, col, row + 1, col + 1))
			return true;
		else
			return false;
	}

	/**
	 * Checks if the player's move creates an SOS word
	 * @return true if SOS is formed on board, false otherwise
	 */
	private boolean isSOS(int i1, int j1, int i2, int j2, int i3, int j3) {
		if (i1 < 0 || i1 >= size || j1 < 0 || j1 >= size || i2 < 0 || i2 >= size || j2 < 0 || j2 >= size || i3 < 0
				|| i3 >= size || j3 < 0 || j3 >= size)
			return false;
		String str = "" + board.getElement(i1, j1) + board.getElement(i2, j2) + board.getElement(i3, j3);
		if (str.equals("SOS"))
			return true;
		else
			return false;
	}

	/**
	 * closing process, returns current scores
	 * @return game status
	 */
	public String gameOver() {
		String status = "Score" + System.lineSeparator();
		inProgress = false;
		for (int i = 0; i < players.length; i++) {
			status += players[i] + "\t: " + scores[i] + System.lineSeparator();
		}
		status += "GAME OVER" + System.lineSeparator();
		return status;
	}

}
