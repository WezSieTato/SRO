package rso.middleware.server;

import rso.core.abstraction.BaseNode;
import rso.core.events.EventManager;
import rso.core.events.RSOEvent;
import rso.core.model.Message;
import rso.core.net.SocketReciver;
import rso.core.net.SocketSender;
import rso.core.taskmanager.TaskManager;
import rso.core.taskmanager.TaskMessage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by modzelej on 2015-05-05.
 */
public class MiddlewareThread extends BaseNode implements Runnable {

    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private ServerSocket socket;
    private ArrayList<Socket> connectedSockets;
    private boolean finish = false;
    private int port;

    public void setPort(int port) {
        this.port = port;
    }

    public static String endReciving = EventManager.registerEvent(MiddlewareThread.class, "end reciving");


    private class MiddlewareReciver implements Runnable{
        private SocketReciver reciver;
        private boolean end = false;
        private TaskManager taskManager;

        public MiddlewareReciver(Socket socket) {

            reciver = new SocketReciver(socket);
            taskManager = new TaskManager();
            EventManager.addListener(MiddlewareThread.endReciving, MiddlewareThread.class, new EventManager.EventListener() {
                public void event(RSOEvent event) {
                    end = true;
                }
            });

            EventManager.addListener(HeartbeatTask.newMiddleServer, HeartbeatTask.class, new EventManager.EventListener() {
                public void event(RSOEvent event) {
                    Message.MiddlewareHeartbeat mh = (Message.MiddlewareHeartbeat) event.getObject();
                    LOGGER.log(Level.INFO, "tutaj by sie dodal nowy ziomek ale nie ma tablicy ID na IP");
                }
            });

            taskManager.addTask(new HeartbeatTask());
            Thread t = new Thread(taskManager);
            t.start();

        }

        public void run() {
            while(!end){
               TaskMessage message = reciver.read();

                taskManager.putTaskMessage(message);

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
        connectedSockets = new ArrayList<Socket>();
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
        for (Socket s : connectedSockets) {
            MiddlewareReciver mr = new MiddlewareReciver(s);
            Thread t = new Thread(mr);
            t.start();
        }

            while (!finish) {
                try {
                    Socket midSoc = socket.accept();
                    LOGGER.log(Level.INFO, "new mid socket connected!");
                    connectedSockets.add(midSoc);
                    MiddlewareReciver mrr = new MiddlewareReciver(midSoc);
                    Thread tt = new Thread(mrr);
                    tt.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

    }
}
