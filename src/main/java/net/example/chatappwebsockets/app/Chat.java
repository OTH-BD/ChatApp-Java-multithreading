package net.example.chatappwebsockets.app;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Chat extends Application {
    private VBox messageBox;
    private ScrollPane scrollPane;
    private TextField inputField;
    private PrintWriter out;
    private BufferedReader in;
    private String clientName;
    private Stage primaryStage;
    @Override
    public void start(Stage primaryStage){
        this.primaryStage = primaryStage;
        welcomeScreen();
    }
    public void welcomeScreen(){
        Label label = new Label("Enter your name:");
        TextField nameField = new TextField();
        Button startButton = new Button("Start Chat");

        VBox nameLayout = new VBox(10, label, nameField, startButton);
        nameLayout.setAlignment(Pos.CENTER);
        nameLayout.setPadding(new Insets(20));

        Scene nameScene = new Scene(nameLayout, 400, 300);
        primaryStage.setScene(nameScene);
        primaryStage.setTitle("Welcome");
        primaryStage.show();
        startButton.setOnAction(e -> {
            String name = nameField.getText().trim();
            if (!name.isEmpty()) {
                clientName = name;
                chatScreen();
                new Thread(this::setupConnection).start();
            }
        });
    }
    public void chatScreen(){
        messageBox = new VBox(5);
        messageBox.setPadding(new Insets(10));
        scrollPane = new ScrollPane(messageBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        inputField = new TextField();
        inputField.setPromptText("Type your message here...");
        HBox.setHgrow(inputField, Priority.ALWAYS);
        Button sendButton = new Button("Send");
        sendButton.setMinWidth(70);
        sendButton.setOnAction(e->sendMessage());
        inputField.setOnAction(e->sendMessage());
        HBox inputBox = new HBox(10, inputField, sendButton);
        inputBox.setPadding(new Insets(10));
        inputBox.setAlignment(Pos.CENTER);
        BorderPane root = new BorderPane(scrollPane, null, null, inputBox, null);
        primaryStage.setScene(new Scene(root, 500, 400));
        primaryStage.setTitle("Chat");
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> {
            try {
                if (out != null) out.close();
                if (in != null) in.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }
    private void setupConnection(){
        try {
            Socket socket = new Socket("localhost", 9091);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(),true);
            out.println(clientName);
            addSystemMessage("Welcome, " + clientName + "!");
            String message;
            while ((message = in.readLine()) != null) {
                if (message.startsWith("---")) {
                    if (!message.contains(clientName)) {
                        addSystemMessage(message.replace("---", "").trim());
                    }
                } else if (message.startsWith(clientName + ":")) {
                    addMessage(message, true);
                } else {
                    addMessage(message, false);
                }
            }
        }catch(IOException e)
        {
            addMessage("Connection failed: " + e.getMessage(), false);
        }
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                if (out != null) out.close();
                if (in != null) in.close();
            } catch (IOException ignored) {}
        }));
    }
    private void sendMessage(){
        String message = inputField.getText();
        if(!message.trim().isEmpty()){
            String fullMessage = clientName + ": " + message;
            out.println(fullMessage);
            inputField.clear();
        }
    }
    private void addMessage(String message, boolean isOwnMessage) {
        String sender = message.split(":")[0];
        Label initialsLabel = new Label(sender.substring(0, 1).toUpperCase());
        initialsLabel.setStyle("-fx-background-color: #a0c4ff; -fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-alignment: center; -fx-font-size: 12px;");
        initialsLabel.setMinSize(30, 30);
        initialsLabel.setMaxSize(30, 30);
        initialsLabel.setAlignment(Pos.CENTER);
        initialsLabel.setStyle(initialsLabel.getStyle() + "-fx-background-radius: 50%;");

        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true);
        messageLabel.setPadding(new Insets(5));
        messageLabel.setStyle("-fx-font-size: 13px;");

        String timestamp = java.time.LocalTime.now().withSecond(0).withNano(0).toString();
        Label timeLabel = new Label(timestamp);
        timeLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: gray;");
        timeLabel.setPadding(new Insets(0, 5, 5, 0));
        timeLabel.setAlignment(Pos.BOTTOM_LEFT);

        VBox bubble = new VBox(messageLabel, timeLabel);
        bubble.setStyle(isOwnMessage
                ? "-fx-background-color: #dcf8c6; -fx-background-radius: 16px;"
                : "-fx-background-color: #ffffff; -fx-background-radius: 16px; -fx-border-color: #ccc;"
        );
        bubble.setMaxWidth(300);
        bubble.setPadding(new Insets(3));

        HBox wrapper;
        if(isOwnMessage){
            wrapper = new HBox(bubble);
            wrapper.setPadding(new Insets(5, 10, 5, 10));
            wrapper.setAlignment(Pos.CENTER_RIGHT);
        }
        else {
            wrapper = new HBox(10,initialsLabel,bubble);
            wrapper.setAlignment(Pos.CENTER_LEFT);
        }

        Platform.runLater(() -> {
            messageBox.getChildren().add(wrapper);
            scrollPane.layout();
            scrollPane.setVvalue(1.0);
        });
    }
    private void addSystemMessage(String message) {
        Label label = new Label(message);
        label.setStyle("-fx-font-style: italic; -fx-text-fill: gray;");
        label.setAlignment(Pos.CENTER);
        label.setWrapText(true);

        HBox wrapper = new HBox(label);
        wrapper.setAlignment(Pos.CENTER);
        wrapper.setPadding(new Insets(10, 0, 10, 0));

        Platform.runLater(() -> {
            messageBox.getChildren().add(wrapper);
            scrollPane.layout();
            scrollPane.setVvalue(1.0);
        });
    }
    public static void main(String[] args){
        launch(args);
    }
}