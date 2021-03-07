package mindcollaps.lib.reqRes.res;

import mindcollaps.lib.Server;

import java.io.Serializable;

public class ResNotFound extends Response implements Serializable {

    public static final long serialVersionUID = 42L;

    public ResNotFound(Server server) {
        super(server);
    }

    @Override
    public int getHttpCode() {
        return 404;
    }
}
