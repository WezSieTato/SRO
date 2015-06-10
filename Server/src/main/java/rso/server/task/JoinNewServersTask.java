package rso.server.task;

import rso.core.model.Message;
import rso.core.taskmanager.RequestSend;
import rso.core.taskmanager.Task;
import rso.core.taskmanager.TaskMessage;
import rso.server.server.RingManager;

/**
 * Created by marcin on 10/06/15.
 */
public class JoinNewServersTask extends RingTask {

    public JoinNewServersTask() {
        setPriority(3);
    }

    @Override
    public boolean canProcessMessage(TaskMessage taskMessage) {
        if( super.canProcessMessage(taskMessage)){
            return getRingManager().isWaiting();
        }

        return false;
    }

    @Override
    public boolean processMessage(TaskMessage taskMessage) {

        RingManager ringManager = getRingManager();

//        if(ringManager.isRing()){
//            ringManager.createRing();
//        } else {
            ringManager.rearrage(taskMessage.getMessage().getToken(), true);
//        }

        RequestSend req = new RequestSend(ringManager.getNext(), ringManager.tokenBuilder(Message.TokenType.NONE).build());
        send(req);

        return false;
    }

}
