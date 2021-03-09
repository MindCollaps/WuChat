package mindcollaps.netConnect.client;

import com.sun.xml.internal.ws.api.pipe.Engine;
import javafx.application.Platform;
import mindcollaps.client.ClientEngine;
import mindcollaps.client.controller.ChatPrgm;
import mindcollaps.client.controller.LoginPrgm;
import mindcollaps.lib.Client;
import mindcollaps.lib.Message;
import mindcollaps.utility.UtilityBase;
import org.json.simple.JSONObject;

import java.io.*;
import java.net.Socket;
import java.util.Map.Entry;

public class NetConnectClient {

    private ClientEngine engine;
    private int port = 5007;
    private String host = "127.0.0.1";
    private Socket socket;
    private Client client;
    private PrintWriter writer;
    private BufferedReader reader;

    public NetConnectClient(ClientEngine engine) {
        this.engine = engine;
    }

    public void connectToServer() throws IOException {
        connect();
    }

    public void connectToServer(String host, int port) throws IOException {
        this.host = host;
        this.port = port;
        connect();
    }

    public void stop() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        if (socket == null)
            return false;
        return socket.isConnected();
    }

    public void handleMessage(String str) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                NetConnectClient.this.actualHandle(str);
            }
        });
    }

    private void actualHandle(String str){
        System.out.println(str);
        JSONObject ob = UtilityBase.convertStringToJson(str);
        if(ob == null)
            return;
        String cmd = (String) ob.get("cmd");
        switch (cmd.toLowerCase()) {
            case "message":
                Message msg = UtilityBase.convertJsonToMessage((JSONObject) ob.get("message"));
                engine.getChatPrgm().receiveMessage(msg);
                break;

            case "ping":
                sendMessage("{\"cmd\": \"accept\"}");
                break;

            case "client":
                Client client = UtilityBase.convertJsonToClient((JSONObject) ob.get("client"));
                this.client = client;
                engine.getLoginPrgm().receivePassword();
                break;

            case "chat":
                JSONObject clientJS = (JSONObject) ob.get("client");
                engine.getChatPrgm().setupChat((String) clientJS.get("id"), (String) clientJS.get("name"));
                break;

            case "logon":
                engine.getLoginPrgm().passwordWrong();
                break;

            case "loggedin":
                engine.getLoginPrgm().connectionApproved();
                break;
        }
    }

    public void sendMessage(String str) {
        System.out.println("Send");
        System.out.println(str);
        if(writer == null) {
            try {
                writer = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        writer.println(str);
    }

    private void connect() throws IOException {
        Socket socket = null;
        socket = new Socket(host, port);
        if (socket.isConnected())
            System.out.println("Client is connected to " + host + ":" + port);
        else {
            System.out.println("Client cant connect to " + host + ":" + port);
            return;
        }

        socket.setKeepAlive(true);
        this.socket = socket;
        startMessageListener();
    }

    private void startMessageListener() {
        new Thread(new ServerMessageListener()).start();
    }

    public Client getClient() {
        return client;
    }

    private class ServerMessageListener implements Runnable {
        @Override
        public void run() {
            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (true){
                try {
                    handleMessage(reader.readLine());
                } catch (IOException e) {
                    stop();
                    e.printStackTrace();
                    break;
                }
            }
        }
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
