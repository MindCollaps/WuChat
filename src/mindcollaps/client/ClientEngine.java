package mindcollaps.client;

import mindcollaps.client.controller.ChatPrgm;
import mindcollaps.client.controller.LoginPrgm;
import mindcollaps.netConnect.client.NetConnectClient;

public class ClientEngine {

    private ChatPrgm chatPrgm;
    private LoginPrgm loginPrgm;
    private NetConnectClient client;

    public ChatPrgm getChatPrgm() {
        return chatPrgm;
    }

    public LoginPrgm getLoginPrgm() {
        return loginPrgm;
    }

    public NetConnectClient getClient() {
        return client;
    }

    public void setChatPrgm(ChatPrgm chatPrgm) {
        this.chatPrgm = chatPrgm;
    }

    public void setLoginPrgm(LoginPrgm loginPrgm) {
        this.loginPrgm = loginPrgm;
    }

    public void setClient(NetConnectClient client) {
        this.client = client;
    }
}
