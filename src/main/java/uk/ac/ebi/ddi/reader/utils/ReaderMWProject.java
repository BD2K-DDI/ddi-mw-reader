package uk.ac.ebi.ddi.reader.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.ddi.reader.extws.mw.model.dataset.*;
import uk.ac.ebi.ddi.reader.model.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Reader using SAX the XML file
 * @author ypriverol
 */
public class ReaderMWProject {

    private static final Logger logger = LoggerFactory.getLogger(ReaderMWProject.class);

    private static final String METABOLOME_REPOSITORY = "MetabolomicsWorkbench";

    private static final String METABOLOMEWORKBENCH_LINK = "http://www.metabolomicsworkbench.org/data/DRCCMetadata.php?Mode=Study&StudyID=";

    /**
     * This method read the PX summary file and return a project structure to be use by the
     * EBE exporter.
     * @return Project object model
     */
    public static Project readProject(DataSet dataset,
                                      Analysis analysis,
                                      MetaboliteList metabolites,
                                      FactorList factorList) throws Exception {
        Project proj = new Project();

        proj.setAccession(dataset.getId());
        
        proj.setRepositoryName(METABOLOME_REPOSITORY);

        proj.setTitle(dataset.getTitle());

        proj.setProjectDescription(dataset.getDescription());

        proj.setInstrument(transformInstrument(analysis));

        proj.setSpecie(transformSpecie(dataset));

        proj.setSubmitter(transformSubmitter(dataset));

        proj.setMetaboligths(metabolites);

        proj.setDatasetLink(METABOLOMEWORKBENCH_LINK + proj.getAccession());

        proj.setExperimentTypes(transformExperimentTypes(analysis.getType()));

        proj.setFactors(transformFactors(factorList));

        proj.setSubmissionDate(transformDate(dataset.getSubmit_date()));

        proj.setDataProcessingProtocol(analysis.getSummary());

        return proj;
    }

    private static Date transformDate(String submit_date) {
        DateFormat formatter = new SimpleDateFormat("yy-MM-dd");
        Date date = null;
        try {
            date = formatter.parse(submit_date);
        } catch (ParseException e) {
            logger.debug(e.getLocalizedMessage());
        }
        return date;
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

    private static List<String> transformExperimentTypes(String type) {
        String mapTerm = Synonyms.getTermBySynonym(type);
        if(mapTerm == null)
            mapTerm = type;
        List<String> experimentTypes = new ArrayList<String>();
        experimentTypes.add(mapTerm);
        return experimentTypes;
    }

    private static Set<String> transformFactors(FactorList factorList) {
        Set<String> factorListResult = new HashSet<String>();
        if(factorList != null && factorList.factors != null && factorList.factors.size()> 0){
            for(Factor factor: factorList.factors.values()){
                if(factor != null && factor.getFactors() != null){
                    factorListResult.add(factor.getFactors());
                }
            }
        }
        return factorListResult;
    }

//ist of File Name related with the Dataset
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
