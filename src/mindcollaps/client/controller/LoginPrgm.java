package mindcollaps.client.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import mindcollaps.netConnect.client.NetConnectClient;

public class LoginPrgm extends FXController {

    @FXML
    public TextField serverHost;
    @FXML
    public PasswordField userPw;
    @FXML
    public TextField userName;
    @FXML
    public TextField serverPort;
    @FXML
    public Label errorCode;



    @FXML
    public void onLogin(ActionEvent actionEvent) {
        NetConnectClient c = new NetConnectClient(getEngine());
        try {
            c.connectToServer(serverHost.getText(), Integer.parseInt(serverPort.getText()));
        } catch (Exception e) {
            errorCode.setText("Cant connect to server!");
            return;
        }
        getEngine().setClient(c);
    }

    public void connectionApproved() {
        System.out.println("Logged in!");
        getPrimaryStage().setTitle("Logged in: " + getEngine().getClient().getClient().getClientId());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mindcollaps/resource/chatWindow.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Scene scene = new Scene(root);
        ChatPrgm c = loader.getController();
        c.initController(getPrimaryStage(), scene, getEngine());
        getEngine().setChatPrgm(c);

        Platform.runLater(() -> getPrimaryStage().setScene(scene));
    }

    public void receivePassword() {
        String pin = userPw.getText();
        String name = userName.getText();
        getEngine().getClient().sendMessage("{\"cmd\": \"login\", \"id\": \"" + getEngine().getClient().getClient().getClientId() + "\", \"pin\": \"" + pin + "\", \"name\": \"" + name + "\"}");
    }

    public void passwordWrong() {
        errorCode.setText("Wrong pin or username!");
    }
}
