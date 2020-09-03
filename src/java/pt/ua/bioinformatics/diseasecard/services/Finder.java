package pt.ua.bioinformatics.diseasecard.services;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.json.JSONArray;
import org.json.JSONObject;
import pt.ua.bioinformatics.coeus.api.DB;
import pt.ua.bioinformatics.coeus.common.Boot;
import pt.ua.bioinformatics.coeus.common.Config;
import pt.ua.bioinformatics.diseasecard.domain.Disease;
import pt.ua.bioinformatics.diseasecard.domain.SearchResult;

/**
 *
 * Helper methods to load disease information, lists and other miscellaneous
 * topics. Finds everything.
 *
 * @author pedrolopes
 */
public class Finder {

    private String query;
    private Matcher match_omim_id;
    private Pattern omimid = Pattern.compile("[0-9]{6}");
    private DB db = new DB("DC4");
    private JSONObject result = new JSONObject();
    private HashMap<Integer, Disease> map = new HashMap<Integer, Disease>();
    private HashMap<String, ArrayList<String>> results = new HashMap<String, ArrayList<String>>();
    private LinkedHashMap<String, SearchResult> network = new LinkedHashMap<String, SearchResult>();

    public Finder() {
    }
    
    public Finder(String query) {
        this.query = query;
        Boot.start();
    }

    public HashMap<String, ArrayList<String>> getResults() {
        return results;
    }

    public void setResults(HashMap<String, ArrayList<String>> results) {
        this.results = results;
    }

    public DB getDb() {
        return db;
    }

    public void setDb(DB db) {
        this.db = db;
    }

    public HashMap<Integer, Disease> getMap() {
        return map;
    }

    public void setMap(HashMap<Integer, Disease> map) {
        this.map = map;
    }

    public Matcher getMatch_omim_id() {
        return match_omim_id;
    }

    public void setMatch_omim_id(Matcher match_omim_id) {
        this.match_omim_id = match_omim_id;
    }

    public Pattern getOmimid() {
        return omimid;
    }

    public void setOmimid(Pattern omimid) {
        this.omimid = omimid;
    }

    public JSONObject getResult() {
        return result;
    }

