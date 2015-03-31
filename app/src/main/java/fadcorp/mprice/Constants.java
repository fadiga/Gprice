package fadcorp.mprice;

import android.content.Context;

import java.util.Date;

public class Constants {

    private final static String TAG = Constants.getLogTag("Constants");
    public static final String TO = "to";
    public static final String FROM = "from";
    public static final String RATE = "rate";
    public static final String VALUE = "v";

    public static final String databaseName = "Mprice.db";

    public static final String facebook = "com.facebook.katana";

    public static final String getLogTag(String activity) {
        return String.format("MPLog-%s", activity);
    }

    public static String formatDate(Date date, Context context) {
        java.text.DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(context);
        return dateFormat.format(date);
    }
}
