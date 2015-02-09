package fadcorp.mprice;

import android.util.Log;

import com.orm.SugarApp;
import com.orm.SugarContext;

/**
 * Created by fad on 21/11/14.
 */
public class FadApp extends SugarApp {

    private static final String TAG = Constants.getLogTag("FadApp");

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i(TAG, "Init Database");
        Log.d("Sugar", "DB init");

        SugarContext.init(this);

        Log.d("Sugar", "DB inited");
        Log.i(TAG, "DB inited");
        ReportData x = ReportData.findById(ReportData.class, 1);
    }

    @Override
    public void onTerminate() {
        SugarContext.terminate();
        super.onTerminate();
    }

}