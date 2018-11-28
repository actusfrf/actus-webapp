package com.bundiconsulting.actuswebapp.repositories;

import com.bundiconsulting.actuswebapp.models.Contact;
import org.springframework.data.repository.CrudRepository;

public interface ContactRepository extends CrudRepository<Contact, String> {
    @Override
    void delete(Contact deleted);
}
