package com.sinnia.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.sinnia.R;
import com.sinnia.activites.LoginActivity;
import com.sinnia.activites.MainActivity;
import com.sinnia.activites.OTPVerifyActivity;
import com.sinnia.activites.SelectTypeActivity;
import com.sinnia.data.cart.UserAddress;
import com.sinnia.data.login.LoginResponse;
import com.sinnia.data.login.ProfileResponse;
import com.sinnia.data.login.User;
import com.sinnia.data.login.UserDataList;
import com.sinnia.fragments.orders.FragmentCartDetails;
import com.sinnia.network.HttpHelper;
import com.sinnia.preferences.SiniiaPreferences;
import com.sinnia.utils.SiniiaConstants;
import com.sinnia.utils.SiniiaUtils;

import java.util.List;

public class FragmentProfile extends Fragment implements View.OnClickListener {


    private static final String TAG = "FragmentProfile";
    private Context mContext;
    private SiniiaPreferences siniiaPreferences;
    private TextView mTxtName, mTxtMobile, mTxtEmail, mTxtAddress, mTxtDob, mTxtMale;
    private Button mBtnEdit;
    private ImageView mProfileImage;
    private User user;
    private ProfileResponse profileResponse;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        mContext = getActivity();
        siniiaPreferences = new SiniiaPreferences(mContext);

        mTxtDob = (TextView) rootView.findViewById(R.id.txt_date_birth);
        mTxtMale = (TextView) rootView.findViewById(R.id.txt_male);
        mTxtName = (TextView) rootView.findViewById(R.id.txt_name);
        mTxtMobile = (TextView) rootView.findViewById(R.id.txt_mobile);
        mTxtEmail = (TextView) rootView.findViewById(R.id.txt_email);
        mTxtAddress = (TextView) rootView.findViewById(R.id.txt_address);
        mBtnEdit = (Button) rootView.findViewById(R.id.btn_edit);
        mBtnEdit.setOnClickListener(this);

        mProfileImage = (ImageView) rootView.findViewById(R.id.profile_image);

        setCartNotification(rootView);
        setHasOptionsMenu(true);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ic_notification_layout:
                break;
            case R.id.ic_cart_layout:
                if (siniiaPreferences.getCartCount() != 0) {
                    ((MainActivity) mContext).launchFragment(new FragmentCartDetails(), getString(R.string.fragment_product_details));
                } else {
                    Toast.makeText(mContext, getString(R.string.add_products_cart), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_edit:
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", profileResponse);
                ((MainActivity) mContext).launchFragment(new FragmentUpdateProfile(), getString(R.string.fragment_update_profile), bundle);
                break;
        }
    }

    @Override
    public void onResume() {
        Log.d(TAG, "XXXXXXXXXXXXxx on resume ");
        if (SiniiaUtils.checkForNetworkConnectivity(mContext)) {
            new GetUserDetails().execute();
        } else {
            Toast.makeText(mContext, getString(R.string.please_check_internet_connection), Toast.LENGTH_SHORT).show();
        }

        super.onResume();
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (SiniiaUtils.checkForNetworkConnectivity(mContext)) {
            new GetUserDetails().execute();
        } else {
            Toast.makeText(mContext, getString(R.string.please_check_internet_connection), Toast.LENGTH_SHORT).show();
        }

    }

    public void setCartNotification(View rootView) {
        View cartView = (View) rootView.findViewById(R.id.ic_cart_layout);
        View notificationView = (View) rootView.findViewById(R.id.ic_notification_layout);
        TextView mTxtNotificationCount = (TextView) notificationView.findViewById(R.id.txt_notification_count);
        TextView mTxtCartCount = (TextView) cartView.findViewById(R.id.txt_cart_count);

        ImageView mImgLogo = (ImageView) rootView.findViewById(R.id.ic_logo);

        mImgLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SelectTypeActivity.class);
                startActivity(intent);
            }
        });
        TextView mTxtStall14=(TextView)rootView.findViewById(R.id.txt_stall_14);
        mTxtStall14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SelectTypeActivity.class);
                startActivity(intent);
            }
        });

        if (siniiaPreferences.getCartCount() != 0) {
            mTxtCartCount.setText(siniiaPreferences.getCartCount() + "");
            mTxtCartCount.setVisibility(View.VISIBLE);
        } else {
            mTxtCartCount.setVisibility(View.GONE);
        }
        if (siniiaPreferences.getNotificationCount() != 0) {
            mTxtNotificationCount.setText(siniiaPreferences.getNotificationCount() + "");
            mTxtNotificationCount.setVisibility(View.VISIBLE);
        } else {
            mTxtNotificationCount.setVisibility(View.GONE);
        }
        notificationView.setOnClickListener(this);
        cartView.setOnClickListener(this);
    }

    public class GetUserDetails extends AsyncTask<String, Void, String> {

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
            String result = new HttpHelper().getUserDetails(siniiaPreferences.getUserId());
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
                    profileResponse = new Gson().fromJson(s, ProfileResponse.class);
                    UserDataList userDataList = profileResponse.getUserData();
                    if (userDataList != null) {

                        List<User> users = userDataList.getUser();
                        if (users != null && !users.isEmpty()) {
                            user = users.get(users.size() - 1);
                            Log.d(TAG, "XXXXXXXXXXXXXXXXXXXx user " + user.getAddress1());
                            mTxtName.setText(user.getName());
                            mTxtMobile.setText(user.getMobileCountry() + " - " + user.getMobileNumber());
                            mTxtEmail.setText(user.getEmail());

                            UserAddress userAddress = profileResponse.getUserAddressData();
                            if (userAddress != null) {
                                mTxtAddress.setText(userAddress.getLandmark() + " " + userAddress.getAddress1()
                                        + " \n" + userAddress.getAddress2() + " pin code - " + userAddress.getPinCode());
                            }

                            if (user.getProfilePicUrl() != null) {
                                SiniiaUtils.loadPicassoImageWithOutCircle(mContext, user.getProfilePicUrl(), mProfileImage);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
