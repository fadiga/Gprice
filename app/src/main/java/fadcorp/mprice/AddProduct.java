package fadcorp.mprice;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;


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
    private DatePicker datePicker;
    private Date date;
    private Integer sId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product);

        setupUI();
    }

    protected void setupUI() {

        nameField = (EditText) findViewById(R.id.nameField);
        priceField = (EditText) findViewById(R.id.priceField);
        datePicker = (DatePicker) findViewById(R.id.datePicker3);
        nameField.requestFocus();

        saveButton = (Button) findViewById(R.id.saveButton);
        try {
            Bundle extras = getIntent().getExtras();
            sId = Integer.valueOf(extras.getString("id"));

            ReportData x = ReportData.findById(ReportData.class, sId);
            nameField.setText(x.getName());
            priceField.setText(String.valueOf(x.getPrice()));
        } catch (Exception a){
        }
        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // ensure data is OK
                // save data to DB

                if(!validInputChecks()){return;};

                date = getDateFromDatePicket(datePicker);
                if(sId == -1){
                    if(!validChecks()){return;};
                    storeReportData();
                } else {
                    updateReport(sId);
                }
                finish();
            }
        });
    }

    protected void storeReportData() {
       ReportData report = new ReportData(date,
                stringFromField(nameField).toLowerCase(),
                floatFromField(priceField));
        report.save();

    }

    protected  void updateReport(long sid) {

        ReportData rpt =  ReportData.findById(ReportData.class, sid);
                rpt.setName(stringFromField(nameField).toLowerCase());
                rpt.setPrice(floatFromField(priceField));
                rpt.setModifiedOn(date);
                rpt.save();
    }
    protected float floatFromField(EditText editText) {
        return floatFromField(editText, -1);
    }
    protected float floatFromField(EditText editText, int fallback) {
        String text = stringFromField(editText);
        if (text.length() > 0) {
            return Float.parseFloat(text);
        }
        return fallback;
    }
    protected String stringFromField(EditText editText) {
        return editText.getText().toString().trim();
    }
    public static java.util.Date getDateFromDatePicket(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
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

        String value = stringFromField(nameField).toLowerCase();
        List rpt = ReportData.find(ReportData.class, "name = ?", value);
        if (!rpt.isEmpty()) {
            nameField.setError(String.format("%s existe déjà dans la base de donnée", value));
            nameField.requestFocus();
            return false;
        }
        return true;
    }

}
