package rso.server.task;

import rso.core.model.Message;
import rso.core.taskmanager.RequestSend;
import rso.core.taskmanager.Task;
import rso.core.taskmanager.TaskMessage;
import rso.server.server.RingManager;

/**
 * Created by marcin on 10/06/15.
 */
public class EmptyTokenTask extends RingTask {

    public EmptyTokenTask() {
        setPriority(5);
    }

    @Override
    public boolean processMessage(TaskMessage taskMessage) {

        RingManager ringManager = getRingManager();
        ringManager.rearrage(taskMessage.getMessage().getToken(), true);
        RequestSend req = new RequestSend(ringManager.getNext(), ringManager.tokenBuilder(Message.TokenType.NONE).build());
        send(req);

        return false;
    }
}
