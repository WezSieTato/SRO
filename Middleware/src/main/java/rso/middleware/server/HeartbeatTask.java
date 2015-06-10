package rso.middleware.server;

import rso.core.events.EventManager;
import rso.core.events.RSOEvent;
import rso.core.model.Message;

import java.util.logging.Logger;

/**
 * Created by modzelej on 2015-05-05.
 */
public class HeartbeatTask implements Runnable{
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    public static String newMiddleServer = EventManager.registerEvent(HeartbeatTask.class, "new middle layer server");
    public static String sendMidHeartbeat = EventManager.registerEvent(HeartbeatTask.class, "heartbeat");
    private boolean send = false;
    private Object guard = new Object();
    private boolean finish = false;
    private int connectionNum = 0;

    public HeartbeatTask() {

        EventManager.addListener(MiddlewareConnectionsManager.sendHeartbeat, MiddlewareConnectionsManager.class, new EventManager.EventListener() {
            public void event(RSOEvent event) {
                synchronized (guard){
                    guard.notify();
                }
            }
        });

        EventManager.addListener(MiddlewareThread.userConnectionsNum, MiddlewareThread.class, new EventManager.EventListener() {
            public void event(RSOEvent event) {
                connectionNum = (Integer) event.getObject();

            }
        });

    }

    public void run() {
        while(!finish){

            try {
                synchronized (guard){
                    guard.wait();
                }
                Message.MiddlewareHeartbeat.Builder builderRequest = Message.MiddlewareHeartbeat.newBuilder();
                builderRequest.setServerId(1).setConnectedClients(connectionNum).setMessageType(Message.MiddlewareMessageType.Heartbeat);

                Message.RSOMessage.Builder snd = Message.RSOMessage.newBuilder().setMiddlewareHeartbeat(builderRequest.build());
                EventManager.event(HeartbeatTask.class, sendMidHeartbeat, snd.build());


            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
