package com.sinnia.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sinnia.R;
import com.sinnia.data.products.Products;
import com.sinnia.listeners.ItemClickListener;
import com.sinnia.listeners.SeeAllLisentenrs;
import com.sinnia.utils.SectionModel;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by AXIOM on 02-04-2018.
 */

public class SectionRecyclerViewAdapter extends RecyclerView.Adapter<SectionRecyclerViewAdapter.SectionViewHolder> {


    class SectionViewHolder extends RecyclerView.ViewHolder {
        private TextView sectionLabel;
        private RecyclerView itemRecyclerView;
        private TextView mTxtSeeAll;

        public SectionViewHolder(View itemView) {
            super(itemView);
            sectionLabel = (TextView) itemView.findViewById(R.id.section_label);
            itemRecyclerView = (RecyclerView) itemView.findViewById(R.id.item_recycler_view);
            mTxtSeeAll=(TextView)itemView.findViewById(R.id.txt_see_all);
        }
    }

    private Context context;
    private ArrayList<SectionModel> sectionModelArrayList;
    private ItemClickListener mItemClickListener;
    private SeeAllLisentenrs seeAllLisentenrs;

    private List<SectionModel> originDataList;




    public SectionRecyclerViewAdapter(Context context, ArrayList<SectionModel> sectionModelArrayList,
                                      ItemClickListener itemClickListener, SeeAllLisentenrs seeAllLisentenrs) {
        this.context = context;
        this.sectionModelArrayList = sectionModelArrayList;
        this.mItemClickListener=itemClickListener;
        this.seeAllLisentenrs=seeAllLisentenrs;

        this.originDataList=new ArrayList<>();
        this.originDataList.addAll(sectionModelArrayList);
    }

    @Override
    public SectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.section_custom_row_layout, parent, false);
        return new SectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SectionViewHolder holder, int position) {
        final SectionModel sectionModel = sectionModelArrayList.get(position);
        holder.sectionLabel.setText(sectionModel.getSectionLabel());
        //recycler view for items
        holder.itemRecyclerView.setHasFixedSize(true);
        holder.itemRecyclerView.setNestedScrollingEnabled(false);

        ProductsAdapter adapter = new ProductsAdapter(context, sectionModel.getItemArrayList(),mItemClickListener);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        holder.itemRecyclerView.setLayoutManager(linearLayoutManager);
        holder.itemRecyclerView.setItemAnimator(new DefaultItemAnimator());
        holder.itemRecyclerView.setAdapter(adapter);


        holder.mTxtSeeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(seeAllLisentenrs!=null){
                    seeAllLisentenrs.seeAll();
                }
            }
        });

        adapter.notifyDataSetChanged();


    }

    @Override
    public int getItemCount() {
        return sectionModelArrayList.size();
    }

    public void alertFilter(String newText) {
        String charSet = newText.toLowerCase();
        sectionModelArrayList.clear();
        if (charSet.length() == 0) {
            sectionModelArrayList.addAll(originDataList);
        } else {
            for (SectionModel sectionModel : originDataList) {
                List<Products> totalList =sectionModel.getItemArrayList();
                List<Products> selectedList=new ArrayList<>();
                for(Products products : totalList)
                {
                    if(products.getProductName().toLowerCase().contains(charSet)){
                        selectedList.add(products);
                    }
                }
                SectionModel add = new SectionModel(sectionModel.getSectionLabel(), selectedList);
                sectionModelArrayList.add(add);
            }
        }
        notifyDataSetChanged();
    }




}
