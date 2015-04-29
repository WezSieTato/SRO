package rso.middleware.core.events;

import javax.swing.event.EventListenerList;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by modz on 2015-04-29.
 */
public class EventManager {

    private static class ListenerDescriptor {

        private final String event;

        private final String sender;

        private final EventListener listener;

        public ListenerDescriptor(String evnt, String send, EventListener lis) {
            event = evnt;
            sender = send;
            listener = lis;
        }

        public String getEvent() {
            return event;
        }

        public String getSender() {
            return sender;
        }

        public EventListener getListener() {
            return listener;
        }

    }

    private static final String core_event_name = "event";

    private static Thread dispatcherThread;

    private static Map<String, Class<?>> registeringClasses;

    private static Long listenerNumber;

    private static Map<Long, ListenerDescriptor> registeredListeners;

    private static int eventNumber;

    EventListenerList eventList = new EventListenerList();

    private static LinkedBlockingQueue<RSOEvent> eventQueue;

    private static Map<String, Map<String, List<EventListener>>> listeners;

    private static Object guard;

    private static Object terminator;

    private static boolean endThis;

    static {
        eventNumber = 0;
        listenerNumber = 1L;
        registeringClasses = new HashMap<String, Class<?>>();
        registeredListeners = new HashMap<Long, ListenerDescriptor>();
        listeners = new HashMap<String, Map<String, List<EventListener>>>();
        dispatcherThread = null;

        initial();
    }

    public static String registerEvent(Class<?> registrar, String coreName) {
        String name = registrar.getSimpleName() + ":" + coreName + "_" + eventNumber++;
        synchronized (registeringClasses) {
            registeringClasses.put(name, registrar);
        }
        return name;
    }

    public interface EventListener {

        public void event(RSOEvent event);

    };

    public static Long addListener(String event, EventListener listener) {
        return addListener(event, (String) null, listener);
    }

    public static Long addListener(String event, Class<?> clazz, EventListener listener) {
        return addListener(event, clazz == null ? (String) null : clazz.getCanonicalName(), listener);
    }

    public static Long addListener(String event, String sender, EventListener listener) {
        if (listener == null) {
            return 0L;
        }
        synchronized (guard) {
            Map<String, List<EventListener>> sndrs = listeners.get(event);
            if (sndrs == null) {
                sndrs = new HashMap<String, List<EventListener>>();
                listeners.put(event, sndrs);
            }
            List<EventListener> list = sndrs.get(sender);
            if (list == null) {
                list = new ArrayList<EventListener>();
                sndrs.put(sender, list);
            }
            list.add(listener);
            Long thisNumber = listenerNumber++;
            registeredListeners.put(thisNumber, new ListenerDescriptor(event, sender, listener));
            return thisNumber;
        }
    }

    public static List<Long> removeListeners(List<Long> listeners) {
        List<Long> unremoved = new ArrayList<Long>();
        for (Long listener : listeners) {
            if (!removeListener(listener)) {
                unremoved.add(listener);
            }
        }
        return unremoved;
    }

    public static Long[] removeListeners(Long[] listeners) {
        List<Long> unremoved = new ArrayList<Long>();
        for (Long listener : listeners) {
            if (!removeListener(listener)) {
                unremoved.add(listener);
            }
        }
        return unremoved.toArray(new Long[0]);
    }

    private static boolean rawRemoveListener(String event, String sender, EventListener listener) {
        Map<String, List<EventListener>> sndrs = listeners.get(event);
        boolean ret = false;
        if (sndrs != null) {
            List<EventListener> list = sndrs.get(sender);
            if (list != null) {
                if (list.contains(listener)) {
                    list.remove(listener);
                    ret = true;
                }
            }
        }
        return ret;
    }

    public static boolean removeListener(Long listener) {
        synchronized (guard) {
            ListenerDescriptor lst = registeredListeners.get(listener);
            if (lst == null) {
                return false;
            }
            rawRemoveListener(lst.getEvent(), lst.getSender(), lst.getListener());
            registeredListeners.remove(listener);
            return true;
        }
    }

    public static boolean removeListener(String event, EventListener listener) {
        return removeListener(event, (String) null, listener);
    }

    public static boolean removeListener(String event, Class<?> clazz, EventListener listener) {
        return removeListener(event, clazz == null ? (String) null : clazz.getCanonicalName(), listener);
    }

    public static boolean removeListener(String event, String sender, EventListener listener) {
        synchronized (guard) {
            return rawRemoveListener(event, sender, listener);
        }
    }


    public static void event(Class<?> clazz, String event, Object object) {
        eventCreate(clazz.getCanonicalName(), event, object);
    }

    public static void event(String sender, String event, Object object) {
        eventCreate(sender, event, object);
    }

    private static void eventCreate(String sender, String event, Object object) {
        RSOEvent evnt = new RSOEvent(sender, event, object);
        event(evnt);
    }

    private static Object event(RSOEvent evnt) {
        try {
            eventQueue.put(evnt);
        } catch (InterruptedException e) {
            // we want to silence this exception
        }
        synchronized (guard) {
            guard.notifyAll();
        }
        return null;
    }

    protected static Object dispatch(final RSOEvent evnt) {
        final RSOEvent evntR = evnt;
        final List<EventListener> targetReceivers = new ArrayList<EventListener>();
        synchronized (guard) {
            Map<String, List<EventListener>> allListenersOfEvent = listeners.get(evnt.getEvent());
            if (allListenersOfEvent != null) {
                List<EventListener> allListenersWithMatchingSender = allListenersOfEvent.get(evnt.getSender());
                if (allListenersWithMatchingSender != null) {
                    targetReceivers.addAll(allListenersWithMatchingSender);
                }
                List<EventListener> allListenersWithDeclaredAnySender = allListenersOfEvent.get(null);
                if (allListenersWithDeclaredAnySender != null) {
                    targetReceivers.addAll(allListenersWithDeclaredAnySender);
                }
            }
        }

        return null;


    }

    protected static void dispatchQueue() {
        for (; !eventQueue.isEmpty();) {
            final RSOEvent evnt = eventQueue.poll();
            if (evnt == null) {
                break;
            }
            dispatch(evnt);
        }
    }


    private static void initial() {
        endThis = false;

        Runnable dispatcher = new Runnable() {

            public void run() {
                for (; !endThis;) {
                    try {
                        if (eventQueue.isEmpty()) {
                            synchronized (guard) {
                                guard.wait();
                            }
                        }
                        dispatchQueue();
                    } catch (InterruptedException e) {
                    }
                }
                synchronized (terminator) {
                    terminator.notifyAll();
                }
            }
        };
        dispatcherThread = new Thread(dispatcher, "StoreIntel Dispatcher");
        dispatcherThread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

            public void uncaughtException(Thread thread, Throwable ex) {
                ex.printStackTrace();
            }
        });
        dispatcherThread.start();

    }

}
