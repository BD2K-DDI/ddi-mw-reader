package uk.ac.ebi.ddi.reader.extws.mw.config;

/**
 * This class help to configure the web-service provider that would be used.
 */
public class MWWsConfigProd extends AbstractMWWsConfig {

    public MWWsConfigProd() {
        super("http", "www.metabolomicsworkbench.org");
    }
}
