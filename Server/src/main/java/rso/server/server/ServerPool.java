package rso.server.server;

import rso.core.model.Message;
import rso.core.net.SocketSender;

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
    private Dictionary<String, SocketSender> socketMap = new Hashtable<String, SocketSender>();

    public void addSender(String id, String ip, int port){
        try {
            Socket socket = new Socket(InetAddress.getByName(ip), port);
            SocketSender socketSender = new SocketSender(socket);
            socketMap.put(id, socketSender);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addSender(String id, SocketSender socketSender){
        socketMap.put(id, socketSender);
    }

    public void addSender(String id, Socket socketSender){
        socketMap.put(id, new SocketSender(socketSender));
    }

    public void send(String id, Message.RSOMessage message){
        System.out.println("Wysylam pod ip " + id);
        socketMap.get(id).send(message);
    }

}
