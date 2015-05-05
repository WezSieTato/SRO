package rso.core.net;

import rso.core.model.Message;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by modzelej on 2015-05-05.
 */
public class SocketSender{

    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private PrintWriter output;
    private Socket socket;

    public SocketSender(Socket socket) throws IOException {
        this.socket = socket;

        init();
    }

    private void init() {
        if(socket != null){
            try {
                output = new PrintWriter(socket.getOutputStream(), false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        else{
            LOGGER.log(Level.WARNING, "OutputStream is NULL!!!!");
        }
    }

    public void send(Message.RSOMessage message){
        if(output != null){

            try {
                message.writeTo(socket.getOutputStream());
                socket.getOutputStream().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else{
            LOGGER.log(Level.WARNING, "PrintWriter is NULL!!!!");
        }
    }


}
