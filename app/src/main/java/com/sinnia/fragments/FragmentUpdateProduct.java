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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.sinnia.data.postaproduct.Detail;
import com.sinnia.data.postaproduct.ProductCategory;
import com.sinnia.data.postaproduct.ProductTypeNames;
import com.sinnia.data.products.Products;
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

public class FragmentUpdateProduct extends Fragment implements View.OnClickListener, LocationListener {

    private Context mContext;

    private Spinner mSpCategory, mSpProductName, mSpType, mSpGrade, mSpUnits/*, mSpPickUpLocation*/;
    private EditText mEdtCount, mEdtPrice, mEdtMinQty, /*mEdtRadius, */
            mEdtProductDescription,
            mEdtPinCode, mEdtLandMark, mEdtAddress, mEdtContact, mEdtCountry, mEdtState, mEdtCity;
    private TextView mTxtMyLocation;

    private ImageView imgOne, imgTwo, imgThree, imgFour;
    private Button mBtnPostAProduct;
    private int selectedImgId;
    private static final int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private static final String TAG = "FragmentUpdateProduct";
    private String locaLat, locaLong;

    private SiniiaPreferences siniiaPreferences;

    public byte[] imgBytes1 = new byte[0];
    public byte[] imgBytes2 = new byte[0];
    public byte[] imgBytes3 = new byte[0];
    public byte[] imgBytes4 = new byte[0];

    private String category, productName, type, grade, units, counts, price, minOty,
    /* radius, highlights, */productDescription, pickUpLocation, pinCode, landMark,
            address, addressId, width, height, weight, country,
            state, city, length;
    ;

    private ProductCategory selectedProductCategory;

    private Products products;

