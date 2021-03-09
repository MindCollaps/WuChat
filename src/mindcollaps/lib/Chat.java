package mindcollaps.lib;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import mindcollaps.client.controller.ChatPrgm;

import java.util.ArrayList;

public class Chat {

    private Client chatTo;
    private final ArrayList<Message> foreignMessages = new ArrayList<>();
    private ArrayList<Message> ownMessages = new ArrayList<>();
    private HBox displayedMember;

    private int notifications = 0;
    private Label newNotification;

    public HBox setupChatBox(String name, Client client, ChatPrgm prgm){
        HBox box = new HBox();
        Label label = new Label(name);
        box.getChildren().add(label);
        box.setOnMouseClicked(e -> {
            prgm.switchChat(this);
        });
        Label notification = new Label("Notification: 0");
        box.getChildren().add(notification);

        this.displayedMember = box;
        this.newNotification = notification;
        return box;
    }

    public Chat(Client chatTo) {
        this.chatTo = chatTo;
    }

    public void addForeignMessage(Message message){
        foreignMessages.add(message);
    }

    public void removeForeignMessage(Message message){
        foreignMessages.remove(message);
    }

    public void removeForeignMessage(String id){
        foreignMessages.removeIf(msg -> msg.getMessageId().equals(id));
    }

    public void addOwnMessage(Message message){
        ownMessages.add(message);
    }

    public void removeOwnMessage(Message message){
        ownMessages.remove(message);
    }

    public void removeOwnMessage(String id){
        ownMessages.removeIf(msg -> msg.getMessageId().equals(id));
    }

    public Client getChatTo() {
        return chatTo;
    }

    public void setChatTo(Client chatTo) {
        this.chatTo = chatTo;
    }

    public ArrayList<Message> getForeignMessages() {
        return foreignMessages;
    }

    public ArrayList<Message> getOwnMessages() {
        return ownMessages;
    }

    public void setOwnMessages(ArrayList<Message> ownMessages) {
        this.ownMessages = ownMessages;
    }

    public HBox getDisplayedMember() {
        return displayedMember;
    }

    public void setDisplayedMember(HBox displayedMember) {
        this.displayedMember = displayedMember;
    }

    public int getNotifications() {
        return notifications;
    }

    public void setNotifications(int notifications) {
        this.notifications = notifications;
    }

    public Label getNewNotification() {
        return newNotification;
    }

    public void setNewNotification(Label newNotification) {
        this.newNotification = newNotification;
    }
}
