package rso.middleware.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rso.core.abstraction.BaseNode;
import rso.core.events.EventManager;
import rso.core.events.RSOEvent;
import rso.core.model.Message;
import rso.core.net.SocketIdPair;
import rso.core.net.SocketReciver;
import rso.core.net.SocketSender;
import rso.core.taskmanager.TaskManager;
import rso.core.taskmanager.TaskMessage;
import rso.middleware.MiddlewareLayer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by modzelej on 2015-05-05.
 */
public class MiddlewareThread implements Runnable {


    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private ServerSocket socket;
    private LinkedBlockingQueue<SocketIdPair> connectedSockets;
    private boolean finish = false;
    private int bestServerId = -1;


    public static String endReciving = EventManager.registerEvent(MiddlewareThread.class, "end reciving");
    public static String userConnectionsNum = EventManager.registerEvent(MiddlewareThread.class, "useer connection number");


    private class MiddlewareReciver implements Runnable{
        private SocketReciver reciver;
        private boolean end = false;
        private Socket socket;



        public MiddlewareReciver(final Socket socket) {

            reciver = new SocketReciver(socket);
            this.socket = socket;
            EventManager.addListener(MiddlewareThread.endReciving, MiddlewareThread.class, new EventManager.EventListener() {
                public void event(RSOEvent event) {
                    end = true;
                }
            });

            EventManager.addListener(HeartbeatTask.newMiddleServer, HeartbeatTask.class, new EventManager.EventListener() {
                public void event(RSOEvent event) {
                    Message.MiddlewareHeartbeat mh = (Message.MiddlewareHeartbeat) event.getObject();
                    LOGGER.log(Level.INFO, "Nowy user");

                }
            });

            EventManager.addListener(ClientRequestTask.sendToClient, ClientRequestTask.class, new EventManager.EventListener() {
                public void event(RSOEvent event) {
                    SocketSender snd = null;
//                    try {
                        snd = new SocketSender(socket);
                        snd.send((Message.RSOMessage) event.getObject());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }

                }
            });

            EventManager.addListener(MiddlewareConnectionsManager.requestUserNum, MiddlewareConnectionsManager.class, new EventManager.EventListener() {
                public void event(RSOEvent event) {
                    EventManager.event(MiddlewareThread.class, MiddlewareThread.userConnectionsNum, connectedSockets.size());
                }
            });

            EventManager.addListener(MiddlewareConnectionsManager.redirectServer, MiddlewareConnectionsManager.class, new EventManager.EventListener() {
                public void event(RSOEvent event) {
                    bestServerId = ((Integer) event.getObject()).intValue();
                }
            });

            MiddlewareLayer.taskManager.addTask(new ClientRequestTask());


        }

        public void run() {

            if(bestServerId != -1){

                try {
                    if(!MiddlewareLayer.middlwareIPs.get(bestServerId).equals(InetAddress.getLocalHost().getHostAddress())){

                        Message.MiddlewareHeartbeat.Builder hrt = Message.RSOMessage.newBuilder().getMiddlewareHeartbeatBuilder();
                        hrt.setConnectedClients(1).setServerId(bestServerId).setMessageType(Message.MiddlewareMessageType.Heartbeat);

                        Message.MiddlewareMessage.Builder builder = Message.MiddlewareMessage.newBuilder();
                        builder.setSubjectName("RSO").setNodeId(1);
                        Message.RSOMessage message = Message.RSOMessage.newBuilder().setMiddlewareMessage(builder).build();

                        SocketSender snd = new SocketSender(socket);
                        snd.send(message);


                        end = true;
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }

            while(!end){
               TaskMessage message = reciver.read();

                MiddlewareLayer.taskManager.putTaskMessage(message);

                LOGGER.log(Level.INFO, message.toString());
            }
        }
    }

    public MiddlewareThread(ServerSocket socket){
        this.socket = socket;

        init();
    }

    private void init() {
        EventManager.addListener(Server.disconnectWithoutRedirectEvent, Server.class, new EventManager.EventListener() {
            public void event(RSOEvent event) {
                finish = true;
            }
        });
        connectedSockets = new LinkedBlockingQueue<SocketIdPair>(100);
//        for(String s: addresses){
//            try {
//                Socket tmp = new Socket(InetAddress.getByName(s), 6970);
//                if(tmp != null){
//                    connectedSockets.add(tmp);
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

    }


    public void run() {
        LOGGER.log(Level.INFO, "start Middlware Thread");
        for (SocketIdPair s : connectedSockets) {
            MiddlewareReciver mr = new MiddlewareReciver(s.getSocket());
            Thread t = new Thread(mr);
            t.start();
        }

            while (!finish) {
                try {
                    Socket midSoc = socket.accept();
                    LOGGER.log(Level.INFO, "new client connected!");
                    connectedSockets.put(new SocketIdPair(1, midSoc));
                    EventManager.event(MiddlewareThread.class, MiddlewareThread.userConnectionsNum, connectedSockets.size());
                    MiddlewareReciver mrr = new MiddlewareReciver(midSoc);
                    Thread tt = new Thread(mrr);
                    tt.start();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

    }
}
