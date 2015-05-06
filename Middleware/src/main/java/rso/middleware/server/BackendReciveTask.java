package rso.middleware.server;

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

    public BackendReciveTask() {
        setPriority(2);
        addFilterForConnectionDirection(ConnectionDirection.InnerToMiddleware);
    }

    @Override
    public boolean processMessage(TaskMessage taskMessage) {


            LOGGER.log(Level.INFO, "Dostalem message RECIVE o taki: " + taskMessage.getMessage().getMiddlewareResponse().getChanges().getStudents(0).getSurname());

            return true;

    }
}
