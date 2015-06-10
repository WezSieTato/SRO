package rso.server.server;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by marcin on 09/06/15.
 */
public class RingManager {

    private LinkedList<String> ips = new LinkedList<String>();

    public void addNext(String ip){
        ips.addFirst(ip);
    }

    public String getPrev(){
        return ips.getLast();
    }

    public String getNext(){
        return ips.getFirst();
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
}