    public void setResult(JSONObject result) {
        this.result = result;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    /**
     * Finds disease matches in COEUS S3DB.
     * <p>
     * Used in search. </p>
     *
     * @return
     */
    public String find(String type) {
        if (query.length() > 3) {
            ModifiableSolrParams params = new ModifiableSolrParams();
            if (type.equals("id")) {
                //params.set("q", "id:*\"" + query.toLowerCase() + "\"* OR id:*\"" + WordUtils.capitalize(query.toLowerCase()) + "\"*");
                params.set("q", "title:*" + query + "*");
                params.set("rows", 512);
            } else if (type.equals("full")) {
                params.set("q", query + "*");
                params.set("rows", 1024);
            }
            try {
                SolrServer server = new HttpSolrServer(Config.getIndex());
                QueryResponse response = server.query(params);
                SolrDocumentList docs = response.getResults();
                if (docs.isEmpty()) {
                    result.put("status", 110);
                } else if (docs.size() == 1) {
                    result.put("status", 121);
                } else {
                    result.put("status", 122);
                }
                JSONArray mtp = new JSONArray();

                for (SolrDocument sol : docs) {
                    if (!network.containsKey(sol.get("omim").toString())) {
                        SearchResult sr = new SearchResult(sol.get("omim").toString());
                        sr.setName(Boot.getAPI().getOmimName(sol.get("omim").toString()));
                        if (sol.get("id").toString().contains("name")) {
                            sr.getAlias().add(sol.get("id").toString());
                        } else {
                            sr.getNetwork().add(sol.get("id").toString());
                        }
                        network.put(sol.get("omim").toString(), sr);

                    } else {
                        network.get(sol.get("omim").toString()).getNetwork().add(sol.get("id").toString());
                    }
                }

                int size = 0;
                for (SearchResult s : network.values()) {
                    JSONObject o = new JSONObject();
                    if (!s.getName().equals("")) {
                        o.put("name", s.getName());
                        o.put("omim", Integer.parseInt(s.getOmim()));
                        JSONArray alias = new JSONArray();
                        for (String link : s.getAlias()) {
                            alias.put(link);
                            size++;
                        }
                        JSONArray links = new JSONArray();
                        for (String link : s.getNetwork()) {
                            if (!link.contains("hpo")) {
                                links.put(link);
                                size++;
                            }
                        }
                        o.put("alias", alias);
                        o.put("links", links);
                        mtp.put(o);
                    }
                }
                result.put("size", size);
                result.put("results", mtp);
            } catch (Exception ex) {
                if (Config.isDebug()) {
                    System.out.println("[COEUS][Diseasecard][Finder] Unable to find disease");
                    Logger.getLogger(Finder.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            try {
                result.put("status", 140);
            } catch (Exception ex) {
                if (Config.isDebug()) {
                    System.out.println("[COEUS][Diseasecard][Finder] Unable to find disease");
                    Logger.getLogger(Finder.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        //return (ArrayList<Disease>) new ArrayList(map.values());
        return result.toString();
    }

    /**
     * Finds disease matches in DC4 index.
     * <p>
     * Used in Autocomplete. </p>
     *
     * @return
     */
    public String get(String type) {
        JSONArray list = new JSONArray();
        ModifiableSolrParams params = new ModifiableSolrParams();

        //arams.set("q", "id:*\"" + query.toLowerCase() + "\"* OR id:*\"" + WordUtils.capitalize(query.toLowerCase()) + "\"*");
        params.set("q", "title:*" + query + "*");
        params.set("rows", 254);

        try {
            String s = Config.getIndex();
            SolrServer server = new HttpSolrServer(Config.getIndex());
            QueryResponse response = server.query(params);
            SolrDocumentList docs = response.getResults();
            for (SolrDocument sol : docs) {
                JSONObject obj = new JSONObject();
                obj.put("info", sol.get("title").toString());
                obj.put("omim", Integer.parseInt(sol.get("omim").toString()));
                list.put(obj);
            }
            result.put("results", list);
        } catch (Exception ex) {
            if (Config.isDebug()) {
                System.out.println("[COEUS][Diseasecard][Finder] Unable to get disease");
                Logger.getLogger(Finder.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return list.toString();
    }

    /**
     * Load JSON object with activity items (why is it called status?).
     *
     * @param limit
     * @return
     */
    public String status(int limit) {
        JSONObject response = new JSONObject();
        JSONArray list = new JSONArray();
        PreparedStatement p;
        try {
            db.connect(DC4.getIndexString());
            p = db.getConnection().prepareStatement("SELECT ts, action, query, ip FROM Activity ORDER BY ts DESC LIMIT ? ;");
            p.setInt(1, limit);
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                JSONArray o = new JSONArray();
                o.put(rs.getString("ts"));
                o.put(rs.getString("action"));
                o.put(rs.getString("query"));
                o.put(rs.getString("ip"));
                list.put(o);
            }
            db.close();
            response.put("aaData", list);
        } catch (Exception ex) {
            if (Config.isDebug()) {
                System.out.println("[COEUS][Diseasecard][Finder] Unable to load status data info");
                Logger.getLogger(Finder.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return response.toString();
    }

    /**
     *
     * Loads JSON object with disease browsing list based on initial char.
     *
     * @param key First character for disease name.
     * @return
     */
    public String browse(String key) {
        JSONObject alldiseases = new JSONObject();
        JSONArray list = new JSONArray();
        PreparedStatement p;
        try {
            db.connect(DC4.getIndexString());
            p = db.getConnection().prepareStatement("SELECT * FROM Diseases WHERE name REGEXP ? ORDER BY name ASC;");
            if (key.equals("0")) {
                p.setString(1, "^[0-9\\[\\{].*");
            } else {
                p.setString(1, "^[" + key + "].*");
            }
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                JSONArray o = new JSONArray();
                o.put("<a rel=\"tooltip\" title=\"View " + rs.getString("name") + "\" href=\"./entry/" + rs.getInt("omim") + "\">" + rs.getInt("omim") + "</a>");
                o.put("<i class=\"icon-angle-right\"></i> <a rel=\"tooltip\" title=\"View " + rs.getString("name") + "\" href=\"./entry/" + rs.getInt("omim") + "\">" + rs.getString("name") + "</a><a href=\"http://omim.org/entry/" + rs.getInt("omim") + "\" class=\"pull-right\" target=\"_blank\"><i class=\"icon-external-link\"></i></a>");
                double progress = (rs.getInt("c") / 1127.0) * 100;
                String type;
                if (progress > 30.0) {
                    type = "";
                } else if (progress > 20.0) {
                    type = " progress-bar-info";
                } else if (progress > 15.0) {
                    type = " progress-bar-success";
                } else if (progress > 10.0) {
                    type = " progress-bar-warning";
                } else {
                    progress = 10.0;
                    type = " progress-bar-danger";
                }
                o.put("<div class=\"progress\"><div rel=\"tooltip\" title=\"" + rs.getInt("c") + " connections\" class=\"progress-bar " + type + "\" role=\"progressbar\" style=\"width: " + progress + "%;\">" + rs.getInt("c") + "</div></div>");
                list.put(o);
            }
            db.close();
            alldiseases.put("aaData", list);
        } catch (Exception ex) {
            if (Config.isDebug()) {
                System.out.println("[COEUS][Diseasecard][Finder] Unable to get diseases info");
                Logger.getLogger(Finder.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return alldiseases.toString();
    }

    /**
     *
     * Query tester for Solr indexer.
     *
     * @return
     */
    public String query() {
        String response = null;
        try {
            SolrServer server = new HttpSolrServer(Config.getIndex());
            ModifiableSolrParams params = new ModifiableSolrParams();
            params.set("q", query);
            params.set("rows", 1000);
            params.set("defType", "edismax");
            params.set("qf", "content^0.2 title^0.9 id^1");
            params.set("wt", "json");
            response = server.query(params).toString();
        } catch (Exception ex) {
            if (Config.isDebug()) {
                System.out.println("[COEUS][Diseasecard][Finder] Unable to get disease");
                Logger.getLogger(Finder.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return response;
    }
}
