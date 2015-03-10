package fadcorp.mprice;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.db_manager);
        
        File sd = Environment.getExternalStorageDirectory();

        if (!sd.canWrite()) {
            return;
        }
        File data = Environment.getDataDirectory();
        String currentDBPath = "data//"+ getPackageName()+"//databases//"+databaseName+"";
        currentDB = new File(data, currentDBPath);
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
        File mPath = new File(Environment.getExternalStorageDirectory() + "//DIR//");
        fileDialog = new FileDialog(this, mPath);
        fileDialog.setFileEndsWith(".db");
        fileDialog.addFileListener(new FileDialog.FileSelectedListener() {
            public void fileSelected(File file) {
                Log.d(TAG, "" + getClass().getName());
                Log.d(TAG, "selected file " + file.toString());
            }
        });
        //fileDialog.addDirectoryListener(new FileDialog.DirectorySelectedListener() {
        //  public void directorySelected(File directory) {
        //      Log.d(getClass().getName(), "selected dir " + directory.toString());
        //  }
        //});
        //fileDialog.setSelectDirectoryOption(false);
        fileDialog.showDialog();
    }
    //exporting database
    public void exportDatabse(String databaseName) {
        try {
            String backupDBName = String.format("backup_%s", databaseName);
            File backupDB = new File(backupDir, backupDBName);

            Log.d(TAG, backupDir.toString() + " " + currentDB + " " + currentDB.exists());
            if (currentDB.exists()) {
                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Toast.makeText(getBaseContext(), databaseName + " a été exportée avec succès dans \n" + backupDB.getAbsolutePath(), Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }
}
