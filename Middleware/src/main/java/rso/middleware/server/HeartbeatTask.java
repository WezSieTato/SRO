package rso.middleware.server;

import rso.core.events.EventManager;
import rso.core.model.Message;
import rso.core.net.SocketSender;
import rso.core.taskmanager.Task;
import rso.core.taskmanager.TaskMessage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by modzelej on 2015-05-05.
 */
public class HeartbeatTask extends Task {
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    public static String newMiddleServer = EventManager.registerEvent(HeartbeatTask.class, "new middle layer server");

    public HeartbeatTask() {
        setPriority(1);
        addFilterForConnectionDirection(ConnectionDirection.MiddlewareToMiddleware);
    }

    @Override
    public boolean processMessage(TaskMessage taskMessage) {

        try {
            if(taskMessage.getMessage().hasMiddlewareHeartbeat()){

                LOGGER.log(Level.INFO, "Otrzyma?em takiego heartbeata: " + taskMessage.getMessage().toString());

                EventManager.event(HeartbeatTask.class, newMiddleServer, taskMessage.getMessage().getMiddlewareHeartbeat());

                SocketSender sender = new SocketSender(taskMessage.getSocket());

                Message.MiddlewareHeartbeat.Builder hrt = Message.RSOMessage.newBuilder().getMiddlewareHeartbeatBuilder();
                hrt.setConnectedClients(69).setServerId(32131).setMessageType(Message.MiddlewareMessageType.Heartbeat);

                Message.RSOMessage.Builder snd = Message.RSOMessage.newBuilder().setMiddlewareHeartbeat(hrt.build());
                sender.send(snd.build());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
