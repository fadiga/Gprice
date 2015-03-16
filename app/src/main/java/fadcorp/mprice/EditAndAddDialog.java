package fadcorp.mprice;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by fad on 28/11/14.
 */
public class EditAndAddDialog extends Dialog{
    private static final String TAG =  Constants.getLogTag("EditAndAddDialog");
    private EditText nameField;
    private EditText priceField;
    private Button saveButton;
    private Button saveContinuousButton;
    private Date date;
    private long sId;
    private TextView dateField;
    private ReportData oldReport;
    private boolean next;

    public EditAndAddDialog(Context context, long articleId) {
        super(context);
        sId = articleId;
        try {
            oldReport = ReportData.findById(ReportData.class, sId);
            setTitle("Mise à jour de " + oldReport.getName());
        }catch (Exception e){
            oldReport = null;
            setTitle("Nouvel acticle");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product);
        setupUI();
    }

    protected void setupUI() {

        nameField = (EditText) findViewById(R.id.nameField);
        priceField = (EditText) findViewById(R.id.priceField);
        dateField = (TextView) findViewById(R.id.editDate);
        nameField.requestFocus();

        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        date = c.getTime();
        dateField.setText(Constants.formatDate(date, getContext()));

        Button saveAndNewBtt = (Button) findViewById(R.id.saveContinuousButton);
        if(oldReport != null) {
            nameField.setText(oldReport.getName());
            priceField.setText(String.valueOf(oldReport.getPrice()));
            dateField.setText(Constants.formatDate(oldReport.getModifiedOn(), getContext()));
            saveAndNewBtt.setVisibility(View.GONE);

        }
        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next = false;
                checkAndSave();
            }
        });
        saveContinuousButton = (Button) findViewById(R.id.saveContinuousButton);
        saveContinuousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next = true;
                checkAndSave();
            }
        });
        final DatePickerDialog dpd = new DatePickerDialog(getContext(),
        new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
                date = Utils.strDateToDate(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                dateField.setText(Constants.formatDate(date, getContext()));
            }
        }, mYear, mMonth, mDay);
        dateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpd.show();
            }
        });
    }
    protected void checkAndSave() {
        if (!validInputChecks()) { return; };
        if (!validDuplicateChecks()) { return; };
        if (oldReport == null) {
            Log.d(TAG, "oldReport null");
            storeReportData();
        } else {
            Log.d(TAG, "oldReport " + oldReport.getName());
            updateReport(oldReport);
        }
        nameField.setText("");
        nameField.requestFocus();
        priceField.setText("");
        if (!next){
            dismiss();
        }
        Intent a = new Intent(getContext(), Home.class);
        getContext().startActivity(a);
    }
    protected void storeReportData() {
       ReportData report = new ReportData(date,
                Utils.stringFromField(nameField).toLowerCase(),
                Utils.floatFromField(priceField));
       report.save();
    }
    protected void updateReport(ReportData rpt) {
        rpt.setName(Utils.stringFromField(nameField).toLowerCase());
        rpt.setPrice(Utils.floatFromField(priceField));
        rpt.setModifiedOn(date);
        rpt.save();
    }
    protected void addErrorToField(EditText editText, String message) {
        editText.setError(message);
        editText.requestFocus();
    }
    protected boolean doCheckAndProceed(boolean test, String error_msg, EditText editText) {
        if (test) {
            addErrorToField(editText, error_msg);
            return false;
        } else {
            addErrorToField(editText, null);
        }
        return true;
    }
    protected boolean assertNotEmpty(EditText editText) {
        boolean test = (editText.getText().toString().trim().length() == 0);
        String error_msg = String.format(getContext().getString(R.string.error_field_empty));
        return doCheckAndProceed(test, error_msg, editText);
    }
    protected boolean validInputChecks() {
        return assertNotEmpty(nameField) && assertNotEmpty(priceField);
    }
    protected boolean validDuplicateChecks() {
            Log.d(TAG, "validDuplicateChecks");
        String value = Utils.stringFromField(nameField).toLowerCase();
        List rpt = ReportData.find(ReportData.class, "name = ?", value);
        if(oldReport != null) {
            Log.d(TAG, "oldReport null");
            if(oldReport.getName() == value){
                Log.d(TAG, " oldReport null" + oldReport.getName() +" "+ value);
                return true;
            }
        }
        if (!rpt.isEmpty()) {
            nameField.setError(String.format("%s existe déjà dans la base de donnée", value));
            nameField.requestFocus();
            return false;
        }
        return true;
    }
}
