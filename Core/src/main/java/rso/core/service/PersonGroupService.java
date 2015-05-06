package rso.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rso.core.model.Group;
import rso.core.model.Person;
import rso.core.model.PersonGroup;
import rso.core.repository.GroupRepository;
import rso.core.repository.PersonRepository;

/**
 * Created by kometa on 06.05.2015.
 */

@Service
public class PersonGroupService {

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    PersonRepository personRepository;

    public PersonGroup buildPersonGroups(String personUuid, String groupUuid) {

        Person person = personRepository.findByUuid(personUuid);
        Group group = groupRepository.findByUuid(groupUuid);

        PersonGroup generated = new PersonGroup();
        generated.setGroup(group);
        generated.setPerson(person);

        return generated;
    }

}
