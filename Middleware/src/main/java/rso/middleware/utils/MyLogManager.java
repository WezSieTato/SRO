package rso.middleware.utils;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Created by modz on 2015-04-29.
 */
public class MyLogManager {
    private static ConsoleHandler ch = new ConsoleHandler();

    public static void setLoggingLevel(Level level){
        ch.setLevel(level);
        LogManager.getLogManager().getLogger(Logger.GLOBAL_LOGGER_NAME).setLevel(level);
    }

    public static void setConsoleLog(){
        LogManager.getLogManager().getLogger(Logger.GLOBAL_LOGGER_NAME).addHandler(ch);
    }
}
