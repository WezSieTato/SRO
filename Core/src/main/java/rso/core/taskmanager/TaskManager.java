package rso.core.taskmanager;

import rso.core.events.RSOEvent;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by WezSieTato on 04/05/15.
 */
public class TaskManager implements  Runnable{

    private LinkedBlockingQueue<TaskMessage> messageQueue = new LinkedBlockingQueue<TaskMessage>();
    private LinkedList<Task> taskList = new LinkedList<Task>();
    private LinkedList<Task> taskToDeleteList = new LinkedList<Task>();

    public void run() {
        while(true){
            TaskMessage taskMessage = messageQueue.poll();

            removeTasksToDelete();

            for (Task task : taskList) {
                if(task.canProcessMessage(taskMessage))
                    if(!task.processMessage(taskMessage))
                        break;
            }
        }
    }

    public void addTask(Task task){
        task.setTaskManager(this);
        taskList.add(task);

        Collections.sort(taskList, new Comparator<Task>() {
            public int compare(Task o1, Task o2) {
                return o1.getPriority() - o2.getPriority();
            }
        });
    }

    public void addTaskToDeleteList(Task task){
        taskToDeleteList.add(task);
    }

    private void removeTasksToDelete(){
        for(Task task : taskToDeleteList){
            taskList.remove(task);
        }

        taskToDeleteList.clear();
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
