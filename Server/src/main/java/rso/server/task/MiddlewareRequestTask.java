package rso.server.task;

import rso.core.taskmanager.Task;
import rso.core.taskmanager.TaskMessage;

/**
 * Created by marcin on 05/05/15.
 */
public class MiddlewareRequestTask extends Task{

    public MiddlewareRequestTask() {
        addFilterForConnectionDirection(ConnectionDirection.MiddlewareToInner);
    }

    @Override
    public boolean processMessage(TaskMessage taskMessage) {
        return false;
    }
}
