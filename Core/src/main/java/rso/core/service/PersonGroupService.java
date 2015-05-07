package rso.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rso.core.model.Group;
import rso.core.model.Person;
import rso.core.model.PersonGroup;
import rso.core.repository.GroupRepository;
import rso.core.repository.PersonGroupRepository;
import rso.core.repository.PersonRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by kometa on 06.05.2015.
 */

@Service
public class PersonGroupService {

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    PersonGroupRepository personGroupRepository;

    public PersonGroup buildPersonGroups(String personUuid, String groupUuid) {

        Person person = personRepository.findByUuid(personUuid);
        Group group = groupRepository.findByUuid(groupUuid);

        PersonGroup generated = new PersonGroup();
        generated.setGroup(group);
        generated.setPerson(person);

        return generated;
    }

    public List<PersonGroup> findNewerThan(Date time) {

        return personGroupRepository.findByTimestampGreaterThan(time);
    }

    public void addPersonGroup(PersonGroup personGroup) {
        personGroupRepository.save(personGroup);
    }

    public PersonGroup findByUuid(String uuid) {
        return personGroupRepository.findByUuid(uuid);
    }
}
