package com.puf.dynamobeuth.model;

import javafx.beans.property.SimpleStringProperty;

public class HighscoreEntry {
    private final SimpleStringProperty name;
    private final SimpleStringProperty score;

    public HighscoreEntry() {
        this("", "");
    }

    public HighscoreEntry(String name, String score){
        this.name = new SimpleStringProperty(name);
        this.score = new SimpleStringProperty(score);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getScore() {
        return score.get();
    }

    public void setScore(String score) {
        this.score.set(score);
    }
}

