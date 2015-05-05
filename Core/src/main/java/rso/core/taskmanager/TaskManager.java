package rso.core.taskmanager;

import rso.core.events.RSOEvent;

import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by WezSieTato on 04/05/15.
 */
public class TaskManager implements  Runnable{

    private LinkedBlockingQueue<TaskMessage> messageQueue = new LinkedBlockingQueue<TaskMessage>();
    private LinkedList<Task> taskList = new LinkedList<Task>();

    public void run() {
        while(true){
            TaskMessage taskMessage = messageQueue.poll();
            for (Task task : taskList) {
                if(task.canProcessMessage(taskMessage))
                    if(!task.processMessage(taskMessage))
                        break;
            }
        }
    }

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
