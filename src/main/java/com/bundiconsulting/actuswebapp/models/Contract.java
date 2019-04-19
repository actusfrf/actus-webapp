package com.bundiconsulting.actuswebapp.models;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.util.HashMap;
import java.util.Map;

@Document(collection = "forms")
public class Contract {

    @Field (value="ContractType")
    private String contractType;
    @Field (value="Name")
    private String name;
    @Field (value="Description")
    private String description;

    /**
     * No args constructor for use in serialization
     *
     */
    public Contract() {
    }

    /**
     *
     * @param contractType
     * @param name
     * @param description
     */
    public Contract(String contractType, String name, String description) {
        super();
        this.contractType = contractType;
        this.name = name;
        this.description = description;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
