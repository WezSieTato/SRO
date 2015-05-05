package rso.core.taskmanager;

import com.google.protobuf.GeneratedMessage;
import rso.core.model.Message;

import java.net.Socket;

/**
 * Created by marcin on 05/05/15.
 */
public class TaskMessage {
    private Message.RSOMessage message;
    private Socket socket;

    public GeneratedMessage getMessage() {
        return message;
    }

    public Socket getSocket() {
        return socket;
    }

    public TaskMessage(Message.RSOMessage message, Socket socket) {
        this.message = message;
        this.socket = socket;
    }
}
