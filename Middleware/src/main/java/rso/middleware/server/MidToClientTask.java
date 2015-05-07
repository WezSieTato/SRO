package rso.middleware.server;

import rso.core.events.EventManager;
import rso.core.taskmanager.Task;
import rso.core.taskmanager.TaskMessage;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by modzelej on 2015-05-06.
 */
public class MidToClientTask extends Task {

    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    public static String sendToMiddleware = EventManager.registerEvent(MidToClientTask.class, "slanie do klienta");

    public MidToClientTask() {
        setPriority(5);
        addFilterForConnectionDirection(ConnectionDirection.ClientWithMiddleware);
    }
    @Override
    public boolean processMessage(TaskMessage taskMessage) {

        String name = taskMessage.getMessage().getMiddlewareMessage().getSubjectName();
        int num = taskMessage.getMessage().getMiddlewareMessage().getRegisteredStudents();
        LOGGER.log(Level.INFO, "Na przedmiot  " + name + " zapisamnych jest " + num);

        EventManager.event(MidToClientTask.class, sendToMiddleware, "ok");



        return false;
    }
}
