package fadcorp.mprice;

import android.os.Bundle;
import android.os.Environment;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.util.Date;

/**
 * Created by fad on 23/02/15.
 */
public class ExportImportDB extends Utils {
    private static final String TAG = Constants.getLogTag("ExportImportDB");
    private String packageName;
    private String databaseName = "Mprice.db";
    private Button btt;
    private File currentDB;
    private File backupDir;
    private Button importDbBtt;
    private FileDialog fileDialog;
    private Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.db_manager);

        File sd = Environment.getExternalStorageDirectory();

        if (!sd.canWrite()) {
            return;
        }

        currentDB = getApplicationContext().getDatabasePath(databaseName);
        packageName = getPackageName();

        backupDir = new File(sd.getAbsolutePath()+ "/" + packageName);
        if(!backupDir.exists()) {
            if(backupDir.mkdir()) {}
        }
        btt = (Button) findViewById(R.id.exportDB);
        btt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //exportDB();
                exportDatabse(databaseName);
            }
        });
        importDbBtt = (Button) findViewById(R.id.importDB);
        importDbBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                importDB();

            }
        });
    }

    //importing database
    private void importDB() {
        exportDatabse(databaseName);
        File mPath = new File(Environment.getExternalStorageDirectory() + "//DIR//");
        fileDialog = new FileDialog(this, mPath);
        fileDialog.setFileEndsWith(".db");
        fileDialog.addFileListener(new FileDialog.FileSelectedListener() {
            public void fileSelected(File file) {
                Log.d(TAG, "selected file " + file.toString());
                Utils.copyFile(ExportImportDB.this, file, currentDB);
                Toast.makeText(getBaseContext(), databaseName + " a été importée avec succès depuis \n" + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
            }
        });
        fileDialog.showDialog();
    }
    //exporting database
    public void exportDatabse(String databaseName) {
        Time t = new Time(Time.getCurrentTimezone());
        t.setToNow();
        String dateNow = t.format("%Y-%m-%d-%Hh-%Mm");
        String backupDBName = String.format("backup-%s.db", dateNow);
        File backupDB = new File(backupDir, backupDBName);

        Log.d(TAG,  currentDB + " " + currentDB.exists());
        Utils.copyFile(this, currentDB, backupDB);
        Toast.makeText(getBaseContext(), databaseName + " a été exportée avec succès dans \n" + backupDB.getAbsolutePath(), Toast.LENGTH_LONG).show();

    }
}
