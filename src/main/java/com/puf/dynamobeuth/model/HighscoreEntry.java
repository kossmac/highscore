package com.puf.dynamobeuth.model;

public class HighscoreEntry {
    private String name, key;
    private int score;

    public HighscoreEntry() {}

    public HighscoreEntry(String name, int score){
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return this.name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getScore() {
        return this.score;
    }
}

