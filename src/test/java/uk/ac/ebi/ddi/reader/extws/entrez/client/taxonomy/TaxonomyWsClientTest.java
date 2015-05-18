package uk.ac.ebi.ddi.reader.extws.entrez.client.taxonomy;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ebi.ddi.reader.extws.entrez.config.TaxWsConfigProd;
import uk.ac.ebi.ddi.reader.extws.entrez.ncbiresult.NCBITaxResult;
import uk.ac.ebi.ddi.reader.extws.mw.client.dataset.DatasetWsClient;
import uk.ac.ebi.ddi.reader.extws.mw.config.AbstractMWWsConfig;

import static org.junit.Assert.*;

@ContextConfiguration(locations = {"/test-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)

public class TaxonomyWsClientTest {


    @Autowired
    TaxWsConfigProd taxWsConfig;

    TaxonomyWsClient taxWsClient;


    @Before
    public void setUp() throws Exception {
        taxWsClient = new TaxonomyWsClient(taxWsConfig);
    }


    @Test
    public void testGetNCBITax() throws Exception {
        NCBITaxResult result = taxWsClient.getNCBITax("Homo sapiens");
        if(result != null && result.getNCBITaxonomy() != null){
            System.out.println(result.getResult().getIdList()[0]);
        }
    }
}