package uk.ac.ebi.ddi.mw.extws.mw.client;

import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.ddi.mw.extws.mw.config.AbstractMWWsConfig;

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
        this.restTemplate = new RestTemplate(clientHttpRequestFactory());
    }

    private ClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(200000);
        factory.setConnectTimeout(20000);
        return factory;
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
