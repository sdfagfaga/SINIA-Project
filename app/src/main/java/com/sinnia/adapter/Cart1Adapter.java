package com.sinnia.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.sinnia.R;
import com.sinnia.data.products.Products;
import com.sinnia.listeners.PriceListner;
import com.sinnia.preferences.SiniiaPreferences;
import com.sinnia.utils.SiniiaUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cart1Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "BuySellAdapter";
    private Context mContext;
    private List<Products> cartProductsList;

    private PriceListner priceListner;
    private Map<String, Long> costMap;
    private Map<String, Products> selectedArrayList;
    private SiniiaPreferences siniiaPreferences;

    public Cart1Adapter(Context mContext, List<Products> cartProductsList, PriceListner priceListner) {
        this.mContext = mContext;
        this.cartProductsList = cartProductsList;
        this.priceListner = priceListner;
        siniiaPreferences = new SiniiaPreferences(mContext);
        costMap = new HashMap<>();
        selectedArrayList = new HashMap<>();
        for (Products products : cartProductsList) {
            selectedArrayList.put(products.getId() + "", products);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_one_address_item_layout, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder recyleViewHolder, int position) {
        try {
            final ItemViewHolder holder = (ItemViewHolder) recyleViewHolder;
            final Products buySellItem = cartProductsList.get(position);
            if (buySellItem != null) {

                holder.txtProductName.setText(buySellItem.getProductName());
                holder.txtProductType.setText("(" + buySellItem.getProductType() + ")");
                holder.txtPricePerUnit.setText(SiniiaUtils.getCurrencySymbol(siniiaPreferences.getCountryCode()) + buySellItem.getPricePerUnit());
                holder.txtQunatityTypePriceUnit.setText("(" + buySellItem.getQuantityType() + " - " + buySellItem.getQuantityPerUnit() + ")");

                String url = buySellItem.getThumbImageURL();
                if (url != null) {
                    if (url.contains(",")) {
                        SiniiaUtils.loadPicassoImageWithOutCircle(mContext, url.split(",")[0], holder.mImgCart);
                    } else {
                        SiniiaUtils.loadPicassoImageWithOutCircle(mContext, url, holder.mImgCart);
                    }
                }


                holder.mTxtQty.setText(buySellItem.getMinQuantity() + "");

                holder.mImgMinus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int value = Integer.parseInt(holder.mTxtQty.getText().toString());
                        if (value == 1) {
                            Toast.makeText(mContext, "At least you need to order one item", Toast.LENGTH_SHORT).show();
                        } else {
                            holder.mTxtQty.setText((value - 1) + "");
                            String selectQunaitiy = holder.mTxtQty.getText().toString();
                            long productPrice = (Integer.parseInt(selectQunaitiy) * buySellItem.getPricePerUnit());
                            holder.mTxtTotalCost.setText(SiniiaUtils.getCurrencySymbol(siniiaPreferences.getCountryCode()) + " " + productPrice);
                            buySellItem.setSelectedQuantity(Integer.parseInt(selectQunaitiy));
                            buySellItem.setSelectedTotal(productPrice);
                            if (priceListner != null) {
                                priceListner.getTotalPrice(selectedArrayList);
                            }
                        }

                    }
                });

                holder.mImgAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int value = Integer.parseInt(holder.mTxtQty.getText().toString());
                        if (value == buySellItem.getQuantityAvailable()) {
                            Toast.makeText(mContext, "At Maximum you can to order " + buySellItem.getQuantityAvailable(), Toast.LENGTH_SHORT).show();
                        } else {
                            holder.mTxtQty.setText((value + 1) + "");

                            String selectQunaitiy = holder.mTxtQty.getText().toString();
                            long productPrice = (Integer.parseInt(selectQunaitiy) * buySellItem.getPricePerUnit());
                            holder.mTxtTotalCost.setText(SiniiaUtils.getCurrencySymbol(siniiaPreferences.getCountryCode()) + " " + productPrice);

                            buySellItem.setSelectedQuantity(Integer.parseInt(selectQunaitiy));
                            buySellItem.setSelectedTotal(productPrice);

                            if (priceListner != null) {
                                priceListner.getTotalPrice(selectedArrayList);
                            }
                        }


                    }
                });


                long productPrice = (buySellItem.getMinQuantity() * buySellItem.getPricePerUnit());
                holder.mTxtTotalCost.setText(SiniiaUtils.getCurrencySymbol(siniiaPreferences.getCountryCode()) + " " + productPrice);
                buySellItem.setSelectedQuantity(buySellItem.getMinQuantity());
                buySellItem.setSelectedTotal(productPrice);
                if (priceListner != null) {
                    priceListner.getTotalPrice(selectedArrayList);
                }


                holder.productBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            selectedArrayList.put(buySellItem.getId() + "", buySellItem);
                        } else {
                            selectedArrayList.remove(buySellItem.getId() + "");
                        }
                        if (priceListner != null) {
                            priceListner.getTotalPrice(selectedArrayList);
                        }
                    }
                });

                holder.mImgDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //    selectedArrayList.remove(buySellItem.getId() + "");
                        if (priceListner != null) {
                            priceListner.deleteCartItem(buySellItem.getId() + "");
                        }
                    }
                });

                holder.bind(buySellItem);
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    public Map<String, Products> getSelectedArrayList() {
        return selectedArrayList;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView txtProductName, txtProductType, txtPricePerUnit, txtQunatityTypePriceUnit, mTxtTotalCost, mTxtQty;
        ImageView mImgCart, mImgDelete, mImgMinus, mImgAdd;
        //  Spinner mSpQty;
        CheckBox productBox;

        public ItemViewHolder(View view) {
            super(view);
            txtProductName = (TextView) view.findViewById(R.id.txt_product_name);
            txtProductType = (TextView) view.findViewById(R.id.txt_product_type);
            txtQunatityTypePriceUnit = (TextView) view.findViewById(R.id.txt_qunatity_type_per_unit);
            mTxtTotalCost = (TextView) view.findViewById(R.id.txt_total_cost);
            txtPricePerUnit = (TextView) view.findViewById(R.id.txt_product_per_unit);
            //    mSpQty = (Spinner) view.findViewById(R.id.sp_qunatity);
            mImgCart = (ImageView) view.findViewById(R.id.img_cart);
            productBox = (CheckBox) view.findViewById(R.id.item_check_box);
            mImgDelete = (ImageView) view.findViewById(R.id.img_delete);
            mImgMinus = (ImageView) view.findViewById(R.id.ic_minus_product);
            mImgAdd = (ImageView) view.findViewById(R.id.ic_add_product);
            mTxtQty = (TextView) view.findViewById(R.id.sp_qunatity);


        }

        public void bind(Products buySellItem) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }


}
