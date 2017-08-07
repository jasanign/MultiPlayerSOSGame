package server;
/**
 * Creates the board for the game of SOS
 * @author GJ (Gajjan Jasani)
 * @version 11/18/2015
 */
public class SosBoard {
	/** game constant for S */
	public static final char S_MARK = 'S';
	/** game constant for O */
	public static final char O_MARK = 'O';
	/** game constant for unused spot */
	public static final char EMPTY_MARK = ' ';
	/** board holder */
	private char[][] board;
	/** board size*/
	private int size;

	/**
	 * Constructor for SOS board
	 * @param size board size
	 */
	public SosBoard(int size) {
		this.size = size;
		board = new char[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++)
				board[i][j] = EMPTY_MARK;
		}
	}

	/**
	 * Check if the move at specified row and column is valid (the specified
	 * position is empty)
	 * @param row the row
	 * @param col the column
	 * @return true if the move is valid, false otherwise
	 */
	public boolean isValidMove(int row, int col) {
		if (row < 0 || row >= size || col < 0 || col >= size)
			return false;

		if (board[row][col] == EMPTY_MARK) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Check if board is full.
	 * @return true if board is full, false otherwise.
	 */
	public boolean isFull() {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++)
				if (board[i][j] == EMPTY_MARK) {
					return false;
				}
		}
		return true;
	}

	/**
	 * Set given character at given spot
	 * @param c given character
	 * @param row row
	 * @param col column
	 */
	public void setElement(char c, int row, int col) {
		switch (c) {
		case 's':
		case 'S':
			board[row][col] = S_MARK;
			break;
		case 'o':
		case 'O':
			board[row][col] = O_MARK;
			break;
		}

	}

	/**
	 * character at the given position
	 * @param row row
	 * @param col column
	 * @return character at given spot
	 */
	public char getElement(int row, int col) {
		return board[row][col];
	}

	/**
	 * Display the board.
	 * @return current board
	 */
	public String boardToStr() {
		String result = "   ";
		for (int j = 0; j < size; j++)
			result += " " + j + "  ";
		result += System.lineSeparator();
		String orizBorder = "  +";
		for (int j = 0; j < size; j++)
			orizBorder += "---+";

		for (int i = 0; i < size; i++) {
			result += orizBorder + System.lineSeparator();
			result += i + " |";
			for (int j = 0; j < size; j++)
				result += " " + String.valueOf((board[i][j])) + " |";
			result += System.lineSeparator();
		}
		result += orizBorder + System.lineSeparator();
		return result;
	}

}
