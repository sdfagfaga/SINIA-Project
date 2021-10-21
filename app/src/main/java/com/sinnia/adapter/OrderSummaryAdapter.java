package com.sinnia.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sinnia.R;
import com.sinnia.data.products.Products;
import com.sinnia.preferences.SiniiaPreferences;
import com.sinnia.utils.SiniiaUtils;

import java.util.List;

public class OrderSummaryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "BuySellAdapter";
    private Context mContext;
    private List<Products> cartProductsList;

    private long totalPrice;
    private SiniiaPreferences siniiaPreferences;

    public OrderSummaryAdapter(Context mContext, List<Products> cartProductsList) {
        this.mContext = mContext;
        this.cartProductsList = cartProductsList;
        totalPrice = 0;
        siniiaPreferences=new SiniiaPreferences(mContext);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_summary_item_layout, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder recyleViewHolder, int position) {
        final ItemViewHolder holder = (ItemViewHolder) recyleViewHolder;
        final Products buySellItem = cartProductsList.get(position);
        if (buySellItem != null) {

            holder.txtProductName.setText(buySellItem.getProductName());
            holder.txtProductType.setText("(" + buySellItem.getProductType() + ")");
            holder.txtPricePerUnit.setText(SiniiaUtils.getCurrencySymbol(siniiaPreferences.getCountryCode())+" "+ buySellItem.getPricePerUnit());
            holder.txtQunatityTypePriceUnit.setText("(" + buySellItem.getQuantityType() + " - " + buySellItem.getQuantityPerUnit() + ")");

            String url = buySellItem.getThumbImageURL();
            if (url != null) {
                if (url.contains(",")) {
                    SiniiaUtils.loadPicassoImageWithOutCircle(mContext, url.split(",")[0], holder.mImgCart);
                } else {
                    SiniiaUtils.loadPicassoImageWithOutCircle(mContext, url, holder.mImgCart);
                }
            }

            holder.mQunatity.setText(buySellItem.getSelectedQuantity() + "");
            holder.mTxtTotalCost.setText(buySellItem.getSelectedTotal() + "");


            holder.bind(buySellItem);
        }

    }

    @Override
    public int getItemCount() {
        if (cartProductsList != null) {
            return cartProductsList.size();
        } else {
            return 0;
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView txtProductName, txtProductType, txtPricePerUnit, txtQunatityTypePriceUnit, mTxtTotalCost, mQunatity;
        ImageView mImgCart;


        public ItemViewHolder(View view) {
            super(view);
            txtProductName = (TextView) view.findViewById(R.id.txt_product_name);
            txtProductType = (TextView) view.findViewById(R.id.txt_product_type);
            txtQunatityTypePriceUnit = (TextView) view.findViewById(R.id.txt_qunatity_type_per_unit);
            mTxtTotalCost = (TextView) view.findViewById(R.id.txt_total_cost);
            txtPricePerUnit = (TextView) view.findViewById(R.id.txt_product_per_unit);
            mQunatity = (TextView) view.findViewById(R.id.txt_quantity);
            mImgCart = (ImageView) view.findViewById(R.id.img_cart);
        }

        public void bind(Products buySellItem) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }

    public long getTotalPrice() {
        return totalPrice;
    }
}
