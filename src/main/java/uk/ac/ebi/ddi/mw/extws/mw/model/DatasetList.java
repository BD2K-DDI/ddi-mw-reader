package uk.ac.ebi.ddi.mw.extws.mw.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 18/05/2015
 */
@JsonIgnoreProperties(ignoreUnknown = true)

public class DatasetList {

    public Map<String, DataSet> datasets = new HashMap<String, DataSet>();

    @JsonAnyGetter
    public Map<String, DataSet> any() {
        return datasets;
    }

    @JsonAnySetter
    public void set(String name, DataSet value) {
        datasets.put(name, value);
    }

    public boolean hasUnknowProperties() {
        return !datasets.isEmpty();
    }

}
