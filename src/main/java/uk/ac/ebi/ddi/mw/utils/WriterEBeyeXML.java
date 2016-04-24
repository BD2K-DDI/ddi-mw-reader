package uk.ac.ebi.ddi.mw.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import uk.ac.ebi.ddi.mw.extws.mw.model.Metabolite;
import uk.ac.ebi.ddi.mw.model.Instrument;
import uk.ac.ebi.ddi.mw.model.Project;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * GenerateEBeyeXML object.
 *
 * Generates EB-eye search XML to a given output directory based upon a PX Submission project
 * supplied as a Project and Submission.
 *
 * @author  Yasset Perez-Riverol
 */
public class WriterEBeyeXML {

    private static final Logger logger = LoggerFactory.getLogger(WriterEBeyeXML.class);

    private static final String NOT_AVAILABLE = "Not available";

    private static final String OMICS_TYPE    = "Metabolomics";

    private static final String DEFAULT_EXPERIMENT_TYPE = "Mass Spectrometry";

    private static final String METABOLOME_WORKBENCH_DESCRIPTION = "The Metabolomics Workbench will serve as a national and international repository for metabolomics data and metadata and will provide analysis tools and access to metabolite standards, protocols, tutorials, training, and more.";

    private Project project;

    private File outputDirectory;

    /**
     * Constructor.
     *
     * @param proj  (required) public project to be used for generating the EB-eye XML.
     * @param outputDirectory   (required) target output directory.
     */
    public WriterEBeyeXML(Project proj, File outputDirectory) {
        this.project = proj;
        this.outputDirectory = outputDirectory;
    }

