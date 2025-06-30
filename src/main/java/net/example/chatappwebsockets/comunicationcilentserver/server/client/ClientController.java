package net.example.chatappwebsockets.comunicationcilentserver.server.client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.*;
import java.net.*;

public class ClientController {
    @FXML private TextArea messageArea;
    @FXML private TextField inputField;

    private PrintWriter out;

    public void initialize() {
        new Thread(() -> {
            try (Socket socket = new Socket("localhost", 5000)) {
                appendMessage("Connecté au serveur");

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                String line;
                while ((line = in.readLine()) != null) {
                    appendMessage("Serveur : " + line);
                }
            } catch (IOException e) {
                appendMessage("Erreur : " + e.getMessage());
            }
        }).start();
    }

    @FXML
    private void handleSend() {
        String message = inputField.getText();
        if (!message.isEmpty() && out != null) {
            out.println(message);
            appendMessage("Moi : " + message);
            inputField.clear();
        }
    }

    private void appendMessage(String msg) {
        Platform.runLater(() -> messageArea.appendText(msg + "\n"));
    }
}