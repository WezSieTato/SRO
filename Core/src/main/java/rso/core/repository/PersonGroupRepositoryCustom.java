package rso.core.repository;

import rso.core.model.PersonGroup;

import java.util.List;

/**
 * Created by kometa on 28.05.2015.
 */
public interface PersonGroupRepositoryCustom {

    void saveItems(List<PersonGroup> personGroupList);
}
