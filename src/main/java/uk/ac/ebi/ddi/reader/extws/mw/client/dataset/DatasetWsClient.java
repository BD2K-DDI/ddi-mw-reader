package uk.ac.ebi.ddi.reader.extws.mw.client.dataset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.ebi.ddi.reader.extws.mw.model.dataset.DatasetList;
import uk.ac.ebi.ddi.reader.extws.mw.client.MWClient;
import uk.ac.ebi.ddi.reader.extws.mw.config.AbstractMWWsConfig;


/**
 * @author Yasset Perez-Riverol ypriverol
 */
public class DatasetWsClient extends MWClient{

    private static final Logger logger = LoggerFactory.getLogger(DatasetWsClient.class);

    /**
     * Default constructor for Ws clients
     *
     * @param config
     */
    public DatasetWsClient(AbstractMWWsConfig config) {
        super(config);
    }

    /**
     * Returns the Datasets from MtabolomeWorbench
     * @return A list of entries and the facets included
     */
    public DatasetList getAllDatasets(){

        String url = String.format("%s://%s/rest/study/study_id/ST/summary",
                config.getProtocol(), config.getHostName());
        //Todo: Needs to be removed in the future, this is for debugging
        logger.debug(url);

        return this.restTemplate.getForObject(url, DatasetList.class);
    }


}
