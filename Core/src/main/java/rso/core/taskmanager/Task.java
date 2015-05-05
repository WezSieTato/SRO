package rso.core.taskmanager;

import com.google.protobuf.GeneratedMessage;

/**
 * Created by modz on 2015-04-29.
 */
public abstract class Task {

    private int priority = 4;

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

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
