package mindcollaps.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import mindcollaps.lib.Chat;
import mindcollaps.lib.Client;
import mindcollaps.lib.Message;
import mindcollaps.netConnect.client.NetConnectClient;
import mindcollaps.netConnect.server.NetConnectServer;
import mindcollaps.utility.AllertBox;
import org.json.simple.JSONObject;
import org.junit.runners.AllTests;

import javax.annotation.Resources;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class Controller {

    //<Chat, String (Client ID)>
    private HashMap<Chat, String> chats = new HashMap<>();
    private Chat currentChat;

    @FXML
    public TextField chatTxtField;
    @FXML
    public VBox chatVbox;
    @FXML
    public Button chatBtn;

    private NetConnectClient client;

    @FXML
    public void onConnect(ActionEvent actionEvent) {
        String s = new AllertBox(chatBtn.getScene(), Modality.APPLICATION_MODAL).displayTextField("Chat", "Connect..", "Connect", false);
        JSONObject cmds = new JSONObject();
        cmds.put("cmd", "checkClient");
        cmds.put("client",s);
        client.sendMessage(cmds.toJSONString());
    }

    @FXML
    public void onReconnect(ActionEvent actionEvent) {
        stopConnection();
        Timer t = new Timer();
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                connectToServer();
            }
        };
        t.schedule(tt, 1000);
    }

    @FXML
    public void onClose(ActionEvent actionEvent) {
        stopConnection();
        System.exit(0);
    }

    @FXML
    public void onAbout(ActionEvent actionEvent) {
    }

    @FXML
    public void onSend(ActionEvent actionEvent) {
        Message msg = new Message();
        msg.setContent(chatTxtField.getText());
        msg.setFromDate(new Date());
        msg.setMessageType(Message.MessageType.Text);
        msg.setFromClient(client.getClient());
        msg.setMessageId("!padding!");
        currentChat.addOwnMessage(msg);


        JSONObject cmds = buildCommand("send", "msg", msg.toJson());
        cmds.put("to", currentChat.getChatTo().getClientId());
        client.sendMessage(cmds.toJSONString());
    }

    @FXML
    public void initialize(){
        connectToServer();
    }

    public void removeMessage(Message message){
        chatVbox.getChildren().remove(message.getMessageDisplayed());
    }

    public void receiveMessage(Message message){

    }

    public void setupChat(String partnerId, String partnerName){
        Client client = new Client();
        client.setClientId(partnerId);
        client.setClientDisplayName(partnerName);
        this.currentChat = new Chat(client);
    }

    private void stopConnection(){
        if(this.client != null)
            this.client.stop();

        this.client = null;
    }

    private JSONObject buildCommand(String cmd, String contentName, JSONObject content){
        JSONObject obj = new JSONObject();
        obj.put("cmd", cmd);
        obj.put(contentName, content);
        obj.put("id", client.getClient().getClientId());
        return obj;
    }

    private void connectToServer(){
        if(this.client != null){
            if(this.client.isConnected()){
                System.out.println("Already connected!");
                return;
            }
        }
        NetConnectClient c = new NetConnectClient(this);
        this.client = c;
        c.connectToServer();
        askForName();
    }

    private void askForName(){
        String name = new AllertBox(chatBtn.getScene(), Modality.APPLICATION_MODAL).displayTextField("Username","Username", "login", true);
        Timer t = new Timer();
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                client.getClient().setClientDisplayName(name);
                client.sendMessage(buildCommand("client", "client", client.getClient().toJson()).toJSONString());
            }
        };
        t.schedule(tt, 4000);
    }
}
