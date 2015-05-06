package rso.middleware.server;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import rso.core.events.EventManager;
import rso.middleware.utils.Config;

/**
 * Created by modz on 2015-04-29.
 */
public class Server implements Runnable{

    private int id;
    private int clientId = 1;
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private ServerSocket welcomeSocket;
    private ArrayList<ClientThread> clients;


    public static String disconnectWithoutRedirectEvent = EventManager.registerEvent(Server.class, "disconnecting clients");

    public Server(int id){
        this.id = id;


        init();
        LOGGER.log(Level.INFO, "Server created. ID = " + this.id);
    }

    private void init() {
        clients = new ArrayList<ClientThread>(Config.MAX_CLIENTS);

        try {
            welcomeSocket = new ServerSocket(6972);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    private void cleanup(){
        EventManager.event(Server.class, disconnectWithoutRedirectEvent, "server shutdown");

        try {
            welcomeSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ServerSocket getWelcomeSocket() {
        return welcomeSocket;
    }

    public void setWelcomeSocket(ServerSocket welcomeSocket) {
        this.welcomeSocket = welcomeSocket;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void run() {
        LOGGER.log(Level.INFO, "Server started. ID = " + id);
        while(true){
            try {
                Socket connectionSocket = welcomeSocket.accept();

                ClientThread ct = new ClientThread(clientId, connectionSocket);
                Thread t = new Thread(ct);
                t.start();
                clients.add(ct);
                clientId++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
