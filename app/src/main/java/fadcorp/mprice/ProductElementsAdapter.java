package fadcorp.mprice;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Filter;


public class ProductElementsAdapter extends BaseAdapter {

    private static final String TAG = Constants.getLogTag("ProductElementsAdapter");
    private Context context;
    private ArrayList<ProductElement> productElements;
    private ArrayList<ProductElement> productElementsOrigin;
    private ItemFilter mFilter = new ItemFilter();

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

        TextView nameProduct = (TextView)convertView.findViewById(R.id.nameProduct);
        nameProduct.setText(productElements.get(position).getName());
        TextView price = (TextView)convertView.findViewById(R.id.priceValue);
        price.setText(productElements.get(position).getPrice());
        TextView date = (TextView)convertView.findViewById(R.id.dateP);
        date.setText(productElements.get(position).getModifiedOn());
        ImageView image = (ImageView) convertView.findViewById(R.id.editProd);
        final long prodId = productElements.get(position).getProdId();
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(context, AddProduct.class);
                a.putExtra("id", String.valueOf(prodId));
                context.startActivity(a);
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
                if (filterable.getName() .toLowerCase().contains(filterString)) {
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