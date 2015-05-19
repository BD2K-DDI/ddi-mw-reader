package uk.ac.ebi.ddi.reader.extws.mw.client.dataset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.ebi.ddi.reader.extws.mw.model.dataset.*;
import uk.ac.ebi.ddi.reader.extws.mw.client.MWClient;
import uk.ac.ebi.ddi.reader.extws.mw.config.AbstractMWWsConfig;


import java.util.Map;


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

    public Analysis getAnalysisInformantion(String id){

        String url = String.format("%s://%s/rest/study/study_id/%s/analysis",
                config.getProtocol(), config.getHostName(), id);
        //Todo: Needs to be removed in the future, this is for debugging
        logger.debug(url);

        return this.restTemplate.getForObject(url, Analysis.class);

    }

    public MetaboliteList getMataboliteList(String id){

        String url = String.format("%s://%s/rest/study/study_id/%s/metabolites",
                config.getProtocol(), config.getHostName(), id);
        //Todo: Needs to be removed in the future, this is for debugging
        logger.debug(url);
        MetaboliteList metaboliteList = null;
        try{
            metaboliteList = this.restTemplate.getForObject(url, MetaboliteList.class);
        }catch(Exception e){
            logger.debug(e.getLocalizedMessage());
        }
        return metaboliteList;
    }

    public ChebiID getChebiId(String pubchemId){

        String url = String.format("%s://%s/rest/compound/pubchem_cid/%s/chebi_id/",
                config.getProtocol(), config.getHostName(), pubchemId);
        //Todo: Needs to be removed in the future, this is for debugging

        logger.debug(url);
        ChebiID id = null;
        try{
            id =this.restTemplate.getForObject(url, ChebiID.class);
        }catch(Exception e){
            logger.debug(e.getLocalizedMessage());
        }

        return id;
    }

    public MetaboliteList updateChebiId(MetaboliteList metabolites){
        if(metabolites != null && metabolites.metabolites != null && metabolites.metabolites.size() > 0){
            System.out.println(metabolites.metabolites.size());
            for(Map.Entry entry: metabolites.metabolites.entrySet()){
                String key = (String) entry.getKey();
                Metabolite met = (Metabolite) entry.getValue();
                if(met != null && met.getPubchem() != null){
                    ChebiID id = getChebiId(met.getPubchem());
                    if(id != null)
                        met.setChebi(id.getChebi_id());
                }
                metabolites.metabolites.put(key,met);
            }
        }
        return metabolites;
    }


}
