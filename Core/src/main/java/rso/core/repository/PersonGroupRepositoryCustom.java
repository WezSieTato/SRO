package rso.core.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import rso.core.model.PersonGroup;

import java.util.List;

/**
 * Created by kometa on 28.05.2015.
 */
public interface PersonGroupRepositoryCustom {
    @Modifying
    @Transactional
    void saveItems(List<PersonGroup> personGroupList);
}
