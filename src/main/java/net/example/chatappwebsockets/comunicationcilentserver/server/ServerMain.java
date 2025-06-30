package net.example.chatappwebsockets.comunicationcilentserver.server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ServerMain extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/server/server.fxml"));
        stage.setScene(new Scene(loader.load()));
        stage.setTitle("Serveur");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}