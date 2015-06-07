package fadcorp.mprice;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

import com.orm.SugarContext;

import java.io.File;


public class Preferences extends PreferenceActivity {

    private static final String TAG = Constants.getLogTag("Preferences");
    private File currentDB;
    private String packageName;
    private String databaseName;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        Utils.themeRefresh(this);
        super.onCreate(savedInstanceState);

//        setTitle(String.format("%1$s/%2$s", getString(R.string.app_name),
//                               getString(R.string.menu_settings)));
        databaseName = Constants.databaseName;
        currentDB = getApplicationContext().getDatabasePath(databaseName);
        packageName = getPackageName();

//        String passwork = sharedPrefs.getString("passwork", "");
//        String moneyType = sharedPrefs.getString("moneyType", null);

        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString("passwork", "");
        editor.apply();
        addPreferencesFromResource(R.xml.preferences);

        ListPreference list = (ListPreference) findPreference("theme");
        list.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Log.d(TAG, "Theme is change");
                Intent a = new Intent(Preferences.this, Home.class);
                startActivity(a);
                return true;
            }
        });
        final Preference restBtt = (Preference)findPreference("restDB");
        restBtt.setSummary(String.format(getString(R.string.summary_reset_db), 3));

        restBtt.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            int counter = 3;

            @Override
            public boolean onPreferenceClick(Preference preference) {
                counter--;
                restBtt.setSummary(String.format(getString(R.string.summary_reset_db), counter));
                if (counter == 1) {
                    restBtt.setTitle(getString(R.string.rest_confermed));
                }
                if (counter == 0) {
                    counter = 3;
                    restDatabase(Constants.databaseName);
                }
                return false;
            }
        });

        final Preference exportBtt = (Preference)findPreference("exportDB");
        exportBtt.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                exportDatabse(true);
                return false;
            }
        });
        final Preference importBtt = (Preference)findPreference("importDB");
        importBtt.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                importDB();
                return false;
            }
        });
    }

    //importing database
    private void importDB() {
        exportDatabse(false);
        File mPath = new File(Environment.getExternalStorageDirectory() + "//DIR//");
        FileDialog fileDialog = new FileDialog(this, mPath);
        fileDialog.setFileEndsWith(".db");
        fileDialog.addFileListener(new FileDialog.FileSelectedListener() {
            public void fileSelected(File file) {
                Log.d(TAG, "selected file " + file.toString());
                Utils.copyFile(Preferences.this, file, currentDB);
                Utils.motification(Preferences.this, "", String.format(getBaseContext().getString(R.string.importOk, databaseName, file.getAbsolutePath())));
            }
        });
        fileDialog.showDialog();
    }
    //exporting database
    public void exportDatabse(boolean motif) {
        File sd = Environment.getExternalStorageDirectory();

        File backupDir = new File(sd.getAbsolutePath() + "/" + packageName);
        if (!sd.canWrite()) {
            Utils.motification(Preferences.this, "", String.format("Impossible d'ecrire sur %s",
                                                                          backupDir));
            return;
        }

        if(!backupDir.exists()) {
            if(backupDir.mkdir()) {}
        }
        Time t = new Time(Time.getCurrentTimezone());
        t.setToNow();
        String suffixNameDB = t.format("%Y-%m-%d-%Hh-%Mm");
        if(!motif){
            suffixNameDB = suffixNameDB + "old";
        }
        String backupDBName = String.format("backup-%s.db", suffixNameDB);
        File backupDB = new File(backupDir, backupDBName);

        Log.d(TAG, currentDB + " " + currentDB.exists());
        Utils.copyFile(this, currentDB, backupDB);
        if(motif) {
            Utils.motification(Preferences.this, "", String.format(getBaseContext().getString(R.string.exportOk, databaseName, backupDB.getAbsolutePath())));
        }
    }
    public void restDatabase (String databaseName) {
        SugarContext.terminate();
        getApplicationContext().deleteDatabase(databaseName);
        SugarContext.init(this);
        Intent i = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        Utils.motification(Preferences.this, "", String.format(getBaseContext().getString(R.string.delDB, databaseName)));
    }
}
