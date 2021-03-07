package mindcollaps.lib.reqRes.req;

import mindcollaps.lib.Client;

import java.io.Serializable;

public class ReqConnect extends Request implements Serializable {

    public static final long serialVersionUID = 42L;

    private String userName;
    private String pin;

    //true = register, false = login
    private boolean register;

    public ReqConnect(Client client) {
        super(client);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public boolean isRegister() {
        return register;
    }

    public void setRegister(boolean register) {
        this.register = register;
    }
}
