package com.game;

public class Player {
    private String name;
    private char symbol;
    private int score;

    public Player(String name, char symbol) {
        this.name = name;
        this.symbol = symbol;
        this.score = 0;
    }

    public String getName() {
        return name;
    }

    public char getSymbol() {
        return symbol;
    }

    public int getScore() {
        return score;
    }

    public void incrementScore() {
        score++;
    }

    public void setName(String name) {
        this.name = name;
    }
} 