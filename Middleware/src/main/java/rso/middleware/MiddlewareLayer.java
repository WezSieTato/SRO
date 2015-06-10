package rso.middleware;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import rso.core.abstraction.BaseNode;
import rso.core.taskmanager.TaskManager;
import rso.middleware.server.BackendThread;
import rso.middleware.server.MiddlewareConnectionsManager;
import rso.middleware.server.MiddlewareThread;
import rso.middleware.server.Server;
import rso.middleware.utils.MyLogManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;

/**
 * Created by modz on 2015-04-29.
 */
@Component("middleware")
public class MiddlewareLayer extends BaseNode{


    public static final TaskManager taskManager = new TaskManager();
    @Value("${rso.addresses.middleware}")
    public static String[] middlwareIPs;

    static {
        Thread t = new Thread(taskManager);
        t.start();

    }

    private MiddlewareThread middlewareThread;
    private BackendThread backendThread;
    private Server clientServer;
    private MiddlewareConnectionsManager middlewareConnectionsManager;

    private void init() {
//        Test1 t1 = new Test1();
//        Test3 t3 = new Test3();
//
//        Thread tt1 = new Thread(t1);
//        Thread tt3 = new Thread(t3);
//
//        tt3.start();
//        tt1.start();
//        Test2 t2 = new Test2();

        try {
            middlewareThread = new MiddlewareThread(new ServerSocket(6971));
        } catch (IOException e) {
            e.printStackTrace();
        }
        backendThread = new BackendThread();
        clientServer = new Server(1);
        middlewareConnectionsManager = new MiddlewareConnectionsManager();


//     //        t2.start();   t1.start();

//        t3.start();

//        ClientProgram cp = new ClientProgram();
//
//        Thread gwn = new Thread(cp);
//        gwn.start();


        Thread t1 = new Thread(middlewareThread);
//        Thread t2 = new Thread(backendThread);
//        Thread t3 = new Thread(clientServer);
        Thread t4 = new Thread(middlewareConnectionsManager);

        t1.start();
//        t2.start();
        t4.start();
//        t3.start();




    }

    public void run() {
        MyLogManager.setLoggingLevel(Level.ALL);
//        MyLogManager.setConsoleLog();

        init();
    }
}
