package rso.core.model;

import rso.core.abstraction.Identified;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by kometa on 05.05.2015.
 */

@Entity
@Table(name = "class")
public class Group extends Identified {

    @Column(name = "name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
