package fadcorp.mprice;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
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
public class AddProduct extends Activity{
    private EditText nameField;
    private EditText priceField;
    private Button saveButton;
    private Button saveContinuousButton;
    private Date date;
    private Integer sId = -1;
    private TextView dateField;

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
        //dateField.setText(mDay + "-" + (mMonth + 1) + "-" + mYear);
        dateField.setText(Constants.formatDate(date, this));

        Button saveAndNewBtt = (Button) findViewById(R.id.saveContinuousButton);
        try {
            Bundle extras = getIntent().getExtras();
            sId = Integer.valueOf(extras.getString("id"));

            ReportData x = ReportData.findById(ReportData.class, sId);

            nameField.setText(x.getName());
            priceField.setText(String.valueOf(x.getPrice()));
            dateField.setText(Constants.formatDate(x.getModifiedOn(), this));
            saveAndNewBtt.setVisibility(View.GONE);
        } catch (Exception a){
        }
        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (!validInputChecks()) {
                return;
            };
            //date = Utils.strDateToDate(dateField.getText().toString());
            if (sId == -1) {
                if (!validChecks()) {
                    return;
                };
                storeReportData();
            } else {
                updateReport(sId);
            }
            finish();
            }
        });

        saveContinuousButton = (Button) findViewById(R.id.saveContinuousButton);
        saveContinuousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validInputChecks()) {
                    return;
                };
                //date = Utils.strDateToDate(dateField.getText().toString());
                if (sId == -1) {
                    if (!validChecks()) {
                        return;
                    };
                    storeReportData();
                } else {
                    updateReport(sId);
                }
               priceField.setText("");
               nameField.setText("");
               nameField.requestFocus();
            }
        });

        final DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        //dateField.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        date = Utils.strDateToDate(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        dateField.setText(Constants.formatDate(date, AddProduct.this));
                    }
                }, mYear, mMonth, mDay);
        dateField.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // ensure data is OK
                // save data to DB
                dpd.show();
            }
        });
    }

    protected void storeReportData() {
       ReportData report = new ReportData(date,
                Utils.stringFromField(nameField).toLowerCase(),
                Utils.floatFromField(priceField));
        report.save();

    }

    protected void updateReport(long sid) {

        ReportData rpt =  ReportData.findById(ReportData.class, sid);
                rpt.setName(Utils.stringFromField(nameField).toLowerCase());
                rpt.setPrice(Utils.floatFromField(priceField));
                rpt.setModifiedOn(date);
                rpt.save();
    }
    protected void addErrorToField(EditText editText, String message) {
        editText.setError(message);
        // editText.requestFocus();
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
        String error_msg = String.format(getString(R.string.error_field_empty));
        return doCheckAndProceed(test, error_msg, editText);
    }
    protected boolean validInputChecks() {
        return assertNotEmpty(nameField) && assertNotEmpty(priceField);
    }
    protected boolean validChecks() {

        String value = Utils.stringFromField(nameField).toLowerCase();
        List rpt = ReportData.find(ReportData.class, "name = ?", value);
        if (!rpt.isEmpty()) {
            nameField.setError(String.format("%s existe déjà dans la base de donnée", value));
            nameField.requestFocus();
            return false;
        }
        return true;
    }

}
