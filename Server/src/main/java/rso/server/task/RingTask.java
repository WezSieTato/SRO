package rso.server.task;

import rso.core.taskmanager.Task;
import rso.server.server.RingManager;

/**
 * Created by marcin on 10/06/15.
 */
public abstract class RingTask extends Task {

    private RingManager ringManager;

    public RingTask() {
        addFilterForConnectionDirection(Task.ConnectionDirection.InnerToInner);
    }

    public RingManager getRingManager() {
        return ringManager;
    }

    public void setRingManager(RingManager ringManager) {
        this.ringManager = ringManager;
    }
}
