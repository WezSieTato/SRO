package rso.middleware.elements;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.Socket;

/**
 * Created by modz on 2015-05-28.
 */
public class MiddlwareServer {

    private String address;
    private boolean online;
    private Socket  socket;

    public MiddlwareServer(String address, int port) {
        this.address = address;
        this.port = port;

        //INIT SOCKET in getter
        getSocket();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    private int port;


    public Socket getSocket(){
        if(socket == null){
            try {
                socket = new Socket(address, port);
                setOnline(true);
                return socket;
            } catch (IOException e) {
                 
            }
            finally {
                return null;
            }
        }
        else{
            return socket;
        }


    }


}
