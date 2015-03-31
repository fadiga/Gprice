package fadcorp.mprice;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;


public class ProductElementsAdapter extends BaseAdapter {

    private static final String TAG = Constants.getLogTag("ProductElementsAdapter");
    private Context context;
    private ArrayList<ProductElement> productElements;
    private ArrayList<ProductElement> productElementsOrigin;
    private ItemFilter mFilter = new ItemFilter();
    private TextView devise;

    public ProductElementsAdapter(Context context, ArrayList<ProductElement> productElements) {
        this.context = context;
        this.productElements = productElements;
        this.productElementsOrigin = productElements;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public int getCount() {
        return productElements.size();
    }

    public Object getItem(int position) {
        return productElements.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = (View) inflater.inflate(
                    R.layout.row_list_product, null);
        }

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        final String to = sharedPrefs.getString("moneyType", "Fcfa");
        final float priceValue = productElements.get(position).getPrice();
        TextView nameProduct = (TextView)convertView.findViewById(R.id.nameProduct);
        nameProduct.setText(productElements.get(position).getName());
        final TextView priceField = (TextView)convertView.findViewById(R.id.priceValue);
        priceField.setText(Utils.getDefaultCurrencyFormat(priceValue));
        devise = (TextView) convertView.findViewById(R.id.devise);
        devise.setText(to);
        TextView date = (TextView)convertView.findViewById(R.id.dateP);
        date.setText(productElements.get(position).getModifiedOn());
        //ImageView image = (ImageView) convertView.findViewById(R.id.editProd);
        final long prodId = productElements.get(position).getProdId();
        nameProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditAndAddDialog editAndAddDialog = new EditAndAddDialog(context, prodId);
                editAndAddDialog.show();
            }
        });
        priceField.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                final String from = "Eur";

                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.devises);
                dialog.setTitle("Conversion de " + to);
                ProgressBar prog = (ProgressBar) dialog.findViewById(R.id.progressBar);
                prog.setVisibility(View.GONE);
                //TextView title = (TextView) dialog.findViewById(R.id.textViewDialog);
                //title.setText();
                final EditText amontField = (EditText) dialog.findViewById(R.id.editText);
                amontField.setText(Float.toString(priceValue));
                Button dialogBttOK = (Button) dialog.findViewById(R.id.dialogButtonOK);
                dialogBttOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new GetJsonDevises((android.app.Activity) context, dialog).execute(from,
                                to, String.valueOf(Utils.floatFromField(amontField)));
                    }
                });
                Button dialogBttCancel = (Button) dialog.findViewById(R.id.dialogButtonCancel);
                dialogBttCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                //dialog.show();
            }
        });
        /*ImageView image = (ImageView) convertView.findViewById(R.id.tagImage);
         if( position > 1) {
             image.setImageResource(R.drawable.downicon);
         }*/

        return convertView;
    }


    public Filter getFilter() {
        return mFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();
            final ArrayList<ProductElement> list = productElementsOrigin;
            int count = list.size();
            final ArrayList<ProductElement> nlist = new ArrayList<ProductElement>(count);
            ProductElement filterable;
            for (int i = 0; i < count; i++) {
                filterable = list.get(i);
                if (filterable.getName().toLowerCase().contains(filterString)) {
                    nlist.add(filterable);
                }
            }
            results.values = nlist;
            results.count = nlist.size();
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            productElements = (ArrayList<ProductElement>) results.values;
            notifyDataSetChanged();
        }
    }
}