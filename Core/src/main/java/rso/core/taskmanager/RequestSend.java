package rso.core.taskmanager;

import rso.core.model.Message;

/**
 * Created by marcin on 10/06/15.
 */
public class RequestSend {

    private String ip;
    private Message.RSOMessage message;

    public RequestSend(String ip, Message.RSOMessage message) {
        this.ip = ip;
        this.message = message;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Message.RSOMessage getMessage() {
        return message;
    }

    public void setMessage(Message.RSOMessage message) {
        this.message = message;
    }
}
