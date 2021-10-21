package com.sinnia.paypal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sinnia.R;
import com.sinnia.activites.MainActivity;
import com.sinnia.adapter.OrderSummaryAdapter;
import com.sinnia.data.cart.UserAddress;
import com.sinnia.data.products.Products;
import com.sinnia.fragments.FragmentBuySell;
import com.sinnia.fragments.orders.FragmentCartDetails;
import com.sinnia.preferences.SiniiaPreferences;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class ConfirmationActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = "ConfirmationActivity";
    private Context mContext;
    private SiniiaPreferences siniiaPreferences;

    private RecyclerView mRvOrders;
    private TextView mTxtPinCode, mTxtAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        UserAddress productAddress = null;
        //Getting Intent
        Intent intent = getIntent();
        try {
            JSONObject jsonDetails = new JSONObject(intent.getStringExtra("PaymentDetails"));
            productAddress = (UserAddress) getIntent().getSerializableExtra(FragmentCartDetails.ADDRESS);
            //Displaying payment details
            showDetails(jsonDetails.getJSONObject("response"), intent.getDoubleExtra("PaymentAmount", 0.0));
        } catch (JSONException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        mContext = this;
        siniiaPreferences = new SiniiaPreferences(mContext);
        mRvOrders = (RecyclerView) findViewById(R.id.rv_recycler);
        mTxtPinCode = (TextView) findViewById(R.id.txt_deliver_pin_code);
        mTxtAddress = (TextView) findViewById(R.id.txt_address);
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


        Button mBtnGotoBuySell = (Button) findViewById(R.id.btn_goto_buy_sell);
        mBtnGotoBuySell.setOnClickListener(this);
        if (getIntent() != null) {
            ArrayList<Products> orderProductsList = (ArrayList<Products>) getIntent().getSerializableExtra(FragmentCartDetails.SUMMARY);
            OrderSummaryAdapter orderSummaryAdapter = new OrderSummaryAdapter(mContext, orderProductsList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
            mRvOrders.setLayoutManager(mLayoutManager);
            mRvOrders.setItemAnimator(new DefaultItemAnimator());
            mRvOrders.setAdapter(orderSummaryAdapter);
            orderSummaryAdapter.notifyDataSetChanged();
        }
    }

    private void showDetails(JSONObject jsonDetails, double paymentAmount) throws JSONException {
        //Views
        TextView textViewId = (TextView) findViewById(R.id.paymentId);
        TextView textViewStatus = (TextView) findViewById(R.id.paymentStatus);
        TextView textViewAmount = (TextView) findViewById(R.id.paymentAmount);

        //Showing the details from json object
        textViewId.setText(jsonDetails.getString("id"));
        textViewStatus.setText(jsonDetails.getString("state"));
        textViewAmount.setText(paymentAmount + " USD");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_goto_buy_sell:
              //  ((MainActivity) mContext).clearFragments();
                launchFragment(new FragmentBuySell(), getString(R.string.buy_sell_fragment));
                break;
        }
    }

    public void launchFragment(Fragment fragment, String fragmentName) {
        FragmentManager manager = getSupportFragmentManager();
        Fragment homeFrag = getSupportFragmentManager().findFragmentById(R.id.fragment);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fragment, fragment, fragmentName);
        transaction.hide(homeFrag);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }
}
