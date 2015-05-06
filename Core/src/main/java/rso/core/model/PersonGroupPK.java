package rso.core.model;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * Created by kometa on 05.05.2015.
 */
@Embeddable
public class PersonGroupPK implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_student")
    Person person;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_class")
    Group group;


    public PersonGroupPK() {
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