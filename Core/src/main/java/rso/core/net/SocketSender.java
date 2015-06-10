package rso.core.net;

import com.google.protobuf.CodedOutputStream;
import rso.core.model.Message;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by modzelej on 2015-05-05.
 */
public class SocketSender{

    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private Socket socket;

    public SocketSender(Socket socket) {
        this.socket = socket;

        init();
    }

    private void init() {

    }

    public void send(Message.RSOMessage message){
        if(socket != null){

            try {
                message.writeDelimitedTo(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else{
            LOGGER.log(Level.WARNING, "Socket is NULL!!!!");
        }
    }


}
