package rso.core.repository;

import rso.core.model.PersonGroup;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by kometa on 28.05.2015.
 */
public class PersonGroupRepositoryImpl implements PersonGroupRepositoryCustom {

    private static final String QUERY_BEGIN = "INSERT INTO student_class (id_student, id_class, timestamp, uuid) ";
    @PersistenceContext
    EntityManager entityManager;
    private boolean first = true;


    public void saveItems(List<PersonGroup> personGroupList) {
        StringBuilder stringBuilder = new StringBuilder();

        for (PersonGroup personGroup : personGroupList) {
            stringBuilder.append(appendPersonGroup(personGroup));
        }

        entityManager.createQuery(stringBuilder.toString()).executeUpdate();
    }


    private String appendPersonGroup(PersonGroup personGroup) {


        StringBuilder stringBuilder = new StringBuilder();


        if (first)
            first = false;
        else
            stringBuilder.append(",");

        stringBuilder.
                append("(").
                append(personGroup.getPerson().getId()).append(",").
                append(personGroup.getGroup().getId()).append(",").
                append(personGroup.getTimestamp().getTime()).append(",").
                append(personGroup.getUuid()).
                append(")");

        return stringBuilder.toString();
    }
}
