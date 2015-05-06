package rso.server;

import rso.core.events.EventManager;
import rso.core.model.Message;
import rso.core.net.SocketReciver;
import rso.core.taskmanager.TaskMessage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by marcin on 05/05/15.
 */
public class Receiver implements Runnable{

    private SocketReciver socketReciver;
    public static String messageReceived = EventManager.registerEvent(Receiver.class, "message received");

    public Receiver(String ip, int port) {
        try {
            Socket socket = new Socket(InetAddress.getByName(ip), port);
            socketReciver = new SocketReciver(socket);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void run() {
        while (true){
            TaskMessage message = socketReciver.read();
            EventManager.event(Receiver.class, messageReceived, message);
        }
    }

}
