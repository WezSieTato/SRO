package rso.middleware.server;

import rso.core.events.EventManager;
import rso.core.events.RSOEvent;
import rso.core.taskmanager.Task;
import rso.core.taskmanager.TaskMessage;

import java.util.logging.Logger;

/**
 * Created by modzelej on 2015-05-05.
 */
public class HeartbeatTaskRecive extends Task {
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    public static String response = EventManager.registerEvent(HeartbeatTaskRecive.class, "response");


    public HeartbeatTaskRecive() {
        setPriority(1);
        addFilterForConnectionDirection(ConnectionDirection.MiddlewareToMiddleware);

    }

    @Override
    public boolean processMessage(TaskMessage taskMessage) {


        EventManager.event(HeartbeatTaskRecive.class, response, new Integer(taskMessage.getMessage().getMiddlewareHeartbeat().getConnectedClients()));

        return true;
    }
}
