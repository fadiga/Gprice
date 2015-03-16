package fadcorp.mprice;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by fad on 14/12/14.
 */
public class Utils extends Activity{

    private static final String TAG = Constants.getLogTag("Utils");

    public static void copyFile(FileInputStream fromFile, FileOutputStream toFile) throws IOException {
        FileChannel fromChannel = null;
        FileChannel toChannel = null;
        try {
            fromChannel = fromFile.getChannel();
            toChannel = toFile.getChannel();
            fromChannel.transferTo(0, fromChannel.size(), toChannel);
        } finally {
            try {
                if (fromChannel != null) {
                    fromChannel.close();
                }
            } finally {
                if (toChannel != null) {
                    toChannel.close();
                }
            }
        }
    }

    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }
    /*
    http://rate-exchange.appspot.com/currency?from=EUR&to=XOF&q=12
     */
    public static String formatUrl(String from, String to, String value){
        return String.format("http://rate-exchange.appspot.com/currency?from=%s&to=%s&q=%s", from, to, value);
    }


    public static String getFromUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
        }
        return data;
    }

    public static ProgressDialog getStandardProgressDialog(Activity activity,
                                                           String title,
                                                           String message,
                                                           boolean cancelable) {
        ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setIcon(R.drawable.ic_launcher);
        progressDialog.setCancelable(cancelable);
        return progressDialog;
    }

    public static boolean isOnline(Activity activity) {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    public static void toast (Context context , int msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        //toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static Date strDateToDate(String strDate) {

        Log.d(TAG, " StrDate" + strDate);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        try {
            date = dateFormat.parse(strDate.replace("T", " "));
        } catch (ParseException e) {
            Log.d(TAG, "ParseException" + e.toString());
        }
        return date;
    }

    public static String dateTostrDate(Date date) {

        SimpleDateFormat timeFormat = new SimpleDateFormat("dd-MM-yyyy");
        String finalDate = timeFormat.format(date);

        return finalDate;
    }

    public static String getDefaultCurrencyFormat(float value)
    {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.getDefault());
        nf.setGroupingUsed(true);
        nf.setMaximumFractionDigits(3);
        return nf.format(value);
    }

    public static void copyFile(Activity context, File from, File to) {
        try {

            FileChannel src = new FileInputStream(from).getChannel();
            FileChannel dst = new FileOutputStream(to).getChannel();
            dst.transferFrom(src, 0, src.size());
            src.close();
            dst.close();
        } catch (Exception e){
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    protected static float floatFromField(EditText editText) {
        return floatFromField(editText, -1);
    }
    protected static float floatFromField(EditText editText, int fallback) {
        String text = stringFromField(editText);
        if (text.length() > 0) {
            return Float.parseFloat(text);
        }
        return fallback;
    }
    protected static String stringFromField(EditText editText) {
        return editText.getText().toString().trim();
    }
}
