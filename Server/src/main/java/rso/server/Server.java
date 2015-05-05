package rso.server;

import org.springframework.stereotype.Component;
import rso.core.abstraction.BaseNode;

/**
 * Created by kometa on 04.05.2015.
 */
@Component("server")
public class Server extends BaseNode {

    public void run() {
        System.out.println("Server");
    }
}
