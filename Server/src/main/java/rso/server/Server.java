package rso.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import rso.core.abstraction.BaseNode;
import rso.core.events.EventManager;
import rso.core.events.RSOEvent;
import rso.core.taskmanager.RequestSend;
import rso.core.taskmanager.Task;
import rso.server.server.ServerPool;
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
    private ServerPool serverPool = new ServerPool();

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

        EventManager.addListener(Task.messageToSend, Task.class, new EventManager.EventListener() {
            public void event(RSOEvent event) {
                RequestSend req = (RequestSend)event.getObject();
                serverPool.send(req.getIp(), req.getMessage());

            }
        });

        EventManager.addListener(EntryTask.entryEvent, EntryTask.class, new EventManager.EventListener() {
            public void event(RSOEvent event) {
                Socket socket = ((Socket) event.getObject());
                String ip = socket.getInetAddress().getHostAddress();
                System.out.println("Nowy serwer! " + ip);

                serverPool.addSender(ip, socket);

                if (!ringManager.isRing()) {

                } else {
                    ringManager.addToQueue(ip);
                }
            }
        });

        taskManager.addTask(new MiddlewareRequestTask());
        taskManager.addTask(new EntryTask());
    }

    public void run() {
        System.out.println("Server. Kopytko");
        serverThread = new ServerThread(portExternal);

        new Thread(new StartingPoint(serverIps, portInternal)).start();

        new Thread(new ServerThread(portInternal)).start();
        new Thread(serverThread).start();
        new Thread(taskManager).start();
        new Thread(generatorTest).start();

    }
}
