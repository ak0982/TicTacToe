package com.game.model;

/**
 * GameState represents the current state of a Tic-Tac-Toe game.
 * This class is responsible for:
 * - Managing the game board
 * - Tracking turns
 * - Validating moves
 * - Checking for winners
 */
public class GameState {
    /** 
     * The game board is represented as a 1D array of 9 cells
     * Index mapping:
     * [0][1][2]
     * [3][4][5]
     * [6][7][8]
     */
    private String[] board;
    
    /** 
     * Tracks whose turn it is
     * true = X's turn
     * false = O's turn
     */
    private boolean xIsNext;
    
    /** 
     * Stores the winner's symbol (X or O) or "draw"
     * null means game is still in progress
     */
    private String winner;
    
    /** 
     * Stores the winning line positions [start, middle, end]
     * Used for UI highlighting of winning combination
     */
    private int[] winningLine;

    /**
     * Default constructor - creates a new game with X starting
     */
    public GameState() {
        this(true); // Default to X starting
    }

    /**
     * Creates a new game with specified starting player
     * @param xStarts true if X starts, false if O starts
     */
    public GameState(boolean xStarts) {
        // Initialize empty board (all cells null)
        this.board = new String[9];
        // Set starting player
        this.xIsNext = xStarts;
        System.out.println("GameState created - X starts: " + xStarts + ", xIsNext: " + this.xIsNext);
    }

    /**
     * Resets the game to initial state
     * Used when starting a new game
     */
    public void reset() {
        board = new String[9];  // Clear board
        xIsNext = true;         // X starts
        winner = null;          // No winner
        winningLine = null;     // No winning line
    }

    /**
     * Attempts to make a move on the board
     * @param position Board position (0-8)
     * @return true if move was valid and made, false otherwise
     */
    public boolean makeMove(int position) {
        // Validate move position and game state
        if (position < 0 || position >= 9 || board[position] != null || winner != null) {
            // Log detailed error information
            System.out.println("Invalid move - Position: " + position + 
                             ", Out of bounds: " + (position < 0 || position >= 9) +
                             ", Already occupied: " + (board[position] != null) +
                             ", Game over: " + (winner != null));
            return false;
        }

        // Determine current player's symbol
        String currentSymbol = xIsNext ? "X" : "O";
        // Place symbol on board
        board[position] = currentSymbol;
        System.out.println("Move made - Position: " + position + ", Symbol: " + currentSymbol);
        
        // Check if this move resulted in a win
        checkWinner();
        if (winner != null) {
            System.out.println("Game over - Winner: " + winner);
        }
        
        // Toggle turn to next player
        xIsNext = !xIsNext;
        System.out.println("Turn changed - Next turn: " + (xIsNext ? "X" : "O"));
        return true;
    }

    /**
     * Checks if there's a winner or if game is drawn
     * Updates winner and winningLine fields accordingly
     */
    private void checkWinner() {
        // All possible winning combinations
        // Each sub-array represents positions that form a winning line
        int[][] lines = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8},  // Horizontal rows
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8},  // Vertical columns
            {0, 4, 8}, {2, 4, 6}              // Diagonals
        };

        // Check each winning combination
        for (int[] line : lines) {
            // Get symbols at each position in the current line
            String a = board[line[0]];
            String b = board[line[1]];
            String c = board[line[2]];

            // If all positions have same non-null symbol, we have a winner
            if (a != null && a.equals(b) && a.equals(c)) {
                winner = a;  // Store winning symbol
                winningLine = line;  // Store winning positions for UI
                return;
            }
        }

        // If no winner, check for draw (all cells filled)
        boolean isDraw = true;
        for (String cell : board) {
            if (cell == null) {  // Found empty cell
                isDraw = false;
                break;
            }
        }
        if (isDraw) {
            winner = "draw";
        }
    }

    // Getter methods
    public String[] getBoard() {
        return board;
    }

    public boolean isXNext() {
        return xIsNext;
    }

    public String getWinner() {
        return winner;
    }

    public int[] getWinningLine() {
        return winningLine;
    }
} 