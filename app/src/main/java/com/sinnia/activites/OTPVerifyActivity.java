package com.sinnia.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.common.api.Api;
import com.google.gson.Gson;
import com.sinnia.R;
import com.sinnia.data.login.LoginRequest;
import com.sinnia.data.login.LoginResponse;
import com.sinnia.data.login.User;
import com.sinnia.data.login.UserDataList;
import com.sinnia.data.selecttype.ApiResponse;
import com.sinnia.data.verifyotp.VerifyOTPResponse;
import com.sinnia.network.HttpHelper;
import com.sinnia.preferences.SiniiaPreferences;
import com.sinnia.utils.SiniiaConstants;
import com.sinnia.utils.SiniiaUtils;

import java.util.List;

public class OTPVerifyActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBtnVerify;
    private TextView mTxtPhoneNumber, mTxtResendOTP;
    private PinView mPinOTP;
    private Context mContext;

    private static final String TAG = "OTPVerifyActivity";
    private SiniiaPreferences siniiaPreferences;
    private String otpTo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p_verify);
        mContext = this;
        siniiaPreferences = new SiniiaPreferences(mContext);

        mBtnVerify = (Button) findViewById(R.id.btn_verify);
        mTxtPhoneNumber = (TextView) findViewById(R.id.txt_phone_number_text);
        mTxtResendOTP = (TextView) findViewById(R.id.txt_resend_otp);
        mPinOTP = (PinView) findViewById(R.id.firstPinView);
        mBtnVerify.setOnClickListener(this);
        mTxtResendOTP.setOnClickListener(this);


      /*  if (siniiaPreferences.getMobileNumber() != null) {
            otpTo = siniiaPreferences.getCountryCode() + "-" + siniiaPreferences.getMobileNumber();
        } else {*/
        otpTo = siniiaPreferences.getEmail();
        //   }

        mTxtPhoneNumber.setText("Please type verification code send to " + otpTo);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_verify:
                if (SiniiaUtils.checkForNetworkConnectivity(mContext)) {
                    String otp = mPinOTP.getText().toString();
                    if (otp != null && !otp.isEmpty()) {
                        new VerifyOTP().execute(otp);
                    } else {
                        Toast.makeText(mContext, getString(R.string.please_check_otp), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, getString(R.string.please_check_internet_connection), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.txt_resend_otp:

                /*Resend OTP*/
                LoginRequest loginRequest = new LoginRequest();
              /*  if (siniiaPreferences.getMobileNumber() != null) {
                    loginRequest.setMobileCountry(siniiaPreferences.getCountryCode());
                    loginRequest.setMobileNumber(siniiaPreferences.getMobileNumber());
                } else {*/
                loginRequest.setEmail(siniiaPreferences.getEmail());
                //   }
                new ResendOTP().execute(new Gson().toJson(loginRequest).toString());

                break;
        }
    }

    public class VerifyOTP extends AsyncTask<String, Void, String> {

        private ProgressDialog dialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(OTPVerifyActivity.this);
            dialog.setMessage("Please wait.....");
            dialog.show();

        }

        @Override
        protected String doInBackground(String... strings) {
            //   String result = new HttpHelper().getVerifyOTP(otpTo, strings[0]);
            String result = new HttpHelper().verifyEmailWithOTP(strings[0], siniiaPreferences.getEmail());
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
                    VerifyOTPResponse verifyOTPResponse = new Gson().fromJson(s, VerifyOTPResponse.class);
                    if (verifyOTPResponse != null) {
                        String status = verifyOTPResponse.getStatus();
                        if (status != null && status.equalsIgnoreCase("200")) {
                            siniiaPreferences.setRegisterStatus(SiniiaConstants.OTP_VERIFIED);
                            UserDataList userDataList = verifyOTPResponse.getUserData();
                            List<User> userList = userDataList.getUser();
                            if (userList != null && !userList.isEmpty()) {
                                User user = userList.get(0);
                                siniiaPreferences.setUserId(user.getId() + "");
                                String name = user.getName();
                                siniiaPreferences.setUserName(name);

                                Intent intent = new Intent(mContext, CongratulationsActiviity.class);
                                startActivity(intent);
                                finishAffinity();
                            }

                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    public class ResendOTP extends AsyncTask<String, Void, String> {
        private ProgressDialog dialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(OTPVerifyActivity.this);
            dialog.setMessage("Please wait.....");
            dialog.show();

        }

        @Override
        protected String doInBackground(String... strings) {
            String result = new HttpHelper().getResendOT(strings[0]);
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
                        Toast.makeText(mContext, apiResponse.getDescription(), Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
