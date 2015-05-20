package uk.ac.ebi.ddi.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import uk.ac.ebi.ddi.reader.extws.entrez.client.taxonomy.TaxonomyWsClient;
import uk.ac.ebi.ddi.reader.extws.entrez.config.TaxWsConfigProd;
import uk.ac.ebi.ddi.reader.extws.entrez.ncbiresult.NCBITaxResult;
import uk.ac.ebi.ddi.reader.extws.mw.client.dataset.DatasetWsClient;

import uk.ac.ebi.ddi.reader.extws.mw.config.MWWsConfigProd;
import uk.ac.ebi.ddi.reader.extws.mw.model.dataset.*;
import uk.ac.ebi.ddi.reader.model.Project;

import uk.ac.ebi.ddi.reader.utils.ReaderMWProject;
import uk.ac.ebi.ddi.reader.utils.WriterEBeyeXML;


import java.io.File;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * This program takes a ProteomeXchange URL and generate for all the experiments the
 *
 * @author Yasset Perez-Riverol
 */

public class GenerateMWEbeFiles {

    private static HashMap<String, String> pageBuffer = new HashMap<String, String>();

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
            DatasetWsClient datasetWsClient = new DatasetWsClient(mwWsConfigProd);

            TaxonomyWsClient taxonomyWsClient = new TaxonomyWsClient(taxWsConfigProd);

            //RestTemplate rest = (RestTemplate) ctx.getBean("restTemplate");

            DatasetList datasets = datasetWsClient.getAllDatasets();

            if (datasets != null && datasets.datasets != null) {
                for (DataSet dataset : datasets.datasets.values()) {
                    Analysis analysis = null;
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
                        NCBITaxResult texId = taxonomyWsClient.getNCBITax(dataset.getSubject_species());
                        if(texId != null &&
                                texId.getNCBITaxonomy() != null &&
                                texId.getNCBITaxonomy().length > 0 &&
                                texId.getNCBITaxonomy()[0] != null)
                                dataset.setTaxonomy(texId.getNCBITaxonomy()[0]);
                    }
                    Project proj = ReaderMWProject.readProject(dataset, analysis, metabolites, factorList);
                    WriterEBeyeXML writer = new WriterEBeyeXML(proj, new File(outputFolder), null);
                    writer.generate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
