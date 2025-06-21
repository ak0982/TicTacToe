package com.game.model;

public class GameMove {
    private String gameId;
    private String playerId;
    private int position;

    // Default constructor for JSON deserialization
    public GameMove() {
    }

    public GameMove(String gameId, String playerId, int position) {
        this.gameId = gameId;
        this.playerId = playerId;
        this.position = position;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
} 