    /**
     * Performs the EB-eye generation of a defined public project, submission summary, and output directory.
     * @throws Exception
     */
    public void generate() throws Exception {

        if (project==null || outputDirectory==null) {
            logger.error("The project, submission, and output directory all needs to be set before genearting EB-eye XML.");
        }
        if (!project.isPublicProject()) {
            logger.error("Project " + project.getAccession() + " is still private, not generating EB-eye XML.");
        } else {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            //Add database Name Node

            Element database = document.createElement("database");
            document.appendChild(database);

            //Add the name of the database
            Element name = document.createElement("name");
            name.appendChild(document.createTextNode(project.getRepositoryName()));
            database.appendChild(name);

            //Add the description of the database
            Element description = document.createElement("description");
            description.appendChild(document.createTextNode(METABOLOME_WORKBENCH_DESCRIPTION));
            database.appendChild(description);

            //Database release
            Element release = document.createElement("release");
            release.appendChild(document.createTextNode("1.0"));
            database.appendChild(release);

            //Release date (This release date is related whit the day where the data was generated)
            Element releaseDate = document.createElement("release_date");
            releaseDate.appendChild(document.createTextNode(new SimpleDateFormat("yyyy-MM-dd").format(new Date())));
            database.appendChild(releaseDate);

            Element entryCount = document.createElement("entry_count");
            entryCount.appendChild(document.createTextNode("1"));
            database.appendChild(entryCount);

            //Start to index the entries of the project
            Element entries = document.createElement("entries");
            database.appendChild(entries);

            //The project entry to be fill in the document
            Element entry = document.createElement("entry");
            entry.setAttribute("id", project.getAccession());

            Element projectName = document.createElement("name");
            projectName.appendChild(document.createTextNode(project.getTitle()));
            entry.appendChild(projectName);

            String projDescription = project.getTitle();
            // We validate here if
            if (project.getProjectDescription()!=null && !project.getProjectDescription().isEmpty())
                projDescription = project.getProjectDescription();

            Element projectTitle = document.createElement("description");
            projectTitle.appendChild(document.createTextNode(projDescription));
            entry.appendChild(projectTitle);

            Element crossReferences = document.createElement("cross_references");
            entry.appendChild(crossReferences);

            if (project.getTaxonomies() != null && !project.getTaxonomies().isEmpty()){
                for(String taxonomy: project.getTaxonomies()){
                    Element refSpecies = document.createElement("ref");
                    refSpecies.setAttribute("dbkey", taxonomy);
                    refSpecies.setAttribute("dbname", "TAXONOMY");
                    crossReferences.appendChild(refSpecies);
                }
            }

            if (project !=null && project.getMetaboligths() != null && project.getMetaboligths().size() > 0 ) {
                for (Metabolite met : project.getMetaboligths()) {
                    if(met != null && met.getChebi() != null){
                        Element refMet = document.createElement("ref");
                        refMet.setAttribute("dbkey", met.getChebi());
                        refMet.setAttribute("dbname", "ChEBI");
                        crossReferences.appendChild(refMet);
                    }
                }
            }

            Element dates = document.createElement("dates");
            entry.appendChild(dates);

            if(project.getSubmissionDate() != null){
                Element dateSubmitted = document.createElement("date");
                dateSubmitted.setAttribute("value", new SimpleDateFormat("yyyy-MM-dd").format(project.getSubmissionDate()));
                dateSubmitted.setAttribute("type", "submission");
                dates.appendChild(dateSubmitted);
            }

            /**
             * Add additional Fields for DDI project to be able to find the projects. Specially additional metadata
             * such as omics field, ptms, study type, data protocol sample protocol, etc.
             */

            Element additionalFields = document.createElement("additional_fields");
            entry.appendChild(additionalFields);


            // Add the omics type
            Element omicsType = document.createElement("field");
            omicsType.setAttribute("name", "omics_type");
            omicsType.appendChild(document.createTextNode(OMICS_TYPE));
            additionalFields.appendChild(omicsType);

            if(project.getDatasetLink() != null){
                Element repoLink = document.createElement("field");
                repoLink.setAttribute("name", "full_dataset_link");
                repoLink.appendChild(document.createTextNode(project.getDatasetLink()));
                additionalFields.appendChild(repoLink);
            }

            //Add the domain source
            Element respository = document.createElement("field");
            respository.setAttribute("name", "repository");
            respository.appendChild(document.createTextNode(project.getRepositoryName()));
            additionalFields.appendChild(respository);

            //Add Data Processing Protocol
            if (project.getDataProcessingProtocol()!=null && !project.getDataProcessingProtocol().isEmpty()) {
                for(String dataprocessing: project.getDataProcessingProtocol()){
                    Element dataProcProt = document.createElement("field");
                    dataProcProt.setAttribute("name", "data_protocol");
                    dataProcProt.appendChild(document.createTextNode(dataprocessing));
                    additionalFields.appendChild(dataProcProt);
                }
            }

            //Add Instrument information
            if (project.getInstrument()!=null && project.getInstrument().size() > 0) {
                for(Instrument instrument:project.getInstrument()){
                    if(instrument != null){
                        Element fieldInstruemnt = document.createElement("field");
                        fieldInstruemnt.setAttribute("name", "instrument_platform");
                        fieldInstruemnt.appendChild(document.createTextNode(instrument.getType()));
                        additionalFields.appendChild(fieldInstruemnt);
                    }
                }
            } else {
                Element fieldInstruemnt = document.createElement("field");
                fieldInstruemnt.setAttribute("name", "instrument_platform");
                fieldInstruemnt.appendChild(document.createTextNode(NOT_AVAILABLE));
                additionalFields.appendChild(fieldInstruemnt);
            }

            //Add Study factors
            if (project.getFactors() != null && project.getFactors().size() > 0) {
                for(String factor: project.getFactors()){
                    if(factor != null){
                        Element factorField = document.createElement("field");
                        factorField.setAttribute("name", "study_factor");
                        factorField.appendChild(document.createTextNode(factor));
                        additionalFields.appendChild(factorField);
                    }
                }
            }

            //Add information about the species
            if (project.getSpecies()!=null && !project.getSpecie().isEmpty()) {
                for(String specie: project.getSpecies()){
                    Element refSpecies = document.createElement("field");
                    refSpecies.setAttribute("name", "species");
                    refSpecies.appendChild(document.createTextNode(specie));
                    additionalFields.appendChild(refSpecies);
                }
            } else {
                Element refSpecies = document.createElement("field");
                refSpecies.setAttribute("name", "species");
                refSpecies.appendChild(document.createTextNode(NOT_AVAILABLE));
                additionalFields.appendChild(refSpecies);
            }


             //Add information about experiment type
            if (project.getExperimentTypes()!=null && project.getExperimentTypes().size()> 0) {
                for (String expType : project.getExperimentTypes()) {
                    Element refExpType = document.createElement("field");
                    refExpType.setAttribute("name", "technology_type");
                    refExpType.appendChild(document.createTextNode(expType));
                    additionalFields.appendChild(refExpType);
                }
            } else {
                Element refExpType = document.createElement("field");
                refExpType.setAttribute("name", "technology_type");
                refExpType.appendChild(document.createTextNode(NOT_AVAILABLE));
                additionalFields.appendChild(refExpType);
            }

            //Add curator tags and keywords
            if (project.getProjectTags()!=null && project.getProjectTags().size()>0) {
                for (String projectTag : project.getProjectTags()) {
                    Element fieldProjTag = document.createElement("field");
                    fieldProjTag.setAttribute("name", "curator_keywords");
                    fieldProjTag.appendChild(document.createTextNode(projectTag));
                    additionalFields.appendChild(fieldProjTag);
                }
            }

            /**
             * Add the matabolite names for search purpose for extra reasons
             */
            if(project.getMetaboligths() != null && project.getMetaboligths().size() > 0){
                for(Metabolite met: project.getMetaboligths()){
                    if(met != null && met.getName() != null){
                        Element metName = document.createElement("field");
                        metName.setAttribute("name", "metabolite_name");
                        metName.appendChild(document.createTextNode(met.getName()));
                        additionalFields.appendChild(metName);
                    }
                }
            }

            /**
             * Add the matabolite pubchem ids for search purpose for extra reasons
             */
            if(project.getMetaboligths() != null && project.getMetaboligths().size() > 0){
                for(Metabolite met: project.getMetaboligths()){
                    if(met != null && met.getPubchem() != null){
                        Element metName = document.createElement("field");
                        metName.setAttribute("name", "pubchem_id");
                        metName.appendChild(document.createTextNode(met.getPubchem()));
                        additionalFields.appendChild(metName);
                    }
                }
            }

            //Add disease information
            if (project.getDiseases()!=null && project.getDiseases().size()>0) {
                for (String disease : project.getDiseases()) {
                    Element refDisease = document.createElement("field");
                    refDisease.setAttribute("name", "disease");
                    refDisease.appendChild(document.createTextNode(disease));
                    additionalFields.appendChild(refDisease);
                }
            } else {
                Element refDisease = document.createElement("field");
                refDisease.setAttribute("name", "disease");
                refDisease.appendChild(document.createTextNode(NOT_AVAILABLE));
                additionalFields.appendChild(refDisease);
            }

            //Tissue information
            if (project.getTissues()!=null && project.getTissues().size()>0) {
                for (String tissue : project.getTissues()) {
                    Element fieldTissue = document.createElement("field");
                    fieldTissue.setAttribute("name", "tissue");
                    fieldTissue.appendChild(document.createTextNode(tissue));
                    additionalFields.appendChild(fieldTissue);
                }
            } else {
                Element fieldTissue = document.createElement("field");
                fieldTissue.setAttribute("name", "tissue");
                fieldTissue.appendChild(document.createTextNode(NOT_AVAILABLE));
                additionalFields.appendChild(fieldTissue);
            }




            //Add submitter information
            if(project.getSubmitter() != null){
                if(project.getSubmitter().getName() != null){
                    Element submitter = document.createElement("field");
                    submitter.setAttribute("name", "submitter");
                    submitter.appendChild(document.createTextNode(project.getSubmitter().getName()));
                    additionalFields.appendChild(submitter);
                }
                if(project.getSubmitter().getEmail() != null){
                    Element submitterMail = document.createElement("field");
                    submitterMail.setAttribute("name", "submitter_mail");
                    submitterMail.appendChild(document.createTextNode(project.getSubmitter().getEmail()));
                    additionalFields.appendChild(submitterMail);
                }
                if(project.getSubmitter().getAffiliation() != null){
                    Element submitterAffiliation = document.createElement("field");
                    submitterAffiliation.setAttribute("name", "submitter_affiliation");
                    submitterAffiliation.appendChild(document.createTextNode(project.getSubmitter().getAffiliation()));
                    additionalFields.appendChild(submitterAffiliation);
                }
            }

            entries.appendChild(entry);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            DOMSource source = new DOMSource(document);
            File outputXML = new File(outputDirectory, project.getRepositoryName().trim() + "_EBEYE_" + project.getAccession() + ".xml");
            StreamResult result = new StreamResult(outputXML.toURI().getPath());
            transformer.transform(source, result);
            logger.info("Finished generating EB-eye XML file for: " + outputDirectory + File.separator + "MW_EBEYE_" + project.getAccession() + ".xml" );
        }

    }
}
