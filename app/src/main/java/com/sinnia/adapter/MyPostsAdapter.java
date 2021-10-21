package com.sinnia.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sinnia.R;
import com.sinnia.data.cart.UserAddress;
import com.sinnia.data.products.Products;
import com.sinnia.fragments.FragmentBuySell;
import com.sinnia.listeners.MyPostListeners;
import com.sinnia.preferences.SiniiaPreferences;
import com.sinnia.utils.SiniiaUtils;

import java.util.ArrayList;
import java.util.List;

public class MyPostsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "BuySellAdapter";
    private Context mContext;
    private List<Products> myPostProducts;
    private MyPostListeners myPostListeners;

    private List<Products> originDataList;
    private SiniiaPreferences siniiaPreferences;


    public MyPostsAdapter(Context mContext, List<Products> myPostProducts,
                          MyPostListeners myPostListeners) {
        this.mContext = mContext;
        this.myPostProducts = myPostProducts;
        this.myPostListeners = myPostListeners;
        siniiaPreferences=new SiniiaPreferences(mContext);
        this.originDataList=new ArrayList<>();
        this.originDataList.addAll(myPostProducts);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_post_item_layout, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder recyleViewHolder, int position) {
        final ItemViewHolder holder = (ItemViewHolder) recyleViewHolder;
        final Products buySellItem = myPostProducts.get(position);
        if (buySellItem != null) {

            holder.txtProductName.setText(buySellItem.getProductName());
            holder.txtRadius.setText(buySellItem.getRadius());
            holder.txtMinQty.setText(buySellItem.getMinQuantity() + " " + buySellItem.getQuantityType());
            holder.txtPrice.setText(SiniiaUtils.getCurrencySymbol(siniiaPreferences.getCountryCode())+" " + buySellItem.getPricePerUnit() + " - per " + buySellItem.getQuantityType());
            if (buySellItem.getProductStatus() == FragmentBuySell.ACTIVE) {
                holder.mTxtStatus.setText(mContext.getString(R.string.activated));
                holder.mTxtStatus.setTextColor(mContext.getColor(R.color.green));
                holder.imgActive.setImageDrawable(mContext.getDrawable(R.drawable.ic_active_post));
            } else {
                holder.mTxtStatus.setText(mContext.getString(R.string.de_activated));
                holder.mTxtStatus.setTextColor(mContext.getColor(R.color.purple_color));
                holder.imgActive.setImageDrawable(mContext.getDrawable(R.drawable.ic_inactive));
            }
            UserAddress productAddress = buySellItem.getAddress();
            if (productAddress != null) {
                String address = null;
                if (productAddress.getAddress1() != null) {
                    address = productAddress.getAddress1();
                }
                if (productAddress.getAddress2() != null) {
                    address = address + ", " + productAddress.getAddress2();
                }
                if (productAddress.getLandmark() != null) {
                    address = address + "\n" + productAddress.getLandmark();
                }
                if (productAddress.getPinCode() != null) {
                    address = address + ", " + productAddress.getPinCode();
                }
                holder.txtProductAddress.setText(address);
            }

            holder.imgActive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (myPostListeners != null) {
                        myPostListeners.clickOnActiveOrInactiveButton(buySellItem.getProductStatus(), buySellItem.getId() + "");
                    }
                }
            });

            holder.mTxtEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (myPostListeners != null) {
                        myPostListeners.clickOnEdit(buySellItem);
                    }
                }
            });

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
        if (myPostProducts != null) {
            return myPostProducts.size();
        } else {
            return 0;
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView txtProductName, txtProductAddress, txtRadius, txtMinQty, txtPrice, mTxtEdit, mTxtStatus;
        ImageView img, imgActive;

        public ItemViewHolder(View view) {
            super(view);
            txtProductName = (TextView) view.findViewById(R.id.txt_product_name);
            txtProductAddress = (TextView) view.findViewById(R.id.txt_product_address);
            txtRadius = (TextView) view.findViewById(R.id.txt_radius);
            txtMinQty = (TextView) view.findViewById(R.id.txt_minm_qty);
            txtPrice = (TextView) view.findViewById(R.id.txt_price);
            img = (ImageView) view.findViewById(R.id.img_cart);
            imgActive = (ImageView) view.findViewById(R.id.img_post_active);
            mTxtEdit = (TextView) view.findViewById(R.id.txt_edit);
            mTxtStatus = (TextView) view.findViewById(R.id.mTxt_status);
        }

        public void bind(final Products buySellItem) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                }
            });
        }
    }

    public void alertFilter(String newText) {
        String charSet = newText.toLowerCase();
        myPostProducts.clear();
        if (charSet.length() == 0) {
            myPostProducts.addAll(originDataList);
        } else {
            for (Products products : originDataList) {
                if (products.getProductName().toLowerCase().contains(charSet)) {
                    myPostProducts.add(products);
                }
            }
        }
        notifyDataSetChanged();
    }
}
