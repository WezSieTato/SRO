package rso.middleware.server;

import rso.core.events.EventManager;
import rso.core.events.RSOEvent;
import rso.core.model.Message;
import rso.core.net.SocketReciver;
import rso.core.net.SocketSender;
import rso.core.taskmanager.TaskMessage;
import rso.middleware.MiddlewareLayer;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by modz on 2015-04-29.
 */
public class ClientThread implements Runnable {
    private int id;
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private Socket socket;
    private ClientReciver clientReciver;
    private SocketSender sender;


    private class ClientReciver implements Runnable{
        private SocketReciver reciver;
        private boolean end = false;


        public ClientReciver(Socket socket) {

            reciver = new SocketReciver(socket);

            MiddlewareLayer.taskManager.addTask(new ClientRequestTask());


        }

        public void run() {
            while(!end){
                TaskMessage message = reciver.read();

                MiddlewareLayer.taskManager.putTaskMessage(message);

                LOGGER.log(Level.INFO, message.toString());
            }
        }
    }


    ClientThread(int clientId, Socket socket){
        this.id = clientId;
        this.socket = socket;

        init();
        LOGGER.log(Level.INFO, "Client thread created. ID = " + id);
    }

    private void init() {
        EventManager.addListener(Server.disconnectWithoutRedirectEvent, Server.class, new EventManager.EventListener() {
            public void event(RSOEvent event) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        EventManager.addListener(ClientRequestTask.sendToClient, ClientRequestTask.class, new EventManager.EventListener() {
            public void event(RSOEvent event) {
                Message.MiddlewareMessage.Builder builder1 = Message.MiddlewareMessage.newBuilder();
                builder1.setSubjectId(1337).setNodeId(2);
                Message.RSOMessage.Builder builder2 = Message.RSOMessage.newBuilder();
                builder2.setMiddlewareMessage(builder1.build());

                sender.send(builder2.build());
            }
        });

        if(socket != null){
            try {
                sender = new SocketSender(socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
            clientReciver = new ClientReciver(socket);
            Thread t = new Thread(clientReciver);
            t.start();
        }
    }


    public void run() {
        cleanup();
        LOGGER.log(Level.INFO, "Client thread ended. ID = " + id);
    }

    private void cleanup(){
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
