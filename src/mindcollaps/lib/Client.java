package mindcollaps.lib;

import org.json.simple.JSONObject;

import java.io.Serializable;

public class Client implements Serializable {

    public static final long serialVersionUID = 42L;

    private String clientId;
    private String clientDisplayName;
    private String clientServerAddress;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientDisplayName() {
        return clientDisplayName;
    }

    public void setClientDisplayName(String clientDisplayName) {
        this.clientDisplayName = clientDisplayName;
    }

    public String getClientServerAddress() {
        return clientServerAddress;
    }

    public void setClientServerAddress(String clientServerAddress) {
        this.clientServerAddress = clientServerAddress;
    }

    public JSONObject toJson(){
        JSONObject obj = new JSONObject();
        obj.put("id", clientId);
        obj.put("name", clientDisplayName);
        return obj;
    }
}
