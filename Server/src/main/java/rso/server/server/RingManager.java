package rso.server.server;

import rso.core.model.Message;
import rso.core.net.SocketSender;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by marcin on 09/06/15.
 */
public class RingManager {

    private LinkedList<String> ips = new LinkedList<String>();
    private LinkedList<String> waitingIps = new LinkedList<String>();
    private String myIp;

    public RingManager() {
        try {
            myIp = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }


    public void addNext(String ip){
        ips.addFirst(ip);
    }

    public void addToQueue(String ip){
        waitingIps.addFirst(ip);
    }

    public String getPrev(){

        Iterator<String> itr = ips.descendingIterator();
        while(itr.hasNext()){
            String string = itr.next();
            if(!string.equals(myIp))
                return string;
        }

        return null;
    }

    public String getNext(){
        for(String string : ips){
            if(!string.equals(myIp))
                return string;
        }
        return null;
    }

    public LinkedList<String> getIps() {
        return ips;
    }

    public void setIps(LinkedList<String> ips) {
        this.ips = ips;
    }

    public boolean isRing(){
        return !ips.isEmpty();
    }

    public boolean isWaiting(){
        return !waitingIps.isEmpty();
    }

    public void createRing(){
        ips.add(myIp);
        ips.addAll(waitingIps);
        waitingIps.clear();

    }


    public Message.RSOMessage.Builder tokenBuilder(Message.TokenType type){
        Message.Token.Builder builder = Message.Token.newBuilder();
        builder.setTokenType(type);

        return Message.RSOMessage.newBuilder().setToken(builder);

    }

    public void initWithSocket(SocketSender socketSender){
        Message.RSOMessage.Builder builder =  Message.RSOMessage.newBuilder();
        builder.setToken(Message.Token.newBuilder().setTokenType(Message.TokenType.NONE));
        Message.RSOMessage msg = builder.build();
    }


}
