package rso.core.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rso.core.model.Group;
import rso.core.model.Person;
import rso.core.model.PersonGroup;
import rso.core.repository.GroupRepository;
import rso.core.repository.PersonGroupRepository;
import rso.core.repository.PersonRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * Created by kometa on 06.05.2015.
 */
@Component
public class Utils {

    @Autowired
    PersonRepository personRepository;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    PersonGroupRepository personGroupRepository;

    public Date getOldestDate() {

        ArrayList<Date> dates = new ArrayList<Date>();

        Person person = personRepository.findNewestRecord();
        Group group = groupRepository.findNewestRecord();
        PersonGroup personGroup = personGroupRepository.findNewestRecord();

        dates.add(person.getTimestamp());
        dates.add(group.getTimestamp());
        dates.add(personGroup.getTimestamp());

        Collections.sort(dates, Collections.reverseOrder());

        return dates.get(0);


    }
}
