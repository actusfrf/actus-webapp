package com.djamware.angular.repositories;

import com.djamware.angular.models.Contact;
import org.springframework.data.repository.CrudRepository;

public interface ContactRepository extends CrudRepository<Contact, String> {
    @Override
    Contact findOne(String id);

    @Override
    void delete(Contact deleted);
}
