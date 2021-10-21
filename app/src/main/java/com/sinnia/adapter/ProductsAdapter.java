package com.sinnia.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sinnia.R;
import com.sinnia.data.products.Products;
import com.sinnia.listeners.ItemClickListener;
import com.sinnia.preferences.SiniiaPreferences;
import com.sinnia.utils.SiniiaUtils;

import java.util.List;


/**
 * Created by AXIOM on 20-03-2018.
 */

public class ProductsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "ProductsAdapter";
    private Context mContext;
    private List<Products> productsList;
    private ItemClickListener itemClickListener;
    private SiniiaPreferences siniiaPreferences;

    public ProductsAdapter(Context mContext, List<Products> productsList, ItemClickListener itemClickListener) {
        this.mContext = mContext;
        this.productsList = productsList;
        this.itemClickListener = itemClickListener;
        siniiaPreferences=new SiniiaPreferences(mContext);


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.products_item_layout, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder recyleViewHolder, int position) {
        final ItemViewHolder holder = (ItemViewHolder) recyleViewHolder;
        final Products products = productsList.get(position);
        if (products != null) {
            String productsName = products.getProductName();
            holder.txtProductName.setText(productsName);
            holder.txtCost.setText(SiniiaUtils.getCurrencySymbol(siniiaPreferences.getCountryCode())+" " + products.getPricePerUnit() + " - 1 " + products.getQuantityType());
            String url = products.getThumbImageURL();
            if (url != null) {
                if (url.contains(",")) {
                    SiniiaUtils.loadPicassoImageWithOutCircle(mContext, url.split(",")[0], holder.mImgProduct);
                } else {
                    SiniiaUtils.loadPicassoImageWithOutCircle(mContext, url, holder.mImgProduct);
                }
            }
            holder.bind(products);
        }

    }

    @Override
    public int getItemCount() {
        if (productsList != null) {
            return productsList.size();
        } else {
            return 0;
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView txtProductName, txtCost;
        ImageView mImgProduct;

        public ItemViewHolder(View view) {
            super(view);
            txtProductName = (TextView) view.findViewById(R.id.txt_name);
            txtCost = (TextView) view.findViewById(R.id.txt_cost);
            mImgProduct = (ImageView) view.findViewById(R.id.img_product);
        }

        public void bind(final Products products) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemClickListener != null) {
                        itemClickListener.clickOnRVItem(products);
                    }
                }
            });
        }
    }
}
