package uk.ac.ebi.ddi.reader.extws.mw.model.dataset;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 18/05/2015
 */

@JsonIgnoreProperties(ignoreUnknown = true)

public class DataSet {

    @JsonProperty("study_id")
    String id;

    @JsonProperty("study_title")
    String title;

    @JsonProperty("study_type")
    String type;

    @JsonProperty("institute")
    String institute;

    @JsonProperty("department")
    String department;

    @JsonProperty("last_name")
    String last_name;

    @JsonProperty("first_name")
    String firstname;

    @JsonProperty("email")
    String email;

    @JsonProperty("submit_date")
    String submit_date;

    @JsonProperty("study_summary")
    String description;

    @JsonProperty("subject_species")
    String subject_species;
    private String taxonomy;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInstitute() {
        return institute;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubmit_date() {
        return submit_date;
    }

    public void setSubmit_date(String submit_date) {
        this.submit_date = submit_date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSubject_species() {
        return subject_species;
    }

    public void setSubject_species(String subject_species) {
        this.subject_species = subject_species;
    }

    public String getTaxonomy() {
        return taxonomy;
    }
}
