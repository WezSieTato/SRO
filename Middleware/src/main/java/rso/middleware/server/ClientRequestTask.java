package rso.middleware.server;

import rso.core.events.EventManager;
import rso.core.taskmanager.Task;
import rso.core.taskmanager.TaskMessage;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by modzelej on 2015-05-06.
 */
public class ClientRequestTask extends Task {

    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    public static String sendToClient = EventManager.registerEvent(ClientRequestTask.class, "slanie do klienta");

    public ClientRequestTask() {
        setPriority(5);
        addFilterForConnectionDirection(ConnectionDirection.ClientWithMiddleware);
    }
    @Override
    public boolean processMessage(TaskMessage taskMessage) {

        LOGGER.log(Level.INFO, "Dosta?em takie cus: " + taskMessage.getMessage().getMiddlewareMessage().getSubjectId());
        EventManager.event(ClientRequestTask.class, sendToClient, "takie tam");

        return false;
    }
}
