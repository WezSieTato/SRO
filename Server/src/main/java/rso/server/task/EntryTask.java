package rso.server.task;

import rso.core.events.EventManager;
import rso.core.model.Message;
import rso.core.taskmanager.Task;
import rso.core.taskmanager.TaskMessage;

/**
 * Created by marcin on 05/05/15.
 */
public class EntryTask extends RingTask {

    public static String entryEvent = EventManager.registerEvent(EntryTask.class, "Entry event");

    @Override
    public boolean canProcessMessage(TaskMessage taskMessage) {
        if( super.canProcessMessage(taskMessage)){
            return taskMessage.getMessage().getToken().getTokenType() == Message.TokenType.ENTRY;
        }

        return false;
    }

    @Override
    public boolean processMessage(TaskMessage taskMessage) {
        System.out.println("EntryTask");

        EventManager.event(EntryTask.class, entryEvent, taskMessage.getSocket());

        return false;
    }
}
