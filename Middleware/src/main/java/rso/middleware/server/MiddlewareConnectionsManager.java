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
    public static String redirectServer = EventManager.registerEvent(MiddlewareConnectionsManager.class, "get redirect server id");
    public Integer globalConnections = 0;
    ServerSocket serverSocket;
    ArrayList<SocketIdPair> middlewareSockets;
    private boolean finish = false;
    private Object guard = new Object();

    public MiddlewareConnectionsManager() {
        MiddlewareLayer.taskManager.addTask(new HeartbeatTaskRecive());
        init();
    }

    private void init() {

        middlewareSockets = new ArrayList<SocketIdPair>();
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
                synchronized (guard){
                    if (!middlewareSockets.contains(midSoc)) {
                        //    middlewareSockets.add(midSoc);
                        MiddlewareReciver mrr = new MiddlewareReciver(midSoc, false, null);
                        Thread tt = new Thread(mrr);
                        tt.start();
                    }
                }


            } catch (IOException e) {
                e.printStackTrace();

            }

        }

    }

    private class MiddlewareReciver implements Runnable {

        Timer heartTimer = new Timer();
        Timer cancelTimer = new Timer();
        Timer clientNumTimer = new Timer();
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
                    cancelTimer.schedule(new CancelTimerTask(), 20000);
                    firstRun = false;
                }

            }
        };
        TimerTask clientNumTask = new TimerTask() {
            @Override
            public void run() {
                SocketIdPair tmp = (middlewareSockets.size() > 0 ? middlewareSockets.get(0) : null);

                synchronized (guard){
                    for(int i = 0; i < middlewareSockets.size(); ++i) {
                        if(tmp != null && tmp.getId() < middlewareSockets.get(i).getId()){
                            tmp  = middlewareSockets.get(i);
                        }
                    }
                }


                for(String s: MiddlewareLayer.middlwareIPs){
                    if(tmp != null && s.equals(tmp.getSocket().getInetAddress().getHostAddress())){
                        EventManager.event(MiddlewareConnectionsManager.class, MiddlewareConnectionsManager.requestUserNum, tmp.getId());
                    }
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
                    Integer clientNum = (Integer) event.getObject();
                    LOGGER.log(Level.INFO, clientNum.toString());
                    synchronized (guard){
                        for(int i = 0; i < middlewareSockets.size(); ++i) {
                            if (middlewareSockets.get(i).getSocket().equals(socket)) {
                                middlewareSockets.get(i).setId(clientNum.intValue());
                            }
                        }
                    }

                    cancelTimer.cancel();
                    cancelTimer = new Timer();
                    cancelTimer.schedule(new CancelTimerTask(), 20000);
                }
            });

            clientNumTimer.schedule(clientNumTask, 1000);

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
                synchronized (guard){
                    if (middlewareSockets.contains(socket)) {
                        end = true;
                        return;
                    }
                    middlewareSockets.add(new SocketIdPair(999, socket));
                }


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
                        synchronized (guard){
                            middlewareSockets.remove(this);
                        }
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
                end = true;
                try {
                    heartTimer.cancel();
                    LOGGER.log(Level.INFO, "\nWYWALAM SERWER\n");
                    synchronized (guard){
                        for(int i = 0; i < middlewareSockets.size(); ++i) {
                            if (middlewareSockets.get(i).getSocket().equals(socket)) {
                                middlewareSockets.remove(i);
                            }
                        }
                    }

                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
