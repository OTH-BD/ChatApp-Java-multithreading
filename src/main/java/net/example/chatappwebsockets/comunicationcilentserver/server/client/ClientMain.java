package net.example.chatappwebsockets.comunicationcilentserver.server.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientMain extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/client.fxml"));
        stage.setScene(new Scene(loader.load()));
        stage.setTitle("Client");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}