package rso.server.server;

import rso.core.events.EventManager;
import rso.core.events.RSOEvent;
import rso.core.model.Message;
import rso.core.net.SocketReciver;
import rso.core.net.SocketSender;
import rso.core.taskmanager.Task;
import rso.core.taskmanager.TaskMessage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by marcin on 05/05/15.
 */
public class ServerThread implements Runnable{

    private ServerSocket serverSocket;
    public static String messageReceived = EventManager.registerEvent(ServerThread.class, "message received");

    private SocketSender socketSender;

    public ServerThread(int port) {
        try {
            serverSocket = new ServerSocket(port);


        } catch (IOException e) {
            e.printStackTrace();
        }

        EventManager.addListener(Task.messageToSend, Task.class, new EventManager.EventListener() {
            public void event(RSOEvent event) {
                if(socketSender != null)
                    socketSender.send((Message.RSOMessage)event.getObject());
            }
        });
    }


    public void run() {



        while (true){

            try {
                Socket socket = serverSocket.accept();
                ServerReceiver rec = new ServerReceiver(socket);
                Thread thread = new Thread(rec);
                thread.start();

                System.out.println(socket.getInetAddress().getHostAddress());

                socketSender = new SocketSender(socket);

            } catch (IOException e) {
                e.printStackTrace();
            }



        }
    }

    private  class ServerReceiver implements  Runnable{

        private  SocketReciver socketReciver;
        public ServerReceiver(Socket socket) {

            socketReciver = new SocketReciver(socket);

        }

        public void run() {
            while(true) {
                try {
                    TaskMessage message = socketReciver.read();
                    EventManager.event(ServerThread.class, messageReceived, message);
                } catch (Exception e){
                    try {
                        socketReciver.getSocket().close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } finally {
                        break;
                    }

                } finally {

                }
            }
        }
    }

}
