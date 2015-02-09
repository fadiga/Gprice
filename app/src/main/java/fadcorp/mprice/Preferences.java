package fadcorp.mprice;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;


public class Preferences extends PreferenceActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(String.format("%1$s/%2$s", getString(R.string.app_name),
                               getString(R.string.menu_settings)));

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String username = sharedPrefs.getString("username", "");
        String passwork = sharedPrefs.getString("passwork", "");
        //String moneyType = sharedPrefs.getString("moneyType","CFa");
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString("username", "");
        editor.putString("passwork", "");
        //editor.putString("moneyType", "");
        editor.apply();
        addPreferencesFromResource(R.layout.preferences);
    }

}
