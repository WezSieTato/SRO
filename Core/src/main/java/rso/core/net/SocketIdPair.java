package rso.core.net;

import java.net.Socket;

/**
 * Created by modzelej on 2015-05-06.
 */
public class SocketIdPair {

    private int id;
    private Socket socket;

    public SocketIdPair(int id, Socket socket) {
        this.id = id;
        this.socket = socket;
    }

    public int getId() {
        return id;
    }

    public Socket getSocket() {
        return socket;
    }
}
