package uk.ac.ebi.ddi.reader.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import uk.ac.ebi.ddi.reader.extws.mw.model.dataset.Analysis;
import uk.ac.ebi.ddi.reader.extws.mw.model.dataset.DataSet;
import uk.ac.ebi.ddi.reader.extws.mw.model.dataset.FactorList;
import uk.ac.ebi.ddi.reader.extws.mw.model.dataset.MetaboliteList;
import uk.ac.ebi.ddi.reader.model.*;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


/**
 * Reader using SAX the XML file
 * @author ypriverol
 */
public class ReaderMWProject {

    private static final Logger logger = LoggerFactory.getLogger(ReaderMWProject.class);

    private static final String METABOLOME_REPOSITORY = "Metabolomics Workbench";

    private static final String METABOLOMEWORKBENCH_LINK = "http://www.metabolomicsworkbench.org/data/DRCCMetadata.php?Mode=Study&StudyID=";

    /**
     * This method read the PX summary file and return a project structure to be use by the
     * EBE exporter.
     * @return Project object model
     */
    public static Project readProject(DataSet dataset,
                                      Analysis analysis,
                                      MetaboliteList metabolities,
                                      FactorList factorList) throws Exception {
          Project proj = new Project();

          proj.setAccession(dataset.getId());
        
          proj.setRepositoryName(METABOLOME_REPOSITORY);

          proj.setTitle(dataset.getTitle());

          proj.setProjectDescription(dataset.getDescription());

          proj.setInstrument(transformInstrument(analysis));

          proj.setSpecie(transformSpecie(dataset));

          proj.setSubmitter(transformSubmitter(dataset));
//
//
//        //Set DatasetLink
          proj.setDatasetLink(METABOLOMEWORKBENCH_LINK + proj.getAccession());

          return proj;
    }

    private static Specie transformSpecie(DataSet dataset) {
        Specie specie = null;
        if(dataset != null)
            specie = new Specie(dataset.getSubject_species(), dataset.getTaxonomy());
        return specie;
    }

    private static Submitter transformSubmitter(DataSet dataset) {
        Submitter submitter = null;
        if(dataset != null && dataset.getFirstname() != null){
            submitter = new Submitter();
            submitter.setFirstName(dataset.getFirstname());
            submitter.setLastName(dataset.getLast_name());
            submitter.setEmail(dataset.getEmail());
            submitter.setAffiliation(dataset.getDepartment(), dataset.getInstitute());
        }
        return submitter;
    }

