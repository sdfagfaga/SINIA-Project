package com.sinnia.activites;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.sinnia.R;
import com.sinnia.data.count.CountResponse;
import com.sinnia.network.HttpHelper;
import com.sinnia.preferences.SiniiaPreferences;
import com.sinnia.utils.SiniiaConstants;

import java.util.ArrayList;
import java.util.List;

public class SplashScreen extends AppCompatActivity {


    private Context mContext;
    private SiniiaPreferences siniiaPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mContext = this;
        siniiaPreferences = new SiniiaPreferences(mContext);



        new GetCountTask().execute();
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {

                    if (checkPermission()) {
                        sleep(2 * 1000);
                        gotoNextActivity();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    public void gotoNextActivity() {
        Intent intent = null;
        switch (siniiaPreferences.getRegisterStatus()) {
            case 0:
                intent = new Intent(mContext, LoginActivity.class);
                break;
            case SiniiaConstants.OTP_SENT:
                intent = new Intent(mContext, OTPVerifyActivity.class);
                break;
            case SiniiaConstants.OTP_VERIFIED:
                intent = new Intent(mContext, SelectTypeActivity.class);
                break;
            case SiniiaConstants.SELECT_TYPE:
                intent = new Intent(mContext, RegisterActivity.class);
                break;
            case SiniiaConstants.REGISTERED:
                intent = new Intent(mContext, MainActivity.class);
                break;

        }

        startActivity(intent);
        finishAffinity();
    }

    private boolean checkPermission() {
        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int camera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int storage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);


        List<String> listPermissionsNeeded = new ArrayList<>();

        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    1);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        try {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                gotoNextActivity();
            } else {
                checkPermission();
                // We were not granted permission this time, so don't try to show the contact picker
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class GetCountTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String result = new HttpHelper().getCount(siniiaPreferences.getUserId());
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                if (s != null) {
                    CountResponse countResponse = new Gson().fromJson(s, CountResponse.class);
                    if (countResponse != null && countResponse.getStatus() == 200) {
                        if (countResponse.getCounts() != null) {
                            siniiaPreferences.setCartCount(Integer.parseInt(countResponse.getCounts().getBasketOrdersCount()));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
