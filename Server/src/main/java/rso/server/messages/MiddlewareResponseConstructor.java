package rso.server.messages;

import org.hibernate.mapping.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rso.core.abstraction.BaseContext;
import rso.core.model.Group;
import rso.core.model.Message;
import rso.core.model.Person;
import rso.core.model.PersonGroup;
import rso.core.service.GroupService;
import rso.core.service.PersonGroupService;
import rso.core.service.PersonService;

import java.util.Date;
import java.util.List;

/**
 * Created by marcin on 06/05/15.
 */
public class MiddlewareResponseConstructor {

    private Message.EntityState.Builder builder;


    PersonService personService;


    GroupService groupService;


    PersonGroupService personGroupService;

    public Message.RSOMessage construct(long timestamp){

        personService = BaseContext.getInstance().getApplicationContext().getBean(PersonService.class);
        groupService = BaseContext.getInstance().getApplicationContext().getBean(GroupService.class);
        personGroupService = BaseContext.getInstance().getApplicationContext().getBean(PersonGroupService.class);

        builder = Message.EntityState.newBuilder();
        Date date = new Date(timestamp);
        List<Person> personList = personService.findNewerThan(date);

        for (Person person : personList){
            addStudent(person.getUuid(), person.getTimestamp().getTime());
        }

        java.util.Collection<Group> groupList = groupService.findNewerThan(date);
        for(Group group : groupList){
            addGroup(group.getUuid(), group.getName(), group.getTimestamp().getTime());
        }

        for(PersonGroup personGroup : personGroupService.findNewerThan(date)){
            addAssociations(personGroup.getUuid(), personGroup.getPerson().getUuid(),
                    personGroup.getGroup().getUuid(), personGroup.getTimestamp().getTime());
        }

        Message.MiddlewareResponse.Builder resp = Message.MiddlewareResponse.newBuilder().setChanges(builder);

        return Message.RSOMessage.newBuilder().setMiddlewareResponse(resp).build();
    }

    private void addStudent(String uuid, long timestamp){
        Message.Person.Builder personBuilder = Message.Person.newBuilder();
        personBuilder.setUuid(uuid).setTimestamp(timestamp);
        builder.addStudents(personBuilder);
    }

    private void addGroup(String uuid, String name, long timestamp){
        Message.Subject.Builder subjectBuilder = Message.Subject.newBuilder();
        subjectBuilder.setUuid(uuid).setName(name).setTimestamp(timestamp);
        builder.addSubjects(subjectBuilder);
    }

    private void addAssociations(String uuid, String uuidPerson, String uuidGroup, long timestamp){
        Message.PersonSubject.Builder pgBuilder = Message.PersonSubject.newBuilder();
        pgBuilder.setUuid(uuid).setUUIDPerson(uuidPerson).setUUIDSubject(uuidGroup).setTimestamp(timestamp);
        builder.addPersonSubjects(pgBuilder);
    }
}
