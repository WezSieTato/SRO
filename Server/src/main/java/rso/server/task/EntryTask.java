package rso.server.task;

import rso.core.taskmanager.Task;
import rso.core.taskmanager.TaskMessage;

/**
 * Created by marcin on 05/05/15.
 */
public class EntryTask extends Task {

    public EntryTask() {
        addFilterForConnectionDirection(ConnectionDirection.InnerToInner);
    }

    @Override
    public boolean processMessage(TaskMessage taskMessage) {
        return false;
    }
}
