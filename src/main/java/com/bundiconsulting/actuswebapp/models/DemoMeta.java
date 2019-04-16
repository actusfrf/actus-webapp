package com.bundiconsulting.actuswebapp.models;

import java.util.Map;

import java.util.HashMap;
import java.util.Map;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Document(collection = "demos")
public class DemoMeta {

    @Id
    private String _id;
    @Field(value="Identifier")
    private String identifier;
    @Field (value="Label")
    private String label;
    @Field (value="ContractType")
    private String contractType;
    @Field (value="Version")
    private String version;
    @Field (value="Description")
    private String description;

    /**
     * No args constructor for use in serialization
     *
     */
    public DemoMeta() {
    }

    /**
     *
     * @param description
     * @param contractType
     * @param label
     * @param identifier
     * @param version
     */
    public DemoMeta(String identifier, String label, String contractType, String version, String description) {
        super();
        this.identifier = identifier;
        this.label = label;
        this.contractType = contractType;
        this.version = version;
        this.description = description;
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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
