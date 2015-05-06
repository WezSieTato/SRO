package rso.core.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by kometa on 05.05.2015.
 */
@Entity
@Table(name = "student_class")
@IdClass(PersonGroupPK.class)
public class PersonGroup {

    @Id
    Person person;

    @Id
    Group group;

    private Date timestamp;

    public PersonGroup() {
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}