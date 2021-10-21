package com.sinnia.fragments.orders;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.places.Place;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.sinnia.R;
import com.sinnia.activites.MainActivity;
import com.sinnia.activites.OTPVerifyActivity;
import com.sinnia.data.cart.UserAddress;
import com.sinnia.data.login.LoginRequest;
import com.sinnia.data.productdetails.PlaceOrder;
import com.sinnia.data.products.Products;
import com.sinnia.data.selecttype.ApiResponse;
import com.sinnia.data.shippers.ShipperRate;
import com.sinnia.network.HttpHelper;
import com.sinnia.paypal.ConfirmationActivity;
import com.sinnia.paypal.PayPalConfig;
import com.sinnia.preferences.SiniiaPreferences;
import com.sinnia.utils.SiniiaUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class FragmentPayment extends Fragment implements View.OnClickListener {

    private static final String TAG = "FragmentPayment";
    private Context mContext;
    private SiniiaPreferences siniiaPreferences;
    private TextView mTxtTotalPrice;
    private ArrayList<Products> orderProductsList;
    private String shipperId;


    //Paypal intent request code to track onActivityResult method
    public static final int PAYPAL_REQUEST_CODE = 123;
    private double paymentAmount;
    private UserAddress userAddress;


    //Paypal Configuration Object
    private static PayPalConfiguration config = new PayPalConfiguration()
            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfig.PAYPAL_CONFIG_ENVIROMENT)
            .clientId(PayPalConfig.PAYPAL_CLIENT_ID);


    private RadioButton mRdPayPal, mRdMobilePayment;
    private static final String PAY_PAL = "pay_pal";
    private static final String MOBILE_PAYMENT = "mobile_payment";
    private String paymentType;
    private String provider;
    private ShipperRate shipperRate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_payment, container, false);
        mContext = getActivity();
        siniiaPreferences = new SiniiaPreferences(mContext);

        mTxtTotalPrice = (TextView) rootView.findViewById(R.id.txt_total);
        mRdPayPal = (RadioButton) rootView.findViewById(R.id.rb_paypal);
        mRdMobilePayment = (RadioButton) rootView.findViewById(R.id.rb_mobile_payment);

        ImageView leftArrow = (ImageView) rootView.findViewById(R.id.ic_left_arrow);
        leftArrow.setOnClickListener(this);

        Button mBtnPlaceOrder = (Button) rootView.findViewById(R.id.btn_continue);
        mBtnPlaceOrder.setOnClickListener(this);
        if (getArguments() != null) {
            orderProductsList = (ArrayList<Products>) getArguments().getSerializable(FragmentCartDetails.SUMMARY);
            userAddress = (UserAddress) getArguments().getSerializable(FragmentCartDetails.SELECTED_ADDRESS);
            shipperId = getArguments().getString(FragmentShipping.SHIPPING_ID);
            shipperRate = (ShipperRate) getArguments().getSerializable(FragmentShipping.SHIPPER_DATA);

            if (orderProductsList != null) {
                double totalPrice = 0;
                for (Products products : orderProductsList) {
                    totalPrice = totalPrice + products.getSelectedTotal();
                }
                paymentAmount = totalPrice;

                /*adding shipper amount*/
                DecimalFormat df2 = new DecimalFormat("#.##");
                if (shipperRate != null && shipperRate.getAmount() != null) {
                    paymentAmount = paymentAmount + shipperRate.getAmount();
                }
                paymentAmount = Math.round(paymentAmount * 100.0) / 100.0;


                mTxtTotalPrice.setText(SiniiaUtils.getCurrencySymbol(siniiaPreferences.getCountryCode()) + paymentAmount);
            }
        }

        Intent intent = new Intent(mContext, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        getActivity().startService(intent);
        paymentType = PAY_PAL;


        return rootView;
    }

    @Override
    public void onDestroy() {
        getActivity().stopService(new Intent(mContext, PayPalService.class));
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //If the result is from paypal
        if (requestCode == PAYPAL_REQUEST_CODE) {
            //If the result is OK i.e. user has not canceled the payment
            if (resultCode == Activity.RESULT_OK) {
                //Getting the payment confirmation
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                //if confirmation is not null
                if (confirm != null) {
                    try {
                        //Getting the payment details
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        Log.i("paymentExample", paymentDetails);


                        Log.d(TAG, "XXXXXXXXXXXx On activity Result Address " + userAddress);
                        if (SiniiaUtils.checkForNetworkConnectivity(mContext)) {
                            List<PlaceOrder> placeOrderList = new ArrayList<>();
                            for (Products products : orderProductsList) {
                                PlaceOrder placeOrder = new PlaceOrder();
                                placeOrder.setProductId(products.getId() + "");
                                placeOrder.setPaymentDetails(paymentDetails);
                                placeOrder.setPaymentAmount(paymentAmount + "");
                                placeOrder.setUserId(siniiaPreferences.getUserId());
                             //   placeOrder.setShipmentId(shipperId + "");
                                placeOrder.setQuantity(products.getSelectedQuantity() + "");
                                placeOrder.setQuantityPrice(products.getSelectedTotal() + "");
                                /*Need to add delivery address Id */
                                //   placeOrder.setDeliveryAddressId(selectedAddressId + "");
                                placeOrder.setUserAddress(userAddress);
                                placeOrder.setPaymentType(paymentType);
                                placeOrder.setShipmentObjectId(shipperId);
                              //  placeOrder.setProvider(shipperRate.getProvider());
                                placeOrderList.add(placeOrder);
                            }
                            new PlaceOrderTask().execute(new Gson().toJson(placeOrderList).toString(), paymentDetails, paymentAmount + "");
                        } else {
                            Toast.makeText(mContext, getString(R.string.please_check_internet_connection), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ic_left_arrow:
                if (getActivity() != null && getActivity().getSupportFragmentManager() != null)
                    getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.btn_continue:
                if (mRdPayPal.isChecked()) {
                    paymentType = PAY_PAL;
                    if (SiniiaUtils.checkForNetworkConnectivity(mContext)) {
                        getPaypalPayment();
                    } else {
                        Toast.makeText(mContext, getString(R.string.please_check_internet_connection), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    paymentType = MOBILE_PAYMENT;
                    Toast.makeText(mContext, "Implementing...", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void getPaypalPayment() {


        /*Converting amount in Based on country code.*/
        BigDecimal pay = null;
        if (siniiaPreferences.getCountryCode().equalsIgnoreCase("91")) {
            pay = SiniiaUtils.convertInDollar(siniiaPreferences.getCountryCode(), paymentAmount);
        } else {
            pay = new BigDecimal(paymentAmount);
        }


        //Creating a paypalpayment
        PayPalPayment payment = new PayPalPayment(pay,
                SiniiaUtils.getCurrencyCode(siniiaPreferences.getCountryCode()), "Ordering Product",
                PayPalPayment.PAYMENT_INTENT_SALE);

        //Creating Paypal Payment activity intent
        Intent intent = new Intent(mContext, PaymentActivity.class);
        //putting the paypal configuration to the intent
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        //Puting paypal payment to the intent
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        //Starting the intent activity for result
        //the request code will be used on the method onActivityResult
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    public class PlaceOrderTask extends AsyncTask<String, Void, String> {
        private ProgressDialog dialog;
        private String paymentDetails;
        private String paymentAmount;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Please wait.....");
            if (getActivity() != null) {
                dialog.show();
            }

        }

        @Override
        protected String doInBackground(String... strings) {
            paymentDetails = strings[1];
            paymentAmount = strings[2];
            String result = new HttpHelper().placeAnOrder(strings[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                Log.d(TAG, "XXXXXXXXXXXXx result " + s);
                if (s != null) {
                    ApiResponse apiResponse = new Gson().fromJson(s, ApiResponse.class);
                    if (apiResponse != null) {
                        if (apiResponse.getStatus() == 200) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(FragmentCartDetails.SUMMARY, orderProductsList);
                            bundle.putString("PaymentDetails", paymentDetails);
                            bundle.putString("PaymentAmount", paymentAmount);
                            bundle.putSerializable(FragmentCartDetails.ADDRESS, userAddress);
                            ((MainActivity) mContext).launchFragment(new FragmentPaymentDetails(), getString(R.string.fragment_payment_details), bundle);
                        }
                        Toast.makeText(mContext, apiResponse.getDescription(), Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


}
