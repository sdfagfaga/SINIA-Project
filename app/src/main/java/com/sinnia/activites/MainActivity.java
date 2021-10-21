package com.sinnia.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sinnia.R;
import com.sinnia.fragments.FragmentBuySell;
import com.sinnia.fragments.orders.FragmentCartDetails;
import com.sinnia.fragments.FragmentHome;
import com.sinnia.fragments.FragmentMore;
import com.sinnia.fragments.FragmentProductDetails;
import com.sinnia.fragments.FragmentProducts;
import com.sinnia.fragments.FragmentProfile;
import com.sinnia.preferences.SiniiaPreferences;
import com.sinnia.utils.SiniiaConstants;
import com.sinnia.utils.SiniiaUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        ResultCallback<LocationSettingsResult>, LocationListener {


    private static final String TAG = "MainActivity";

    private BottomNavigationView bottomNavigationView;
    private Context mContext;

    private GoogleApiClient mGoogleApiClient;
    private LocationListener mLocationListener;
    private static final String DIALOG_ERROR = "dialog_error";
    // Bool to track whether the app is already resolving an error
    private boolean mResolvingError = false;
    private static final String STATE_RESOLVING_ERROR = "resolving_error";
    private Location mLastLocation;
    public static final int REQUEST_CHECK_SETTINGS = 102;
    private static final int REQUEST_RESOLVE_ERROR = 1001;

    private SiniiaPreferences siniiaPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();
        siniiaPreferences = new SiniiaPreferences(mContext);
        // checking the fragment is exist or not.
        getSupportFragmentManager().addOnBackStackChangedListener(this);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.home_fragment));
        if (fragment == null) {
            Fragment fragmentHome = new FragmentHome();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.fragment, fragmentHome, getResources().getString(R.string.home_fragment));
            transaction.commit();
        }

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setItemIconTintList(null);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment);
                Log.d(TAG, " XXXXXXXXXXXxx Current Fragment" + currentFragment);
                switch (menuItem.getItemId()) {
                    case R.id.action_home:
                        clearFragments();
                        break;
                    case R.id.action_buy_sell:
                        if (currentFragment == null || (currentFragment.getClass() != null && !currentFragment.getClass().equals(FragmentBuySell.class))) {
                            clearFragments();
                            launchFragment(new FragmentBuySell(), getResources().getString(R.string.buy_sell_fragment));
                        }
                        break;
                    case R.id.action_products:
                        if (currentFragment.getClass() != null && !currentFragment.getClass().equals(FragmentProducts.class)) {
                            clearFragments();
                            launchFragment(new FragmentProducts(), getResources().getString(R.string.products_fragment));
                        }
                        break;

                    case R.id.action_profile:
                        if (currentFragment.getClass() != null && !currentFragment.getClass().equals(FragmentProfile.class)) {
                            clearFragments();
                            launchFragment(new FragmentProfile(), getResources().getString(R.string.profile_fragment));
                        }
                        break;
                    case R.id.action_more:
                        clearFragments();
                        if (currentFragment.getClass() != null && !currentFragment.getClass().equals(FragmentMore.class)) {
                            launchFragment(new FragmentMore(), getResources().getString(R.string.more_fragment));
                        }
                        break;

                }
                return true;
            }
        });

        buildGoogleApiClient();


    }

    public void clearFragments() {
        FragmentManager manager = getSupportFragmentManager();
        for (int i = 0; i <= manager.getBackStackEntryCount(); i++) {
            manager.popBackStack();
        }
    }


    @Override
    public void onBackStackChanged() {

        // removing the global navigation for the second level screens.
        FragmentManager manager = getSupportFragmentManager();
        Log.d(TAG, " XXXXXXXXXXXX   manager " + manager.getBackStackEntryCount());

        if (manager.getBackStackEntryCount() == 0) {
            Log.d(TAG, " notification ");
            // checking the fragment is exist or not.
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.home_fragment));
            if (fragment == null) {
                Fragment fragmentHome = new FragmentHome();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.add(R.id.fragment, fragmentHome, getResources().getString(R.string.home_fragment));
                transaction.commitAllowingStateLoss();
            }
        }

        if (manager.getBackStackEntryCount() >= 2) {
            hideGlobalNavigation();
        } else {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment);
            if (currentFragment != null && ((currentFragment.getClass().equals(FragmentCartDetails.class)) ||
                    (currentFragment.getClass().equals(FragmentProductDetails.class)))) {
                hideGlobalNavigation();
            } else {
                showGlobalNavigation();
            }
        }

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment);
        if (currentFragment != null && currentFragment.getClass() != null) {
            if (currentFragment.getClass().equals(FragmentHome.class)) {
                bottomNavigationView.getMenu().getItem(0).setChecked(true);
                //  ((FragmentHome) currentFragment).setCallCartMethodes();
            } else if (currentFragment.getClass().equals(FragmentBuySell.class)) {
                bottomNavigationView.getMenu().getItem(1).setChecked(true);
                ((FragmentBuySell) currentFragment).callMethode();
            } else if (currentFragment.getClass().equals(FragmentProducts.class)) {
                bottomNavigationView.getMenu().getItem(2).setChecked(true);
            } else if (currentFragment.getClass().equals(FragmentProfile.class)) {
                bottomNavigationView.getMenu().getItem(3).setChecked(true);
            } else if (currentFragment.getClass().equals(FragmentMore.class)) {
                bottomNavigationView.getMenu().getItem(4).setChecked(true);
            }

        }
    }

    public void showGlobalNavigation() {
        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
    }

    public void hideGlobalNavigation() {
        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(View.GONE);
        }

    }

    public void setBottomNavigationItemClicked(int index) {
        if (bottomNavigationView != null) {
            bottomNavigationView.getMenu().getItem(index).setChecked(true);
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

    public void launchFragment(Fragment fragment, String fragmentName, Bundle bundle) {
        FragmentManager manager = getSupportFragmentManager();
        Fragment homeFrag = getSupportFragmentManager().findFragmentById(R.id.fragment);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fragment, fragment, fragmentName);
        transaction.hide(homeFrag);
        fragment.setArguments(bundle);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
            SiniiaUtils.closeKeyboard(mContext, getCurrentFocus().getWindowToken());
        }
        /*Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment);
        if (currentFragment != null && currentFragment.getClass() != null) {
            if (currentFragment.getClass().equals(FragmentHome.class)) {
                bottomNavigationView.getMenu().getItem(0).setChecked(true);
                //  ((FragmentHome) currentFragment).setCallCartMethodes();
            } else if (currentFragment.getClass().equals(FragmentBuySell.class)) {
                bottomNavigationView.getMenu().getItem(1).setChecked(true);
                ((FragmentBuySell) currentFragment).callMethode();
            } else if (currentFragment.getClass().equals(FragmentProducts.class)) {
                bottomNavigationView.getMenu().getItem(2).setChecked(true);
            } else if (currentFragment.getClass().equals(FragmentProfile.class)) {
                bottomNavigationView.getMenu().getItem(3).setChecked(true);
            } else if (currentFragment.getClass().equals(FragmentMore.class)) {
                bottomNavigationView.getMenu().getItem(4).setChecked(true);
            }

        }*/
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
            }
        } else {

        }
    }
}
