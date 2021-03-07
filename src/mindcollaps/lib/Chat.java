package mindcollaps.lib;

import java.util.ArrayList;

public class Chat {

    private Client chatTo;
    private final ArrayList<Message> foreignMessages = new ArrayList<>();
    private ArrayList<Message> ownMessages = new ArrayList<>();

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
}
