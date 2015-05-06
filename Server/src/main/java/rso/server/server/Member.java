package rso.server.server;

import rso.core.model.Message;
import rso.core.net.SocketReciver;
import rso.core.net.SocketSender;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by marcin on 05/05/15.
 */
public class Member {

    private SocketSender socketSender;

    public Member(String ip, int port) {
        try {
            Socket socket = new Socket(InetAddress.getByName(ip), port);
            socketSender = new SocketSender(socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(Message.RSOMessage message){
        socketSender.send(message);
    }

}
