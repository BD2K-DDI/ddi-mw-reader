package uk.ac.ebi.ddi.mw.extws.mw.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import uk.ac.ebi.ddi.mw.utils.Utilities;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 24/04/2016
 */
public class TissueList {

    public Map<String, Tissue> tissues = new HashMap<String, Tissue>();

    @JsonAnyGetter
    public Map<String, Tissue> any() {
        return tissues;
    }

    @JsonAnySetter
    public void set(String name, Tissue value) {
        tissues.put(name, value);
    }

    public boolean hasUnknowProperties() {
        return !tissues.isEmpty();
    }

    public Set<String> getTissuesByDataset(String id) {
        Set<String> tissuesResult = new HashSet<String>();
        if(tissues != null && !tissues.isEmpty())
            for(Tissue tissue: tissues.values())
                if(tissue.getStudyId().equalsIgnoreCase(id))
                    tissuesResult.add(Utilities.toTitleCase(tissue.getTissue()));
        return tissuesResult;
    }
}
