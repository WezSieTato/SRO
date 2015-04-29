package rso.middleware;

import rso.middleware.utils.MyLogManager;

import java.util.logging.Level;

/**
 * Created by modz on 2015-04-29.
 */
public class MiddlewareLayer {

    MiddlewareLayer(){
        MyLogManager.setLoggingLevel(Level.ALL);
        MyLogManager.setConsoleLog();

        init();
    }

    private void init() {
//        Test1 t1 = new Test1();
//        Test3 t3 = new Test3();
//        Test2 t2 = new Test2();

    }
}
