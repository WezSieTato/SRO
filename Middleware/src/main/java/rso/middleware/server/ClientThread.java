package rso.middleware.server;

import rso.core.events.EventManager;
import rso.core.events.RSOEvent;

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
