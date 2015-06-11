package rso.core.repository;

import rso.core.model.PersonGroup;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by kometa on 28.05.2015.
 */
public class PersonGroupRepositoryImpl implements PersonGroupRepositoryCustom {

    private static final String QUERY_BEGIN = "INSERT INTO student_class (id_student, id_class, timestamp, uuid) VALUES ";
    @PersistenceContext
    EntityManager entityManager;
    private boolean first = true;
    private DateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:MM:SS");


    public void saveItems(List<PersonGroup> personGroupList) {
        StringBuilder stringBuilder = new StringBuilder();
stringBuilder.append(QUERY_BEGIN);
        for (PersonGroup personGroup : personGroupList) {
            stringBuilder.append(appendPersonGroup(personGroup));
        }
        first = true;
String s = stringBuilder.toString();
try {
    entityManager.createNativeQuery(s).executeUpdate();
}catch(Exception e ){

}
    }


    private String appendPersonGroup(PersonGroup personGroup) {


        StringBuilder stringBuilder = new StringBuilder();


        if (first)
            first = false;
        else
            stringBuilder.append("\n,");
try {
    stringBuilder.
            append("(").
            append(personGroup.getPerson().getId()).append(",").
            append(personGroup.getGroup().getId()).append(",'").
            append(df.format(personGroup.getTimestamp())).append("','").
            append(personGroup.getUuid()).
            append("')");
}catch(Exception e ){

    return "";
}
        return stringBuilder.toString();
    }
}
