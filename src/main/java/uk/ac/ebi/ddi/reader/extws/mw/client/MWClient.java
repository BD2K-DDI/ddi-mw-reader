package uk.ac.ebi.ddi.reader.extws.mw.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.ddi.reader.extws.mw.config.AbstractMWWsConfig;

/**
 * Abstract client to query the EBI search.
 *
 * @author ypriverol
 */
public class MWClient {

    protected RestTemplate restTemplate;

    protected AbstractMWWsConfig config;

    /**
     * Default constructor for Archive clients
     * @param config
     */
    public MWClient(AbstractMWWsConfig config){
        this.config = config;
        this.restTemplate = new RestTemplate();
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public AbstractMWWsConfig getConfig() {
        return config;
    }

    public void setConfig(AbstractMWWsConfig config) {
        this.config = config;
    }
}
