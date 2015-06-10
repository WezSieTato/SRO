package rso.middleware.server;

import rso.core.events.EventManager;
import rso.core.taskmanager.Task;
import rso.core.taskmanager.TaskMessage;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by modzelej on 2015-05-06.
 */
public class MidToClientRedirectTask extends Task {

    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    public static String redirectEvent = EventManager.registerEvent(MidToClientRedirectTask.class, "redirectEvent");

    public MidToClientRedirectTask() {
        setPriority(5);
        addFilterForConnectionDirection(ConnectionDirection.ClientWithMiddleware);
    }
    @Override
    public boolean processMessage(TaskMessage taskMessage) {

        int id = taskMessage.getMessage().getMiddlewareHeartbeat().getServerId();
        LOGGER.log(Level.INFO, "PRzekierowanie na  " + id + " - - - - UWAGA ZMIANA SOCKETA ");

        EventManager.event(MidToClientRedirectTask.class, redirectEvent, new Integer(id));



        return false;
    }
}
