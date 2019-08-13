package org.actus.webapp.repositories;

import org.actus.webapp.models.DemoMeta;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface DemoMetaRepository extends CrudRepository<DemoMeta, String> {
    @Override
    void delete(DemoMeta deleted);

    List<DemoMeta> findByContractType(String contractType);
}

