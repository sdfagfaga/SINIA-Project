package com.sinnia.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.hbb20.CountryCodePicker;
import com.sinnia.BuildConfig;
import com.sinnia.R;
import com.sinnia.data.register.RegisterRequest;
import com.sinnia.data.selecttype.ApiResponse;
import com.sinnia.network.HttpHelper;
import com.sinnia.preferences.SiniiaPreferences;
import com.sinnia.utils.SiniiaConstants;
import com.sinnia.utils.SiniiaUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        ResultCallback<LocationSettingsResult>, LocationListener {

    private static final String TAG = "RegisterActivity";
    private Context mContext;
    private SiniiaPreferences siniiaPreferences;
    private Button mBtnProfile, mBtnPayment, mBtnRegister;

    private TextView mTxtMale, mTxtFemale, mTxtDateOfBirth, mTxtUseMyLocation;

    private EditText mEdtName, mEdtPinCode, mEdtLandMark, mEdtAddress1,
            mEdtAddress2, mEdtEmail, mEdtMobileNumber, mEdtState, mEdtCity, mEdtPassword, mEdtConformPassword;
    private GoogleApiClient mGoogleApiClient;
    private LocationListener mLocationListener;
    private static final String DIALOG_ERROR = "dialog_error";
    // Bool to track whether the app is already resolving an error
    private boolean mResolvingError = false;
    private static final String STATE_RESOLVING_ERROR = "resolving_error";
    private Location mLastLocation;
    public static final int REQUEST_CHECK_SETTINGS = 102;
    private static final int REQUEST_RESOLVE_ERROR = 1001;

    private String dob, gender, locaLat, locaLong,email;
    private CountryCodePicker countryCodePicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mContext = getApplicationContext();
        siniiaPreferences = new SiniiaPreferences(mContext);

        mBtnProfile = (Button) findViewById(R.id.btn_profile);
        mBtnPayment = (Button) findViewById(R.id.btn_payment);
        mBtnRegister = (Button) findViewById(R.id.btn_register);

        mTxtMale = (TextView) findViewById(R.id.txt_male);
        mTxtFemale = (TextView) findViewById(R.id.txt_female);
        mTxtDateOfBirth = (TextView) findViewById(R.id.txt_date_of_birth);
        mTxtUseMyLocation = (TextView) findViewById(R.id.txt_use_my_location);

        mBtnProfile.setOnClickListener(this);
        mBtnPayment.setOnClickListener(this);
        mBtnRegister.setOnClickListener(this);

        mTxtMale.setOnClickListener(this);
        mTxtFemale.setOnClickListener(this);
        mTxtDateOfBirth.setOnClickListener(this);
        mTxtUseMyLocation.setOnClickListener(this);

        mEdtName = (EditText) findViewById(R.id.edt_full_name);
        mEdtPinCode = (EditText) findViewById(R.id.edt_pincode);
        mEdtLandMark = (EditText) findViewById(R.id.edt_landmark);
        mEdtAddress1 = (EditText) findViewById(R.id.edt_address1);
        mEdtAddress2 = (EditText) findViewById(R.id.edt_address2);
        mEdtEmail = (EditText) findViewById(R.id.edt_email);
        mEdtMobileNumber = (EditText) findViewById(R.id.edt_phone_number);
        countryCodePicker = (CountryCodePicker) findViewById(R.id.ccp);
        mEdtPassword = (EditText) findViewById(R.id.edt_password);
        mEdtConformPassword = (EditText) findViewById(R.id.edt_conform_password);

        mEdtState = (EditText) findViewById(R.id.edt_state);
        mEdtCity = (EditText) findViewById(R.id.edt_city);

        String email = siniiaPreferences.getEmail();
        /*If user register with E-mail we can't change*/
        if (email != null && email.isEmpty()) {
            mEdtEmail.setText(email);
            mEdtEmail.setFocusable(false);
        } else {
            mEdtEmail.setFocusable(true);
        }

        String countryCode = siniiaPreferences.getCountryCode();
        String mobileNumber = siniiaPreferences.getMobileNumber();
        if (countryCode != null && !countryCode.isEmpty()) {
            mEdtMobileNumber.setText(mobileNumber);
            countryCodePicker.setCountryForPhoneCode(Integer.parseInt(countryCode));
          //  countryCodePicker.setFocusable(false);
          //  mEdtMobileNumber.setFocusable(false);
        } else {
          //  countryCodePicker.setFocusable(true);
           // mEdtMobileNumber.setFocusable(true);
        }

        buildGoogleApiClient();


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
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_profile:
                break;
            case R.id.btn_payment:
                break;
            case R.id.btn_register:
                if (checkValidation()) {
                    RegisterRequest registerRequest = new RegisterRequest();
                    registerRequest.setName(mEdtName.getText().toString());
                    registerRequest.setGender(gender);
                    registerRequest.setDob(dob);
                    registerRequest.setPinCode(mEdtPinCode.getText().toString());
                    registerRequest.setLandmark(mEdtLandMark.getText().toString());
                    registerRequest.setAddress1(mEdtAddress1.getText().toString());
                    registerRequest.setAddress2(mEdtAddress2.getText().toString());
                    registerRequest.setEmail(mEdtEmail.getText().toString());
                    registerRequest.setState(mEdtState.getText().toString());
                    registerRequest.setCity(mEdtCity.getText().toString());
                    registerRequest.setPassword(mEdtPassword.getText().toString());
                    registerRequest.setVersionCode(BuildConfig.VERSION_CODE);
                    registerRequest.setVersionName(BuildConfig.VERSION_NAME);
                    registerRequest.setDevice("Android");
                   // if (siniiaPreferences.getMobileNumber() != null) {
                       // registerRequest.setMobileCountry(siniiaPreferences.getCountryCode());
                     //   registerRequest.setMobileNumber(siniiaPreferences.getMobileNumber());
                   // } else {
                        registerRequest.setMobileCountry(countryCodePicker.getSelectedCountryCode());
                        registerRequest.setMobileNumber(mEdtMobileNumber.getText().toString());
                   // }
                 //   registerRequest.setId(siniiaPreferences.getUserId());
                    if (locaLat != null) {
                        registerRequest.setLastLocationLat(locaLat);
                        registerRequest.setLastLocationLong(locaLong);
                    }

                    if (SiniiaUtils.checkForNetworkConnectivity(mContext)) {
                        new RegisterUser().execute(new Gson().toJson(registerRequest).toString());
                    } else {
                        Toast.makeText(mContext, getString(R.string.please_check_internet_connection), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.txt_male:
                mTxtMale.setCompoundDrawables(getDrawable(R.drawable.ic_male_selected), null, null, null);
                mTxtFemale.setCompoundDrawables(getDrawable(R.drawable.ic_femal_unselected), null, null, null);
                mTxtMale.setTextColor(getColor(R.color.colorPrimary));
                mTxtFemale.setTextColor(getColor(R.color.dark_black_color));
                gender = "Male";
                break;
            case R.id.txt_female:
                mTxtMale.setCompoundDrawables(getDrawable(R.drawable.ic_male_unselected), null, null, null);
                mTxtFemale.setCompoundDrawables(getDrawable(R.drawable.ic_femal_selected), null, null, null);
                mTxtMale.setTextColor(getColor(R.color.dark_black_color));
                mTxtFemale.setTextColor(getColor(R.color.colorPrimary));
                gender = "Female";
                break;
            case R.id.txt_date_of_birth:
                getDateDialog(mTxtDateOfBirth);
                break;
            case R.id.txt_use_my_location:
                requstNewLocation(this);
                Location mCurrentLocation = getLastKnownLocation();
                if (mCurrentLocation != null) {
                    getAddressFromLocation(mCurrentLocation);
                }
                break;

        }

    }

    public void getDateDialog(final TextView editText) {
        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePicker = new DatePickerDialog(RegisterActivity.this, R.style.MyDatePickerDialogTheme,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar calendarNew = Calendar.getInstance();
                        calendarNew.set(year, monthOfYear, dayOfMonth);
                        Date d = calendarNew.getTime();
                        Log.d(TAG, " date " + d);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        String date = sdf.format(d);
                        editText.setText(date);
                        dob = date;
                        editText.setTextColor(getResources().getColor(R.color.colorPrimary));
                    }
                }, yy, mm, dd);
        // datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000); // disable old dates.
        datePicker.show();
    } // closed

    public boolean checkValidation() {
        String fullName = mEdtName.getText().toString();
        String pinCode = mEdtPinCode.getText().toString();
        String landMark = mEdtLandMark.getText().toString();
        String address1 = mEdtAddress1.getText().toString();
        String address2 = mEdtAddress2.getText().toString();
        email = mEdtEmail.getText().toString();
        String state = mEdtState.getText().toString();
        String city = mEdtCity.getText().toString();
        String mobileNumber = mEdtMobileNumber.getText().toString();
        String password = mEdtPassword.getText().toString();
        String conformPassword = mEdtConformPassword.getText().toString();
        if (fullName != null && !fullName.isEmpty()) {
            siniiaPreferences.setUserName(fullName);
            if (dob != null) {
                if (pinCode != null && !pinCode.isEmpty()) {
                    if (landMark != null && !landMark.isEmpty()) {
                        if (address1 != null && !address1.isEmpty()) {
                            if (address2 != null && !address2.isEmpty()) {
                                if (email != null && !email.isEmpty() && SiniiaUtils.isValidEmail(email)) {
                                    if (mobileNumber != null && !mobileNumber.isEmpty()) {
                                        if (state != null && !state.isEmpty()) {
                                            if (city != null && !city.isEmpty()) {
                                                if (password != null && !password.isEmpty()) {
                                                    if (conformPassword != null && !conformPassword.isEmpty()) {
                                                        if (password.equals(conformPassword)) {
                                                            return true;
                                                        } else {
                                                            Toast.makeText(mContext, "Password & Conform password desn't match", Toast.LENGTH_SHORT).show();
                                                            return false;
                                                        }
                                                    } else {
                                                        Toast.makeText(mContext, "Please enter conform password", Toast.LENGTH_SHORT).show();
                                                        return false;
                                                    }
                                                } else {
                                                    Toast.makeText(mContext, "Please enter password", Toast.LENGTH_SHORT).show();
                                                    return false;
                                                }
                                            } else {
                                                Toast.makeText(mContext, "Please enter city", Toast.LENGTH_SHORT).show();
                                                return false;
                                            }
                                        } else {
                                            Toast.makeText(mContext, "Please enter state", Toast.LENGTH_SHORT).show();
                                            return false;
                                        }
                                    } else {
                                        Toast.makeText(mContext, "Please enter mobile number", Toast.LENGTH_SHORT).show();
                                        return false;
                                    }
                                } else {
                                    Toast.makeText(mContext, "Please enter valid email", Toast.LENGTH_SHORT).show();
                                    return false;
                                }
                            } else {
                                Toast.makeText(mContext, "Please enter address2", Toast.LENGTH_SHORT).show();
                                return false;
                            }
                        } else {
                            Toast.makeText(mContext, "Please enter address1", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    } else {
                        Toast.makeText(mContext, "Please enter landmark", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                } else {
                    Toast.makeText(mContext, "Please enter pin code", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                Toast.makeText(mContext, "Please select date of birth", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(mContext, "Please enter full name", Toast.LENGTH_SHORT).show();
            return false;
        }
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
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
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
            } catch (IOException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException illegalArgumentException) {
                illegalArgumentException.printStackTrace();
            }
            if (addresses != null && addresses.size() != 0) {
                Address address = addresses.get(0);
                Log.d(TAG, "location 1" + address.getLocality());
                Log.d(TAG, "location 2" + address.getSubLocality());
                Log.d(TAG, "location 3" + address.getSubAdminArea());
                Log.d(TAG, "location 4" + address.getPremises());
                Log.d(TAG, "location 5" + address.getFeatureName());
                Log.d(TAG, " address " + address.getAddressLine(0));
                String loc = null;
                try {
                    loc = address.getAddressLine(0);

                } catch (Exception e) {
                    loc = address.getSubLocality() + ", " + address.getSubAdminArea() + ", " + address.getLocality();
                    e.printStackTrace();
                }
                mEdtPinCode.setText(address.getPostalCode());
                mEdtLandMark.setText(address.getLocality());
                mEdtAddress1.setText(address.getSubLocality());
                mEdtAddress2.setText(address.getSubAdminArea());

                mEdtState.setText(address.getAdminArea());
                mEdtCity.setText(address.getSubAdminArea());
                locaLat = location.getLatitude() + "";
                locaLong = location.getLongitude() + "";
            }
        } else {

        }
    }

    public class RegisterUser extends AsyncTask<String, Void, String> {

        private ProgressDialog dialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(RegisterActivity.this);
            dialog.setMessage("Please wait.....");
            dialog.show();

        }

        @Override
        protected String doInBackground(String... strings) {
            String result = new HttpHelper().registerUserId(strings[0]);
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
                            siniiaPreferences.setRegisterStatus(SiniiaConstants.OTP_SENT);
                            siniiaPreferences.setEmail(email);
                            Intent intent = new Intent(mContext, OTPVerifyActivity.class);
                            intent.putExtra("name", mEdtName.getText().toString());
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
