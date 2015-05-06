package rso.core.net;

import rso.core.model.Message;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Created by marcin on 06/05/15.
 */
public class ServerPool {
    private Dictionary<Integer, SocketSender> socketMap = new Hashtable<Integer, SocketSender>();

    public void addSender(int id, String ip, int port){
        try {
            Socket socket = new Socket(InetAddress.getByName(ip), port);
            SocketSender socketSender = new SocketSender(socket);
            socketMap.put(id, socketSender);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addSender(int id, SocketSender socketSender){
        socketMap.put(id, socketSender);
    }

    public void send(int id, Message.RSOMessage message){
        socketMap.get(id).send(message);
    }

}
