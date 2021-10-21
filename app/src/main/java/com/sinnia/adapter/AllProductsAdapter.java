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
import com.sinnia.utils.AllProducts;
import com.sinnia.utils.SiniiaUtils;

import java.util.ArrayList;
import java.util.List;

public class AllProductsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "ProductsAdapter";
    private Context mContext;
    private List<Products> allProductsList;
    private ItemClickListener itemClickListener;

    private List<Products> originDataList;

    public AllProductsAdapter(Context mContext, List<Products> allProductsList, ItemClickListener itemClickListener) {
        this.mContext = mContext;
        this.allProductsList = allProductsList;
        this.itemClickListener=itemClickListener;

        this.originDataList=new ArrayList<>();
        this.originDataList.addAll(allProductsList);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.all_products_item, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder recyleViewHolder, int position) {
        final ItemViewHolder holder = (ItemViewHolder) recyleViewHolder;
        Log.d(TAG, "XXXXXXXXXXXXXXXXX size " + allProductsList.size());
        final Products allProducts = allProductsList.get(position);
        if (allProducts != null) {
            holder.txtProductName.setText(allProducts.getProductName());
            holder.txtCost.setText("R "+allProducts.getPricePerUnit());
            holder.txtProductNumber.setText("( "+allProducts.getQuantityType()+" - "+allProducts.getQuantityPerUnit()+" )");
            holder.txtProductType.setText("Type : "+allProducts.getProductType());
            holder.txtProductMinOTY.setText("Minm Qty : "+allProducts.getMinQuantity());
            String url = allProducts.getThumbImageURL();
            if (url != null) {
                if (url.contains(",")) {
                    SiniiaUtils.loadPicassoImageWithOutCircle(mContext, url.split(",")[0], holder.mImgProducts);
                } else {
                    SiniiaUtils.loadPicassoImageWithOutCircle(mContext, url, holder.mImgProducts);
                }
            }
            holder.bind(allProducts);
        }

    }

    @Override
    public int getItemCount() {
        if (allProductsList != null) {
            return allProductsList.size();
        } else {
            return 0;
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView txtProductName, txtCost,txtProductNumber,txtProductType,txtProductMinOTY;
        ImageView mImgProducts;

        public ItemViewHolder(View view) {
            super(view);
            txtProductName = (TextView) view.findViewById(R.id.txt_product_name);
            txtCost = (TextView) view.findViewById(R.id.txt_product_cost);
            txtProductNumber = (TextView) view.findViewById(R.id.txt_product_number);
            txtProductType = (TextView) view.findViewById(R.id.txt_product_type);
            txtProductMinOTY = (TextView) view.findViewById(R.id.txt_minm_qty);
            mImgProducts=(ImageView)view.findViewById(R.id.img_product);
        }

        public void bind(final Products allProducts) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(itemClickListener!=null){
                        itemClickListener.clickOnRVItem(allProducts);
                    }
                }
            });
        }
    }

    public void alertFilter(String newText) {
        String charSet = newText.toLowerCase();
        allProductsList.clear();
        if (charSet.length() == 0) {
            allProductsList.addAll(originDataList);
        } else {
            for (Products products : originDataList) {
                if (products.getProductName().toLowerCase().contains(charSet)) {
                    allProductsList.add(products);
                }
            }
        }
        notifyDataSetChanged();
    }
}