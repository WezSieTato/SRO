package rso.core.model;

import org.hibernate.annotations.Cascade;
import rso.core.abstraction.Identified;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by kometa on 27.04.2015. via iOS
 */

@Entity
@Table(name = "student")
@NamedEntityGraph(name = "Person.classes",
        attributeNodes = {@NamedAttributeNode(value = "personGroups", subgraph = "group")},
        subgraphs = {@NamedSubgraph(name = "group", attributeNodes = @NamedAttributeNode("group"))}
)

public class Person extends Identified {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "person", orphanRemoval = true, cascade =
            {CascadeType.PERSIST, CascadeType.MERGE})
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    List<PersonGroup> personGroups = new ArrayList<PersonGroup>();
    @Column(name = "first_name", nullable = true)
    private String firstName;
    @Column(name = "last_name", nullable = true)
    private String lastName;
    @Column(name = "birth_date", nullable = true)
    private Date dateOfBirth;

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<PersonGroup> getPersonGroups() {
        return personGroups;
    }

    public void setPersonGroups(List<PersonGroup> personGroups) {
        this.personGroups = personGroups;
    }

    public void setupGroups(List<Group> groups) {


        List<PersonGroup> personGroupList = new ArrayList<PersonGroup>();

        for (int i = 0; i < groups.size(); ++i) {
            PersonGroup personGroup = new PersonGroup();

            personGroup.setPerson(this);
            personGroup.setGroup(groups.get(i));
            personGroupList.add(personGroup);
        }
        this.setPersonGroups(personGroupList);
    }


}
