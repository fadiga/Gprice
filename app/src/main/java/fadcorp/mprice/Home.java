package fadcorp.mprice;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.orm.query.Select;

import java.util.ArrayList;
import java.util.List;

public class Home extends Utils {

    private static final String TAG = Constants.getLogTag("Home");
    private ListView mListView;
    private EditText search;

    private ProductElementsAdapter mAdapter;

    // TODO automatical backup online

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ActionBar actionBar = getActionBar();
        // add the custom view to the action bar
        actionBar.setCustomView(R.layout.finder);
        search = (EditText) actionBar.getCustomView().findViewById(R.id.searchfield);
        search.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mAdapter.getFilter().filter(s);
            }
        });
        ImageView addProduct = (ImageView) actionBar.getCustomView().findViewById(R.id.addProduct);
        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                EditAndAddDialog editAndAddDialog = new EditAndAddDialog(Home.this, -1, null);
                editAndAddDialog.show();
            }
        });

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
        setupUI();
        //TelephonyManager tMgr = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        //String mPhoneNumber = tMgr.getLine1Number();
        //Toast.makeText(this, mPhoneNumber, Toast.LENGTH_SHORT).show();
    }

    public void setupUI() {

        Log.i(TAG, "setupUI");
        mListView = (ListView)findViewById(R.id.listProd);
        ProductElement productElement;
        ArrayList<ProductElement> productElements = new ArrayList<ProductElement>();
        List<ReportData> productDataList;
        productDataList = Select.from(ReportData.class).orderBy("name").list();

        LinearLayout r = (LinearLayout)findViewById(R.id.is_empty_id);
        try{
            productDataList.get(0);
            r.setVisibility(View.GONE);
        } catch (Exception e){
            Log.d(TAG, e.toString());
            View addBtt = r.findViewById(R.id.addBtt);
            addBtt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditAndAddDialog editAndAddDialog = new EditAndAddDialog(Home.this, -1, null);
                    editAndAddDialog.show();
                }
            });
        }
        try {
            for (ReportData prod : productDataList) {
                productElement = new ProductElement();
                productElement.setProdId(prod.getId());
                productElement.setPrice(prod.getPrice());
                productElement.setName(prod.getName());
                productElement.setBarCode(prod.getBarCode());
                productElement.setModifiedOn(Constants.formatDate(prod.getModifiedOn(), this));
                productElements.add(productElement);
            }
        } catch (Exception e){
            Log.e(TAG, "prodDataList-" + e);
        }
        mAdapter = new ProductElementsAdapter(Home.this, productElements);
        mListView.setClipChildren(true);
        //mListView.setItemsCanFocus(false);
        mListView.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent a = new Intent(Home.this, Preferences.class);
            startActivity(a);
        }
        if (id == R.id.action_scan) {
            Intent a = new Intent(Home.this, BarcodeScan.class);
            startActivity(a);
        }
        if (id == R.id.convert_devise) {

            boolean isOnline = Utils.isOnline(Home.this);
            if (!isOnline) {
                Utils.toast(getApplicationContext(), R.string.required_connexion_body);
            } else {
                Intent a = new Intent(Home.this, ConvirtDevise.class);
                startActivity(a);
            }
        }
        if (id == R.id.about) {
            Intent a = new Intent(Home.this, About.class);
            startActivity(a);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch(keyCode){
            case KeyEvent.KEYCODE_BACK :
                new AlertDialog.Builder(this)
                        .setTitle("Quitter")
                        .setMessage("Voulez vous vraiment quitter ?")
                        .setPositiveButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                        System.exit(0);
                                }})
                        .setNegativeButton(android.R.string.cancel,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {}
                                }).create().show();
                return true;
        }
        return false;
    }
}
