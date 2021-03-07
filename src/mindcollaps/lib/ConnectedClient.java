package mindcollaps.lib;

import mindcollaps.netConnect.server.NetConClientConnection;

import java.util.ArrayList;
import java.util.Iterator;

public class ConnectedClient extends Client{

    private NetConClientConnection clientConnection;
    private String clientAddress;
    private ArrayList<Message> messages;

    public NetConClientConnection getClientConnection() {
        return clientConnection;
    }

    public void setClientConnection(NetConClientConnection clientConnection) {
        this.clientConnection = clientConnection;
    }

    public String getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }

    public boolean containsId(Message message){
        Iterator<Message> msgs = messages.iterator();
        while (msgs.hasNext()){
            Message msg = msgs.next();
            if(msg.getMessageId().equals(message.getMessageId()))
                return true;
        }
        return false;
    }

    public boolean addMessage(Message message){
        if(!containsId(message)) {
            messages.add(message);
            return true;
        }
        return false;
    }
}
