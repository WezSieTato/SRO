package rso.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import rso.core.abstraction.BaseNode;
import rso.core.events.EventManager;
import rso.core.events.RSOEvent;
import rso.core.model.Message;
import rso.core.taskmanager.RequestSend;
import rso.core.taskmanager.Task;
import rso.server.server.*;
import rso.core.taskmanager.TaskManager;
import rso.core.taskmanager.TaskMessage;
import rso.server.task.*;

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
                RequestSend req = (RequestSend) event.getObject();
                serverPool.send(req.getIp(), req.getMessage());

            }
        });

        EventManager.addListener(EntryTask.entryEvent, EntryTask.class, new EventManager.EventListener() {
            public void event(RSOEvent event) {
                Socket socket = ((Socket) event.getObject());
                String ip = socket.getInetAddress().getHostAddress();
                System.out.println("Nowy serwer! " + ip);

                new Thread(new ServerReceiver(socket)).start();

                serverPool.addSender(ip, socket);

                if (!ringManager.isRing()) {
                    ringManager.addToQueue(ip);
                    ringManager.createRing();

                    serverPool.send(ringManager.getNext(), ringManager.tokenBuilder(Message.TokenType.NONE).build());
                } else {
                    ringManager.addToQueue(ip);
                }
            }
        });

        addRingTask(new JoinNewServersTask());
        addRingTask(new EmptyTokenTask());
        addRingTask(new EntryTask());

        taskManager.addTask(new MiddlewareRequestTask());
    }

    private void addRingTask(RingTask ringTask){
        ringTask.setRingManager(ringManager);
        taskManager.addTask(ringTask);
    }

    public void run() {
        System.out.println("Server. Kopytko");
        serverThread = new ServerThread(portExternal);

        StartingPoint startingPoint = new StartingPoint(serverIps, portInternal);
        startingPoint.setServerPool(serverPool);
        new Thread(startingPoint).start();

        new Thread(new ServerThread(portInternal)).start();
        new Thread(serverThread).start();
        new Thread(taskManager).start();
        new Thread(generatorTest).start();

    }
}
