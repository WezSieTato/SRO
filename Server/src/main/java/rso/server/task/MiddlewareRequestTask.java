package rso.server.task;

import rso.core.model.Message;
import rso.core.net.SocketSender;
import rso.core.taskmanager.Task;
import rso.core.taskmanager.TaskMessage;
import rso.server.messages.MiddlewareResponseConstructor;

import javax.swing.text.html.parser.Entity;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by marcin on 05/05/15.
 */
public class MiddlewareRequestTask extends Task{

    public MiddlewareRequestTask() {
        addFilterForConnectionDirection(ConnectionDirection.MiddlewareToInner);
    }

    @Override
    public boolean processMessage(TaskMessage taskMessage) {
        System.out.println("Siema dostaje messega!!! "  + taskMessage.getMessage().getMiddlewareRequest().getTimestamp());

        long timestamp = taskMessage.getMessage().getMiddlewareRequest().getTimestamp();
        MiddlewareResponseConstructor constructor = new MiddlewareResponseConstructor();
        Message.RSOMessage message = constructor.construct(timestamp);
        send(message);

        return false;
    }
}
