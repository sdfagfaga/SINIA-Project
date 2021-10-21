package com.sinnia.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.LocationListener;
import com.google.gson.Gson;
import com.sinnia.R;
import com.sinnia.activites.MainActivity;
import com.sinnia.data.cart.UserAddress;
import com.sinnia.data.login.ProfileResponse;
import com.sinnia.data.login.User;
import com.sinnia.data.login.UserDataList;
import com.sinnia.data.selecttype.ApiResponse;
import com.sinnia.network.HttpHelper;
import com.sinnia.preferences.SiniiaPreferences;
import com.sinnia.utils.SiniiaUtils;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class FragmentUpdateProfile extends Fragment implements View.OnClickListener, LocationListener {

    private Context mContext;
    private EditText mEdtPinCode, mEdtLandMark, mEdtAddress1, mEdtAddress2, mEdtState, mEdtCity;
    private static final String TAG = "FragmentUpdateProfile";
    private String locaLat, locaLong;
    private static final int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    ImageView mImgProfile;
    public byte[] imgBytes = new byte[0];
    private SiniiaPreferences siniiaPreferences;
    private String landMark, address1, address2, pinCode, state, city;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_update_profile, container, false);
        mContext = getActivity();
        siniiaPreferences = new SiniiaPreferences(mContext);
        ImageView leftArrow = (ImageView) rootView.findViewById(R.id.ic_left_arrow);
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null && getActivity().getSupportFragmentManager() != null)
                    getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        mImgProfile = (ImageView) rootView.findViewById(R.id.image_profile);
        mImgProfile.setOnClickListener(this);
        Button btnUpdate = (Button) rootView.findViewById(R.id.btn_update);
        btnUpdate.setOnClickListener(this);
        TextView mTxtUseMyLocation = (TextView) rootView.findViewById(R.id.txt_use_my_location);
        mTxtUseMyLocation.setOnClickListener(this);

        mEdtPinCode = (EditText) rootView.findViewById(R.id.edt_pincode);
        mEdtLandMark = (EditText) rootView.findViewById(R.id.edt_landmark);
        mEdtAddress1 = (EditText) rootView.findViewById(R.id.edt_address1);
        mEdtAddress2 = (EditText) rootView.findViewById(R.id.edt_address2);
        mEdtState = (EditText) rootView.findViewById(R.id.edt_state);
        mEdtCity = (EditText) rootView.findViewById(R.id.edt_city);

        if (getArguments() != null) {
            ProfileResponse profileResponse = (ProfileResponse) getArguments().getSerializable("user");
            if (profileResponse != null) {
                UserDataList userDataList = profileResponse.getUserData();
                if (userDataList != null && userDataList.getUser().size() != 0) {
                    List<User> users = userDataList.getUser();
                    User user = users.get(users.size() - 1);
                    if (user != null) {
                        Log.d(TAG, "XXXXXXXXXX profile " + user.getAddress1());
                        if (user.getProfilePicUrl() != null) {
                            SiniiaUtils.loadPicassoImageWithOutCircle(mContext, user.getProfilePicUrl(), mImgProfile);
                        }
                    }

                    if (profileResponse.getUserAddressData() != null) {
                        UserAddress userAddress = profileResponse.getUserAddressData();
                        mEdtLandMark.setText(userAddress.getLandmark());
                        mEdtPinCode.setText(userAddress.getPinCode());
                        mEdtAddress2.setText(userAddress.getAddress2());
                        mEdtAddress1.setText(userAddress.getAddress1());
                        mEdtState.setText(userAddress.getState());
                        mEdtCity.setText(userAddress.getCity());
                    }
                }
            }
        }

        return rootView;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_profile:
                selectImage();
                break;
            case R.id.txt_use_my_location:
                ((MainActivity) mContext).requstNewLocation(this);
                Location location = ((MainActivity) mContext).getLastKnownLocation();
                getAddressFromLocation(location);
                break;
            case R.id.btn_update:
                pinCode = mEdtPinCode.getText().toString();
                landMark = mEdtLandMark.getText().toString();
                address1 = mEdtAddress1.getText().toString();
                address2 = mEdtAddress2.getText().toString();
                state = mEdtState.getText().toString();
                city = mEdtCity.getText().toString();
                if (pinCode != null && !pinCode.isEmpty()) {
                    if (landMark != null && !landMark.isEmpty()) {
                        if (address1 != null && !address1.isEmpty()) {
                            if (address2 != null && !address2.isEmpty()) {
                                if (state != null && !state.isEmpty()) {
                                    if (city != null && !city.isEmpty()) {
                                        if (SiniiaUtils.checkForNetworkConnectivity(mContext)) {
                                            new UpdateProfile().execute();
                                        } else
                                            Toast.makeText(mContext, getString(R.string.please_check_internet_connection), Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(mContext, "Please enter city", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(mContext, "Please enter state", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(mContext, "Please enter address2", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(mContext, "Please enter address1", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(mContext, "Please enter landmark", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, "Please enter pin code", Toast.LENGTH_SHORT).show();
                }

                break;
        }
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
                Log.d(TAG, " address " + address.getPremises());
                mEdtState.setText(address.getAdminArea());
                mEdtCity.setText(address.getSubAdminArea());
                String loc = null;
                try {
                    loc = address.getAddressLine(0);

                } catch (Exception e) {
                    loc = address.getSubLocality() + ", " + address.getSubAdminArea() + ", " + address.getLocality();
                    e.printStackTrace();
                }

                mEdtPinCode.setText(address.getPostalCode());
                mEdtAddress2.setText(address.getSubAdminArea());
                mEdtLandMark.setText(address.getLocality());
                mEdtAddress1.setText(loc);
                locaLat = location.getLatitude() + "";
                locaLong = location.getLongitude() + "";
            }
        } else {

        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    private void selectImage() {

        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    if (checkCameraPermission()) {
                        cameraIntent();
                    }

                } else if (items[item].equals("Choose from Library")) {
                    galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);// only for the gallery and images.
        startActivityForResult(Intent.createChooser(intent, "Select image"), SELECT_FILE);

    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    public boolean checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 1);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "reqCode:" + requestCode + ", result:" + resultCode);

        switch (requestCode) {
            case SELECT_FILE:
                onSelectFromGalleryResult(data);
                break;
            case REQUEST_CAMERA:
                onCaptureImageResult(data);
                break;
            default:
                // do nothing
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = null;
        if (data != null) {
            try {
                thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");

                FileOutputStream fo;

                destination.createNewFile();
                fo = new FileOutputStream(destination);
                fo.write(bytes.toByteArray());
                fo.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (thumbnail != null)
                setImageView(R.id.image_profile, thumbnail);
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        if (data != null) {
            Uri selectedImageUri = data.getData();
            String picturePath = getPath(getActivity().getApplicationContext(), selectedImageUri);
            Bitmap bitmapImage = BitmapFactory.decodeFile(picturePath);
            if (bitmapImage != null) {
                Bitmap scaled = Bitmap.createScaledBitmap(bitmapImage, 512, 512, true);
                setImageView(R.id.image_profile, scaled);
            } else {
                Toast.makeText(mContext, "Selected image is unable load", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setImageView(int imageViewId, Bitmap thumbnail) {
        switch (imageViewId) {
            case R.id.image_profile:
                mImgProfile.setImageBitmap(thumbnail);
                imgBytes = SiniiaUtils.convertBitmapToBytes(thumbnail);
                break;
        }
    }

    private static String getPath(Context applicationContext, Uri selectedImageUri) {
        String mPathResult = null;
        String[] mArrProj = {MediaStore.Images.Media.DATA};
        Cursor mCursor = applicationContext.getContentResolver().query(selectedImageUri, mArrProj, null, null, null);
        if (mCursor != null) {
            if (mCursor.moveToFirst()) {
                int column_index = mCursor.getColumnIndexOrThrow(mArrProj[0]);
                mPathResult = mCursor.getString(column_index);
            }
            mCursor.close();
        }
        if (mPathResult == null) {
            mPathResult = "Not found";
        }
        return mPathResult;
    }

    public class UpdateProfile extends AsyncTask<String, String, String> {
        ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setMessage("Please wait...");
            mProgressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            JSONArray jsonArray = new JSONArray();
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("address1", address1);
                jsonObject.put("address2", address2);
                jsonObject.put("landmark", landMark);
                jsonObject.put("pinCode", pinCode);
                jsonObject.put("LastLocationLat", locaLat);
                jsonObject.put("LastLocationLong", locaLong);
                jsonObject.put("state", state);
                jsonObject.put("city", city);
                jsonObject.put("id", siniiaPreferences.getUserId());
                jsonArray.put(jsonObject);
                Log.d(TAG, "XXXXXXXXXXx img " + imgBytes.length);
                if (imgBytes != null && imgBytes.length != 0) {
                    Log.d(TAG, "XXXXXXXXXXx img ");
                    builder.addPart("image.jpeg", new ByteArrayBody(imgBytes, "image.jpeg"));
                }
                builder.addPart("input.json", new ByteArrayBody(jsonArray.toString().getBytes(), "input.json"));

                Log.d(TAG, " jsonArray " + jsonArray.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }
            String result = new HttpHelper().postMultiPartUserProfile(builder.build());
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, " result  " + s);
            try {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
                Log.d(TAG, "XXXXXXXXXXXXx result " + s);
                if (s != null) {
                    ApiResponse apiResponse = new Gson().fromJson(s, ApiResponse.class);
                    if (apiResponse != null) {
                        Toast.makeText(mContext, apiResponse.getDescription(), Toast.LENGTH_SHORT).show();
                        if (apiResponse.getStatus() == 200) {
                            if (getActivity() != null && getActivity().getSupportFragmentManager() != null) {
                                getActivity().getSupportFragmentManager().popBackStack();
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
