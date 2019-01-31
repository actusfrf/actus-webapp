package com.bundiconsulting.actuswebapp.repositories;

import com.bundiconsulting.actuswebapp.models.Demo;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
/*import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.core.query.Criteria;*/

public interface DemoRepository extends CrudRepository<Demo, String> {
    @Override
    void delete(Demo deleted);

    List<Demo> findByContractType(String contractType);
}

