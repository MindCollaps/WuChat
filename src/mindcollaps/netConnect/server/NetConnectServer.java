package mindcollaps.netConnect.server;

import mindcollaps.lib.Client;
import mindcollaps.lib.ConnectedClient;
import mindcollaps.lib.Message;
import mindcollaps.server.ServerEngine;
import mindcollaps.utility.UtilityBase;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class NetConnectServer {

    private ServerEngine engine;
    private ServerSocket server;
    private int port = 5007;

    public NetConnectServer(ServerEngine engine) {
        this.engine = engine;
    }

    public void startServer(){
        ServerSocket server = null;
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            server.setSoTimeout(5000);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        this.server = server;
        startClientListener();
    }


    public void messageHandler(String str, NetConClientConnection res){
        System.out.println(str);
        JSONObject js = UtilityBase.convertStringToJson(str);
        String cmd = (String) js.get("cmd");

        switch (cmd.toLowerCase()){
            case "send":
                Message msg = UtilityBase.convertJsonToMessage((JSONObject) js.get("msg"));
                Client to = UtilityBase.convertJsonToClient((JSONObject) js.get("to"));
                ConnectedClient from = engine.getClientById((String) js.get("id"));
                engine.sendMessageToClient(from, to, msg);
                break;

            case "checkclient":
                engine.checkClient((String) js.get("client"), res);
                break;

            case "client":
                Client c = UtilityBase.convertJsonToClient((JSONObject) js.get("client"));
                engine.updateClient(c, c.getClientId());
                break;
        }
    }

    public void removeClient(NetConClientConnection con){
        engine.removeClient(con);
    }

    private void startClientListener(){
        new Thread(new ClientAcceptListener(this)).start();
    }

    private class ClientAcceptListener implements Runnable{

        private NetConnectServer srv;

        public ClientAcceptListener(NetConnectServer srv) {
            this.srv = srv;
        }

        @Override
        public void run() {
            while (true){
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Socket s = null;
                try {
                    s = server.accept();
                } catch (IOException e) {
                    continue;
                }
                try {
                    s.setKeepAlive(true);
                } catch (SocketException e) {
                    e.printStackTrace();
                }
                NetConClientConnection nccc = new NetConClientConnection(s, srv);
                System.out.println("New client " + nccc.getSocket().getInetAddress().getHostAddress());
                engine.addClient(nccc);
            }
        }
    }
}
