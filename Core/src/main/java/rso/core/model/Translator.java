package rso.core.model;

import com.fasterxml.jackson.databind.deser.Deserializers;
import rso.core.abstraction.BaseContext;
import rso.core.service.GroupService;
import rso.core.service.PersonService;

import java.util.Date;

/**
 * Created by marcin on 07/05/15.
 */
public class Translator {

    public static Person translatePerson(Message.Person personMessage){
        Person person = new Person();
        if(personMessage.hasUuid()) person.setUuid(personMessage.getUuid());
        if(personMessage.hasBirthDate()) person.setDateOfBirth(new Date(personMessage.getBirthDate()));
        if(personMessage.hasName()) person.setFirstName(personMessage.getName());
        if(personMessage.hasSurname()) person.setLastName(personMessage.getSurname());
        if(personMessage.hasTimestamp()) person.setTimestamp(new Date(personMessage.getTimestamp()));

        return person;
    }

    public static Group translateSubject(Message.Subject subject){
        Group group = new Group();

        if(subject.hasUuid()) group.setUuid(subject.getUuid());
        if(subject.hasName()) group.setName(subject.getName());
        if(subject.hasTimestamp()) group.setTimestamp(new Date(subject.getTimestamp()));

        return group;
    }


    public static  PersonGroup translatePersonSubject(Message.PersonSubject personSubject){
        PersonGroup personGroup = new PersonGroup();

        if(personSubject.hasUuid()) personGroup.setUuid(personSubject.getUuid());
        if(personSubject.hasTimestamp()) personGroup.setTimestamp(new Date(personSubject.getTimestamp()));

        PersonService personService = BaseContext.getInstance().getApplicationContext().getBean(PersonService.class);
        Person person = personService.findByUuid(personSubject.getUUIDPerson());
        personGroup.setPerson(person);

        GroupService groupService = BaseContext.getInstance().getApplicationContext().getBean(GroupService.class);
        Group group = groupService.findByUuid(personSubject.getUUIDSubject());
        personGroup.setGroup(group);

        return personGroup;
    }
}
