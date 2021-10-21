package com.sinnia.fragments.orders;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.sinnia.R;
import com.sinnia.activites.MainActivity;
import com.sinnia.adapter.ShipperAdapter;
import com.sinnia.data.cart.UserAddress;
import com.sinnia.data.productdetails.PlaceOrder;
import com.sinnia.data.products.Products;
import com.sinnia.data.shippers.Shipper;
import com.sinnia.data.shippers.ShipperData;
import com.sinnia.data.shippers.ShipperResponse;
import com.sinnia.data.shippers.ShippersFromApi;
import com.sinnia.listeners.RadioButtonListener;
import com.sinnia.network.HttpHelper;
import com.sinnia.preferences.SiniiaPreferences;
import com.sinnia.utils.SiniiaUtils;

import java.util.ArrayList;
import java.util.List;

public class FragmentShipping extends Fragment implements View.OnClickListener, RadioButtonListener {


    private static final String TAG = "FragmentShipping";
    public static final String SHIPPER_DATA = "Shipper_data";
    private Context mContext;
    private SiniiaPreferences siniiaPreferences;
    private ArrayList<Products> orderProductsList;
    private RecyclerView mRvShipper;
    private RadioButtonListener radioButtonListener;
    private ShipperAdapter shipperAdapter;
    private String selectedShipperId;
    public static String SHIPPING_ID = "shipperId";

    private int selectedAddressId;
    private UserAddress userAddress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shipping_layout, container, false);
        mContext = getActivity();
        siniiaPreferences = new SiniiaPreferences(mContext);
        radioButtonListener = this;
        mRvShipper = (RecyclerView) rootView.findViewById(R.id.rv_recycler);
        TextView mTxtTotalPrice = (TextView) rootView.findViewById(R.id.txt_total);
        ImageView leftArrow = (ImageView) rootView.findViewById(R.id.ic_left_arrow);
        leftArrow.setOnClickListener(this);

        Button mBtnPlaceOrder = (Button) rootView.findViewById(R.id.btn_continue);
        mBtnPlaceOrder.setOnClickListener(this);
        if (getArguments() != null) {
            orderProductsList = (ArrayList<Products>) getArguments().getSerializable(FragmentCartDetails.SUMMARY);
            selectedAddressId = getArguments().getInt(FragmentCartDetails.SELECT_ADDRESS_ID);
            userAddress = (UserAddress) getArguments().getSerializable(FragmentCartDetails.SELECTED_ADDRESS);
            if (orderProductsList != null) {
                double totalPrice = 0;
                for (Products products : orderProductsList) {
                    totalPrice = totalPrice + products.getSelectedTotal();
                }
                mTxtTotalPrice.setText(SiniiaUtils.getCurrencySymbol(siniiaPreferences.getCountryCode()) + totalPrice);
            }
        }

        if (SiniiaUtils.checkForNetworkConnectivity(mContext)) {
            List<PlaceOrder> placeOrderList = new ArrayList<>();
            for (Products products : orderProductsList) {
                PlaceOrder placeOrder = new PlaceOrder();
                placeOrder.setProductId(products.getId() + "");
                placeOrder.setUserId(siniiaPreferences.getUserId());
                placeOrder.setQuantity(products.getSelectedQuantity() + "");
                placeOrder.setQuantityPrice(products.getSelectedTotal() + "");
                placeOrder.setUserAddress(userAddress);
                placeOrderList.add(placeOrder);
            }
            new GetShippers().execute(new Gson().toJson(placeOrderList).toString());
        } else {
            Toast.makeText(mContext, getString(R.string.please_check_internet_connection), Toast.LENGTH_SHORT).show();
        }

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ic_left_arrow:
                if (getActivity() != null && getActivity().getSupportFragmentManager() != null) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
                break;
            case R.id.btn_continue:
                if (shipperAdapter != null) {
                    selectedShipperId = shipperAdapter.getSelectedShipperId();
                }
                if (selectedShipperId != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString(SHIPPING_ID, selectedShipperId);
                    bundle.putSerializable(FragmentCartDetails.SUMMARY, orderProductsList);
                    bundle.putSerializable(FragmentCartDetails.SELECTED_ADDRESS, userAddress);
                    bundle.putInt(FragmentCartDetails.SELECT_ADDRESS_ID, selectedAddressId);
                  //  bundle.putSerializable(FragmentShipping.SHIPPER_DATA, shipperAdapter.getSelectedShipperRate());
                    ((MainActivity) mContext).launchFragment(new FragmentPayment(), getString(R.string.fragment_payment), bundle);
                } else {
                    Toast.makeText(mContext, "No shippers available", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    @Override
    public void clickOnShipperRadioButton(String shipperId) {
        selectedShipperId = shipperId;
    }

    public class GetShippers extends AsyncTask<String, Void, String> {

        private ProgressDialog dialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Please wait.....");
            dialog.show();

        }

        @Override
        protected String doInBackground(String... strings) {
            String result = new HttpHelper().getShipperData(SiniiaUtils.getCountryName(siniiaPreferences.getCountryCode()));
            //  String result=new HttpHelper().getShipperDataByrder(strings[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (s != null) {
                    ShippersFromApi shipperResponse = new Gson().fromJson(s, ShippersFromApi.class);
                    if (shipperResponse != null && shipperResponse.getStatus() == 200) {
                        if (shipperResponse.getData() != null && shipperResponse.getData().get(0) != null) {
                            shipperAdapter = new ShipperAdapter(mContext, shipperResponse.getData(), null, selectedShipperId, radioButtonListener);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                            mRvShipper.setLayoutManager(mLayoutManager);
                            mRvShipper.setItemAnimator(new DefaultItemAnimator());
                            mRvShipper.setAdapter(shipperAdapter);
                            shipperAdapter.notifyDataSetChanged();
                        }

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
