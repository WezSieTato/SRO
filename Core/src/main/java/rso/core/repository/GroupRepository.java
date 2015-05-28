package rso.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rso.core.model.Group;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by kometa on 05.05.2015.
 */
public interface GroupRepository extends JpaRepository<Group, Long> {

    @Query(value = "select count(*) from student_class sc join class c on sc.id_class = c.id where c.name = :name", nativeQuery = true)
    int getUsersCountRegisteredForGroup(@Param("name") String name);


    Group findByUuid(String uuid);

    Collection<Group> findByUuidIn(Collection<String> uuids);

    List<Group> findByTimestampGreaterThan(Date date);


    @Query(value = "Select * from class pg order by pg.timestamp desc limit 0, 1", nativeQuery = true)
    Group findNewestRecord();
}
