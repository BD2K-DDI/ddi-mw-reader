package uk.ac.ebi.ddi.mw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import uk.ac.ebi.ddi.mw.extws.entrez.client.taxonomy.TaxonomyWsClient;
import uk.ac.ebi.ddi.mw.extws.entrez.config.TaxWsConfigProd;
import uk.ac.ebi.ddi.mw.extws.entrez.ncbiresult.NCBITaxResult;
import uk.ac.ebi.ddi.mw.extws.mw.client.DatasetWsClient;

import uk.ac.ebi.ddi.mw.extws.mw.config.MWWsConfigProd;
import uk.ac.ebi.ddi.mw.extws.mw.model.*;
import uk.ac.ebi.ddi.mw.model.Project;

import uk.ac.ebi.ddi.mw.utils.ReaderMWProject;
import uk.ac.ebi.ddi.mw.utils.WriterEBeyeXML;


import java.io.File;


/**
 * This program takes a ProteomeXchange URL and generate for all the experiments the
 *
 * @author Yasset Perez-Riverol
 */

public class GenerateMWEbeFiles {

    private static final Logger logger = LoggerFactory.getLogger(GenerateMWEbeFiles.class);

  /**
     * This program take an output folder as a parameter an create different EBE eyes files for
     * all the project in ProteomeXchange. It loop all the project in MetabolomeWorkbench and print them to the give output
     *
     * @param args
     */
    public static void main(String[] args) {

        String outputFolder = null;

        if (args != null && args.length > 0 && args[0] != null)
            outputFolder = args[0];
        else {
            System.exit(-1);
        }

        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring/app-context.xml");
        MWWsConfigProd mwWsConfigProd = (MWWsConfigProd) ctx.getBean("mwWsConfig");
        TaxWsConfigProd taxWsConfigProd = (TaxWsConfigProd) ctx.getBean("taxWsConfig");

        try {
            GenerateMWEbeFiles.generateMWXMLfiles(mwWsConfigProd,taxWsConfigProd, outputFolder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void generateMWXMLfiles( MWWsConfigProd configProd, TaxWsConfigProd taxWsConfigProd, String outputFolder) throws Exception {

        DatasetWsClient datasetWsClient = new DatasetWsClient(configProd);

        TaxonomyWsClient taxonomyWsClient = new TaxonomyWsClient(taxWsConfigProd);

        //RestTemplate rest = (RestTemplate) ctx.getBean("restTemplate");

        DatasetList datasets = datasetWsClient.getAllDatasets();

        if (datasets != null && datasets.datasets != null) {
            for (DataSet dataset : datasets.datasets.values()) {
                AnalysisList analysis = null;
                MetaboliteList metabolites = null;
                FactorList factorList = null;
                if(dataset != null && dataset.getId() != null){
                    analysis = datasetWsClient.getAnalysisInformantion(dataset.getId());
                    metabolites = datasetWsClient.getMataboliteList(dataset.getId());
                    factorList = datasetWsClient.getFactorList(dataset.getId());
                }
                if(metabolites != null && metabolites.metabolites != null && metabolites.metabolites.size() > 0)
                    metabolites = datasetWsClient.updateChebiId(metabolites);

                if(dataset != null && dataset.getSubject_species() != null){
                    NCBITaxResult texId = null;
                    try{
                        texId = taxonomyWsClient.getNCBITax(dataset.getSubject_species());
                    }catch(Exception e){
                        logger.info("Errors with the webservices on NCBI: " + e.getMessage());
                    }
                    if(texId != null &&
                            texId.getNCBITaxonomy() != null &&
                            texId.getNCBITaxonomy().length > 0 &&
                            texId.getNCBITaxonomy()[0] != null)
                        dataset.setTaxonomy(texId.getNCBITaxonomy()[0]);
                }
                Project proj = ReaderMWProject.readProject(dataset, analysis, metabolites, factorList);
                WriterEBeyeXML writer = new WriterEBeyeXML(proj, new File(outputFolder));
                writer.generate();
            }
        }
    }
}
