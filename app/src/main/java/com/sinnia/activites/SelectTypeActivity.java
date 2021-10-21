package com.sinnia.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sinnia.R;
import com.sinnia.data.selecttype.ApiResponse;
import com.sinnia.fragments.orders.FragmentShipperDialog;
import com.sinnia.listeners.TypeListener;
import com.sinnia.network.HttpHelper;
import com.sinnia.preferences.SiniiaPreferences;
import com.sinnia.utils.SiniiaConstants;
import com.sinnia.utils.SiniiaUtils;

public class SelectTypeActivity extends AppCompatActivity implements View.OnClickListener, TypeListener {


    private static final String TAG = "SelectTypeActivity";
    private Button mBtnFarmer, mBtnRetailer, mBtnBuyer, mBtnShipper;

    private Context mContext;

    private SiniiaPreferences siniiaPreferences;
    private TypeListener typeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_type);
        mContext = getApplicationContext();
        siniiaPreferences = new SiniiaPreferences(mContext);
        mBtnFarmer = (Button) findViewById(R.id.btn_farmer);
        mBtnRetailer = (Button) findViewById(R.id.btn_retailer);
        mBtnBuyer = (Button) findViewById(R.id.btn_buyer);
        mBtnShipper = (Button) findViewById(R.id.btn_shipper);

        mBtnFarmer.setOnClickListener(this);
        mBtnRetailer.setOnClickListener(this);
        mBtnBuyer.setOnClickListener(this);
        mBtnShipper.setOnClickListener(this);

        if (siniiaPreferences.getUserType() != null) {
            switch (siniiaPreferences.getUserType()) {
                case SiniiaConstants.FARMER:
                    SiniiaUtils.selectedButton(mContext, mBtnFarmer);
                    break;
                case SiniiaConstants.BUYER:
                    SiniiaUtils.selectedButton(mContext, mBtnBuyer);
                    break;
                case SiniiaConstants.SHIPPER:
                    SiniiaUtils.selectedButton(mContext, mBtnShipper);
                    break;
                case SiniiaConstants.RETAILER:
                    SiniiaUtils.selectedButton(mContext, mBtnRetailer);
                    break;
            }
        }
        siniiaPreferences.setUserType(SiniiaConstants.FARMER);
        typeListener = this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_farmer:
                siniiaPreferences.setUserType(SiniiaConstants.FARMER);
                SiniiaUtils.selectedButton(mContext, mBtnFarmer);
                SiniiaUtils.unSelectedButton(mContext, mBtnRetailer);
                SiniiaUtils.unSelectedButton(mContext, mBtnBuyer);
                SiniiaUtils.unSelectedButton(mContext, mBtnShipper);
                updateUserType();
                break;
            case R.id.btn_retailer:
                siniiaPreferences.setUserType(SiniiaConstants.RETAILER);
                SiniiaUtils.unSelectedButton(mContext, mBtnFarmer);
                SiniiaUtils.selectedButton(mContext, mBtnRetailer);
                SiniiaUtils.unSelectedButton(mContext, mBtnBuyer);
                SiniiaUtils.unSelectedButton(mContext, mBtnShipper);
                updateUserType();
                break;
            case R.id.btn_buyer:
                siniiaPreferences.setUserType(SiniiaConstants.BUYER);
                SiniiaUtils.unSelectedButton(mContext, mBtnFarmer);
                SiniiaUtils.unSelectedButton(mContext, mBtnRetailer);
                SiniiaUtils.selectedButton(mContext, mBtnBuyer);
                SiniiaUtils.unSelectedButton(mContext, mBtnShipper);
                updateUserType();

                break;
            case R.id.btn_shipper:
                siniiaPreferences.setUserType(SiniiaConstants.SHIPPER);
                SiniiaUtils.unSelectedButton(mContext, mBtnFarmer);
                SiniiaUtils.unSelectedButton(mContext, mBtnRetailer);
                SiniiaUtils.unSelectedButton(mContext, mBtnBuyer);
                SiniiaUtils.selectedButton(mContext, mBtnShipper);

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                FragmentShipperDialog approveDialog = new FragmentShipperDialog();
                approveDialog.show(ft, "dialog");
                approveDialog.setListener(typeListener);
                approveDialog.setCancelable(false);


                break;

        }
    }

    void updateUserType() {
        if (SiniiaUtils.checkForNetworkConnectivity(mContext)) {
            new UpdateUserType().execute();
        } else {
            Toast.makeText(mContext, getString(R.string.please_check_internet_connection), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void clickOnListener() {
        updateUserType();
    }


    public class UpdateUserType extends AsyncTask<String, Void, String> {

        private ProgressDialog dialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(SelectTypeActivity.this);
            dialog.setMessage("Please wait.....");
            dialog.show();

        }

        @Override
        protected String doInBackground(String... strings) {
            String result = new HttpHelper().updateUserType(siniiaPreferences.getUserType(), siniiaPreferences.getUserId());
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
                    ApiResponse selectTypeResponse = new Gson().fromJson(s, ApiResponse.class);
                    if (selectTypeResponse != null) {
                        int status = selectTypeResponse.getStatus();
                        if (status == 200) {
                            String name = siniiaPreferences.getUserName();
                            siniiaPreferences.setRegisterStatus(SiniiaConstants.REGISTERED);
                            Intent intent = new Intent(mContext, MainActivity.class);
                            startActivity(intent);
                            finishAffinity();

                        } else {
                            Toast.makeText(mContext, selectTypeResponse.getDescription(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