    private EditText mEdtHeight, mEdtWidth, mEdtWeight, mEdtLength;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_update_product, container, false);
        mContext = getActivity();
        ImageView leftArrow = (ImageView) rootView.findViewById(R.id.ic_left_arrow);
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null && getActivity().getSupportFragmentManager() != null)
                    getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        siniiaPreferences = new SiniiaPreferences(mContext);

        mSpCategory = (Spinner) rootView.findViewById(R.id.spn_category);
        mSpProductName = (Spinner) rootView.findViewById(R.id.sp_product_name);
        mSpType = (Spinner) rootView.findViewById(R.id.spn_type);
        mSpGrade = (Spinner) rootView.findViewById(R.id.spn_grade);
        mSpUnits = (Spinner) rootView.findViewById(R.id.sp_units);
        mEdtCount = (EditText) rootView.findViewById(R.id.edt_count);
        mEdtPrice = (EditText) rootView.findViewById(R.id.edt_price);
        mEdtMinQty = (EditText) rootView.findViewById(R.id.edt_minm_qty);
        mEdtProductDescription = (EditText) rootView.findViewById(R.id.edt_product_description);
        //mSpPickUpLocation = (Spinner) rootView.findViewById(R.id.sp_pick_up_location);
        mEdtPinCode = (EditText) rootView.findViewById(R.id.edt_pincode);
        mTxtMyLocation = (TextView) rootView.findViewById(R.id.txt_use_my_location);
        mEdtLandMark = (EditText) rootView.findViewById(R.id.edt_landmark);
        mEdtAddress = (EditText) rootView.findViewById(R.id.edt_address);
        mEdtContact = (EditText) rootView.findViewById(R.id.edt_contact);
        mEdtState = (EditText) rootView.findViewById(R.id.edt_state);
        mEdtCity = (EditText) rootView.findViewById(R.id.edt_city);
        mEdtCountry = (EditText) rootView.findViewById(R.id.edt_county);

        mEdtWidth = (EditText) rootView.findViewById(R.id.edt_width);
        mEdtWeight = (EditText) rootView.findViewById(R.id.edt_weight);
        mEdtHeight = (EditText) rootView.findViewById(R.id.edt_height);
        mEdtLength = (EditText) rootView.findViewById(R.id.edt_lenght);


        mTxtMyLocation.setOnClickListener(this);

        mBtnPostAProduct = (Button) rootView.findViewById(R.id.btn_post);
        mBtnPostAProduct.setOnClickListener(this);

        imgOne = (ImageView) rootView.findViewById(R.id.img_one);
        imgTwo = (ImageView) rootView.findViewById(R.id.img_two);
        imgThree = (ImageView) rootView.findViewById(R.id.img_three);
        imgFour = (ImageView) rootView.findViewById(R.id.img_four);

        // imgOne.setOnClickListener(this);
        // imgTwo.setOnClickListener(this);
        // imgThree.setOnClickListener(this);
        // imgFour.setOnClickListener(this);


        if (getArguments() != null) {
            products = (Products) getArguments().getSerializable("product");

            mEdtCount.setText(products.getQuantityAvailable() + "");
            mEdtPrice.setText(products.getPricePerUnit() + "");
            mEdtMinQty.setText(products.getMinQuantity() + "");
            //   mEdtRadius.setText(products.getRadius());
            mEdtProductDescription.setText(products.getProductDescription());
            //   mEdtHighLight1.setText(products.getHighlight());

            mEdtHeight.setText(products.getHeight() + "");
            mEdtWeight.setText(products.getWeight() + "");
            mEdtWidth.setText(products.getWidth() + "");
            mEdtLength.setText(products.getLength() + "");
            mEdtState.setText(products.getState());
            mEdtCountry.setText(products.getCountry());
            mEdtCity.setText(products.getCity());

            UserAddress userAddress = products.getAddress();
            if (userAddress != null) {
                mEdtPinCode.setText(userAddress.getPinCode());
                mEdtLandMark.setText(userAddress.getLandmark());
                mEdtAddress.setText(userAddress.getAddress1());
            }
            mEdtContact.setText(siniiaPreferences.getMobileNumber());

            String url = products.getThumbImageURL();
            Log.d(TAG, " Url " + url);
            if (url != null) {
                if (url.contains(",")) {
                    String[] urls = url.split(",");
                    for (int i = 0; i < urls.length; i++) {
                        if (i == 0) {
                            SiniiaUtils.loadPicassoImageWithOutCircle(mContext, urls[0], imgOne);
                        } else if (i == 1) {
                            SiniiaUtils.loadPicassoImageWithOutCircle(mContext, urls[1], imgTwo);

                        } else if (i == 2) {
                            SiniiaUtils.loadPicassoImageWithOutCircle(mContext, urls[2], imgThree);

                        } else if (i == 2) {
                            SiniiaUtils.loadPicassoImageWithOutCircle(mContext, urls[3], imgFour);
                        }
                    }
                } else {
                    SiniiaUtils.loadPicassoImageWithOutCircle(mContext, url, imgOne);
                }
            }


        }

        mSpCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedProductCategory = (ProductCategory) parent.getSelectedItem();
                category = selectedProductCategory.getCategory();
                if (selectedProductCategory != null) {
                    final ArrayAdapter<Detail> productNameAdapter = new ArrayAdapter<Detail>(mContext,
                            android.R.layout.simple_spinner_item, selectedProductCategory.getDetails());
                    productNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mSpProductName.setAdapter(productNameAdapter);

                    int typeSelection = 0;
                    for (int i = 0; i < selectedProductCategory.getDetails().size(); i++) {
                        if (products.getProductName().equalsIgnoreCase(selectedProductCategory.getDetails().get(i).getProductName())) {
                            typeSelection = i;
                        }
                    }
                    mSpProductName.setSelection(typeSelection, false);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSpProductName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Detail detail = (Detail) parent.getSelectedItem();

                if (detail != null) {
                    productName = detail.getProductName();
                    String[] types = detail.getProductType().split(",");
                    final ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(mContext,
                            android.R.layout.simple_spinner_item, types);
                    typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mSpType.setAdapter(typeAdapter);


                    int typeSelection = 0;
                    for (int i = 0; i < types.length; i++) {
                        if (products.getProductName().equalsIgnoreCase(types[i])) {
                            typeSelection = i;
                        }
                    }
                    mSpType.setSelection(typeSelection, false);


                    String[] grades = detail.getProductGrade().split(",");
                    final ArrayAdapter<String> gradeAdapter = new ArrayAdapter<String>(mContext,
                            android.R.layout.simple_spinner_item, grades);
                    gradeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mSpGrade.setAdapter(gradeAdapter);

                    int gradeSelection = 0;
                    for (int i = 0; i < grades.length; i++) {
                        if (products.getProductGrade().equalsIgnoreCase(grades[i])) {
                            gradeSelection = i;
                        }
                    }
                    mSpGrade.setSelection(gradeSelection, false);


                    String[] units = detail.getProductUnits().split(",");
                    final ArrayAdapter<String> unitsAdapter = new ArrayAdapter<String>(mContext,
                            android.R.layout.simple_spinner_item, detail.getProductUnits().split(","));
                    unitsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mSpUnits.setAdapter(unitsAdapter);

                    int unitsSelection = 0;
                    for (int i = 0; i < units.length; i++) {
                        if (products.getProductGrade().equalsIgnoreCase(units[i])) {
                            unitsSelection = i;
                        }
                    }
                    mSpUnits.setSelection(unitsSelection, false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

       /* mSpPickUpLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                UserAddress userAddressData = (UserAddress) parent.getSelectedItem();
                addressId = userAddressData.getAddressId() + "";

                if (userAddressData != null) {
                    mEdtPinCode.setText(userAddressData.getPinCode() + "");
                    mEdtAddress.setText(userAddressData.getAddress1());
                    mEdtLandMark.setText(userAddressData.getLandmark());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        mEdtContact.setText(siniiaPreferences.getMobileNumber());

        if (SiniiaUtils.checkForNetworkConnectivity(mContext)) {
            new GetProductsTypes().execute();
        } else {
            Toast.makeText(mContext, getString(R.string.please_check_internet_connection), Toast.LENGTH_SHORT).show();
        }


        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_one:
                selectedImgId = R.id.img_one;
                selectImage();
                break;
            case R.id.img_two:
                selectedImgId = R.id.img_two;
                selectImage();
                break;
            case R.id.img_three:
                selectedImgId = R.id.img_three;
                selectImage();
                break;
            case R.id.img_four:
                selectedImgId = R.id.img_four;
                selectImage();
                break;
            case R.id.txt_use_my_location:
                ((MainActivity) mContext).requstNewLocation(this);
                Location location = ((MainActivity) mContext).getLastKnownLocation();
                getAddressFromLocation(location);
                break;
            case R.id.btn_post:
                type = mSpType.getSelectedItem().toString();
                grade = mSpGrade.getSelectedItem().toString();
                units = mSpUnits.getSelectedItem().toString();
                counts = mEdtCount.getText().toString();
                price = mEdtPrice.getText().toString();
                minOty = mEdtMinQty.getText().toString();
               /* radius = mEdtRadius.getText().toString();
                highlights = mEdtHighLight1.getText().toString();*/
                productDescription = mEdtProductDescription.getText().toString();
                pinCode = mEdtPinCode.getText().toString();
                landMark = mEdtLandMark.getText().toString();
                address = mEdtAddress.getText().toString();

                width = mEdtWidth.getText().toString();
                height = mEdtHeight.getText().toString();
                weight = mEdtWeight.getText().toString();
                length = mEdtLength.getText().toString();
                state = mEdtState.getText().toString();
                city = mEdtCity.getText().toString();
                country = mEdtCountry.getText().toString();

                if (counts != null && !counts.isEmpty()) {
                    if (price != null && !price.isEmpty()) {
                        if (minOty != null && !minOty.isEmpty()) {
                           /* if (radius != null && !radius.isEmpty()) {
                                if (highlights != null && !highlights.isEmpty()) {*/
                            if (width != null && !width.isEmpty()) {
                                if (height != null && !height.isEmpty()) {
                                    if (weight != null && !weight.isEmpty()) {
                                        if (productDescription != null && !productDescription.isEmpty()) {
                                            if (pinCode != null && !pinCode.isEmpty()) {
                                                if (country != null && !country.isEmpty()) {
                                                    if (state != null && !state.isEmpty()) {
                                                        if (city != null && !city.isEmpty()) {
                                                            if (landMark != null && !landMark.isEmpty()) {
                                                                if (address != null && !address.isEmpty()) {
                                                                    //    if (imgBytes1 != null && imgBytes1.length != 0) {
                                                                    if (SiniiaUtils.checkForNetworkConnectivity(mContext)) {
                                                                        new PostAProduct().execute();
                                                                    } else {
                                                                        Toast.makeText(mContext, getString(R.string.please_check_internet_connection), Toast.LENGTH_SHORT).show();
                                                                    }
                                                  /*  } else {
                                                        Toast.makeText(mContext, "Please uploaded at least one image", Toast.LENGTH_SHORT).show();
                                                    }*/
                                                                } else {
                                                                    Toast.makeText(mContext, "Please enter address", Toast.LENGTH_SHORT).show();
                                                                }
                                                            } else {
                                                                Toast.makeText(mContext, "Please enter landmark", Toast.LENGTH_SHORT).show();
                                                            }
                                                        } else {
                                                            Toast.makeText(mContext, "Please enter city", Toast.LENGTH_SHORT).show();
                                                        }
                                                    } else {
                                                        Toast.makeText(mContext, "Please enter state", Toast.LENGTH_SHORT).show();
                                                    }
                                                } else {
                                                    Toast.makeText(mContext, "Please enter country", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Toast.makeText(mContext, "Please enter pin code", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(mContext, "Please enter product description", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(mContext, "Please enter weight", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(mContext, "Please enter height", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(mContext, "Please enter width", Toast.LENGTH_SHORT).show();
                            }
                               /* } else {
                                    Toast.makeText(mContext, "Please enter highlights", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(mContext, "Please enter radius", Toast.LENGTH_SHORT).show();
                            }*/
                        } else {
                            Toast.makeText(mContext, "Please enter minimum quantity", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(mContext, "Please enter price", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, "Please enter count", Toast.LENGTH_SHORT).show();
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

                String loc = null;
                try {
                    loc = address.getAddressLine(0);

                } catch (Exception e) {
                    loc = address.getSubLocality() + ", " + address.getSubAdminArea() + ", " + address.getLocality();
                    e.printStackTrace();
                }
                mEdtPinCode.setText(address.getPostalCode());
                mEdtAddress.setText(loc);
                locaLat = location.getLatitude() + "";
                locaLong = location.getLongitude() + "";

                mEdtLandMark.setText(address.getPremises());
                mEdtCountry.setText(address.getCountryName());
                mEdtState.setText(address.getAdminArea());
                mEdtCity.setText(address.getSubAdminArea());
            }
        } else {

        }
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
                setImageView(selectedImgId, thumbnail);
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
                setImageView(selectedImgId, scaled);
            } else {
                Toast.makeText(mContext, "Selected image is unable load", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setImageView(int imageViewId, Bitmap thumbnail) {
        switch (imageViewId) {
            case R.id.img_one:
                imgOne.setImageBitmap(thumbnail);
                imgTwo.setImageDrawable(getActivity().getDrawable(R.drawable.ic_add_image));
                imgBytes1 = SiniiaUtils.convertBitmapToBytes(thumbnail);
                break;
            case R.id.img_two:
                imgTwo.setImageBitmap(thumbnail);
                imgThree.setImageDrawable(getActivity().getDrawable(R.drawable.ic_add_image));
                imgBytes2 = SiniiaUtils.convertBitmapToBytes(thumbnail);
                break;
            case R.id.img_three:
                imgThree.setImageBitmap(thumbnail);
                imgFour.setImageDrawable(getActivity().getDrawable(R.drawable.ic_add_image));
                imgBytes3 = SiniiaUtils.convertBitmapToBytes(thumbnail);
                break;
            case R.id.img_four:
                imgFour.setImageBitmap(thumbnail);
                imgBytes4 = SiniiaUtils.convertBitmapToBytes(thumbnail);
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

    @Override
    public void onLocationChanged(Location location) {

    }

    public class PostAProduct extends AsyncTask<String, String, String> {
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
                jsonObject.put("quantityType", units);
                jsonObject.put("quantityAvailable", counts);
                jsonObject.put("quantityPerUnit", 1);
                jsonObject.put("productOwnerName", siniiaPreferences.getUserName());
                jsonObject.put("categoryName", category);
                jsonObject.put("productGrade", grade);
                jsonObject.put("productName", productName);
                jsonObject.put("pricePerUnit", price);
                jsonObject.put("availableAddressId", addressId);
                //   jsonObject.put("highlight", highlights);
                jsonObject.put("minQuantity", minOty);
                jsonObject.put("productOwenerID", siniiaPreferences.getUserId());
                jsonObject.put("productDescription", productDescription);
                jsonObject.put("productType", type);
                jsonObject.put("productSubName", productName);

                //  jsonObject.put("radius", radius);
                jsonObject.put("pinCode", pinCode);
                jsonObject.put("landMark", landMark);
                jsonObject.put("address1", address);
                jsonObject.put("LastLocationLat", locaLat);
                jsonObject.put("LastLocationLong", locaLong);
                jsonObject.put("productId", products.getProductId());
                // jsonObject.put("")

                jsonObject.put("weight", weight);
                jsonObject.put("height", height);
                jsonObject.put("width", width);

                jsonObject.put("length", length);
                jsonObject.put("state", state);
                jsonObject.put("city", city);
                jsonObject.put("country", country);

                jsonArray.put(jsonObject);
                Log.d(TAG, "XXXXXXXXXXx img " + imgBytes1.length);
                if (imgBytes1 != null && imgBytes1.length != 0) {
                    builder.addPart("image_1.jpeg", new ByteArrayBody(imgBytes1, "image_1.jpeg"));
                }
                if (imgBytes2 != null && imgBytes2.length != 0) {
                    builder.addPart("image_2.jpeg", new ByteArrayBody(imgBytes2, "image_2.jpeg"));
                }
                if (imgBytes3 != null && imgBytes3.length != 0) {
                    builder.addPart("image_3.jpeg", new ByteArrayBody(imgBytes3, "image_3.jpeg"));
                }
                if (imgBytes4 != null && imgBytes4.length != 0) {
                    builder.addPart("image_4.jpeg", new ByteArrayBody(imgBytes4, "image_4.jpeg"));
                }
                builder.addPart("input.json", new ByteArrayBody(jsonArray.toString().getBytes(), "input.json"));

                Log.d(TAG, " jsonArray " + jsonArray.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }
            String result = new HttpHelper().postMultiPartUpdateAProduct(builder.build());

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

    public class GetProductsTypes extends AsyncTask<String, Void, String> {

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
            String result = new HttpHelper().getProductTypes(siniiaPreferences.getUserId());
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (s != null) {
                    ProductTypeNames apiResponse = new Gson().fromJson(s, ProductTypeNames.class);
                    if (apiResponse != null && apiResponse.getStatus() == 200) {
                        List<ProductCategory> productCategoryList = apiResponse.getProductCategories();
                        if (productCategoryList != null && productCategoryList.size() != 0) {
                            ArrayAdapter<ProductCategory> adapter =
                                    new ArrayAdapter<ProductCategory>(mContext, android.R.layout.simple_spinner_dropdown_item, productCategoryList);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            mSpCategory.setAdapter(adapter);
                            int catSelection = 0;
                            if (productCategoryList != null) {
                                for (int i = 0; i < productCategoryList.size(); i++) {
                                    if (products.getCategoryName().equalsIgnoreCase(productCategoryList.get(i).getCategory())) {
                                        catSelection = i;
                                    }
                                }
                            }
                            mSpCategory.setSelection(catSelection, false);
                        }
                        if (apiResponse.getUserAddressData() != null && apiResponse.getUserAddressData().size() != 0) {
                           /* ArrayAdapter<UserAddress> adapter =
                                    new ArrayAdapter<UserAddress>(mContext, android.R.layout.simple_spinner_dropdown_item, apiResponse.getUserAddressData());
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            mSpPickUpLocation.setAdapter(adapter);

                            int addressSelection = 0;
                            UserAddress userAddress = null;
                            if (apiResponse.getUserAddressData() != null) {
                                for (int i = 0; i < apiResponse.getUserAddressData().size(); i++) {
                                    if (products.getAvailableAddressId() == apiResponse.getUserAddressData().get(i).getAddressId()) {
                                        addressSelection = i;
                                        userAddress = apiResponse.getUserAddressData().get(i);
                                        break;
                                    }
                                }
                            }
                            if (userAddress != null) {
                                mEdtPinCode.setText(userAddress.getPinCode() + "");
                                mEdtAddress.setText(userAddress.getAddress1());
                                mEdtLandMark.setText(userAddress.getLandmark());
                                mSpPickUpLocation.setSelection(addressSelection, false);
                            }*/

                          /*  UserAddress address = apiResponse.getUserAddressData().get(apiResponse.getUserAddressData().size() - 1);
                            if (address != null) {
                                if (address.getPinCode() != null) {
                                    mEdtPinCode.setText(address.getPinCode());
                                }
                                if (address.getLandmark() != null) {
                                    mEdtLandMark.setText(address.getLandmark());
                                }
                                String totalAddress = "";
                                if (address.getAddress1() != null) {
                                    totalAddress = address.getAddress1();
                                }
                                if (address.getAddress2() != null) {
                                    totalAddress = totalAddress + ", " + address.getAddress2();
                                }
                                mEdtAddress.setText(totalAddress);
                            }*/
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


}
