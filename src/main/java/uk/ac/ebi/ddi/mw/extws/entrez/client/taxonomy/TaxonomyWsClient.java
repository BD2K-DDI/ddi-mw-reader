package uk.ac.ebi.ddi.mw.extws.entrez.client.taxonomy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.ddi.mw.extws.entrez.config.TaxWsConfigProd;
import uk.ac.ebi.ddi.mw.extws.entrez.ncbiresult.NCBITaxResult;


/**
 * @author Yasset Perez-Riverol ypriverol
 */
public class TaxonomyWsClient extends WsClient {

    private static final Logger logger = LoggerFactory.getLogger(TaxonomyWsClient.class);

    /**
     * Default constructor for Ws clients
     *
     * @param config
     */
    public TaxonomyWsClient(TaxWsConfigProd config) {
        super(config);
    }

    public NCBITaxResult getNCBITax(String term){

        if(term != null && term.length() > 0){
            String url = String.format("%s://%s/entrez/eutils/esearch.fcgi?db=taxonomy&term=%s&retmode=JSON",
                    config.getProtocol(), config.getHostName(), term);
            //Todo: Needs to be removed in the future, this is for debugging
            logger.debug(url);

            return this.restTemplate.getForObject(url, NCBITaxResult.class);
        }
        return null;

    }


}
