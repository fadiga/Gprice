package fadcorp.mprice;

import android.content.Context;

import java.text.NumberFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Constants {

    private final static String TAG = Constants.getLogTag("Constants");
    public static final String TO = "to";
    public static final String FROM = "from";
    public static final String RATE = "rate";
    public static final String VALUE = "v";

    public static final String getLogTag(String activity) {
        return String.format("MPLog-%s", activity);
    }


    public static String stringFromFloat(float data) {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
        nf.setMaximumFractionDigits(2);
        return nf.format(data);
    }

    public static String stringFromInteger(int data) {
        return String.valueOf(data);
    }

    public static int integerFromReport(int data) {
        if(data < 0) {
            data = 0;
        }
        return data;
    }

    public static String formatDate(Date date, Context context) {
        java.text.DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(context);
        return dateFormat.format(date);
    }

    public static float floatFromReport(float data) {
        if(data < 0) {
            data = (float) 0.00;
        }
        return data;
    }

    public static String getDefaultCurrencyFormat(double value)
    {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.getDefault());
        nf.setGroupingUsed(true);
        return nf.format(value);
    }
    
}
