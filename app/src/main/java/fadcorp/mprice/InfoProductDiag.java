package fadcorp.mprice;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;

/**
 * Created by fad on 23/04/15.
 */
public class InfoProductDiag extends Dialog{
    private static final String TAG =  Constants.getLogTag("NotificationDiag");
    private final String sName;
    private final float lPrice;
    private final Date sDate;
    private final Context ctxt;
    private final long sID;
    private TextView priceField;
    private TextView dateField;
    private TextView deviseField;
    private Button okButton;
    private Button updateBtt;

    public InfoProductDiag(Context context, Long id, String name, float price, Date date) {
        super(context);
        Log.d(TAG, "NotificationDiag");
        ctxt = context;
        sID = id;
        sName = name;
        lPrice = price;
        sDate = date;
        setTitle("info. " + sName);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_product_diag);
        setupUI();

    }

    private void setupUI() {
        Log.d(TAG, "setupUI");

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ctxt);
        final String to = sharedPrefs.getString("moneyType", "Fcfa");
        priceField = (TextView) findViewById(R.id.priceTextView);
        priceField.setText(Utils.getDefaultCurrencyFormat(lPrice));
        deviseField = (TextView) findViewById(R.id.deviceTextView);
        deviseField.setText(to);
        dateField = (TextView) findViewById(R.id.dateField);
        dateField.setText(Constants.formatDate(sDate, getContext()));

        okButton = (Button) findViewById(R.id.okBtt);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                ((BarcodeScan) ctxt).finish();
            }
        });
        updateBtt = (Button) findViewById(R.id.updateBtt);
        updateBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                EditAndAddDialog editAndAddDialog = new EditAndAddDialog(ctxt, sID, true, null);
                editAndAddDialog.show();
            }
        });

    }
}
