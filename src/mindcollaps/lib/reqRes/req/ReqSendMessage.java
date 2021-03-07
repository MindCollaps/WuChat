package mindcollaps.lib.reqRes.req;

import mindcollaps.lib.Client;
import mindcollaps.lib.Message;

import java.io.Serializable;

public class ReqSendMessage extends Request implements Serializable {

    public static final long serialVersionUID = 42L;

    private Message.MessageType messageType;
    private String content;

    public ReqSendMessage(Client client, Message.MessageType messageType, String content) {
        super(client);
        this.messageType = messageType;
        this.content = content;
    }

    public Message.MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(Message.MessageType messageType) {
        this.messageType = messageType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
