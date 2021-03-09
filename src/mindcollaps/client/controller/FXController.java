package mindcollaps.client.controller;

import com.sun.xml.internal.ws.api.pipe.Engine;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mindcollaps.client.ClientEngine;

public abstract class FXController {

    private Stage primaryStage;
    private Scene scene;
    private ClientEngine engine;

    public void initController(Stage primaryStage, Scene scene, ClientEngine engine) {
        this.primaryStage = primaryStage;
        this.scene = scene;
        this.engine = engine;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }


    public ClientEngine getEngine() {
        return engine;
    }

    public void setEngine(ClientEngine engine) {
        this.engine = engine;
    }
}
