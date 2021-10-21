package com.sinnia.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.gson.Gson;
import com.sinnia.BuildConfig;
import com.sinnia.R;
import com.sinnia.data.login.LoginRequest;
import com.sinnia.data.login.LoginResponse;
import com.sinnia.data.login.User;
import com.sinnia.data.login.UserDataList;
import com.sinnia.network.HttpHelper;
import com.sinnia.preferences.SiniiaPreferences;
import com.sinnia.utils.SiniiaConstants;
import com.sinnia.utils.SiniiaUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        ResultCallback<LocationSettingsResult>, LocationListener {

    private Button mBtnLogin;
    private Context mContext;
    private EditText mEdtPassword, mEdtUserName;

    // private CountryCodePicker countryCodePicker;

    private LinearLayout mEmailLayout;

    private boolean isEmail;

    private static final String TAG = "LoginActivity";
    private SiniiaPreferences siniiaPreferences;
    String mobile, email;

    private GoogleApiClient mGoogleApiClient;
    private LocationListener mLocationListener;
    private static final String DIALOG_ERROR = "dialog_error";
    // Bool to track whether the app is already resolving an error
    private boolean mResolvingError = false;
    private static final String STATE_RESOLVING_ERROR = "resolving_error";
    private Location mLastLocation;
    public static final int REQUEST_CHECK_SETTINGS = 102;
    private static final int REQUEST_RESOLVE_ERROR = 1001;


    ProgressDialog dialog = null;
    private TextView mTxtRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mContext = getApplicationContext();
        siniiaPreferences = new SiniiaPreferences(mContext);


        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mTxtRegister = (TextView) findViewById(R.id.txt_register);

        mEdtPassword = (EditText) findViewById(R.id.edt_password);
        mEdtUserName = (EditText) findViewById(R.id.edt_user_name);
        // countryCodePicker = (CountryCodePicker) findViewById(R.id.ccp);

        mEmailLayout = (LinearLayout) findViewById(R.id.email_layout);


        mBtnLogin.setOnClickListener(this);
        mTxtRegister.setOnClickListener(this);

        buildGoogleApiClient();
        requstNewLocation(this);
        Location mCurrentLocation = getLastKnownLocation();
        if (mCurrentLocation != null) {
            getAddressFromLocation(mCurrentLocation);
        }

    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.v(TAG, "location services onConnected");
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (mLocationListener != null) {
            requstNewLocation(mLocationListener);
        } else {
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);
            builder.setAlwaysShow(true);
            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
            result.setResultCallback(this);
        }
    }

    public Location getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        return mLastLocation;

    }

    public void requstNewLocation(LocationListener locationListener) {
        mLocationListener = locationListener;
        Log.d(TAG, "Request new Location");
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            Log.d(TAG, "Request new Location0");
            LocationRequest mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(10000);
            mLocationRequest.setFastestInterval(5000);
            mLocationRequest.setNumUpdates(1);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(mLocationRequest);
            builder.setAlwaysShow(true);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                checkPermission();
                return;
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, locationListener);
            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
            result.setResultCallback(this);
        } else {
            Log.d(TAG, "Request new Location1");
            mGoogleApiClient.connect();
        }
    }

    public void removeRequestedLocationUpdates(LocationListener locationListener) {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, locationListener);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (mResolvingError) {
            // Already attempting to resolve an error.
            return;
        } else if (connectionResult.hasResolution()) {
            try {
                mResolvingError = true;
                connectionResult.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
            } catch (Exception e) {
                // There was an error with the resolution intent. Try again.
                mGoogleApiClient.connect();
            }
        } else {
            // Show dialog using GoogleApiAvailability.getErrorDialog()
            showErrorDialog(connectionResult.getErrorCode());
            mResolvingError = true;
        }

    }

    /* Creates a dialog for an error message */
    private void showErrorDialog(int errorCode) {
        // Create a fragment for the error dialog
        RegisterActivity.ErrorDialogFragment dialogFragment = new RegisterActivity.ErrorDialogFragment();
        // Pass the error that should be displayed
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.show(getSupportFragmentManager(), "errordialog");
    }

    /* A fragment to display an error dialog */
    public static class ErrorDialogFragment extends DialogFragment {
        public ErrorDialogFragment() {
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Get the error code and retrieve the appropriate dialog
            int errorCode = this.getArguments().getInt(DIALOG_ERROR);
            return GoogleApiAvailability.getInstance().getErrorDialog(
                    this.getActivity(), errorCode, REQUEST_RESOLVE_ERROR);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            ((RegisterActivity) getActivity()).onDialogDismissed();
        }
    }

    /* Called from ErrorDialogFragment when the dialog is dismissed. */
    public void onDialogDismissed() {
        mResolvingError = false;
    }

    @Override
    public void onResult(LocationSettingsResult locationSettingsResult) {
        Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                mGoogleApiClient.connect();
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                try {
                    status.startResolutionForResult(this, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private boolean checkPermission() {
        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
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
    public void onLocationChanged(Location location) {
        getAddressFromLocation(location);
    }

    private void getAddressFromLocation(Location location) {


        if (location != null) {
            Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(
                        location.getLatitude(),
                        location.getLongitude(),
                        // In this sample, get just a single address.
                        1);
            } catch (Exception e) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                e.printStackTrace();
            }
            if (addresses != null && addresses.size() != 0) {
                Address address = addresses.get(0);
                Log.d(TAG, "location 1" + address.getLocality());
                Log.d(TAG, "location 2" + address.getSubLocality());
                Log.d(TAG, "location 3" + address.getSubAdminArea());
                Log.d(TAG, "location 4" + address.getPremises());
                Log.d(TAG, "location 5" + address.getFeatureName());
                Log.d(TAG, " address " + address.getAddressLine(0));

                // countryName = address.getCountryName();
                //   Log.d(TAG,"XXXXXXXXXXXx Country Code "+countryName);
                // countryCode = SiniiaUtils.getCountryDialogFromCountryCode(mContext, address.getCountryCode());
                String loc = null;
                try {
                    loc = address.getAddressLine(0);

                } catch (Exception e) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    loc = address.getSubLocality() + ", " + address.getSubAdminArea() + ", " + address.getLocality();
                    e.printStackTrace();
                }
            }

            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        } else {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            //   countryCode = SiniiaUtils.getCountryDialCode(mContext);
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.txt_register:
                Intent intent = new Intent(mContext, RegisterActivity.class);
                startActivity(intent);
                break;


            case R.id.btn_login:

                //   if (countryCode != null) {
                if (SiniiaUtils.checkForNetworkConnectivity(mContext)) {
                    LoginRequest loginRequest = new LoginRequest();
                    loginRequest.setVersionCode(BuildConfig.VERSION_CODE);
                    loginRequest.setVersionName(BuildConfig.VERSION_NAME);
                    loginRequest.setDevice("Android");
                    email = mEdtUserName.getText().toString().trim();
                    String password = mEdtPassword.getText().toString().trim();

                    if (email != null && !email.isEmpty() && SiniiaUtils.isValidEmail(email)) {
                        if (password != null && !password.isEmpty()) {
                            loginRequest.setPassword(password);
                            loginRequest.setEmail(email);
                            new GetLoginOTP().execute(new Gson().toJson(loginRequest).toString());
                        } else {
                            Toast.makeText(mContext, "Please enter password", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(mContext, getString(R.string.please_enter_email), Toast.LENGTH_SHORT).show();
                    }

                       /* if (isEmail) {
                            email = mEdtEmail.getText().toString();
                            if (email != null && !email.isEmpty() && SiniiaUtils.isValidEmail(email)) {
                                loginRequest.setEmail(email);
                                new GetLoginOTP().execute(new Gson().toJson(loginRequest).toString());
                            } else {
                                Toast.makeText(mContext, getString(R.string.please_enter_email), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            mobile = mEdtPhoneNumber.getText().toString();
                            if (mobile != null && !mobile.isEmpty()) {
                                loginRequest.setMobileNumber(mobile);
                                loginRequest.setMobileCountry(countryCode);
                                siniiaPreferences.setCountryName(countryName);
                                new GetLoginOTP().execute(new Gson().toJson(loginRequest).toString());
                            } else {
                                Toast.makeText(mContext, getString(R.string.please_enter_mobile), Toast.LENGTH_SHORT).show();
                            }
                        }*/


                } else {
                    Toast.makeText(mContext, getString(R.string.please_check_internet_connection), Toast.LENGTH_SHORT).show();
                }
              /*  } else {
                    dialog = new ProgressDialog(LoginActivity.this);
                    dialog.setMessage("Please wait.....");
                    dialog.show();
                    requstNewLocation(this);
                    Location mCurrentLocation = getLastKnownLocation();
                    if (mCurrentLocation != null) {
                        getAddressFromLocation(mCurrentLocation);
                    }
                    Toast.makeText(mContext, "Please give location for to county dial code", Toast.LENGTH_SHORT).show();
                }*/
                break;
        }

    }

    public class GetLoginOTP extends AsyncTask<String, Void, String> {

        private ProgressDialog dialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(LoginActivity.this);
            dialog.setMessage("Please wait.....");
            dialog.show();

        }

        @Override
        protected String doInBackground(String... strings) {
            //  String result = new HttpHelper().getLoginOTP(strings[0]);
            String result = new HttpHelper().getEmailLogin(strings[0]);
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
                    LoginResponse loginResponse = new Gson().fromJson(s, LoginResponse.class);
                    if (loginResponse != null) {
                        String status = loginResponse.getStatus();
                        if (status != null && status.equalsIgnoreCase("200")) {
                            if (loginResponse.getUserData() != null) {
                                List<User> userList = loginResponse.getUserData().getUser();
                                if (userList != null && !userList.isEmpty()) {
                                    User user = userList.get(0);
                                    siniiaPreferences.setMobileNumber(user.getMobileNumber());
                                    siniiaPreferences.setCountryCode(user.getMobileCountry());
                                    siniiaPreferences.setCountryName(user.getCountry());
                                    siniiaPreferences.setUserId(user.getId() + "");
                                }
                            }
                            siniiaPreferences.setEmail(email);
                            siniiaPreferences.setRegisterStatus(SiniiaConstants.REGISTERED);
                            Intent intent = new Intent(mContext, MainActivity.class);
                            startActivity(intent);
                            finishAffinity();
                        } else {
                            Toast.makeText(mContext, loginResponse.getDescription(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


}
