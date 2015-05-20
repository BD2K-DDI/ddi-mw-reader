package uk.ac.ebi.ddi.reader.model;

import uk.ac.ebi.ddi.reader.extws.mw.model.dataset.Metabolite;

import java.util.*;

/**
 * General Project information of about the Project
 * @author ypriverol
 */
public class Project {

    private String accession;

    private String repositoryName;

    private String title;

    private String projectDescription;

    private Specie specie;

    private Date submissionDate;

    private Date publicationDate;

    private String sampleProcessingProtocol;

    private String dataProcessingProtocol;

    private Instrument instrument;

    private List<String> experimentTypes;

    private List<String> projectTags;

    private Submitter submitter;

    private String datasetLink;

    private List<Metabolite> metaboligths;
    private Set<String> factors;

    /**
     * Default constructor create a List of every list-based attribute
     */
    public Project() {
        projectTags           = Collections.emptyList();
        experimentTypes       = Collections.emptyList();
    }

    public boolean isPublicProject() {
        return true;
    }

    public String getAccession() {
        return accession;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public String getTitle() {
        return title;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public Specie getSpecie() {
        return specie;
    }

    public Date getSubmissionDate() {
        return submissionDate;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public String getSampleProcessingProtocol() {
        return sampleProcessingProtocol;
    }

    public String getDataProcessingProtocol() {
        return dataProcessingProtocol;
    }

    public Instrument getInstruments() {
        return instrument;
    }

    public List<String> getExperimentTypes() {
        return experimentTypes;
    }

    public List<String> getProjectTags() {
        return projectTags;
    }

    public Submitter getSubmitter() {
        return submitter;
    }

    public void setAccession(String accession) {
        this.accession = accession;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public void setSpecie(Specie specie) {
        this.specie = specie;
    }

    public void setSubmissionDate(Date submissionDate) {
        this.submissionDate = submissionDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public void setSampleProcessingProtocol(String sampleProcessingProtocol) {
        this.sampleProcessingProtocol = sampleProcessingProtocol;
    }

    public void setDataProcessingProtocol(String dataProcessingProtocol) {
        this.dataProcessingProtocol = dataProcessingProtocol;
    }

    public void setInstrument(Instrument instrument) {
        this.instrument = instrument;
    }

    public void setExperimentTypes(List<String> experimentTypes) {
        this.experimentTypes = experimentTypes;
    }

    public void setProjectTags(List<String> projectTags) {
        this.projectTags = projectTags;
    }

    public void setSubmitter(Submitter submitter) {
        this.submitter = submitter;
    }

    public void addCuratorKey(String reviewLevel) {
        if(reviewLevel != null)
            projectTags.add(reviewLevel);
    }

    public String getDatasetLink() {
        return datasetLink;
    }

    public void setDatasetLink(String datasetLink) {
        this.datasetLink = datasetLink;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public List<Metabolite> getMetaboligths() {
        return metaboligths;
    }

    public void setMetaboligths(List<Metabolite> metaboligths) {
        this.metaboligths = metaboligths;
    }

    public void setMetaboligths(Collection<Metabolite> metabolites){
        this.metaboligths = new ArrayList<Metabolite>(metabolites);
    }

    public void setFactors(Set<String> factors) {
        this.factors = factors;
    }

    public Set<String> getFactors() {
        return factors;
    }
}
