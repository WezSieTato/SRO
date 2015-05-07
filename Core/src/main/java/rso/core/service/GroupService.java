package rso.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rso.core.model.Group;
import rso.core.repository.GroupRepository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by kometa on 05.05.2015.
 */
@Service
@Transactional
public class GroupService {

    @Autowired
    GroupRepository groupRepository;


    public Collection<Group> findAll() {

        return groupRepository.findAll();
    }

    public List<Group> findNewerThan(Date time) {

        return groupRepository.findByTimestampGreaterThan(time);
    }

    public Group findByUuid(String uuid) {
        return groupRepository.findByUuid(uuid);
    }

    public void addGroup(Group group) {
        groupRepository.save(group);
    }

}
