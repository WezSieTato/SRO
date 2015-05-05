package rso.core.net;

import rso.core.model.Message;
import rso.core.taskmanager.TaskMessage;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by modzelej on 2015-05-05.
 */
public class SocketReciver {

    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private DataInputStream reader;
    private Socket socket;
    private boolean end = false;
    private String messageString = "";
    byte[] messageByte = new byte[30000];

    public byte[] getMessageByte() {
        return messageByte;
    }

    public String getMessageString() {
        return messageString;
    }

    public Socket getSocket() {
        return socket;
    }

    public SocketReciver( Socket socket){
        this.socket = socket;

        init();
    }

    private void init() {
        if(socket != null){
            try {
                reader = new DataInputStream(socket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            LOGGER.log(Level.WARNING, "Socket is NULL!!!!");
        }

    }

    public TaskMessage read() {
        if (reader != null) {

            int bytesRead = 0;

            try {

//                messageByte[0] = reader.readByte();
//                messageByte[1] = reader.readByte();
//                ByteBuffer byteBuffer = ByteBuffer.wrap(messageByte, 0, 2);
//
//                int bytesToRead = byteBuffer.getShort();
//                LOGGER.log(Level.INFO, "About to read " + bytesToRead + " bytes");
//
//
//                while (!end) {
//                    bytesRead = reader.read(messageByte);
//                    messageString += new String(messageByte, 0, bytesRead);
//                    if (messageString.length() == bytesToRead) {
//                        end = true;
//                    }
//                }

//                LOGGER.log(Level.INFO, "MESSAGE: " + messageString);

                Message.RSOMessage msg = Message.RSOMessage.parseFrom(socket.getInputStream());
                LOGGER.log(Level.INFO, "MESSAGE: RECIVED");
                return new TaskMessage(msg, socket);


            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            LOGGER.log(Level.WARNING, "Reader is NULL!!!!");
            return null;
        }
        return null;
    }



}
