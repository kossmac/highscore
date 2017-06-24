package com.puf.dynamobeuth;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseCredentials;
import com.google.firebase.database.*;
import com.puf.dynamobeuth.model.HighscoreEntry;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
    @FXML
    public Button addButton;
    private static final String DATABASE_URL = "https://space-sweeper.firebaseio.com";
    private static DatabaseReference database;
    private ObservableList<HighscoreEntry> highscoreData;

    public void handleExitButton() {
        System.out.println("Exiting...");
        System.exit(0);
    }

    public void handlePushData() {
        String name = nameField.getText();
        int score = Integer.parseInt(scoreField.getText());

        DatabaseReference highscoreRef = database.child("highscore");

        highscoreRef.push().setValue(new HighscoreEntry(name, score));
    }

    private void startListeners() {
        database.child("highscore").orderByChild("score").addChildEventListener(new ChildEventListener() {
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildName) {
                HighscoreEntry highscoreEntry = dataSnapshot.getValue(HighscoreEntry.class);
                highscoreEntry.setKey(dataSnapshot.getKey());
                highscoreData = tableView.getItems();
                highscoreData.add(highscoreEntry);
            }

            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildName) {}

            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String old_key = dataSnapshot.getKey();
                highscoreData.forEach((entry) -> {
                    if(entry.getKey().equals(old_key)) {
                        highscoreData.remove(entry);
                    }
                });
            }

            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildName) {}

            public void onCancelled(DatabaseError databaseError) {
                System.out.println("startListeners: unable to attach listener to posts");
                System.out.println("startListeners: " + databaseError.getMessage());
            }
        });
    }

    @FXML
    private void initialize() {
        tableView.setRowFactory(
            tableView -> {
                final TableRow<HighscoreEntry> row = new TableRow<>();
                final ContextMenu rowMenu = new ContextMenu();
                MenuItem removeItem = new MenuItem("Löschen");
                removeItem.setOnAction(event -> {
                    HighscoreEntry highscoreEntry = row.getItem();
                    System.out.printf("Deleting: %s with %d\n", highscoreEntry.getName(), highscoreEntry.getScore());
                    database.child("highscore/" + highscoreEntry.getKey()).removeValue();
                });
                rowMenu.getItems().addAll(removeItem);

                row.contextMenuProperty().bind(
                        Bindings.when(Bindings.isNotNull(row.itemProperty()))
                                .then(rowMenu)
                                .otherwise((ContextMenu)null));
                return row;
            }
        );

        scoreField.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) {
                if(!scoreField.getText().matches("\\d+")){
                    addButton.setDisable(true);
                } else {
                    addButton.setDisable(false);
                }
            }
        });

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
