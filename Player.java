package com.minecraft.mobbattle;

/**
 * Player Model Class
 * Represents a user in the Minecraft Mob Battle application.
 * Stored attributes include credentials and battlefield statistics.
 * 
 * Implements basic OOP principles: Encapsulation (private fields, public getters/setters).
 * No inheritance, interfaces, or abstract classes are used here.
 */
public class Player {
    private int id;
    private String username;
    private String password;
    private int wins;
    private int losses;
    private int draws;
    private int score;

    // Default constructor
    public Player() {
    }

    // Constructor with all fields
    public Player(int id, String username, String password, int wins, int losses, int draws, int score) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.wins = wins;
        this.losses = losses;
        this.draws = draws;
        this.score = score;
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getDraws() {
        return draws;
    }

    public void setDraws(int draws) {
        this.draws = draws;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
