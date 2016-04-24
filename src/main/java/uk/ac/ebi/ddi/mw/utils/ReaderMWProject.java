package uk.ac.ebi.ddi.mw.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.ddi.mw.extws.mw.model.*;
import uk.ac.ebi.ddi.mw.model.Instrument;
import uk.ac.ebi.ddi.mw.model.Project;
import uk.ac.ebi.ddi.mw.model.Submitter;


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
                                      AnalysisList analysis,
                                      MetaboliteList metabolites,
                                      FactorList factorList,
                                      Set<uk.ac.ebi.ddi.mw.extws.mw.model.Specie> species,
                                      Set<String> tissues, Set<String> diseases) throws Exception {
        Project proj = new Project();

        proj.setAccession(dataset.getId());
        
        proj.setRepositoryName(METABOLOME_REPOSITORY);

        proj.setTitle(dataset.getTitle());

        proj.setProjectDescription(dataset.getDescription());

        proj.setInstrument(transformInstrument(analysis));

        proj.setTaxonomies(transformTaxonomies(dataset));

        proj.setSpecies(transformSpecies(species));

        proj.setSubmitter(transformSubmitter(dataset));

        proj.setMetaboligths(metabolites);

        proj.setDatasetLink(METABOLOMEWORKBENCH_LINK + proj.getAccession());

        proj.setExperimentTypes(transformExperimentTypes(analysis));

        proj.setFactors(transformFactors(factorList));

        proj.setSubmissionDate(transformDate(dataset.getSubmit_date()));

        proj.setDataProcessingProtocol(transformDataProcessing(analysis));

        proj.setTissues(tissues);

        proj.setDiseases(diseases);

        return proj;
    }

    /**
     * We will use the latin number for each dataset but also the Common name
     * as Metabolome workbench export.
     * @param species
     * @return
     */
    private static List<String> transformSpecies(Set<Specie> species) {
        List<String> speciesResult = new ArrayList<String>();
        if(species != null && !species.isEmpty())
            for(Specie specie: species){
                if(specie.getLantinName() != null)
                    speciesResult.add(specie.getLantinName());
                if(specie.getName() != null)
                    speciesResult.add(specie.getName());
            }

        return speciesResult;
    }

    private static List<String> transformDataProcessing(AnalysisList analysisList) {
        Set<String> experimentTypes = new HashSet<String>();
        if(analysisList != null && analysisList.analysisMap != null && analysisList.analysisMap.size() > 0){
            for(Analysis analysis: analysisList.analysisMap.values()){
               experimentTypes.add(analysis.getSummary());
            }
        }
        return new ArrayList<String>(experimentTypes);
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

    private static List<String> transformTaxonomies(DataSet dataset) {
        List<String> taxonomies = new ArrayList<String>();
         if(dataset != null && dataset.getTaxonomy() != null && !dataset.getTaxonomy().isEmpty())
             for(String taxonomy: dataset.getTaxonomy())
                taxonomies.add(taxonomy);
        return taxonomies;
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

    private static List<String> transformExperimentTypes(AnalysisList analysisList) {
        Set<String> experimentTypes = new HashSet<String>();
        if(analysisList != null && analysisList.analysisMap != null && analysisList.analysisMap.size() > 0){
            for(Analysis analysis: analysisList.analysisMap.values()){
                String mapTerm = Synonyms.getTermBySynonym(analysis.getType());
                if(mapTerm == null)
                    mapTerm = analysis.getInstrument_type();
                experimentTypes.add(mapTerm);
            }
        }
        return new ArrayList<String>(experimentTypes);
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

    private static Set<Instrument> transformInstrument(AnalysisList analysis) {
        Set<Instrument> instruments = new HashSet<Instrument>();
        if(analysis != null && analysis.analysisMap != null && analysis.analysisMap.size() >0){
            for(Analysis analysisValue: analysis.analysisMap.values()){
                if(analysisValue != null && (analysisValue.getInstrument_name() != null || analysisValue.getInstrument_type() != null)){
                    Instrument instrument = new Instrument(analysisValue.getInstrument_type(), analysisValue.getInstrument_name());
                    instruments.add(instrument);
                }
            }
        }
        return instruments;
    }



}
