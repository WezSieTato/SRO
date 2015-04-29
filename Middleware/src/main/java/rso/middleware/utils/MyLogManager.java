package rso.middleware.utils;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Created by modz on 2015-04-29.
 */
public class MyLogManager {

    public static void setLoggingLevel(Level level){
        LogManager.getLogManager().getLogger(Logger.GLOBAL_LOGGER_NAME).setLevel(level);
    }
}
