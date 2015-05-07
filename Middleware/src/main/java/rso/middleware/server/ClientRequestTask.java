package rso.middleware.server;

import rso.core.abstraction.BaseContext;
import rso.core.events.EventManager;
import rso.core.model.Message;
import rso.core.service.GroupService;
import rso.core.service.PersonGroupService;
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

        String name = taskMessage.getMessage().getMiddlewareMessage().getSubjectName();
        LOGGER.log(Level.INFO, "Dosta?em takie cus: " + name);
        GroupService groupService = BaseContext.getInstance().getApplicationContext().getBean(GroupService.class);
        int count = groupService.getUsersCountRegisteredForGroup(name);

        Message.MiddlewareMessage.Builder builder = Message.MiddlewareMessage.newBuilder();
        builder.setRegisteredStudents(count).setSubjectName(name);
        Message.RSOMessage message = Message.RSOMessage.newBuilder().setMiddlewareMessage(builder).build();
        
        EventManager.event(ClientRequestTask.class, sendToClient, message);



        return false;
    }
}
