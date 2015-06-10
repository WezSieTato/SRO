package rso.server.server;

import rso.core.events.EventManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by marcin on 05/05/15.
 */
public class ServerThread implements Runnable{

    private ServerSocket serverSocket;
    public static String messageReceived = EventManager.registerEvent(ServerThread.class, "message received");

//    private SocketSender socketSender;

    public ServerThread(int port) {
        try {
            serverSocket = new ServerSocket(port);


        } catch (IOException e) {
            e.printStackTrace();
        }

//        EventManager.addListener(Task.messageToSend, Task.class, new EventManager.EventListener() {
//            public void event(RSOEvent event) {
//                if(socketSender != null)
//                    socketSender.send((Message.RSOMessage)event.getObject());
//            }
//        });
    }


    public void run() {



        while (true){

            try {
                Socket socket = serverSocket.accept();
                ServerReceiver rec = new ServerReceiver(socket);
                Thread thread = new Thread(rec);
                thread.start();

                System.out.println(socket.getInetAddress().getHostAddress());

//                socketSender = new SocketSender(socket);

            } catch (IOException e) {
                e.printStackTrace();
            }



        }
    }

}