    //
//    /**
//     * Set references for the Project
//     * @param references
//     * @return
//     */
//    private static List<Reference> transformReferences(List<PublicationType> references) {
//        List<Reference> referenceList = new ArrayList<Reference>();
//        for(PublicationType publication: references){
//            Reference ref = new Reference();
//            for(CvParamType cv: publication.getCvParam()){
//               if(cv.getAccession().equalsIgnoreCase(Constants.PUBMED_ACCESSION)){
//                   if(cv.getValue() != null && cv.getValue().length() > 0 &&
//                           StringUtils.isNumeric(cv.getValue()))
//                                   ref.setPubmedId(Integer.parseInt(cv.getValue()));
//               }
//                if(!cv.getAccession().equalsIgnoreCase(Constants.PUBMED_ACCESSION)){
//                    ref.setReferenceLine(cv.getValue());
//                }
//            }
//            if(ref.getPubmedId() != null || ref.getReferenceLine() != null)
//                referenceList.add(ref);
//        }
//        return referenceList;
//    }
//
//    private static List<CvParam> transformExperimentTypes(List<CvParamType> submitterKeywords) {
//        List<CvParam> experimentTypes = new ArrayList<CvParam>();
//        for(CvParamType cvParamType: submitterKeywords){
//            if(cvParamType.getValue().contains(Constants.SRM_KEYWORD) || cvParamType.getName().contains("SRM")){
//                CvParam cvParam = new CvParam("PRIDE:0000311","SRM/MRM", "PRIDE", "SRM/MRM");
//                experimentTypes.add(cvParam);
//            }
//        }
//        return experimentTypes;
//    }
//
//    /**
//     * Retrieve information about the experiment URL
//     * @param fullDatasetLink experiment List URL
//     * @return experiment URL
//     */
//    private static String transformGetDatasetLink(List<FullDatasetLinkType> fullDatasetLink) {
//        if(fullDatasetLink != null && fullDatasetLink.size() >0){
//            for(FullDatasetLinkType datasetLink: fullDatasetLink)
//                if(datasetLink.getCvParam().getAccession().equalsIgnoreCase(Constants.MASSIVEURL_ACCESSION) ||
//                   datasetLink.getCvParam().getAccession().equalsIgnoreCase(Constants.PASSELURL_ACCESSION))
//                    return datasetLink.getCvParam().getValue();
//        }
//        return null;
//    }
//
//    /**
//     * Retrieve the curator keywords
//     * @param submitterKeywords
//     * @return List<String> List of keywords
//     */
//    private static List<String> transformCuratorKeywords(List<CvParamType> submitterKeywords) {
//        List<String> keywords = new ArrayList<String>();
//        for(CvParamType cv: submitterKeywords)
//            if(cv.getAccession().equalsIgnoreCase(Constants.CURATORKEY_ACCESSION))
//                keywords.add(cv.getValue());
//        return keywords;
//    }
//
//    /**
//     * Read Submitter keywords
//     * @param submitterKeywords List<CvParamType>
//     * @return List of keys
//     */
//    private static List<String> transformSubmitterKeywords(List<CvParamType> submitterKeywords) {
//        List<String> keywords = new ArrayList<String>();
//        for(CvParamType cv: submitterKeywords){
//            if(cv.getAccession().equalsIgnoreCase(Constants.SUBMITTERKEY_ACCESSION))
//                keywords.add(cv.getValue());
//        }
//        return keywords;
//    }
//
//    /**
//     * Return the list of File Name related with the Dataset
//     * @param dataFiles List<DatasetFileType>
//     * @return          List<String>
//     */
//    private static List<String> transformDataFiles(List<DatasetFileType> dataFiles) {
//        List<String> files = new ArrayList<String>();
//        for(DatasetFileType file: dataFiles){
//           if(file.getCvParam() != null && file.getCvParam().size() > 0){
//               for(CvParamType cv: file.getCvParam()){
//                   files.add(cv.getValue());
//               }
//           }
//        }
//        return files;
//    }
//
//    /**
//     * Change the GregorianCalendar Date to Date
//     * @param announceDate
//     * @return
//     */
//    private static Date transformDate(XMLGregorianCalendar announceDate) {
//        return announceDate.toGregorianCalendar().getTime();
//    }
//
//    /**
//     * Select Lab Heads from the List of Contacts
//     * @param contactList
//     * @return List<Submitter>
//     */
//    private static List<Submitter> selectLabHeadsFromContacts(List<ContactType> contactList) {
//        List<Submitter> labHeads = new ArrayList<Submitter>();
//        for(ContactType contact: contactList){
//            if(contact.getCvParam() != null && contact.getCvParam().size() > 0){
//                for(CvParamType cv: contact.getCvParam()){
//                    if(cv.getAccession().equalsIgnoreCase(Constants.LABHEAD_ACCESSION)){
//                        labHeads.add(transformSubmitter(contact));
//                    }
//                }
//            }
//        }
//        return labHeads;
//    }
//
//    /**
//     * Select from a List of Contacts the Submitter
//     * @param contactList  Contact List
//     * @return Submitter
//     */
//    private static Submitter selectSubmitterFromContacts(List<ContactType> contactList) {
//        Submitter submitter = new Submitter();
//        for(ContactType contact: contactList){
//            if(contact.getCvParam() != null && contact.getCvParam().size() > 0){
//                for(CvParamType cv: contact.getCvParam()){
//                    if(cv.getAccession().equalsIgnoreCase(Constants.SUBMITTER_ACCESSION)){
//                        return transformSubmitter(contact);
//                    }
//                }
//            }
//        }
//        return submitter;
//    }
//
//    /**
//     * Transform Submitter from contact to Submitter
//     * @param contact ContactType
//     * @return  Submitter
//     */
//    private static Submitter transformSubmitter(ContactType contact) {
//        Submitter submitter = new Submitter();
//        for(CvParamType cv: contact.getCvParam()){
//            if(cv.getAccession().equalsIgnoreCase(CvTermReference.CONTACT_NAME.getAccession()))
//                submitter.setFirstName(cv.getValue());
//
//            if(cv.getAccession().equalsIgnoreCase(CvTermReference.CONTACT_EMAIL.getAccession()))
//                submitter.setEmail(cv.getValue());
//
//            if(cv.getAccession().equalsIgnoreCase(CvTermReference.CONTACT_ORG.getAccession()))
//                submitter.setAffiliation(cv.getValue());
//       }
//        return submitter;
//    }
//
//    /**
//     * Return all the taxonomies related with the File using the NCBI Taxonomy
//     * @param species all the species related with the file in CVTems
//     * @return The list of the accession in NCBI Taxonomy
//     */
//    private static List<String> transformTaxonomies(List<SpeciesType> species) {
//        List<String> taxonomies = new ArrayList<String>();
//        for(SpeciesType specie: species)
//            for(CvParamType cv: specie.getCvParam())
//                if(cv.getAccession().equalsIgnoreCase(Constants.TAXONOMY_ACCESSION))
//                    taxonomies.add(cv.getValue());
//        return taxonomies;
//    }
//
//    /**
//     * TRansform all species to CVParams
//     * @param species List of SpeciesType in PX XM file
//     * @return A List of cv
//     */
//    private static List<CvParam> transformSpecies(List<SpeciesType> species) {
//        List<CvParam> cvParams = new ArrayList<CvParam>();
//        if(species != null && species.size() > 0){
//            for(SpeciesType specie: species){
//                //Remove the species that are wrote in Taxonomy way.
//                List<CvParamType> finalCvs = removeCVParamTypeByAccession(specie.getCvParam(), Constants.TAXONOMY_ACCESSION);
//                cvParams.addAll(transformCVParamTypeList(finalCvs));
//            }
//        }
//        return cvParams;
//    }
//
//    /**
//     * THis function remove all the terms that contains an specific accession Term.
//     * @param cvParam List of all CVParamsType
//     * @param taxonomyAccession the accession ID to be removed
//     * @return The final List of terms
//     */
//    private static List<CvParamType> removeCVParamTypeByAccession(List<CvParamType> cvParam, String taxonomyAccession) {
//        List<CvParamType> cvList = new ArrayList<CvParamType>();
//        for(CvParamType cv: cvParam)
//            if(!cv.getAccession().equalsIgnoreCase(taxonomyAccession))
//              cvList.add(cv);
//        return cvList;
//    }
//
//    /**
//     * Transform a List of instruments to a List of CvParmas
//     * @param instruments List of instruments from PX submission
//     * @return List of CvParams
//     */
    private static Instrument transformInstrument(Analysis analysis) {
        Instrument instrument = null;
        if(analysis != null)
             instrument = new Instrument(analysis.getInstrument_type(), analysis.getInstrument_name());
        return instrument;
    }
//
//    /**
//     * Convert List of CVParamsType to CVparams in the model
//     * @param params List of CVParams Type
//     * @return List of CvParams
//     */
//    private static List<CvParam> transformCVParamTypeList(List<CvParamType> params){
//        List<CvParam> cvParams = new ArrayList<CvParam>();
//        for(CvParamType cv: params){
//            CvParam param = new CvParam(cv.getAccession(), cv.getName(), cv.getUnitName(),cv.getValue());
//            cvParams.add(param);
//        }
//        return cvParams;
//    }


}
