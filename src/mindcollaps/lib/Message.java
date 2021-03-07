package mindcollaps.lib;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import mindcollaps.client.Controller;
import org.json.simple.JSONObject;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {

    public static final long serialVersionUID = 42L;

    private Client fromClient;
    private Date fromDate;
    private String content;
    private String messageId;
    private MessageType messageType;
    private Node messageDisplayed;
    private Label text;

    public Node buildMessage(Controller controller){
        if(messageType == MessageType.Text){
            this.messageDisplayed = generateTextChat(content);
        } else if (messageType == MessageType.Delete) {
            controller.removeMessage(this);
        } else if (messageType == MessageType.Edit){
            text.setText(content);
        } else if (messageType == MessageType.Image){
            //TODO: do it
            System.out.println("THIS ACTION IS NOT DONE YET");
        }
        return this.messageDisplayed;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public Node getMessageDisplayed() {
        return messageDisplayed;
    }

    public void setMessageDisplayed(Node messageDisplayed) {
        this.messageDisplayed = messageDisplayed;
    }

    public Client getFromClient() {
        return fromClient;
    }

    public void setFromClient(Client fromClient) {
        this.fromClient = fromClient;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Label getText() {
        return text;
    }

    public JSONObject toJson(){
        JSONObject obj = new JSONObject();
        obj.put("type", messageType.name());
        obj.put("content", content);
        obj.put("id", messageId);
        obj.put("date", fromDate);
        obj.put("from", fromClient.toJson());
        return obj;
    }

    private HBox generateTextChat(String chat){
        HBox box = new HBox();
        Label text = new Label();
        text.setText(chat);
        this.text = text;
        box.getChildren().add(text);
        return box;
    }

    public enum MessageType{
        Image, Text, Edit, Delete
    }
}
