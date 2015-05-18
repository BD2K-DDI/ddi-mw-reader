package uk.ac.ebi.ddi.reader.extws.mw.client.dataset;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ebi.ddi.reader.extws.mw.model.dataset.DatasetList;
import uk.ac.ebi.ddi.reader.extws.mw.config.AbstractMWWsConfig;

@ContextConfiguration(locations = {"/test-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)

public class DatasetWsClientTest {

    @Autowired
    AbstractMWWsConfig mwWsConfig;

    DatasetWsClient datasetWsClient;


    @Before
    public void setUp() throws Exception {
        datasetWsClient = new DatasetWsClient(mwWsConfig);
    }

    @Test
    public void testGetAllDatasets() throws Exception {

        DatasetList datasets = datasetWsClient.getAllDatasets();
        Assert.assertTrue((datasets.datasets.size() > 0));


    }
}