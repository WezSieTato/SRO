package rso.server;

import org.springframework.stereotype.Component;
import rso.core.abstraction.BaseNode;
import rso.core.events.EventManager;
import rso.core.events.RSOEvent;
import rso.core.taskmanager.TaskManager;
import rso.core.taskmanager.TaskMessage;

/**
 * Created by kometa on 04.05.2015.
 */
@Component("server")
public class Server extends BaseNode {

    private TaskManager taskManager;

    public Server() {
        taskManager = new TaskManager();

        EventManager.addListener(Receiver.messageReceived, Receiver.class, new EventManager.EventListener() {
            public void event(RSOEvent event) {
                TaskMessage taskMessage = (TaskMessage)event.getObject();
                taskManager.putTaskMessage(taskMessage);

            }
        });

    }

    public void run() {
        System.out.println("Server");
    }
}
