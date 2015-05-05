package rso.core.taskmanager;

import rso.core.events.RSOEvent;

import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by WezSieTato on 04/05/15.
 */
public class TaskManager {

    private LinkedBlockingQueue<TaskMessage> messageQueue = new LinkedBlockingQueue<TaskMessage>();
    private LinkedList<Task> taskList = new LinkedList<Task>();

    public void addTask(Task task){
        taskList.add(task);
    }

    public void removeTask(Task task){
        taskList.remove(task);
    }

    public void putTaskMessage(TaskMessage taskMessage){
        try {
            messageQueue.put(taskMessage);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
