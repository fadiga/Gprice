package fadcorp.mprice;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import fadcorp.mprice.Constants;
import fadcorp.mprice.Utils;


public class GetJsonDevises extends AsyncTask<String, Void, Void> {

    private static final String TAG = Constants.getLogTag("GetJsonAndUpdateArticleData");

    private final Activity context;
    private ProgressDialog progressDialog;
    boolean isOnline;

    JSONObject jObject;
    String data = null;
    String data1 = null;
    private String to;
    private String from;
    private int priceV;
    private Object rate;
    private Object v;

    public GetJsonDevises(Activity applicationContext) {
         context = applicationContext;
    }

    @Override
    protected void onPreExecute() {
        // Loading
        isOnline = Utils.isOnline(context);
        if (!isOnline) {
            Utils.toast(context, R.string.required_connexion_body);
            return;
        } else {
            progressDialog = Utils.getStandardProgressDialog(context,
                    "", context.getString(R.string.loading), false);
            progressDialog.show();
        }
    }

    @Override
    protected Void doInBackground(String... params) {
        String defaultDv = params[0];
        String value = params[1];
        try {
            final String devices [] = {"XOF","EUR", "Dolar"};
            for(String devise: devices) {
                if (devise != defaultDv) {
                    String url = Utils.formatUrl(defaultDv, "XOF", value);
                    String url1 = Utils.formatUrl(defaultDv, "XOF", value);
                    data = Utils.getFromUrl(url);
                    data1 = Utils.getFromUrl(url1);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "doInBackground Exception" + e + "\nLe lien (url) vers la liste des articles est mort.");
            return null;
        }
        try {
            jObject = new JSONObject(data);
        } catch (JSONException e) {
            Log.d(TAG, "JSONException " + e.toString());
        }
        try {
            Log.d(TAG, (String) jObject.get(Constants.TO));
            to = jObject.get(Constants.TO).toString();
            from = jObject.get(Constants.FROM).toString();
            rate = jObject.get(Constants.RATE);
            v = jObject.get(Constants.VALUE);
            //Log.d(TAG, (String) v);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        if (isOnline) {
            try {
                if ((progressDialog != null) && progressDialog.isShowing()) {
                    //progressDialog.dismiss();
                }
            } catch (final Exception e) {
                progressDialog = null;
            }

            AlertDialog alertDialog = new AlertDialog.Builder( context).create();
            alertDialog.setTitle("Convertion de " + "");
            alertDialog.setMessage(from + " --> " + to + "=" + v);
            alertDialog.setIcon(R.drawable.ic_launcher);
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Write your code here to execute after dialog closed
                    //Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
                }
            });
            // Showing Alert Message
            alertDialog.show();
        }
    }
}