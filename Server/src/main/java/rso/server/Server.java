package rso.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rso.core.abstraction.BaseNode;
import rso.core.events.EventManager;
import rso.core.events.RSOEvent;
import rso.core.taskmanager.TaskManager;
import rso.core.taskmanager.TaskMessage;
import rso.server.server.ServerThread;
import rso.server.task.MiddlewareRequestTask;

/**
 * Created by kometa on 04.05.2015.
 */
@Component("server")
public class Server extends BaseNode {

    private TaskManager taskManager;
    private  ServerThread serverThread;
    private  ServerThread serverMiddlewareThread;

    @Autowired
    private GeneratorTest generatorTest;

    public Server() {
        taskManager = new TaskManager();

        EventManager.addListener(ServerThread.messageReceived, ServerThread.class, new EventManager.EventListener() {
            public void event(RSOEvent event) {
                TaskMessage taskMessage = (TaskMessage)event.getObject();
                taskManager.putTaskMessage(taskMessage);

            }
        });

        taskManager.addTask(new MiddlewareRequestTask());

    }

    public void run() {
        System.out.println("Server. Kopytko");
        serverThread = new ServerThread(6969);
//        serverMiddlewareThread = new ServerThread(6975);

        Thread t1 = new Thread(serverThread);
        t1.start();

//        Thread t2 = new Thread(serverMiddlewareThread);
//        t2.start();

        Thread t3 = new Thread(taskManager);
        t3.start();

        Thread t4 = new Thread(generatorTest);
        t4.start();

    }
}
