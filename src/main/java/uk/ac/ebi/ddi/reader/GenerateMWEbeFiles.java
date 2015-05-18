package uk.ac.ebi.ddi.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import uk.ac.ebi.ddi.reader.extws.mw.client.dataset.DatasetWsClient;
import uk.ac.ebi.ddi.reader.extws.mw.config.AbstractMWWsConfig;
import uk.ac.ebi.ddi.reader.extws.mw.config.MWWsConfigProd;
import uk.ac.ebi.ddi.reader.extws.mw.model.dataset.DataSet;
import uk.ac.ebi.ddi.reader.extws.mw.model.dataset.DatasetList;
import uk.ac.ebi.ddi.reader.model.Project;
import uk.ac.ebi.ddi.reader.utils.ReadProperties;
import uk.ac.ebi.ddi.reader.utils.WriterEBeyeXML;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;


/**
 * This program takes a ProteomeXchange URL and generate for all the experiments the
 *
 * @author Yasset Perez-Riverol
 */

public class GenerateMWEbeFiles {

    static AbstractMWWsConfig mwWsConfig = new MWWsConfigProd();

    public static DatasetWsClient datasetWsClient;


    private static HashMap<String, String> pageBuffer = new HashMap<String, String>();

    private static final Logger logger = LoggerFactory.getLogger(GenerateMWEbeFiles.class);

  /**
     * This program take an output folder as a parameter an create different EBE eyes files for
     * all the project in ProteomeXchange. It loop all the project in MetabolomeWorkbench and print them to the give output
     *
     * @param args
     */
    public static void main(String[] args) {

        String outputFolder = null;

        if (args != null && args.length > 0 && args[0] != null)
            outputFolder = args[0];
        else {
            System.exit(-1);
        }

        try {

            datasetWsClient = new DatasetWsClient(mwWsConfig);

            DatasetList datasets = datasetWsClient.getAllDatasets();

            if (datasets != null && datasets.datasets != null) {
                for (DataSet dataset : datasets.datasets.values()) {
                    Project proj = null;
                    WriterEBeyeXML writer = new WriterEBeyeXML(proj, new File(outputFolder), null);
                    writer.generate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
