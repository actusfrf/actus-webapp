package com.bundiconsulting.actuswebapp.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Document(collection = "forms")
public class Form {

    @Id
    private String _id;

    @Field (value="Identifier")
    private String identifier;
    @Field (value="ContractType")
    private String contractType;
    @Field (value="Name")
    private String name;
    @Field (value="Description")
    private String description;
    @Field (value="Version")
    private String version;
    @Field (value="Terms")
    private List<Term> terms = null;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     *
     */
    public Form() {
    }

    /**
     *
     * @param description
     * @param terms
     * @param contractType
     * @param name
     * @param identifier
     * @param version
     */
    public Form(String identifier, String contractType, String name, String description, String version, List<Term> terms) {
        super();
        this.identifier = identifier;
        this.contractType = contractType;
	this.name = name;
        this.description = description;
        this.version = version;
        this.terms = terms;
    }


    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<Term> getTerms() {
        return terms;
    }

    public void setTerms(List<Term> terms) {
        this.terms = terms;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
