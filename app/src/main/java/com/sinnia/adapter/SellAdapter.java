package com.sinnia.adapter;

import android.content.Context;
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
import com.sinnia.utils.SellProduct;
import com.sinnia.utils.SiniiaUtils;

import java.util.ArrayList;
import java.util.List;

public class SellAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "SellAdapter";
    private Context mContext;
    private List<Products> productsList;
    private ItemClickListener itemClickListener;
    private List<Products> originDataList;
    private SiniiaPreferences siniiaPreferences;


    public SellAdapter(Context mContext, List<Products> productsList, ItemClickListener itemClickListener) {
        this.mContext = mContext;
        this.productsList = productsList;
        this.itemClickListener = itemClickListener;
        this.originDataList=new ArrayList<>();
        this.originDataList.addAll(productsList);
        siniiaPreferences=new SiniiaPreferences(mContext);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sell_item_layout, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder recyleViewHolder, int position) {
        final ItemViewHolder holder = (ItemViewHolder) recyleViewHolder;
        final Products sellProduct = productsList.get(position);
        if (sellProduct != null) {
            holder.txtProductName.setText(sellProduct.getProductName());
            holder.txtBoxPrice.setText(SiniiaUtils.getCurrencySymbol(siniiaPreferences.getCountryCode())+" " + sellProduct.getPricePerUnit());
            holder.txtQuantity.setText("Qnty: " + sellProduct.getQuantityAvailable());
            holder.txtNumOfBox.setText("(" + sellProduct.getQuantityType() + "-" + sellProduct.getQuantityPerUnit() + ")");
            holder.txtTotalPrice.setText(SiniiaUtils.getCurrencySymbol(siniiaPreferences.getCountryCode())+" " + sellProduct.getPricePerUnit());
            String url = sellProduct.getThumbImageURL();
            if (url != null) {
                if (url.contains(",")) {
                    SiniiaUtils.loadPicassoImageWithOutCircle(mContext, url.split(",")[0], holder.mImgSellImage);
                } else {
                    SiniiaUtils.loadPicassoImageWithOutCircle(mContext, url, holder.mImgSellImage);
                }
            }

            holder.bind(sellProduct);
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
        TextView txtProductName, txtBoxPrice, txtNumOfBox, txtQuantity, txtTotalPrice;
        ImageView mImgSellImage;

        public ItemViewHolder(View view) {
            super(view);
            txtProductName = (TextView) view.findViewById(R.id.txt_product_name);
            txtBoxPrice = (TextView) view.findViewById(R.id.txt_box_price);
            txtNumOfBox = (TextView) view.findViewById(R.id.txt_quantity_boxs);
            txtQuantity = (TextView) view.findViewById(R.id.txt_quantity);
            txtTotalPrice = (TextView) view.findViewById(R.id.txt_total_price);
            mImgSellImage = (ImageView) view.findViewById(R.id.img_sell_product);
        }

        public void bind(final Products sellProduct) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemClickListener != null) {
                        itemClickListener.clickOnRVItem(sellProduct);
                    }
                }
            });
        }
    }

    public void alertFilter(String newText) {
        String charSet = newText.toLowerCase();
        productsList.clear();
        if (charSet.length() == 0) {
            productsList.addAll(originDataList);
        } else {
            for (Products products : originDataList) {
                if (products.getProductName().toLowerCase().contains(charSet)) {
                    productsList.add(products);
                }
            }
        }
        notifyDataSetChanged();
    }
}
