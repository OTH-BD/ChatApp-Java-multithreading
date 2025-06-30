package net.example.chatappwebsockets.comunicationcilentserver.server;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.*;
import java.net.*;

public class ServerController {
    @FXML private TextArea messageArea;
    @FXML private TextField inputField;

    private PrintWriter clientOut;

    public void initialize() {
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(5000)) {
                appendMessage("Serveur démarré sur le port 5000...");
                Socket clientSocket = serverSocket.accept();
                appendMessage("Client connecté : " + clientSocket.getInetAddress());

                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                clientOut = new PrintWriter(clientSocket.getOutputStream(), true);

                String line;
                while ((line = in.readLine()) != null) {
                    appendMessage("Client : " + line);
                }
            } catch (IOException e) {
                appendMessage("Erreur : " + e.getMessage());
            }
        }).start();
    }

    @FXML
    private void handleSend() {
        String message = inputField.getText();
        if (!message.isEmpty() && clientOut != null) {
            clientOut.println(message);
            appendMessage("Moi : " + message);
            inputField.clear();
        }
    }

    private void appendMessage(String msg) {
        Platform.runLater(() -> messageArea.appendText(msg + "\n"));
    }
}