package com.game.model;

import java.util.UUID;

/**
 * Player represents a participant in the Tic-Tac-Toe game.
 * This class is responsible for:
 * - Storing player identity (ID and name)
 * - Managing player's game symbol (X or O)
 * - Providing player information to the game
 */
public class Player {
    /** 
     * Unique identifier for the player
     * Generated using UUID to ensure uniqueness across game sessions
     */
    private String id;
    
    /** 
     * Display name of the player
     * Used in UI and game messages
     */
    private String name;
    
    /** 
     * Player's game symbol (X or O)
     * Can be null initially, assigned when game starts
     * Used to mark the player's moves on the board
     */
    private String symbol;

    /**
     * Creates a new player with specified name and symbol
     * @param name Player's display name
     * @param symbol Player's game symbol (X/O), can be null initially
     */
    public Player(String name, String symbol) {
        // Generate unique ID for this player
        this.id = UUID.randomUUID().toString();
        // Store player's name
        this.name = name;
        // Assign initial symbol (may be null)
        this.symbol = symbol;
    }

    /**
     * Gets the player's unique identifier
     * Used for turn validation and player identification
     * @return Player's UUID as string
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the player's display name
     * Used in UI elements and game messages
     * @return Player's name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the player's game symbol
     * Used for making moves and displaying board
     * @return Player's symbol (X or O)
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Sets the player's game symbol
     * Called when game starts to assign X/O
     * @param symbol New symbol for the player
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
} 