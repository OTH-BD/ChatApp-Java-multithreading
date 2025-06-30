module net.example.chatappwebsockets {
    requires javafx.controls;
    requires javafx.fxml;

    // Ouvre le package contenant ta classe Application pour javafx.graphics (JavaFX Launcher)
    opens net.example.chatappwebsockets.app to javafx.graphics;

    exports net.example.chatappwebsockets.app;
}