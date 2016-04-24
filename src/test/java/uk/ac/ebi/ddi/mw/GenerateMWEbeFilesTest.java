package uk.ac.ebi.ddi.mw;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.access.EjbAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ebi.ddi.mw.extws.mw.client.DatasetWsClient;
import uk.ac.ebi.ddi.mw.extws.mw.config.MWWsConfigProd;
import uk.ac.ebi.ddi.mw.extws.mw.model.*;

import static org.junit.Assert.*;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 24/04/2016
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/test-context.xml"})

public class GenerateMWEbeFilesTest {

    @Autowired
    MWWsConfigProd mwWsConfig;

    DatasetWsClient datasetWsClient;

    @Before
    public void setUp() throws Exception {
        datasetWsClient = new DatasetWsClient(mwWsConfig);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void countDatasets() throws Exception{
        DatasetList list = datasetWsClient.getAllDatasets();
        Assert.assertTrue(list.datasets.size() > 0);
    }

    @Test
    public void countSpecies() throws Exception{
        SpecieList list = datasetWsClient.getSpecies();
        Assert.assertTrue(list.species.size() > 0);
        System.out.println(list.species.values());
    }

    @Test
    public void testAnalysisData() throws  Exception{
        AnalysisList analysis = datasetWsClient.getAnalysisInformantion("ST000001");
        Assert.assertTrue(analysis.analysisMap.get("1").getInstrument_type().equalsIgnoreCase("GC-TOF"));
    }

    @Test
    public void countTissues() throws Exception{
        TissueList list = datasetWsClient.getTissues();
        Assert.assertTrue(list.tissues.size() > 0);
        System.out.println(list.tissues.values());
    }

    @Test
    public void countDiseases() throws Exception{
        DiseaseList list = datasetWsClient.getDiseases();
        Assert.assertTrue(list.diseases.size() > 0);
        System.out.println(list.diseases.values());
    }


}