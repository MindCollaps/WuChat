package mindcollaps.lib.reqRes.res;

import mindcollaps.lib.Server;

import java.io.Serializable;

public abstract class ResOk extends Response implements Serializable {

    public static final long serialVersionUID = 42L;

    public ResOk(Server server) {
        super(server);
    }

    @Override
    public int getHttpCode() {
        return 200;
    }
}
