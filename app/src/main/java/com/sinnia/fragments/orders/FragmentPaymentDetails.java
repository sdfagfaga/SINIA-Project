package com.sinnia.fragments.orders;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sinnia.R;
import com.sinnia.activites.MainActivity;
import com.sinnia.adapter.OrderSummaryAdapter;
import com.sinnia.data.cart.UserAddress;
import com.sinnia.data.products.Products;
import com.sinnia.fragments.FragmentBuySell;
import com.sinnia.fragments.FragmentPostAProduct;
import com.sinnia.preferences.SiniiaPreferences;
import com.sinnia.utils.SiniiaUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FragmentPaymentDetails extends Fragment implements View.OnClickListener {

    private Context mContext;
    private SiniiaPreferences siniiaPreferences;

    private RecyclerView mRvOrders;
    private static final String TAG = "FragmentPaymentDetails";
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_payment_details, container, false);
        mContext = getActivity();
        siniiaPreferences = new SiniiaPreferences(mContext);
        mRvOrders = (RecyclerView) rootView.findViewById(R.id.rv_recycler);
        Button mBtnGotoBuySell = (Button) rootView.findViewById(R.id.btn_goto_buy_sell);
        mBtnGotoBuySell.setOnClickListener(this);
        if (getArguments() != null) {
            try {
                ArrayList<Products> orderProductsList = (ArrayList<Products>) getArguments().getSerializable(FragmentCartDetails.SUMMARY);
                OrderSummaryAdapter orderSummaryAdapter = new OrderSummaryAdapter(mContext, orderProductsList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                mRvOrders.setLayoutManager(mLayoutManager);
                mRvOrders.setItemAnimator(new DefaultItemAnimator());
                mRvOrders.setAdapter(orderSummaryAdapter);
                orderSummaryAdapter.notifyDataSetChanged();
                UserAddress productAddress = (UserAddress) getArguments().getSerializable(FragmentCartDetails.ADDRESS);
                TextView mTxtPinCode = (TextView) rootView.findViewById(R.id.txt_deliver_pin_code);
                TextView mTxtAddress = (TextView) rootView.findViewById(R.id.txt_address);
                Log.d(TAG, "XXXXXXXXXXXXXXXXXXXx Product Address " + productAddress);
                if (productAddress != null) {
                    if (productAddress != null) {
                        String address = null;
                        if (productAddress.getAddress1() != null) {
                            address = productAddress.getAddress1();
                        }
                        if (productAddress.getAddress2() != null) {
                            address = address + ", " + productAddress.getAddress2();
                        }
                        if (productAddress.getLandmark() != null) {
                            address = address + ", " + productAddress.getLandmark();
                        }
                        if (productAddress.getPinCode() != null) {
                            address = address + ", " + productAddress.getPinCode();
                        }
                        mTxtAddress.setText(address);
                    }
                    mTxtPinCode.setText("Delivered Address " + productAddress.getPinCode());
                }
                JSONObject jsonDetails = new JSONObject(getArguments().getString("PaymentDetails"));
                showDetails(jsonDetails.getJSONObject("response"), getArguments().getString("PaymentAmount"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_goto_buy_sell:
                ((MainActivity) mContext).clearFragments();
                ((MainActivity) mContext).launchFragment(new FragmentBuySell(), getString(R.string.buy_sell_fragment));
                break;
        }
    }

    private void showDetails(JSONObject jsonDetails, String paymentAmount) throws JSONException {
        //Views
        TextView textViewId = (TextView) rootView.findViewById(R.id.paymentId);
        TextView textViewStatus = (TextView) rootView.findViewById(R.id.paymentStatus);
        TextView textViewAmount = (TextView) rootView.findViewById(R.id.paymentAmount);
        //Showing the details from json object
        textViewId.setText(jsonDetails.getString("id"));
        textViewStatus.setText(jsonDetails.getString("state"));
        textViewAmount.setText(paymentAmount + SiniiaUtils.getCurrencySymbol(siniiaPreferences.getCountryCode()));
    }
}
