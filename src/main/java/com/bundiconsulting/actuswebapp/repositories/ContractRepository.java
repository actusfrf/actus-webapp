package com.bundiconsulting.actuswebapp.repositories;

import com.bundiconsulting.actuswebapp.models.Contract;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ContractRepository  extends CrudRepository<Contract, String> {
    @Override
    void delete(Contract deleted);
}
