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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by modzelej on 2015-05-07.
 */
public class ClientProto implements Runnable{



    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private Socket socket;
    private LinkedBlockingQueue<SocketIdPair> connectedSockets;
    private boolean finish = false;
    private SocketSender sender;
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


            MiddlewareLayer.taskManager.addTask(new HeartbeatTask());


        }

        public void run() {
            while(!end){
                TaskMessage message = reciver.read();

                MiddlewareLayer.taskManager.putTaskMessage(message);

                LOGGER.log(Level.INFO, message.toString());
            }
        }
    }

    public ClientProto(Socket socket){
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
        LOGGER.log(Level.INFO, "start Client Thread");
        try {
            socket = new Socket("192.168.0.39", 6972);
            sender = new SocketSender(socket);
            MiddlewareReciver mrr = new MiddlewareReciver(socket);
            Thread tt = new Thread(mrr);
            tt.start();

            while (!finish) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Message.MiddlewareHeartbeat.Builder hrt = Message.RSOMessage.newBuilder().getMiddlewareHeartbeatBuilder();
                hrt.setConnectedClients(69).setServerId(32131).setMessageType(Message.MiddlewareMessageType.Heartbeat);

                Message.RSOMessage.Builder snd = Message.RSOMessage.newBuilder().setMiddlewareHeartbeat(hrt.build());
                sender.send(snd.build());


            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}