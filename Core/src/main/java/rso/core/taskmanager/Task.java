package rso.core.taskmanager;

import com.google.protobuf.GeneratedMessage;

/**
 * Created by modz on 2015-04-29.
 */
public abstract class Task {

    private int priority = 4;

    abstract public boolean processMessage(TaskMessage taskMessage);

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
