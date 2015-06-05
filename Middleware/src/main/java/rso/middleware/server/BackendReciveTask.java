package rso.middleware.server;

import rso.core.abstraction.BaseContext;
import rso.core.events.EventManager;
import rso.core.model.*;
import rso.core.service.GroupService;
import rso.core.service.PersonGroupService;
import rso.core.service.PersonService;
import rso.core.taskmanager.Task;
import rso.core.taskmanager.TaskMessage;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by modzelej on 2015-05-06.
 */
public class BackendReciveTask extends Task {
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public static String newData = EventManager.registerEvent(BackendReciveTask.class, "new data");

    public BackendReciveTask() {
        setPriority(2);
        addFilterForConnectionDirection(ConnectionDirection.InnerToMiddleware);
    }

    @Override
    public boolean processMessage(TaskMessage taskMessage) {

        EventManager.event(BackendReciveTask.class, newData, taskMessage.getMessage().getMiddlewareResponse());
        Message.EntityState changes = taskMessage.getMessage().getMiddlewareResponse().getChanges();
        for(Message.Person s : changes.getStudentsList()){

            LOGGER.log(Level.INFO, "Odebrany student " + s.getUuid());
            Person person = Translator.translatePerson(s);
            PersonService personService = BaseContext.getInstance().getApplicationContext().getBean(PersonService.class);
            personService.addPerson(person);
        }

        for(Message.Subject s : changes.getSubjectsList()){

            LOGGER.log(Level.INFO, "Odebrany przedmiot " + s.getName());
            Group group = Translator.translateSubject(s);
            GroupService groupService = BaseContext.getInstance().getApplicationContext().getBean(GroupService.class);
            groupService.addGroup(group);
        }

        for(Message.PersonSubject s : changes.getPersonSubjectsList()){

            LOGGER.log(Level.INFO, "Odebrany zapis na przedmiot " + s.getUuid());
            PersonGroup person = Translator.translatePersonSubject(s);
            PersonGroupService personService = BaseContext.getInstance().getApplicationContext().getBean(PersonGroupService.class);

//            personService.addPersonGroup(person);
            //personService.addPersonGroup(person);
//            Group[] groups = {person.getGroup()};
//            person.getPerson().setupGroups(Arrays.asList(groups));
//
//            personService.updatePerson(person.getPerson());

        }

        return true;

    }
}
