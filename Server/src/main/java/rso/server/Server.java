package rso.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import rso.core.abstraction.BaseNode;
import rso.core.events.EventManager;
import rso.core.events.RSOEvent;
import rso.core.taskmanager.TaskManager;
import rso.core.taskmanager.TaskMessage;
import rso.server.server.RingManager;
import rso.server.server.ServerThread;
import rso.server.server.StartingPoint;
import rso.server.task.EntryTask;
import rso.server.task.MiddlewareRequestTask;

import java.net.Socket;

/**
 * Created by kometa on 04.05.2015.
 */
@Component("server")
public class Server extends BaseNode {

    private TaskManager taskManager;
    private  ServerThread serverThread;
    private RingManager ringManager = new RingManager();

    @Value ("${rso.port.internal}")
    private int portInternal;

    @Value ("${rso.port.external}")
    private int portExternal;

    @Value ("${rso.addresses.server}")
    private String[] serverIps;

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

        EventManager.addListener(EntryTask.entryEvent, EntryTask.class, new EventManager.EventListener() {
            public void event(RSOEvent event) {
                System.out.println("Nowy serwer! " + ((Socket) event.getObject()).getInetAddress().getHostAddress());
            }
        });

        taskManager.addTask(new MiddlewareRequestTask());
        taskManager.addTask(new EntryTask());
    }

    public void run() {
        System.out.println("Server. Kopytko");
        serverThread = new ServerThread(portExternal);

        new Thread(new StartingPoint(serverIps, portInternal));

        new Thread(new ServerThread(portInternal));
        new Thread(serverThread).start();
        new Thread(taskManager).start();
        new Thread(generatorTest).start();

    }
}
