package fadcorp.mprice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
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
        super.onCreate(savedInstanceState);

        setTitle(String.format("%1$s/%2$s", getString(R.string.app_name),
                               getString(R.string.menu_settings)));
        databaseName = Constants.databaseName;
        currentDB = getApplicationContext().getDatabasePath(databaseName);
        packageName = getPackageName();

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String username = sharedPrefs.getString("username", "");
        String passwork = sharedPrefs.getString("passwork", "");
        String moneyType = sharedPrefs.getString("moneyType", null);

        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString("username", "");
        editor.putString("passwork", "");
        //editor.putString("moneyType", moneyType);
        editor.apply();
        addPreferencesFromResource(R.xml.preferences);
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
                exportDatabse();
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
        exportDatabse();
        File mPath = new File(Environment.getExternalStorageDirectory() + "//DIR//");
        FileDialog fileDialog = new FileDialog(this, mPath);
        fileDialog.setFileEndsWith(".db");
        fileDialog.addFileListener(new FileDialog.FileSelectedListener() {
            public void fileSelected(File file) {
                Log.d(TAG, "selected file " + file.toString());
                Utils.copyFile(Preferences.this, file, currentDB);
                Toast.makeText(getBaseContext(), databaseName + " a été importée avec succès depuis \n" + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
            }
        });
        fileDialog.showDialog();
    }
    //exporting database
    public void exportDatabse() {
        File sd = Environment.getExternalStorageDirectory();

        if (!sd.canWrite()) {
            return;
        }

        File backupDir = new File(sd.getAbsolutePath() + "/" + packageName);
        if(!backupDir.exists()) {
            if(backupDir.mkdir()) {}
        }
        Time t = new Time(Time.getCurrentTimezone());
        t.setToNow();
        String dateNow = t.format("%Y-%m-%d-%Hh-%Mm");
        String backupDBName = String.format("backup-%s.db", dateNow);
        File backupDB = new File(backupDir, backupDBName);

        Log.d(TAG,  currentDB + " " + currentDB.exists());
        Utils.copyFile(this, currentDB, backupDB);
        Toast.makeText(getBaseContext(), databaseName + " a été exportée avec succès dans \n" + backupDB.getAbsolutePath(), Toast.LENGTH_LONG).show();

    }
    public void restDatabase (String databaseName) {
        SugarContext.terminate();
        getApplicationContext().deleteDatabase(databaseName);
        SugarContext.init(this);
        Intent i = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        Toast.makeText(getBaseContext(), databaseName +
                " a été supprimée avec succès", Toast.LENGTH_LONG).show();
    }

}
