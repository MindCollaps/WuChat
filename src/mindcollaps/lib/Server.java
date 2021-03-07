package mindcollaps.lib;

import java.io.Serializable;

public class Server implements Serializable {

    public static final long serialVersionUID = 42L;

    private String serverIp;

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }
}
