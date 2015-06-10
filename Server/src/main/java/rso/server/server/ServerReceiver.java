package rso.server.server;

import rso.core.events.EventManager;
import rso.core.net.SocketReciver;
import rso.core.taskmanager.TaskMessage;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by marcin on 11/06/15.
 */
public class ServerReceiver implements Runnable {

    private SocketReciver socketReciver;

    public ServerReceiver(Socket socket) {

        socketReciver = new SocketReciver(socket);

    }

    public void run() {
        while (true) {
            try {
                TaskMessage message = socketReciver.read();
                if (message == null) {
                    break;
                }
                EventManager.event(ServerThread.class, ServerThread.messageReceived, message);
            } catch (Exception e) {
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
