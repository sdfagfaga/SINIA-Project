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
import com.sinnia.utils.BuySellItem;
import com.sinnia.utils.SiniiaUtils;

import java.util.ArrayList;
import java.util.List;

public class BuySellAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "BuySellAdapter";
    private Context mContext;
    private List<Products> buySellItemList;
    private ItemClickListener itemClickListener;

    private List<Products> originDataList;
    private SiniiaPreferences siniiaPreferences;




    public BuySellAdapter(Context mContext, List<Products> buySellItemList, ItemClickListener itemClickListener) {
        this.mContext = mContext;
        this.buySellItemList = buySellItemList;
        this.itemClickListener = itemClickListener;
        this.originDataList=new ArrayList<>();
        this.originDataList.addAll(buySellItemList);
        siniiaPreferences=new SiniiaPreferences(mContext);


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.buy_sell_item_layout, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder recyleViewHolder, int position) {
        final ItemViewHolder holder = (ItemViewHolder) recyleViewHolder;
        final Products buySellItem = buySellItemList.get(position);
        if (buySellItem != null) {
            holder.txtProductName.setText(buySellItem.getProductName());
            holder.txtProductType.setText(" (" + buySellItem.getProductType() + ")");
            holder.txtQuantity.setText(buySellItem.getSelectedQuantity() + "");
            holder.txtGrade.setText(buySellItem.getProductGrade());
            holder.txtCost.setText(SiniiaUtils.getCurrencySymbol(siniiaPreferences.getCountryCode())+" " + buySellItem.getSelectedTotal());
            holder.txtQuantityType.setText(buySellItem.getQuantityType());

            String url = buySellItem.getThumbImageURL();
            if (url != null) {
                if (url.contains(",")) {
                    SiniiaUtils.loadPicassoImageWithOutCircle(mContext, url.split(",")[0], holder.img);
                } else {
                    SiniiaUtils.loadPicassoImageWithOutCircle(mContext, url, holder.img);
                }
            }


            holder.bind(buySellItem);
        }

    }

    @Override
    public int getItemCount() {
        if (buySellItemList != null) {
            return buySellItemList.size();
        } else {
            return 0;
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView txtProductName, txtProductType, txtQuantity, txtCost, txtGrade, txtQuantityType;
        ImageView img;

        public ItemViewHolder(View view) {
            super(view);
            txtProductName = (TextView) view.findViewById(R.id.txt_product_name);
            txtProductType = (TextView) view.findViewById(R.id.txt_product_type);
            txtQuantity = (TextView) view.findViewById(R.id.txt_quantity);
            txtCost = (TextView) view.findViewById(R.id.txt_price);
            txtGrade = (TextView) view.findViewById(R.id.txt_grade);
            img = (ImageView) view.findViewById(R.id.img);
            txtQuantityType = (TextView) view.findViewById(R.id.txt_qunatity_type);
        }

        public void bind(final Products buySellItem) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (itemClickListener != null) {
                        itemClickListener.clickOnRVItem(buySellItem);
                    }
                }
            });
        }
    }

    public void alertFilter(String newText) {
        String charSet = newText.toLowerCase();
        buySellItemList.clear();
        if (charSet.length() == 0) {
            buySellItemList.addAll(originDataList);
        } else {
            for (Products products : originDataList) {
                if (products.getProductName().toLowerCase().contains(charSet)) {
                    buySellItemList.add(products);
                }
            }
        }
        notifyDataSetChanged();
    }
}