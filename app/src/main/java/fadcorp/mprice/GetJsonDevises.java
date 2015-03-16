package fadcorp.mprice;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;


public class GetJsonDevises extends AsyncTask<String, Void, Void> {

    private static final String TAG = Constants.getLogTag("GetJsonAndUpdateArticleData");

    private final Activity context;
    private ProgressDialog progressDialog;
    private Dialog dialog_;
    boolean isOnline;

    JSONObject jObject;
    String data = null;
    private String to;
    private String from;
    private String priceV;
    private Object rate;
    private Object v;

    public GetJsonDevises(Activity applicationContext, Dialog dialog) {
         context = applicationContext;
         dialog_ = dialog;
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
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
            //dialog.;
        }
    }

    @Override
    protected Void doInBackground(String... params) {
        Log.d(TAG, "doInBackground");
        from = params[0];
        to = params[1];
        priceV = params[2];
        try {
            String url = Utils.formatUrl(from, to, priceV);
            Log.d(TAG, url);
            data = Utils.getFromUrl(url);
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
            Log.d(TAG, jObject.names().toString());
            to = jObject.get(Constants.TO).toString();
            from = jObject.get(Constants.FROM).toString();
            rate = jObject.getDouble(Constants.RATE);
            v = jObject.getDouble(Constants.VALUE);
            //Log.d(TAG, (String) v);
        } catch (JSONException e) {
            Log.d(TAG, e.toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        if (isOnline) {
            try {
                if ((progressDialog != null) && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                   // dialog.dismiss();
                    TextView t = (TextView) dialog_.findViewById(R.id.amswer);
                    t.setText(v.toString() + " " + from);
                }
            } catch (final Exception e) {
                progressDialog = null;
            }

        }
    }
}