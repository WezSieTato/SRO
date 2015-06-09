package rso.server.server;

import rso.core.model.Message;
import rso.core.net.SocketSender;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by marcin on 09/06/15.
 */
public class StartingPoint implements Runnable {

    private String[] ips = null;
    private int port;

    public StartingPoint(String[] ips, int port) {
        this.ips = ips;
        this.port = port;
    }

    public void run() {
        Socket socket = null;
        for(String ip : ips){
            if(socket != null)
                break;
            try {
                socket = new Socket(ip, port);
            } catch (IOException e) {
                socket = null;
            }
        }

        if(socket == null){
           Message.RSOMessage.Builder builder =  Message.RSOMessage.newBuilder();
            builder.setToken(Message.Token.newBuilder().setTokenType(Message.TokenType.ENTRY));
            Message.RSOMessage msg = builder.build();

            new SocketSender(socket).send(msg);
        }

    }
}
