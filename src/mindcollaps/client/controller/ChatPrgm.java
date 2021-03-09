package mindcollaps.client.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import mindcollaps.client.ClientEngine;
import mindcollaps.lib.Chat;
import mindcollaps.lib.Client;
import mindcollaps.lib.Message;
import mindcollaps.utility.AllertBox;
import org.json.simple.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class ChatPrgm extends FXController {

    //<Chat, String (Client ID)>
    private HashMap<String, Chat> chats = new HashMap<>();
    private Chat currentChat;

    @FXML
    public VBox chatVbox;
    @FXML
    public VBox usersVbox;
    @FXML
    public Button sendBtn;
    @FXML
    public TextArea chatTxtBox;
    @FXML
    public Label userNameLabel;
    @FXML
    public Label userIdLabel;

    @FXML
    public void onConnect(ActionEvent actionEvent) {
        //TODO: connect
    }

    @FXML
    public void onReconnect(ActionEvent actionEvent) {
        stopConnection();
        Timer t = new Timer();
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                //TODO:: reconnect
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
        msg.setContent(chatTxtBox.getText());
        msg.setFromDate(new Date());
        msg.setMessageType(Message.MessageType.Pending);
        msg.setFromClient(getEngine().getClient().getClient());
        msg.setMessageId("!padding!");
        currentChat.addOwnMessage(msg);
        chatVbox.getChildren().add(msg.buildMessage(this));


        JSONObject cmds = buildCommand("send", "msg", msg.toJson());
        cmds.put("to", currentChat.getChatTo().getClientId());
        getEngine().getClient().sendMessage(cmds.toJSONString());
    }

    @FXML
    public void initialize() {
        HBox box = new HBox();
        Button btn = new Button("Add contact");
        btn.setOnAction(e -> {
            String id = new AllertBox(getScene(), Modality.APPLICATION_MODAL).displayTextField("Client id", "Connect to client", "Submit", false);
            if (id != null) {
                getEngine().getClient().sendMessage("{\"cmd\": \"checkclient\", \"client\": \"" + id + "\"}");
            }
        });
        box.getChildren().add(btn);
        usersVbox.getChildren().add(box);
    }

    public void removeMessage(Message message) {
        chatVbox.getChildren().remove(message.getMessageDisplayed());
    }

    public void receiveMessage(Message message) {
        putMessageInChat(message);
    }

    public void switchChat(Chat chat) {
        this.currentChat = chat;
        buildCurrentChat();
    }

    public void setupChat(String partnerId, String partnerName) {
        Client client = new Client();
        client.setClientId(partnerId);
        client.setClientDisplayName(partnerName);
        Chat chat = new Chat(client);
        usersVbox.getChildren().add(chat.setupChatBox(partnerName, client, this));
        chats.put(partnerId, chat);
    }

    private void putMessageInChat(Message message){
        //Own message
        if(message.getFromClient().getClientId().equals(getEngine().getClient().getClient().getClientId())){
            Chat[] obs = chats.values().toArray(new Chat[0]);
            for (Chat c:obs) {
                if(message.getFromClient().getClientId().equals(c.getChatTo().getClientId())){
                    //Found
                    for (Message msg:c.getOwnMessages()) {
                        if(msg.getMessageId().equals(message.getMessageId())){
                            msg.setMessageType(Message.MessageType.Text);
                        }
                    }
                    break;
                }
            }
        } else {
            //Otherones message
            Chat chat = chats.get(message.getFromClient().getClientId());
            chat.getForeignMessages().add(message);
            buildIfVisible(message, chat);
        }
    }

    private void buildIfVisible(Message message, Chat chat){
        if(currentChat.getChatTo().getClientId().equals(chat.getChatTo().getClientId())){
            chatVbox.getChildren().add(message.buildMessage(this));
        }
    }

    private void buildCurrentChat() {
        userNameLabel.setText(currentChat.getChatTo().getClientDisplayName());
        userIdLabel.setText(currentChat.getChatTo().getClientId());
        chatVbox.getChildren().clear();

        int iOwn = 0;
        int iOther = 0;

        for (int i = 0; i < currentChat.getOwnMessages().size() + currentChat.getForeignMessages().size() - 1; i++) {
            Message ownMsg = null;
            Message otherMsg = null;

            if (currentChat.getOwnMessages().size() <= iOwn)
                ownMsg = currentChat.getOwnMessages().get(iOwn);

            if (currentChat.getForeignMessages().size() <= iOther)
                otherMsg = currentChat.getForeignMessages().get(iOther);

            if (ownMsg != null && otherMsg != null)
                if (ownMsg.getFromDate().before(otherMsg.getFromDate())) {
                    chatVbox.getChildren().add(ownMsg.buildMessage(this));
                    iOwn++;
                } else {
                    chatVbox.getChildren().add(otherMsg.buildMessage(this));
                    iOther++;
                }
            else {
                if (ownMsg == null) {
                    if (otherMsg != null) {
                        chatVbox.getChildren().add(otherMsg.buildMessage(this));
                        iOther++;
                    }
                } else if (otherMsg == null) {
                    chatVbox.getChildren().add(ownMsg.buildMessage(this));
                    iOwn++;
                }
            }
        }
    }

    private void stopConnection() {
        if (getEngine().getClient() != null)
            getEngine().getClient().stop();

        getEngine().setClient(null);
    }

    private JSONObject buildCommand(String cmd, String contentName, JSONObject content) {
        JSONObject obj = new JSONObject();
        obj.put("cmd", cmd);
        obj.put(contentName, content);
        obj.put("id", getEngine().getClient().getClient().getClientId());
        return obj;
    }
}
