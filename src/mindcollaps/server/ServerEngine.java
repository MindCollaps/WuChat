package mindcollaps.server;

import mindcollaps.lib.Client;
import mindcollaps.lib.ConnectedClient;
import mindcollaps.lib.Message;
import mindcollaps.lib.User;
import mindcollaps.netConnect.server.NetConClientConnection;
import mindcollaps.netConnect.server.NetConnectServer;
import mindcollaps.utility.UtilityBase;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

public class ServerEngine {
    private NetConnectServer server;
    private ArrayList<ConnectedClient> clients = new ArrayList<>();
    private ArrayList<User> users = new ArrayList<>();

    public ServerEngine() {
        server = new NetConnectServer(this);
        server.startServer();
    }

    public void loginUser(String clientId, String name, String pin, NetConClientConnection con) {
        Client client = getClientById(clientId);
        if (client != null) {
            //LOGIN CLIENT ID PROCESS
            User user = getUserByClientId(clientId);
            if (user != null) {
                if (pin.equals(user.getPin())) {
                    con.sendMessage("{\"cmd\": \"loggedin\"}");
                } else {
                    con.sendMessage("{\"cmd\": \"logon\"}");
                }
            } else {
                //LOGIN PROCESS BY NAME
                user = findUserByName(name);
                if (user != null) {
                    if (user.getPin().equals(pin)) {
                        con.sendMessage("{\"cmd\": \"loggedin\"}");
                    } else {
                        con.sendMessage("{\"cmd\": \"logon\"}");
                    }
                } else {
                    //REGISTER PROCESS
                    User usr = new User();
                    usr.setClientId(clientId);
                    usr.setName(name);
                    usr.setPin(pin);
                    client.setClientDisplayName(name);
                    con.sendMessage("{\"cmd\": \"loggedin\"}");
                }
            }
        } else {
            System.out.println("NANI");
        }
    }

    private User findUserByName(String name) {
        Iterator<User> userIterator = users.iterator();
        while (userIterator.hasNext()) {
            User usr = userIterator.next();
            if (usr.getName().equals(name))
                return usr;
        }
        return null;
    }

    public void addClient(NetConClientConnection con) {
        ConnectedClient c = new ConnectedClient();
        c.setClientAddress(con.getSocket().getInetAddress().getHostAddress());
        c.setClientConnection(con);
        String token = null;
        boolean found = false;
        while (!found) {
            token = UtilityBase.generateRandomToken(5);
            found = true;
            for (ConnectedClient cls : clients) {
                if (cls.getClientId().equals(token)) {
                    found = false;
                }
            }
        }
        c.setClientId(token);

        Timer t = new Timer();
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                c.getClientConnection().sendMessage(buildCommand("client", "client", c.toJson()).toJSONString());
            }
        };
        t.schedule(tt, 1000);
        clients.add(c);
    }

    public void sendBackClient(NetConClientConnection res) {
        ConnectedClient client = getClientById(res.getSocket().getInetAddress().getHostAddress());
        res.sendMessage(buildCommand("client", "client", client.toJson()).toJSONString());
    }

    public void removeClient(NetConClientConnection con) {
        try {
            con.getSocket().close();
        } catch (Exception e) {
        }
        Iterator<ConnectedClient> cs = clients.iterator();
        while (cs.hasNext()) {
            ConnectedClient client = cs.next();
            if (con.getSocket().getInetAddress().getHostAddress().equals(client.getClientAddress()))
                cs.remove();
        }
    }

    public void sendMessageToClient(Client from, Client to, Message message) {
        ConnectedClient client = getClientByClient(to);
        Message newMessage = new Message();
        newMessage.setFromClient(from);
        newMessage.setFromDate(message.getFromDate());
        newMessage.setContent(message.getContent());
        if (message.getMessageId() != null)
            if (!message.getMessageId().equals(""))
                newMessage.setMessageId(message.getMessageId());
            else {
                message.setMessageId(UtilityBase.generateRandomToken(15));
                while (!client.addMessage(message)) {
                    message.setMessageId(UtilityBase.generateRandomToken(15));
                }
            }
        client.getClientConnection().sendMessage(buildCommand("message", "message", newMessage.toJson()).toJSONString());
    }

    public User getUserByClientId(String id) {
        Iterator<User> us = users.iterator();
        while (us.hasNext()) {
            User usr = us.next();
            if (usr.getClientId().equals(id))
                return usr;
        }
        return null;
    }

    public ConnectedClient getClientByClient(Client client) {
        Iterator<ConnectedClient> cs = clients.iterator();
        ConnectedClient found = null;
        while (cs.hasNext()) {
            ConnectedClient cli = cs.next();
            if (cli.getClientId().equals(client.getClientId())) {
                found = cli;
                break;
            }
        }
        return found;
    }

    public ConnectedClient getClientById(String s) {
        Iterator<ConnectedClient> cs = clients.iterator();
        ConnectedClient found = null;
        while (cs.hasNext()) {
            ConnectedClient cli = cs.next();
            if (cli.getClientId().equals(s)) {
                found = cli;
                break;
            }
        }
        return found;
    }

    public void checkClient(String clientId, NetConClientConnection res) {
        ConnectedClient client = getClientById(clientId);
        if (client == null) {
            return;
        }
        res.sendMessage(buildCommand("chat", "client", client.toJson()).toJSONString());
    }

    private JSONObject buildCommand(String cmd, String contentName, JSONObject content) {
        JSONObject obj = new JSONObject();
        obj.put("cmd", cmd);
        obj.put(contentName, content);
        return obj;
    }
}
