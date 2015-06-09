package rso.server.server;

import rso.core.events.EventManager;
import rso.core.events.RSOEvent;
import rso.core.model.Message;
import rso.core.net.SocketSender;
import rso.server.task.EntryTask;

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
    private boolean stop = false;

    public StartingPoint(String[] ips, int port) {
        this.ips = ips;
        this.port = port;

        EventManager.addListener(EntryTask.entryEvent, EntryTask.class, new EventManager.EventListener() {
            public void event(RSOEvent event) {
                stop = true;
            }
        });
    }

    public void run() {
        Socket socket = null;
        System.out.println("Szukamy serwerow!");
        for(String ip : ips){
            if(socket != null)
                break;
            try {
                socket = new Socket(ip, port);
            } catch (IOException e) {
                socket = null;
                System.out.println("Serwera nie ma z ip" + ip);
            }
        }

        if(socket != null && !stop){
           Message.RSOMessage.Builder builder =  Message.RSOMessage.newBuilder();
            builder.setToken(Message.Token.newBuilder().setTokenType(Message.TokenType.ENTRY));
            Message.RSOMessage msg = builder.build();

            System.out.println("Wyslano zadanie wejscia do " + socket.getInetAddress().getHostAddress());

            new SocketSender(socket).send(msg);
        }

    }
}
