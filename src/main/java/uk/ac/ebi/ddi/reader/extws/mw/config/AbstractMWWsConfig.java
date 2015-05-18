package uk.ac.ebi.ddi.reader.extws.mw.config;

/**
 * @author jadianes
 * @author ypriverol
 *
 */
public abstract class AbstractMWWsConfig {

    private String hostName;
    private String protocol;

    protected AbstractMWWsConfig(String protocol, String hostName) {
        this.hostName = hostName;
        this.protocol = protocol;
    }

    public String getHostName() {
        return hostName;
    }


    public String getProtocol() {
        return protocol;
    }

}
