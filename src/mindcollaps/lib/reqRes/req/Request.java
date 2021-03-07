package mindcollaps.lib.reqRes.req;

import mindcollaps.lib.Client;

import java.io.Serializable;

public abstract class Request implements Serializable {

    public static final long serialVersionUID = 42L;

    private Client client;

    public Request(Client client) {
        this.client = client;
    }

    public Client getClient() {
        return client;
    }
}
