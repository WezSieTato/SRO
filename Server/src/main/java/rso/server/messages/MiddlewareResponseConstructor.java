package rso.server.messages;

import org.hibernate.mapping.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import rso.core.model.Group;
import rso.core.model.Message;
import rso.core.model.Person;
import rso.core.model.PersonGroup;
import rso.core.service.GroupService;
import rso.core.service.PersonGroupService;
import rso.core.service.PersonService;

import java.util.List;

/**
 * Created by marcin on 06/05/15.
 */
public class MiddlewareResponseConstructor {

    private Message.EntityState.Builder builder;

    @Autowired
    PersonService personService;

    @Autowired
    GroupService groupService;

    @Autowired
    PersonGroupService personGroupService;

    public Message.RSOMessage construct(long timestamp){
        builder = Message.EntityState.newBuilder();
        List<Person> personList = personService.findAll();

        for (Person person : personList){
            addStudent(person.getUuid(), 0);
        }

        java.util.Collection<Group> groupList = groupService.findAll();
        for(Group group : groupList){
            addGroup(group.getUuid(), group.getName(), 0);
        }

//        for(PersonGroup personGroup : personGroupService.){
//
//        }

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

        builder.addPersonSubjects(pgBuilder);
    }
}
