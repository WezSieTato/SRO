package rso.middleware.server;

import rso.core.events.EventManager;
import rso.core.events.RSOEvent;
import rso.core.model.Message;
import rso.core.net.SocketReciver;
import rso.core.net.SocketSender;
import rso.core.taskmanager.TaskMessage;
import rso.middleware.MiddlewareLayer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by modzelej on 2015-06-05.
 */
public class MiddlewareConnectionsManager implements Runnable {
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    public static String requestUserNum = EventManager.registerEvent(MiddlewareConnectionsManager.class, "get user connection number");
    public static String sendHeartbeat = EventManager.registerEvent(MiddlewareConnectionsManager.class, "send heatbeat");
    public Integer globalConnections = 0;
    ServerSocket serverSocket;
    ArrayList<Socket> middlewareSockets;
    private boolean finish = false;


    public MiddlewareConnectionsManager() {
        MiddlewareLayer.taskManager.addTask(new HeartbeatTaskRecive());
        init();
    }

    private void init() {

        middlewareSockets = new ArrayList<Socket>();
        EventManager.addListener(MiddlewareThread.userConnectionsNum, MiddlewareThread.class, new EventManager.EventListener() {
            public void event(RSOEvent event) {
                globalConnections = (Integer) event.getObject();
            }
        });

        try {
            serverSocket = new ServerSocket(6970);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {

        LOGGER.log(Level.INFO, "start Middlware-Middleware Thread");

        for (String s : MiddlewareLayer.middlwareIPs) {
            try {
                if(!s.equals(InetAddress.getLocalHost().getHostAddress())){
                    MiddlewareReciver mr = null;

                        mr = new MiddlewareReciver(null, true, s);
                        Thread t = new Thread(mr);
                        t.start();
                }
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }

        }

        while (!finish) {
            try {
                Socket midSoc = serverSocket.accept();
                LOGGER.log(Level.INFO, "Mid 2 Mid new socket");
                if (!middlewareSockets.contains(midSoc)) {
                    //    middlewareSockets.add(midSoc);
                    MiddlewareReciver mrr = new MiddlewareReciver(midSoc, false, null);
                    Thread tt = new Thread(mrr);
                    tt.start();
                }

            } catch (IOException e) {
                e.printStackTrace();

            }

        }

    }

    private class MiddlewareReciver implements Runnable {

        Timer heartTimer = new Timer();
        Timer cancelTimer = new Timer();
        private SocketReciver reciver;
        private boolean end = false;
        private Socket socket;
        private boolean gotResponse = false;
        private boolean firstRun = true;
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                EventManager.event(MiddlewareConnectionsManager.class, MiddlewareConnectionsManager.sendHeartbeat, globalConnections);
                if (firstRun) {
                    cancelTimer.cancel();
                    cancelTimer = new Timer();
                    cancelTimer.schedule(new CancelTimerTask(), 10000);
                    firstRun = false;
                }

            }
        };
        private boolean init = false;
        private String ipServer;
        private HeartbeatTask ht;
        private int numOfHeartbeats = 0;

        public MiddlewareReciver(final Socket socketa, boolean initonnection, String ip) {
            init = initonnection;
            ipServer = ip;

            //reciver = new SocketReciver(socketa);
            this.socket = socketa;

            EventManager.addListener(HeartbeatTask.sendMidHeartbeat, HeartbeatTask.class, new EventManager.EventListener() {
                public void event(RSOEvent event) {
//                    try {
                    SocketSender snd = new SocketSender(socket);
                    if (socket != null) {
                        numOfHeartbeats++;
                        LOGGER.log(Level.INFO, "Przed wyslaniem hb");
                        snd.send((Message.RSOMessage) event.getObject());
                        LOGGER.log(Level.INFO, "Wysylam heartbeata o numerze  " + numOfHeartbeats);
                    }

//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                }
            });

            EventManager.addListener(HeartbeatTaskRecive.response, HeartbeatTaskRecive.class, new EventManager.EventListener() {
                public void event(RSOEvent event) {
                    String heszke = (String) event.getObject();
                    LOGGER.log(Level.INFO, heszke);
                    cancelTimer.cancel();
                    cancelTimer = new Timer();
                    cancelTimer.schedule(new CancelTimerTask(), 10000);
                }
            });



        }

        public void run() {
            if (init) {
                try {
                    socket = new Socket(ipServer, 6970);
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
            if (socket == null) {
                end = true;
                return;
            } else {
                if (middlewareSockets.contains(socket)) {
                    end = true;
                    return;
                }
                middlewareSockets.add(socket);
                reciver = new SocketReciver(socket);
                ht = new HeartbeatTask();
                Thread t = new Thread(ht);
                t.start();
                heartTimer.schedule(timerTask, 10000, 10000);
            }
            while (true) {
                try {
                    TaskMessage message = reciver.read();

                    MiddlewareLayer.taskManager.putTaskMessage(message);
                    LOGGER.log(Level.INFO, message.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        reciver.getSocket().close();
                        middlewareSockets.remove(this);
                        break;
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }


            }
        }

        private class CancelTimerTask extends TimerTask {

            @Override
            public void run() {
                LOGGER.log(Level.ALL, "\nPOWINIENEM GO ODLACZYC TERAZ \n");

            }
        }
    }
}
