package uk.ac.ebi.ddi.reader.extws.entrez.client.taxonomy;

import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.ddi.reader.extws.entrez.config.TaxWsConfigProd;

/**
 * Abstract client to query The Tax NCBI to get the Id
 *
 * @author ypriverol
 */
public class WsClient {

    protected RestTemplate restTemplate;
    protected TaxWsConfigProd config;

    /**
     * Default constructor for Archive clients
     * @param config
     */
    public WsClient(TaxWsConfigProd config){
        this.config = config;
        this.restTemplate = new RestTemplate();
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public TaxWsConfigProd getConfig() {
        return config;
    }

    public void setConfig(TaxWsConfigProd config) {
        this.config = config;
    }
}
