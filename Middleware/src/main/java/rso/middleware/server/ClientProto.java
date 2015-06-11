package rso.middleware.server;

import rso.core.events.EventManager;
import rso.core.events.RSOEvent;
import rso.core.model.Message;
import rso.core.net.SocketIdPair;
import rso.core.net.SocketReciver;
import rso.core.net.SocketSender;
import rso.core.taskmanager.TaskMessage;
import rso.middleware.MiddlewareLayer;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by modzelej on 2015-05-07.
 */
public class ClientProto implements Runnable{



    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    public static String endReciving = EventManager.registerEvent(MiddlewareThread.class, "end reciving");
    private Socket socket;
    private LinkedBlockingQueue<SocketIdPair> connectedSockets;
    private boolean finish = false;
    private SocketSender sender;


    public ClientProto(Socket socket) {
        this.socket = socket;

        init();
    }

    private void init() {
        EventManager.addListener(Server.disconnectWithoutRedirectEvent, Server.class, new EventManager.EventListener() {
            public void event(RSOEvent event) {
                finish = true;
            }
        });

        EventManager.addListener(MidToClientTask.sendToMiddleware, MidToClientTask.class, new EventManager.EventListener() {
            public void event(RSOEvent event) {
                Message.MiddlewareHeartbeat.Builder hrt = Message.RSOMessage.newBuilder().getMiddlewareHeartbeatBuilder();
                hrt.setConnectedClients(69).setServerId(32131).setMessageType(Message.MiddlewareMessageType.Heartbeat);

                Message.MiddlewareMessage.Builder builder = Message.MiddlewareMessage.newBuilder();
                builder.setSubjectName("RSO").setNodeId(1);
                Message.RSOMessage message = Message.RSOMessage.newBuilder().setMiddlewareMessage(builder).build();

                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {

                }
                sender.send(message);
            }
        });
        EventManager.addListener(MidToClientRedirectTask.redirectEvent, MidToClientRedirectTask.class, new EventManager.EventListener() {
            public void event(RSOEvent event) {
                LOGGER.log(Level.INFO, "------------- WYLACZAm SOKET ------------");
                EventManager.event(ClientProto.class, endReciving, "koniec pracy!");
                redirect(((Integer) event.getObject()).intValue());
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
//
//            }
//        }

    }

    public void run() {
        LOGGER.log(Level.INFO, "start Client Thread");
        try {
            socket = new Socket("192.168.1.40", 6971);
            sender = new SocketSender(socket);
            MiddlewareReciver mrr = new MiddlewareReciver(socket);
            Thread tt = new Thread(mrr);
            tt.start();


                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {

                }

            Message.MiddlewareHeartbeat.Builder hrt = Message.RSOMessage.newBuilder().getMiddlewareHeartbeatBuilder();
            hrt.setConnectedClients(69).setServerId(32131).setMessageType(Message.MiddlewareMessageType.Heartbeat);

            Message.MiddlewareMessage.Builder builder = Message.MiddlewareMessage.newBuilder();
            builder.setSubjectName("RSO").setNodeId(1);
            Message.RSOMessage message = Message.RSOMessage.newBuilder().setMiddlewareMessage(builder).build();
            sender.send(message);


        } catch (IOException e) {

        }


    }

    private void redirect(int id){
        LOGGER.log(Level.INFO, "redirecting");
        try {
            socket.close();
        } catch (IOException e) {

        }
        try {
            socket = new Socket(MiddlewareLayer.middlwareIPs[id], 6971);
            sender = new SocketSender(socket);
            MiddlewareReciver mrr = new MiddlewareReciver(socket);
            Thread tt = new Thread(mrr);
            tt.start();


            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {

            }

            Message.MiddlewareHeartbeat.Builder hrt = Message.RSOMessage.newBuilder().getMiddlewareHeartbeatBuilder();
            hrt.setConnectedClients(69).setServerId(32131).setMessageType(Message.MiddlewareMessageType.Heartbeat);

            Message.MiddlewareMessage.Builder builder = Message.MiddlewareMessage.newBuilder();
            builder.setSubjectName("RSO").setNodeId(1);
            Message.RSOMessage message = Message.RSOMessage.newBuilder().setMiddlewareMessage(builder).build();
            sender.send(message);


        } catch (IOException e) {

        }
    }

    private class MiddlewareReciver implements Runnable {
        private SocketReciver reciver;
        private boolean end = false;


        public MiddlewareReciver(Socket socket) {

            reciver = new SocketReciver(socket);
            EventManager.addListener(MiddlewareThread.endReciving, MiddlewareThread.class, new EventManager.EventListener() {
                public void event(RSOEvent event) {
                    end = true;
                }
            });


            MiddlewareLayer.taskManager.addTask(new MidToClientTask());
            MiddlewareLayer.taskManager.addTask(new MidToClientRedirectTask());


        }

        public void run() {
            while (!end) {
                TaskMessage message = reciver.read();

                MiddlewareLayer.taskManager.putTaskMessage(message);

                LOGGER.log(Level.INFO, message.toString());
            }
        }
    }
}
