package rso.core.taskmanager;

import com.google.protobuf.GeneratedMessage;

import java.lang.ref.WeakReference;

/**
 * Created by modz on 2015-04-29.
 */
public abstract class Task {

    private int priority = 4;
    private WeakReference< TaskManager> taskManager = null;

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
    abstract public boolean canProcessMessage(TaskMessage taskMessage);

    public void removeFromManager(){
        getTaskManager().addTaskToDeleteList(this);
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
