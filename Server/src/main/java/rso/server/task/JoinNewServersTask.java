package rso.server.task;

import rso.core.model.Message;
import rso.core.taskmanager.RequestSend;
import rso.core.taskmanager.Task;
import rso.core.taskmanager.TaskMessage;
import rso.server.server.RingManager;

/**
 * Created by marcin on 10/06/15.
 */
public class JoinNewServersTask extends Task {

    private RingManager ringManager;

    public JoinNewServersTask() {
        setPriority(3);
        addFilterForConnectionDirection(ConnectionDirection.InnerToInner);

    }

    @Override
    public boolean canProcessMessage(TaskMessage taskMessage) {
        if( super.canProcessMessage(taskMessage)){
            return ringManager.isWaiting();
        }

        return false;
    }

    @Override
    public boolean processMessage(TaskMessage taskMessage) {

        if(ringManager.isRing()){
            ringManager.createRing();
        } else {

        }

        RequestSend req = new RequestSend(ringManager.getNext(), ringManager.tokenBuilder(Message.TokenType.NONE).build());

        return false;
    }

    public RingManager getRingManager() {
        return ringManager;
    }

    public void setRingManager(RingManager ringManager) {
        this.ringManager = ringManager;
    }
}
