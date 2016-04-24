package uk.ac.ebi.ddi.mw.extws.mw.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 24/04/2016
 */
public class SpecieList {

    public Map<String, Specie> species = new HashMap<String, Specie>();

    @JsonAnyGetter
    public Map<String, Specie> any() {
        return species;
    }

    @JsonAnySetter
    public void set(String name, Specie value) {
        species.put(name, value);
    }

    public boolean hasUnknowProperties() {
        return !species.isEmpty();
    }

    public Set<Specie> getSpeciesByDataset(String id) {
        Set<Specie> tissuesResult = new HashSet<Specie>();
        if(species != null && !species.isEmpty())
            for(Specie specie: species.values())
                if(specie.getStudyId().equalsIgnoreCase(id))
                    tissuesResult.add(specie);
        return tissuesResult;
    }
}
