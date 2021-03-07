package mindcollaps.netConnect.server;

import com.sun.xml.internal.ws.api.databinding.ClientCallBridge;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class NetConClientConnection {

    private Socket socket;
    private NetConnectServer server;
    private PrintWriter writer;
    private BufferedReader reader;

    public NetConClientConnection(Socket socket, NetConnectServer server) {
        this.socket = socket;
        this.server = server;
        startMessageListener();
    }

    public void stop() {
        System.out.println("I got stopped");
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        delete();
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

    private void delete() {
        server.removeClient(this);
    }

    private void startMessageListener() {
        new Thread(new MessageListener(this)).start();
    }

    public Socket getSocket() {
        return socket;
    }

    private class MessageListener implements Runnable {
        private NetConClientConnection clientConnection;

        public MessageListener(NetConClientConnection clientConnection) {
            this.clientConnection = clientConnection;
        }

        @Override
        public void run() {
            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (true){
                try {
                    server.messageHandler(reader.readLine(), clientConnection);
                } catch (IOException e) {
                    stop();
                    server.removeClient(clientConnection);
                    e.printStackTrace();
                    break;
                }
            }
        }
    }
}
