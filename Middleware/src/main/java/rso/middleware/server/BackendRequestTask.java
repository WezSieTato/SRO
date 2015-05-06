package rso.middleware.server;

import rso.core.events.EventManager;
import rso.core.model.Message;
import rso.core.taskmanager.Task;
import rso.core.taskmanager.TaskMessage;
import rso.middleware.MiddlewareLayer;

import java.util.Date;

/**
 * Created by modzelej on 2015-05-06.
 */
public class BackendRequestTask extends Task {
    public static String requestData = EventManager.registerEvent(BackendRequestTask.class, "requesting new data");

    public BackendRequestTask() {
        setPriority(2);
        addFilterForConnectionDirection(ConnectionDirection.MiddlewareToInner);
    }

    @Override
    public boolean processMessage(TaskMessage taskMessage) {


            Message.MiddlewareRequest.Builder builderRequest = Message.MiddlewareRequest.newBuilder();
            builderRequest.setNodeId(5).setTimestamp(new Date().getTime());

            Message.RSOMessage.Builder snd = Message.RSOMessage.newBuilder().setMiddlewareRequest(builderRequest.build());
            EventManager.event(BackendRequestTask.class, requestData, snd.build());

        return true;
    }
}
