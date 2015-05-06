package rso.core.taskmanager;

import com.google.protobuf.GeneratedMessage;
import rso.core.events.EventManager;
import rso.core.model.Message;

import java.lang.ref.WeakReference;

/**
 * Created by modz on 2015-04-29.
 */
public abstract class Task {

    private int priority = 4;
    private WeakReference< TaskManager> taskManager = null;

    public static String messageToSend = EventManager.registerEvent(Task.class, "Message to send");


    public enum ConnectionDirection{
        InnerToInner,
        MiddlewareToMiddleware,
        MiddlewareToInner,
        InnerToMiddleware,
        ClientWithMiddleware
    }

    private ConnectionDirection connectionDirection;
    private boolean connectionDirectionFilter = false;

    protected void send(Message.RSOMessage message){
        EventManager.event(Task.class, messageToSend, message);
    }

    /**
     * Przetwarza wiadomosc
     * @param taskMessage
     * @return zwraca true, jeśli wiadomość może
     *  być przetworzona przez inne taski
     */
    abstract public boolean processMessage(TaskMessage taskMessage);

    /**
     * Sprawdza, czy task może przetworzyć wiadomość
     * @param taskMessage
     * @return true jeśli wiadomość odpowiada taskowi
     */
    public boolean canProcessMessage(TaskMessage taskMessage){
        Message.RSOMessage message = taskMessage.getMessage();
        if(connectionDirectionFilter){
            switch (connectionDirection){
                case InnerToInner:
                    if(!message.hasToken())
                        return false;
                break;

                case MiddlewareToMiddleware:
                    if(!message.hasMiddlewareHeartbeat())
                        return false;
                    break;

                case MiddlewareToInner:
                    if(!message.hasMiddlewareRequest())
                        return false;
                    break;

                case InnerToMiddleware:
                    if(!message.hasMiddlewareRequest())
                        return false;
                    break;

                case ClientWithMiddleware:
                    if(!message.hasMiddlewareMessage())
                        return false;
                    break;
            }

        }

        return true;
    }

    public void removeFromManager(){
        getTaskManager().addTaskToDeleteList(this);
    }

    public void addFilterForConnectionDirection(ConnectionDirection connectionDirection){
        connectionDirectionFilter = true;
        this.connectionDirection = connectionDirection;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setTaskManager(TaskManager taskManager){
        this.taskManager = new WeakReference<TaskManager>(taskManager);
    }

    public TaskManager getTaskManager(){
        return taskManager.get();
    }
}
