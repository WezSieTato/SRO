package rso.middleware.server;

import rso.core.events.EventManager;
import rso.core.model.Message;
import rso.core.taskmanager.Task;
import rso.core.taskmanager.TaskMessage;
import rso.middleware.MiddlewareLayer;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by modzelej on 2015-05-06.
 */
public class BackendReciveTask extends Task {
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public static String newData = EventManager.registerEvent(BackendReciveTask.class, "new data");

    public BackendReciveTask() {
        setPriority(2);
        addFilterForConnectionDirection(ConnectionDirection.InnerToMiddleware);
    }

    @Override
    public boolean processMessage(TaskMessage taskMessage) {

            EventManager.event(BackendReciveTask.class, newData, taskMessage.getMessage().getMiddlewareResponse());
        for(Message.Subject s : taskMessage.getMessage().getMiddlewareResponse().getChanges().getSubjectsList()){

                LOGGER.log(Level.INFO, "Odebrana wiadomosc " + s.getName());

            }




            return true;

    }
}
