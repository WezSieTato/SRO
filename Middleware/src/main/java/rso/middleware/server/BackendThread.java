package rso.middleware.server;

import rso.core.abstraction.BaseContext;
import rso.core.events.EventManager;
import rso.core.events.RSOEvent;
import rso.core.model.Message;
import rso.core.net.SocketReciver;
import rso.core.net.SocketSender;
import rso.core.taskmanager.TaskManager;
import rso.core.taskmanager.TaskMessage;
import rso.core.util.Utils;
import rso.middleware.MiddlewareLayer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by modzelej on 2015-05-06.
 */
public class BackendThread implements Runnable {

    private Socket socket;
    private LinkedBlockingQueue<Message.RSOMessage> messages;
    private boolean end = false;
    private Object guard;
    private SocketSender socketSender;
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private BackendReciver backendReciver;
    public static boolean ready = true;

    public Date getLastTimestamp() {
        return lastTimestamp;
    }

    public void setLastTimestamp(Date lastTimestamp) {
        this.lastTimestamp = lastTimestamp;
    }

    private Date lastTimestamp;

    private class BackendReciver implements Runnable{
        private SocketReciver reciver;
        private boolean end = false;

        public BackendReciver(Socket socket) {

        reciver = new SocketReciver(socket);

            MiddlewareLayer.taskManager.addTask(new BackendReciveTask());
            MiddlewareLayer.taskManager.addTask(new BackendRequestTask());

    }

    public void run() {
        while(!end){
            TaskMessage message = reciver.read();

            MiddlewareLayer.taskManager.putTaskMessage(message);

            LOGGER.log(Level.INFO, message.toString());
        }
    }
}


    public BackendThread(){
       guard = new Object();
        messages = new LinkedBlockingQueue<Message.RSOMessage>(100);

        EventManager.addListener(BackendRequestTask.requestData, BackendRequestTask.class, new EventManager.EventListener() {
            public void event(RSOEvent event) {
                if (socketSender != null) {

                    socketSender.send((Message.RSOMessage) event.getObject());
                }
            }
        });

        EventManager.addListener(BackendReciveTask.newData, BackendReciveTask.class, new EventManager.EventListener() {
            public void event(RSOEvent event) {
                setLastTimestamp(new Date());
                ready = true;
            }
        });



    }


    public void run() {
        initConnection();
        while(!end){
            synchronized (guard){
                try {
                    guard.wait(5000);
                } catch (InterruptedException e) {

                }
            }
            while (!messages.isEmpty()){
                socketSender.send(messages.poll());
            }

            if(ready){
                Message.MiddlewareRequest.Builder builderRequest = Message.MiddlewareRequest.newBuilder();
                builderRequest.setNodeId(5).setTimestamp(lastTimestamp.getTime());

                Message.RSOMessage.Builder snd = Message.RSOMessage.newBuilder().setMiddlewareRequest(builderRequest.build());

                socketSender.send(snd.build());
                ready = false;
            }





        }

    }

    private void initConnection() {
        boolean connected = false;
        Object connectionGuard = new Object();
        while (!connected){
            synchronized (connectionGuard){
                try {
                    connectionGuard.wait(100);
                } catch (InterruptedException e) {

                }
            }
            //for po kazdym serwerze wewnetrznym
            try {
                socket = new Socket("192.168.1.110", 6969);
            } catch (IOException e) {

            }
            if(socket != null){
                connected = true;
            }
        }

//        try {
            socketSender = new SocketSender(socket);
//        } catch (IOException e) {
//
//        }
        lastTimestamp = BaseContext.getInstance().getApplicationContext().getBean(Utils.class).getNewestDate();
        ;
        backendReciver = new BackendReciver(socket);
        Thread t = new Thread(backendReciver);
        t.start();
    }
}
