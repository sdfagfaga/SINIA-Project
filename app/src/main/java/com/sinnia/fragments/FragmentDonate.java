package com.sinnia.fragments;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.sinnia.R;
import com.sinnia.activites.MainActivity;
import com.sinnia.data.AddDonation;
import com.sinnia.data.productdetails.PlaceOrder;
import com.sinnia.data.products.Products;
import com.sinnia.data.selecttype.ApiResponse;
import com.sinnia.fragments.orders.FragmentCartDetails;
import com.sinnia.fragments.orders.FragmentPayment;
import com.sinnia.fragments.orders.FragmentPaymentDetails;
import com.sinnia.network.HttpHelper;
import com.sinnia.paypal.PayPalConfig;
import com.sinnia.preferences.SiniiaPreferences;
import com.sinnia.utils.SiniiaUtils;

import org.json.JSONException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class FragmentDonate extends Fragment implements View.OnClickListener {

    private Context mContext;

    private static final String TAG = "FragmentDonate";
    private EditText mEdtDonateAmount;
    private Button mBtnDonate;
    private SiniiaPreferences siniiaPreferences;

    //Paypal Configuration Object
    private static PayPalConfiguration config = new PayPalConfiguration()
            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfig.PAYPAL_CONFIG_ENVIROMENT)
            .clientId(PayPalConfig.PAYPAL_CLIENT_ID);

    public static final int PAYPAL_REQUEST_CODE = 123;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_donate_layout, container, false);
        mContext = getContext();
        mEdtDonateAmount = (EditText) rootView.findViewById(R.id.edt_donate);
        mBtnDonate = (Button) rootView.findViewById(R.id.btn_donate);
        mBtnDonate.setOnClickListener(this);
        siniiaPreferences = new SiniiaPreferences(mContext);

        Intent intent = new Intent(mContext, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        getActivity().startService(intent);


        ImageView leftArrow = (ImageView) rootView.findViewById(R.id.ic_left_arrow);
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null && getActivity().getSupportFragmentManager() != null)
                    getActivity().getSupportFragmentManager().popBackStack();
            }
        });


        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_donate:
                String donateAmount = mEdtDonateAmount.getText().toString();
                if (donateAmount != null && !donateAmount.isEmpty()) {
                    if (SiniiaUtils.checkForNetworkConnectivity(mContext)) {
                        getPaypalPayment(donateAmount);
                    } else {
                        Toast.makeText(mContext, getString(R.string.please_check_internet_connection), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, "Please enter amount", Toast.LENGTH_SHORT).show();
                }
                break;
        }

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

                        AddDonation addDonation = new AddDonation();
                        addDonation.setUserId(siniiaPreferences.getUserId());
                        addDonation.setPaymentAmount(Double.parseDouble(mEdtDonateAmount.getText().toString()));
                        addDonation.setPaymentDetails(paymentDetails);


                        if (SiniiaUtils.checkForNetworkConnectivity(mContext)) {
                            new DonateTask().execute(new Gson().toJson(addDonation).toString());
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

    private void getPaypalPayment(String paymentAmount) {
        //Creating a paypalpayment
        PayPalPayment payment = new PayPalPayment(SiniiaUtils.convertInDollar(siniiaPreferences.getCountryCode(), Double.parseDouble(paymentAmount)),
                SiniiaUtils.getCurrencyCode(siniiaPreferences.getCountryCode()), "Donate to Framers Charity",
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

    public class DonateTask extends AsyncTask<String, Void, String> {
        private ProgressDialog dialog;


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
            String result = new HttpHelper().addDonation(strings[0]);
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
                            if (getActivity() != null && getActivity().getSupportFragmentManager() != null)
                                getActivity().getSupportFragmentManager().popBackStack();
                            Toast.makeText(mContext, "You have been donated for farmers charity", Toast.LENGTH_SHORT).show();
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
