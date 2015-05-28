package rso.core.net;

import rso.core.model.Message;
import rso.core.taskmanager.TaskMessage;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by modzelej on 2015-05-05.
 */
public class SocketReciver {

    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private Socket socket;
    private boolean end = false;
    private int num = 0;



    public Socket getSocket() {
        return socket;
    }

    public SocketReciver( Socket socket){
        this.socket = socket;

        init();
    }

    private void init() {


    }

    public TaskMessage read() {
        if (socket != null) {

            try {

                Message.RSOMessage msg = Message.RSOMessage.parseDelimitedFrom(socket.getInputStream());
                num++;
                LOGGER.log(Level.INFO, "MESSAGE: RECEIVED " + num);
                return new TaskMessage(msg, socket);


            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            LOGGER.log(Level.WARNING, "Socket is NULL!!!!");
            return null;
        }
        return null;
    }



}
