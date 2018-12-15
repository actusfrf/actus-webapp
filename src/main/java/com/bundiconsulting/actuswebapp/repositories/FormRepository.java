package com.bundiconsulting.actuswebapp.repositories;

import com.bundiconsulting.actuswebapp.models.Form;
import org.springframework.data.repository.CrudRepository;

public interface FormRepository  extends CrudRepository<Form, String> {
    @Override
    void delete(Form deleted);
}
