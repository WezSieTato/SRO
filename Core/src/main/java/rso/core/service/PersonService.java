package rso.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rso.core.model.Person;
import rso.core.repository.PersonRepository;

import javax.transaction.Transactional;
import java.util.*;

/**
 * Created by kometa on 05.05.2015.
 */

@Service
@Transactional
public class PersonService {

    @Autowired
    PersonRepository personRepository;


    public List<Person> findAll() {

        return collectionToSetToList(personRepository.findAll());

    }

    public List<Person> findWithEmptyUuid() {

        return collectionToSetToList(personRepository.findByUuidIsNull());
    }

    public Person findByUuid(String uuid) {

        return personRepository.findByUuid(uuid);
    }

    public void addPerson(Person person) {

        this.save(person);
    }

    public void updatePerson(Person person) {

        this.save(person);
    }

    public void save(Collection<Person> persons) {

        personRepository.save(persons);
    }

    public void assignUuidToPerson(Person person) {
        person.setUuid(UUID.randomUUID().toString());
        this.save(person);

    }

    private List<Person> collectionToSetToList(Collection<Person> persons) {
        return new ArrayList<Person>(new HashSet<Person>(persons));
    }

    private void save(Person person) {
        personRepository.save(person);
    }

    public List<Person> findNewerThan(Date time) {

        return personRepository.findByTimestampGreaterThan(time);
    }
}
