package rso.middleware.server;

import rso.core.abstraction.BaseContext;
import rso.core.events.EventManager;
import rso.core.model.Message;
import rso.core.taskmanager.Task;
import rso.core.taskmanager.TaskMessage;
import rso.core.util.Utils;

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

            Utils util = BaseContext.getInstance().getApplicationContext().getBean(Utils.class);

            Message.MiddlewareRequest.Builder builderRequest = Message.MiddlewareRequest.newBuilder();
            builderRequest.setNodeId(5).setTimestamp(util.getNewestDate().getTime());

            Message.RSOMessage.Builder snd = Message.RSOMessage.newBuilder().setMiddlewareRequest(builderRequest.build());
            EventManager.event(BackendRequestTask.class, requestData, snd.build());

        return true;
    }
}
