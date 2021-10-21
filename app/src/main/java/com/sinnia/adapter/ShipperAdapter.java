package com.sinnia.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sinnia.R;
import com.sinnia.data.products.Products;
import com.sinnia.data.shippers.Shipper;
import com.sinnia.data.shippers.ShipperData;
import com.sinnia.data.shippers.ShipperMessage;
import com.sinnia.data.shippers.ShipperRate;
import com.sinnia.listeners.RadioButtonListener;

import java.util.List;

public class ShipperAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "ShipperAdapter";
    private Context mContext;
    private List<ShipperData> shipperDataList;
    private List<ShipperMessage> shipperMessageList;
    private String selectedShipperId;
    private RadioButtonListener radioButtonListener;


    public ShipperAdapter(Context mContext, List<ShipperData> shipperDataList,
                          List<ShipperMessage> shipperMessageList, String selectedShipperId,
                          RadioButtonListener radioButtonListener) {
        this.mContext = mContext;
        this.shipperDataList = shipperDataList;
        this.selectedShipperId = selectedShipperId;
        this.radioButtonListener = radioButtonListener;
        this.shipperMessageList = shipperMessageList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shipper_item_layout, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder recyleViewHolder, final int position) {
        final ItemViewHolder holder = (ItemViewHolder) recyleViewHolder;
        if (shipperDataList != null) {
            final ShipperData shipper = shipperDataList.get(position);
            selectedShipperId=shipper.getShipperContact();
            holder.mTxtShipperName.setText("Thank you for showing interest in the purchasing the product. The shipment details of the product will be shared by "+shipper.getShipperContact());
            holder.bind(shipper);
        }
    }

    @Override
    public int getItemCount() {
        if (shipperDataList != null) {
            return shipperDataList.size();
        } else {
            return 0;
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView mTxtShipperName;
        RelativeLayout mItemLayout;

        public ItemViewHolder(View view) {
            super(view);
            mTxtShipperName = (TextView) view.findViewById(R.id.txt_shipper_name);
            mItemLayout = (RelativeLayout) view.findViewById(R.id.relative_item);

        }

        public void bind(final ShipperData shipper) {

        }
    }

    public String getSelectedShipperId() {
        return selectedShipperId;
    }

}
