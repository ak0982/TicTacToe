package com.game.model;

import java.util.UUID;

/**
 * GameRoom manages a single instance of a Tic-Tac-Toe game.
 * This class is responsible for:
 * - Managing player joining and setup
 * - Coordinating game start
 * - Handling turn management
 * - Maintaining game session state
 */
public class GameRoom {
    /** 
     * Unique identifier for this game room
     * Format: 6-character uppercase alphanumeric
     */
    private String gameId;
    
    /** 
     * First player to join the room
     * Null until a player joins
     */
    private Player player1;
    
    /** 
     * Second player to join the room
     * Null until a player joins
     */
    private Player player2;
    
    /** 
     * Current state of the game board and turns
     * Manages moves and win detection
     */
    private GameState gameState;
    
    /** 
     * Indicates if the game has started
     * True only after both players join and game is started
     */
    private boolean gameStarted;

    /**
     * Creates a new game room
     * Generates unique game ID and initializes empty game state
     */
    public GameRoom() {
        // Generate short, unique game ID
        this.gameId = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        // Create initial game state (not started)
        this.gameState = new GameState();
        this.gameStarted = false;
    }

    /**
     * Sets the first player of the game
     * Called when first player creates or joins the game
     * @param playerName Display name for Player 1
     */
    public void setPlayer1(String playerName) {
        // Create player without symbol (assigned at game start)
        this.player1 = new Player(playerName, null);
        System.out.println("Player 1 set: " + playerName);
    }

    /**
     * Sets the second player of the game
     * Called when second player joins the game
     * @param playerName Display name for Player 2
     */
    public void setPlayer2(String playerName) {
        // Create player without symbol (assigned at game start)
        this.player2 = new Player(playerName, null);
        System.out.println("Player 2 set: " + playerName);
    }

    /**
     * Starts the game with specified starting player
     * Assigns symbols (X/O) to players and initializes game state
     * @param isPlayer1Starting true if Player 1 starts, false if Player 2 starts
     */
    public void startGame(boolean isPlayer1Starting) {
        // Only start if both players have joined
        if (player1 != null && player2 != null) {
            // Initialize new game state (X always goes first)
            this.gameState = new GameState(true);
            this.gameStarted = true;
            
            // Assign symbols based on who starts
            if (isPlayer1Starting) {
                player1.setSymbol("X");  // Player 1 gets X and goes first
                player2.setSymbol("O");
            } else {
                player2.setSymbol("X");  // Player 2 gets X and goes first
                player1.setSymbol("O");
            }
            
            // Log game start details
            System.out.println("Game started with players: " + player1.getName() + " vs " + player2.getName());
            System.out.println("Starting player: " + (isPlayer1Starting ? player1.getName() : player2.getName()));
            System.out.println("Player 1 (" + player1.getName() + ") plays: " + player1.getSymbol());
            System.out.println("Player 2 (" + player2.getName() + ") plays: " + player2.getSymbol());
            System.out.println("Current turn: " + (gameState.isXNext() ? "X" : "O"));
        }
    }

    /**
     * Starts the game with Player 1 as the starting player
     * Convenience method for default game start
     */
    public void startGame() {
        startGame(true);  // Default to Player 1 starting
    }

    /**
     * Resets the game for a new round with the same players
     * Keeps player information but resets game state
     */
    public void resetGame() {
        // Create new game state
        this.gameState = new GameState(true);
        
        // Keep the same players but randomly decide who starts
        boolean isPlayer1Starting = Math.random() < 0.5;
        
        // Assign symbols based on who starts
        if (isPlayer1Starting) {
            player1.setSymbol("X");  // Player 1 gets X and goes first
            player2.setSymbol("O");
        } else {
            player2.setSymbol("X");  // Player 2 gets X and goes first
            player1.setSymbol("O");
        }
        
        // Game is already started since we have both players
        this.gameStarted = true;
        
        // Log game reset details
        System.out.println("Game reset - New game started");
        System.out.println("Starting player: " + (isPlayer1Starting ? player1.getName() : player2.getName()));
        System.out.println("Player 1 (" + player1.getName() + ") plays: " + player1.getSymbol());
        System.out.println("Player 2 (" + player2.getName() + ") plays: " + player2.getSymbol());
    }

    /**
     * Validates if it's the specified player's turn
     * Checks game state and player symbols
     * @param playerId ID of the player attempting to move
     * @return true if it's the player's turn, false otherwise
     */
    public boolean isPlayerTurn(String playerId) {
        // Check if game is ready for moves
        if (player2 == null || !gameStarted) {
            System.out.println("Game not ready - Player2: " + (player2 != null) + ", Started: " + gameStarted);
            return false;
        }
        
        // Get current game state
        boolean isXTurn = gameState.isXNext();
        boolean isPlayer1 = playerId.equals(player1.getId());
        boolean player1HasX = "X".equals(player1.getSymbol());
        
        // Log turn check details
        System.out.println("=== Turn Check ===");
        System.out.println("Player ID requesting: " + playerId);
        System.out.println("Player 1 ID: " + player1.getId() + " (Symbol: " + player1.getSymbol() + ")");
        System.out.println("Player 2 ID: " + player2.getId() + " (Symbol: " + player2.getSymbol() + ")");
        System.out.println("Is X's turn: " + isXTurn);
        System.out.println("Current symbol's turn: " + (isXTurn ? "X" : "O"));
        System.out.println("Player 1 has X: " + player1HasX);
        System.out.println("Is Player 1: " + isPlayer1);
        
        // Determine if it's the player's turn
        boolean result;
        if (isXTurn) {
            // If it's X's turn, the player with X should play
            result = (isPlayer1 && player1HasX) || (!isPlayer1 && !player1HasX);
        } else {
            // If it's O's turn, the player with O should play
            result = (isPlayer1 && !player1HasX) || (!isPlayer1 && player1HasX);
        }
        
        // Log final decision
        System.out.println("Turn allowed: " + result);
        System.out.println("Current player: " + (isPlayer1 ? player1.getName() : player2.getName()));
        System.out.println("==================");
        
        return result;
    }

    // Getter methods
    
    /**
     * @return Unique identifier for this game room
     */
    public String getGameId() {
        return gameId;
    }

    /**
     * @return First player in the game (null if not joined)
     */
    public Player getPlayer1() {
        return player1;
    }

    /**
     * @return Second player in the game (null if not joined)
     */
    public Player getPlayer2() {
        return player2;
    }

    /**
     * @return Current state of the game board and turns
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * @return true if game has started, false otherwise
     */
    public boolean isGameStarted() {
        return gameStarted;
    }

    /**
     * Checks if both players have joined the game
     * @return true if both players are present, false otherwise
     */
    public boolean areBothPlayersJoined() {
        return player1 != null && player2 != null;
    }
} 