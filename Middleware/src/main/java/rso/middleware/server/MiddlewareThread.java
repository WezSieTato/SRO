package rso.middleware.server;

import rso.core.abstraction.BaseNode;
import rso.core.events.EventManager;
import rso.core.events.RSOEvent;
import rso.core.net.SocketReciver;
import rso.core.net.SocketSender;
import rso.core.taskmanager.TaskMessage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by modzelej on 2015-05-05.
 */
public class MiddlewareThread extends BaseNode implements Runnable {

    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private Socket socket;
    private ArrayList<Socket> connectedSockets;
    private boolean finish = false;

    public static String endReciving = EventManager.registerEvent(MiddlewareThread.class, "end reciving");


    private class MiddlewareReciver implements Runnable{
        private SocketReciver reciver;
        private boolean end = false;

        public MiddlewareReciver(Socket socket) {

            reciver = new SocketReciver(socket);
            EventManager.addListener(MiddlewareThread.endReciving, MiddlewareThread.class, new EventManager.EventListener() {
                public void event(RSOEvent event) {
                    end = true;
                }
            });
        }

        public void run() {
            while(!end){
               TaskMessage message = reciver.read();
                LOGGER.log(Level.INFO, message.toString());
            }
        }
    }

    public MiddlewareThread(){


        init();
    }

    private void init() {
        EventManager.addListener(Server.disconnectWithoutRedirectEvent, Server.class, new EventManager.EventListener() {
            public void event(RSOEvent event) {
                finish = true;
            }
        });
        connectedSockets = new ArrayList<Socket>();
        for(String s: addresses){
            try {
                Socket tmp = new Socket(InetAddress.getByName(s), 6970);
                if(tmp != null){
                    connectedSockets.add(tmp);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    public void run() {
        for(Socket s: connectedSockets){
            MiddlewareReciver mr = new MiddlewareReciver(s);
            mr.run();
        }
        while (!finish){


        }
    }
}
