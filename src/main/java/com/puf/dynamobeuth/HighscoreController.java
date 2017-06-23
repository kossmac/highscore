package com.puf.dynamobeuth;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseCredentials;
import com.google.firebase.database.*;
import com.puf.dynamobeuth.model.HighscoreEntry;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.io.InputStream;

public class HighscoreController extends GridPane {
    @FXML
    public TableView<HighscoreEntry> tableView;
    @FXML
    public TextField nameField;
    @FXML
    public TextField scoreField;
    private static final String DATABASE_URL = "https://space-sweeper.firebaseio.com";
    private static DatabaseReference database;

    public HighscoreController()
    {}

    public void handleExitButton() {
        System.out.println("Exiting...");
        System.exit(0);
    }

    public void handlePushData() {
        String name = nameField.getText();
        String score = scoreField.getText();

        DatabaseReference highscoreRef = database.child("highscore");

        highscoreRef.push().setValue(new HighscoreEntry(name, score));
    }

    public void startListeners() {
        database.child("highscore").orderByChild("score").addChildEventListener(new ChildEventListener() {

            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildName) {
                HighscoreEntry highscoreEntry = dataSnapshot.getValue(HighscoreEntry.class);
                ObservableList<HighscoreEntry> highscoreData = tableView.getItems();
                highscoreData.add(highscoreEntry);
            }

            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildName) {}

            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildName) {}

            public void onCancelled(DatabaseError databaseError) {
                System.out.println("startListeners: unable to attach listener to posts");
                System.out.println("startListeners: " + databaseError.getMessage());
            }
        });
    }

    @FXML
    private void initialize() {
        try {
            InputStream serviceAccount = getClass().getResourceAsStream("/service-account.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredential(FirebaseCredentials.fromCertificate(serviceAccount))
                    .setDatabaseUrl(DATABASE_URL)
                    .build();
            FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            System.out.println("Whatever this means..." + e.getMessage());
            System.exit(1);
        }

        database = FirebaseDatabase.getInstance().getReference();

        startListeners();
    }
}
