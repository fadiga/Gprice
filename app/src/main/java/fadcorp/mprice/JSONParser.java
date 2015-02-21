package fadcorp.mprice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/** A class to parse json data */
public class JSONParser {

    private static final String TAG = Constants.getLogTag("JSONParser");

    // Receives a JSONObject and returns a list
    public List<HashMap<String,Object>> parse(JSONObject jObject){

        JSONArray jArticle = null;
        try {
            // Retrieves all the elements in the 'countries' array
            jArticle = jObject.getJSONArray("article");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Invoking getResources with the array of json object
        // where each json object represent a resource
        return getResource(jArticle);
    }

    private List<HashMap<String, Object>> getResource(JSONArray jResource){
        int resourceCount = jResource.length();
        List<HashMap<String, Object>> resourceList = new ArrayList<HashMap<String,Object>>();
        HashMap<String, Object> resource = null;

        // Taking each resource, parses and adds to list object
        for(int i=0; i<resourceCount;i++){
            try {
                // Call getResource with resource JSON object to parse the resource
                resource = getResource((JSONObject) jResource.get(i));
                resourceList.add(resource);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return resourceList;
    }

    // Parsing the Resource JSON object
    private HashMap<String, Object> getResource(JSONObject jResource){

        HashMap<String, Object> contents = new HashMap<String, Object>();
        String to = "";
        String from =   "";
        long v;
        long rate;
        // {"to": "XOF", "rate": 655.95699999999999, "from": "EUR", "v": 7871.4840000000004}
        try {
            to = jResource.getString(Constants.TO);
            from = jResource.getString(Constants.FROM);
            rate = jResource.getLong(Constants.RATE);
            v = jResource.getLong(Constants.VALUE);
            contents.put(Constants.TO, to);
            contents.put(Constants.FROM, from);
            contents.put(Constants.RATE, rate);
            contents.put(Constants.VALUE, v);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return contents;
    }
}