package mindcollaps.core;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mindcollaps.client.ClientEngine;
import mindcollaps.client.controller.LoginPrgm;
import mindcollaps.server.ServerEngine;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        ClientEngine engine = new ClientEngine();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mindcollaps/resource/loginWindow.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        LoginPrgm loginPrgm = loader.getController();
        Scene scene = new Scene(root);
        loginPrgm.initController(primaryStage, scene, engine);
        engine.setLoginPrgm(loginPrgm);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Wu Chat~");
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> System.exit(0));
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
