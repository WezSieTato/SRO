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
import java.net.UnknownHostException;

/**
 * Created by marcin on 09/06/15.
 */
public class StartingPoint implements Runnable {

    private String[] ips = null;
    private int port;
    private boolean stop = false;
    private ServerPool serverPool;

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
        String myIp = null;
        try {
            myIp = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        String ipp = null;
        System.out.println("Moje ip to " + myIp);
        for(String ip : ips){
            if(ip.equals(myIp))
                continue;
            if(socket != null)
                break;
            try {
                socket = new Socket(ip, port);
                ipp = ip;
            } catch (IOException e) {
                socket = null;
                System.out.println("Serwera nie ma z ip " + ip);
            }
        }

        if(socket != null && !stop){
           Message.RSOMessage.Builder builder =  Message.RSOMessage.newBuilder();
            builder.setToken(Message.Token.newBuilder().setTokenType(Message.TokenType.ENTRY));
            Message.RSOMessage msg = builder.build();

            System.out.println("Wyslano zadanie wejscia do " + socket.getInetAddress().getHostAddress());

            serverPool.addSender(ipp, socket);

            new Thread(new ServerReceiver(socket)).start();

            new SocketSender(socket).send(msg);
        }

    }

    public ServerPool getServerPool() {
        return serverPool;
    }

    public void setServerPool(ServerPool serverPool) {
        this.serverPool = serverPool;
    }
}
