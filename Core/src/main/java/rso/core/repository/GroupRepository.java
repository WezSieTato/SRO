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

    @Query(value = "select count(*) from student_class where id_class = :id", nativeQuery = true)
    int getUsersCountRegisteredForGroup(@Param("id") int id);


    Group findByUuid(String uuid);

    Collection<Group> findByUuidIn(Collection<String> uuids);

    List<Group> findByTimestampGreaterThan(Date date);

}
