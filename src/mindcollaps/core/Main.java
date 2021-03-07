package mindcollaps.core;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mindcollaps.server.ServerEngine;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/mindcollaps/resource/chatWindow.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        if (args.length > 0)
            switch (args[0]) {
                case "server":
                    new ServerEngine();
                    break;

                case "client":
                    launch(args);
                    break;
            }
        else
            launch(args);
    }
}
