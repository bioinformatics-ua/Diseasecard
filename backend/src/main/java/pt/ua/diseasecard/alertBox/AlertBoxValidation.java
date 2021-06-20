package pt.ua.diseasecard.alertBox;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pt.ua.diseasecard.components.data.SparqlAPI;
import pt.ua.diseasecard.components.data.Storage;
import pt.ua.diseasecard.configuration.DiseasecardProperties;
import pt.ua.diseasecard.utils.Predicate;
import pt.ua.diseasecard.utils.PrefixFactory;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;


@Component
public class AlertBoxValidation {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private SparqlAPI sparqlAPI;
    private Storage storage;
    private HashMap<String, String> sourceBaseURLs;
    private Model model;
    private DiseasecardProperties config;


    public AlertBoxValidation(SparqlAPI sparqlAPI, Storage storage, DiseasecardProperties config) {
        this.sparqlAPI = sparqlAPI;
        this.storage = storage;
        this.model = storage.getModel();
        this.config = config;
    }


    public void searchInvalidItems() {
        if (this.config.getDebug()) java.util.logging.Logger.getLogger(AlertBoxValidation.class.getName()).log(Level.INFO,"[Diseasecard][AlertBoxSchedule] Searching Invalid Items at " + dateFormat.format(new Date()) );

        this.storage.removeSourceBaseURLsErrors();
        this.storage.updateDateOfBeginValidation();
        this.getSourcesBaseURLs();

        Resource itemCategory = this.model.getResource(PrefixFactory.getURIForPrefix(this.config.getKeyprefix()) + "Item");
        StmtIterator iter = this.model.listStatements(null, Predicate.get("rdf:type"), itemCategory);

        ExecutorService executorService = Executors.newFixedThreadPool(64);

        while(iter.hasNext()) {
            String itemUri = iter.nextStatement().getSubject().toString();

            String[] info = itemUri.substring(itemUri.lastIndexOf("/")).replace("/", "").split("_", 2);

            String finalURL = this.sourceBaseURLs.get(info[0].toLowerCase()).replace("#replace#", info[1]);

            if (this.config.getDebug()) java.util.logging.Logger.getLogger(AlertBoxValidation.class.getName()).log(Level.INFO,"[Diseasecard][AlertBoxSchedule] Testing URL: " + finalURL);

            executorService.submit(() -> {
                int responseCode = this.validateEndpoint(finalURL, info);

                if (responseCode != 200) {
                    this.storage.saveSourceBaseURLsError(info[0], info[1], finalURL, responseCode+"");
                }
            });
        }

        try {
            executorService.shutdown();
            executorService.awaitTermination(1, TimeUnit.DAYS);
            if (this.config.getDebug()) java.util.logging.Logger.getLogger(AlertBoxValidation.class.getName()).log(Level.INFO,"[Diseasecard][AlertBoxSchedule] Finished Items Validation Process at " + dateFormat.format(new Date()) );
            this.storage.updateDateOfLastValidation();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    /*
        With the specified cron we are telling the program to execute this method at 0am of the 15º and last day of each month.
        The cron annotation follows this structure:
            - second, minute, hour, day, month, weekday
     */

    //@Scheduled(cron = "0 0 0 1,15 * ?" )
    @Scheduled(fixedRate = 500000000 )
    public void lightSearch() {

        /*
            TODO:
                - Select 10/15 items per resource;
                - Test the endpoints;
                - Save errors, if any;
         */

    }


    public JSONObject diseaseRealTimeValidation(JSONObject disease) {

        System.out.println(disease);

        /*
            TODO:
                - Iterate over the items associated to the disease - network;
                - Remove the ones that are invalid;
                - Resend disease;

            Disease Structure:
                {
                    "phenotype": "true",
                    "size": 2,
                    "omim": "123730",
                    "synonyms": [
                        "Cataract, progressive polymorphic cortical (3)  "
                    ],
                    "description": "Crystallin, gamma S",
                    "location": "3q27",
                    "genotype": "true",
                    "network": [
                        "HGNC:2417",
                        "OMIM:123730"
                    ]
                }
         */

        JSONArray network = (JSONArray) disease.get("network");

        ExecutorService executorService = Executors.newFixedThreadPool(64);

        network.forEach( value -> {
            String[] info = value.toString().split(":");
            String finalURL = this.sourceBaseURLs.get(info[0].toLowerCase()).replace("#replace#", info[1]);

            executorService.submit(() -> {
                int responseCode = this.validateEndpoint(finalURL, info);

                if (responseCode != 200) {
                    //TODO: remove from network
                }
            });
        });

        try {
            executorService.shutdown();
            executorService.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



        return disease;
    }


    private int validateEndpoint(String finalURL, String[] info) {

        int responseCode = 0;

        try
        {
            URL url = new URL(finalURL);

            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            huc.setRequestProperty("User-Agent", "Mozilla/5.0 AppleWebKit/537.36 Chrome/65.0.3325.181 Safari/537.36");
            huc.setRequestMethod("HEAD");
            huc.setRequestProperty("Accept", "*/*");
            responseCode = huc.getResponseCode();

        }
        catch (Exception e)
        {
            System.out.println("Error with URL " + finalURL);
            this.storage.saveSourceBaseURLsError(info[0], info[1], finalURL, e.getClass().getSimpleName());
            e.printStackTrace();
        }

        return responseCode;
    }


    private void getSourcesBaseURLs() {
        this.sourceBaseURLs = new HashMap<>();

        try
        {
            JSONParser parser = new JSONParser();
            JSONObject response = (JSONObject) parser.parse(this.sparqlAPI.select("SELECT *"
                    + " WHERE { ?s rdf:type coeus:SourceBaseURL ."
                    + " ?s rdfs:label ?source ."
                    + " ?s coeus:baseURL ?url } ", "js", false));

            JSONObject results = (JSONObject) response.get("results");
            JSONArray bindings = (JSONArray) results.get("bindings");

            for (Object o : bindings) {
                JSONObject binding = (JSONObject) o;
                sourceBaseURLs.put(((JSONObject) binding.get("source")).get("value").toString(), ((JSONObject) binding.get("url")).get("value").toString());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

//        sourceBaseURLs.entrySet().forEach(entry -> {
//            System.out.println(entry.getKey() + " " + entry.getValue());
//        });

    }
}
