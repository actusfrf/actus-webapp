package com.bundiconsulting.actuswebapp.models;

import org.springframework.data.mongodb.core.mapping.Field;
import java.util.HashMap;
import java.util.Map;

public class Term {

    @Field (value="Group")
    private String group;
    @Field (value="Name")
    private String name;
    @Field (value="Type")
    private String type;
    @Field (value="List")
    private String list;
    @Field (value="Description")
    private String description;
    @Field (value="Applicability")
    private String applicability;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     *
     */
    public Term() {
    }

    /**
     *
     * @param description
     * @param name
     * @param applicability
     * @param list
     * @param type
     * @param group
     */
    public Term(String group, String name, String type, String list, String description, String applicability) {
        super();
        this.group = group;
        this.name = name;
        this.type = type;
        this.list = list;
        this.description = description;
        this.applicability = applicability;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getApplicability() {
        return applicability;
    }

    public void setApplicability(String applicability) {
        this.applicability = applicability;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
