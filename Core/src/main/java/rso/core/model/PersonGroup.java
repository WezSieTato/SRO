package rso.core.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by kometa on 05.05.2015.
 */
@Entity
@Table(name = "student_class")
@IdClass(PersonGroupPK.class)
@NamedEntityGraph(name = "PersonGroup.details",
        attributeNodes = {@NamedAttributeNode(value = "person"), @NamedAttributeNode(value = "group")}

)

public class PersonGroup {

    @Id
    Person person;

    @Id
    Group group;

    private String uuid;

    private Date timestamp = new Date();

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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}