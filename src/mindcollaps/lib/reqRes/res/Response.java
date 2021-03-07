package mindcollaps.lib.reqRes.res;

import mindcollaps.lib.Server;

import java.io.Serializable;

public abstract class Response implements Serializable {

    public static final long serialVersionUID = 42L;

    private Server server;

    public Response(Server server) {
        this.server = server;
    }

    public abstract int getHttpCode();

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }
}
