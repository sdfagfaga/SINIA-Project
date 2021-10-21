package com.sinnia.fragments.orders;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.LocationListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;
import com.sinnia.R;
import com.sinnia.activites.MainActivity;
import com.sinnia.adapter.Cart1Adapter;
import com.sinnia.data.cart.CartData;
import com.sinnia.data.cart.CartResponse;
import com.sinnia.data.cart.UserAddress;
import com.sinnia.data.login.User;
import com.sinnia.data.products.AddressDetails;
import com.sinnia.data.products.Products;
import com.sinnia.listeners.PriceListner;
import com.sinnia.network.HttpHelper;
import com.sinnia.preferences.SiniiaPreferences;
import com.sinnia.utils.SiniiaUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class FragmentCartDetails extends Fragment implements PriceListner, View.OnClickListener, LocationListener {

    private static final String TAG = "FragmentCartDetails";
    private Context mContext;
    private SiniiaPreferences siniiaPreferences;
    private RecyclerView mRvCart;
    private Cart1Adapter cart1Adapter;
    private TextView mTxtTotalPrice, mTxtViewPinCode, mTxtAddress;
    private PriceListner priceListner;
    private Button mBtnPlaceOrder;
    private CheckBox mCheckSelectAll;

    ArrayList<Products> cartList;
    //   ArrayList<UserAddress> userAddressList;
    private Button mBtnChange;
    private CustomDialogClass customDialogClass;
    private ArrayList<UserAddress> productAddressList;
    public static final String SELECT_ADDRESS_ID = "selectedAddressId";
    public static final String ADDRESS = "address";
    public static final String SUMMARY = "summary";
    public static final String SELECTED_ADDRESS = "selectAddress";

    public View rootView;

    private BottomSheetBehavior bottomSheetBehavior;
    private ImageView imgCloseAddress;
    private Button mBtnAddressSubmit;
    private TextView mTxtNamePinCode, mTxtChangeAddress, mTxtUseMyLocation, mTxtEnterAddress;
    private EditText mEdtPinCode, mEdtLandmark, mEdtAddress1, mEdtState, mEdtCity;
    private LinearLayout mLinearAddressLayout;
    private UserAddress selectedUserAddress;
    private RadioButton mRdBtnDefaultAddress;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_cart_details, container, false);
        mContext = getActivity();
        siniiaPreferences = new SiniiaPreferences(mContext);
        priceListner = this;
        mCheckSelectAll = (CheckBox) rootView.findViewById(R.id.check_select_all);
        mRvCart = (RecyclerView) rootView.findViewById(R.id.rv_recycler);
        mTxtTotalPrice = (TextView) rootView.findViewById(R.id.txt_total);
        mBtnPlaceOrder = (Button) rootView.findViewById(R.id.btn_place_order);
        mBtnPlaceOrder.setOnClickListener(this);

        mTxtViewPinCode = (TextView) rootView.findViewById(R.id.txt_delivery_pincode);
        mTxtAddress = (TextView) rootView.findViewById(R.id.txt_address_value);

        ImageView leftArrow = (ImageView) rootView.findViewById(R.id.ic_left_arrow);
        leftArrow.setOnClickListener(this);

        if (SiniiaUtils.checkForNetworkConnectivity(mContext)) {
            new GetCartData().execute();
        } else {
            Toast.makeText(mContext, getString(R.string.please_check_internet_connection), Toast.LENGTH_SHORT).show();
        }

        if (SiniiaUtils.checkForNetworkConnectivity(mContext)) {
            new GetUserAddress().execute();
        } else {
            Toast.makeText(mContext, getString(R.string.please_check_internet_connection), Toast.LENGTH_SHORT).show();
        }


        mBtnChange = (Button) rootView.findViewById(R.id.btn_change);
        mBtnChange.setOnClickListener(this);

        LinearLayout mLinearBottomLayout = rootView.findViewById(R.id.bottom_sheet_layout);
        bottomSheetBehavior = BottomSheetBehavior.from(mLinearBottomLayout);

        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (mLinearAddressLayout != null) {
                    if (mLinearAddressLayout.getVisibility() == View.VISIBLE) {
                        mLinearAddressLayout.setVisibility(View.GONE);
                    } else {
                        mLinearAddressLayout.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        imgCloseAddress = (ImageView) rootView.findViewById(R.id.ic_close);
        mBtnAddressSubmit = (Button) rootView.findViewById(R.id.btn_submit);
        mTxtNamePinCode = (TextView) rootView.findViewById(R.id.txt_name_pin_code);
        mTxtChangeAddress = (TextView) rootView.findViewById(R.id.txt_change_address);
        mTxtUseMyLocation = (TextView) rootView.findViewById(R.id.txt_use_my_location);
        mTxtEnterAddress = (TextView) rootView.findViewById(R.id.txt_enter_address);
        mRdBtnDefaultAddress = (RadioButton) rootView.findViewById(R.id.rd_select_address);


        mEdtPinCode = (EditText) rootView.findViewById(R.id.edt_pin_code);
        mEdtLandmark = (EditText) rootView.findViewById(R.id.edt_landmark);
        mEdtAddress1 = (EditText) rootView.findViewById(R.id.edt_address1);

        mEdtState = (EditText) rootView.findViewById(R.id.edt_state);
        mEdtCity = (EditText) rootView.findViewById(R.id.edt_city);


        mLinearAddressLayout = (LinearLayout) rootView.findViewById(R.id.linear_address_layout);

        imgCloseAddress.setOnClickListener(this);
        mTxtUseMyLocation.setOnClickListener(this);
        mTxtEnterAddress.setOnClickListener(this);
        mBtnAddressSubmit.setOnClickListener(this);

        mRdBtnDefaultAddress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (productAddressList != null) {

                        UserAddress userAddress = productAddressList.get(0);
                        selectedAddressId = userAddress.getAddressId();
                        mTxtViewPinCode.setText("Delivery Address - " + userAddress.getPinCode());
                        mTxtAddress.setText(userAddress.getLandmark() + ", " + userAddress.getAddress1() + ", " + userAddress.getAddress2());


                        selectedUserAddress = userAddress;

                    }
                }
                closeOrOpenBottomSheet();
            }
        });


        return rootView;
    }


    void closeOrOpenBottomSheet() {
        if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                String pinCode = mEdtPinCode.getText().toString();
                String landmark = mEdtLandmark.getText().toString();
                String address = mEdtAddress1.getText().toString();
                String state = mEdtState.getText().toString();
                String city = mEdtCity.getText().toString();
                    if (state != null && !state.isEmpty()) {
                        if (city != null && !city.isEmpty()) {
                            if (pinCode != null && !pinCode.isEmpty()) {
                                if (landmark != null && !landmark.isEmpty()) {
                                    if (address != null && !address.isEmpty()) {
                                        selectedUserAddress = new UserAddress();
                                        selectedUserAddress.setPinode(pinCode);
                                        selectedUserAddress.setAddress1(address);
                                        selectedUserAddress.setLandmark(landmark);
                                        selectedUserAddress.setCountry(siniiaPreferences.getCountryName());
                                        selectedUserAddress.setState(state);
                                        selectedUserAddress.setCity(city);
                                        selectedUserAddress.setAddress2(null);
                                        setAddressValues();
                                        closeOrOpenBottomSheet();
                                    } else {
                                        Toast.makeText(mContext, "Please enter valid address", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(mContext, "Please enter valid landmark", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(mContext, "Please enter valid pin code", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(mContext, "Please enter city", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(mContext, "Please enter state", Toast.LENGTH_SHORT).show();
                    }


                break;
            case R.id.txt_enter_address:
                mRdBtnDefaultAddress.setChecked(false);
                if (mLinearAddressLayout != null) {
                    if (mLinearAddressLayout.getVisibility() == View.VISIBLE) {
                        mLinearAddressLayout.setVisibility(View.GONE);
                    } else {
                        mLinearAddressLayout.setVisibility(View.VISIBLE);
                    }
                }
                break;

            case R.id.txt_use_my_location:
                mRdBtnDefaultAddress.setChecked(false);
                ((MainActivity) mContext).requstNewLocation(this);
                Location location = ((MainActivity) mContext).getLastKnownLocation();
                getAddressFromLocation(location);
                break;
            case R.id.ic_close:
                closeOrOpenBottomSheet();
                break;
            case R.id.ic_left_arrow:
                if (getActivity() != null && getActivity().getSupportFragmentManager() != null) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
                closeOrOpenBottomSheet();
                break;
            case R.id.btn_place_order:
                if (cart1Adapter != null && cart1Adapter.getSelectedArrayList() != null) {
                    ArrayList<Products> selectedProductList = new ArrayList<>();
                    selectedProductList.addAll(cart1Adapter.getSelectedArrayList().values());
                    if (selectedProductList.size() != 0) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(SUMMARY, selectedProductList);
                        bundle.putSerializable(ADDRESS, productAddressList);
                        bundle.putInt(SELECT_ADDRESS_ID, selectedAddressId);
                        bundle.putSerializable(SELECTED_ADDRESS, selectedUserAddress);
                        ((MainActivity) mContext).launchFragment(new FragmentOrderSummery(), getString(R.string.fragment_order_summary), bundle);
                    } else {
                        Toast.makeText(mContext, "Please select at least one product", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.btn_change:
                closeOrOpenBottomSheet();
                break;
        }
    }

    @Override
    public void onLocationChanged(Location location) {

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
                selectedUserAddress = new UserAddress();
                Address address = addresses.get(0);
                Log.d(TAG, "location 1" + address.getLocality());
                Log.d(TAG, "location 2" + address.getSubLocality());
                Log.d(TAG, "location 3" + address.getSubAdminArea());
                Log.d(TAG, "location 4" + address.getPremises());
                Log.d(TAG, "location 5" + address.getFeatureName());
                Log.d(TAG, " address " + address.getPremises());
                //  mEdtLandMark.setText(address.getPremises());
                selectedUserAddress.setLandmark(address.getPremises());
                String loc = null;
                try {
                    loc = address.getAddressLine(0);

                } catch (Exception e) {
                    loc = address.getSubAdminArea() + ", " + address.getLocality();
                    e.printStackTrace();
                }

                selectedUserAddress.setPinode(address.getPostalCode());
                //  mEdtAddress.setText(loc);
                selectedUserAddress.setAddress1(loc);
                selectedUserAddress.setAddressLat(location.getLatitude() + "");
                selectedUserAddress.setAddressLong(location.getLongitude() + "");
                selectedUserAddress.setCountry(address.getCountryName());
                selectedUserAddress.setState(address.getAdminArea());
                selectedUserAddress.setCity(address.getSubAdminArea());

                setAddressValues();
                closeOrOpenBottomSheet();

            }
        } else {
            Toast.makeText(mContext, "Unable to get the current location", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getTotalPrice(Map<String, Products> stringLongMap) {
        double totalPrice = 0;
        for (Map.Entry<String, Products> entry : stringLongMap.entrySet()) {
            Products products = entry.getValue();
            Log.d(TAG, "XXXXXXXXXXXxxx products " + products.toString());
            totalPrice = totalPrice + products.getSelectedTotal();
            // do something with key and/or tab
        }
        if (cartList != null && stringLongMap != null) {
            if (cartList.size() == stringLongMap.size()) {
                mCheckSelectAll.setChecked(true);
            } else {
                mCheckSelectAll.setChecked(false);
            }
        }
        mTxtTotalPrice.setText(SiniiaUtils.getCurrencySymbol(siniiaPreferences.getCountryCode()) + totalPrice);
    }

    @Override
    public void deleteCartItem(String id) {
        try {
            if (SiniiaUtils.checkForNetworkConnectivity(mContext)) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("productId", id);
                jsonObject.put("userId", siniiaPreferences.getUserId());
                new DeleteTask().execute(jsonObject.toString());
            } else {
                Toast.makeText(mContext, getString(R.string.please_check_internet_connection), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public class DeleteTask extends AsyncTask<String, Void, String> {

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
            String result = new HttpHelper().deleteFromCart(strings[0]);
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
                    CartResponse productsResponse = new Gson().fromJson(s, CartResponse.class);
                    if (productsResponse != null) {
                        Integer status = productsResponse.getStatus();
                        if (status == 200) {
                            if (SiniiaUtils.checkForNetworkConnectivity(mContext)) {
                                new GetCartData().execute();
                            } else {
                                Toast.makeText(mContext, getString(R.string.please_check_internet_connection), Toast.LENGTH_SHORT).show();
                            }
                        }
                        Toast.makeText(mContext, productsResponse.getDescription(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, "Unable to delete product from cart", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, "Unable to delete product from cart", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(mContext, "Unable to delete product from cart", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }


    public class GetCartData extends AsyncTask<String, Void, String> {

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
            String result = new HttpHelper().getCartData(siniiaPreferences.getUserId());
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
                    CartResponse productsResponse = new Gson().fromJson(s, CartResponse.class);
                    if (productsResponse != null) {
                        Integer status = productsResponse.getStatus();
                        if (status == 200) {
                            CartData cartData = productsResponse.getData();
                            if (cartData != null) {
                                cartList = cartData.getProductData();
                                ArrayList<Products> latestArrayList = new ArrayList<>();
                                for (Products products : cartList) {
                                    Products products1 = new Products();
                                    products1.setSelectedQuantity(0);
                                    products1.setSelectedTotal(0);
                                    products1.setQuantityType(products.getQuantityType());
                                    products1.setQuantityAvailable(products.getQuantityAvailable());
                                    products1.setQuantityPerUnit(products.getQuantityPerUnit());
                                    products1.setProductOwnerName(products.getProductOwnerName());
                                    products1.setCategoryName(products.getCategoryName());
                                    products1.setProductGrade(products.getProductGrade());
                                    products1.setProductName(products.getProductName());
                                    products1.setPricePerUnit(products.getPricePerUnit());
                                    products1.setAvailableAddressId(products.getAvailableAddressId());
                                    products1.setHighlight(products.getHighlight());
                                    products1.setCreatedDate(products.getCreatedDate());
                                    products1.setMinQuantity(products.getMinQuantity());
                                    products1.setProductOwenerID(products.getProductOwenerID());
                                    products1.setId(products.getId());
                                    products1.setProductDescription(products.getProductDescription());
                                    products1.setProductType(products.getProductType());
                                    products1.setProductSubName(products.getProductSubName());
                                    products1.setThumbImageURL(products.getThumbImageURL());
                                    latestArrayList.add(products1);
                                }
                                cart1Adapter = new Cart1Adapter(mContext, latestArrayList, priceListner);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                                mRvCart.setLayoutManager(mLayoutManager);
                                mRvCart.setItemAnimator(new DefaultItemAnimator());
                                mRvCart.setAdapter(cart1Adapter);
                                cart1Adapter.notifyDataSetChanged();


                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class GetUserAddress extends AsyncTask<String, Void, String> {

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
            String result = new HttpHelper().getUserAddress(siniiaPreferences.getUserId());
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
                    AddressDetails productsResponse = new Gson().fromJson(s, AddressDetails.class);
                    if (productsResponse != null) {
                        Integer status = productsResponse.getStatus();
                        if (status == 200) {
                            productAddressList = new ArrayList<>();
                            productAddressList.addAll(productsResponse.getData());
                            if (productsResponse.getData() != null) {
                                UserAddress userAddress = productsResponse.getData().get(0);
                                selectedAddressId = userAddress.getAddressId();
                                mTxtViewPinCode.setText("Delivery Address - " + userAddress.getPinCode());
                                mTxtAddress.setText(userAddress.getLandmark() + ", " + userAddress.getAddress1() + ", " + userAddress.getAddress2());

                                selectedUserAddress = userAddress;
                                mTxtNamePinCode.setText(siniiaPreferences.getUserName() + ", " + userAddress.getPinCode());
                                mTxtChangeAddress.setText(userAddress.getLandmark() + ", " + userAddress.getAddress1() + ", " + userAddress.getAddress2());

                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class CustomDialogClass extends Dialog {

        public Activity c;
        public Dialog d;
        private RecyclerView mRvAddress;
        private List<UserAddress> productAddressList;

        public CustomDialogClass(Activity a, List<UserAddress> productAddressList) {
            super(a);
            this.c = a;
            this.productAddressList = productAddressList;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.custom_address_change_layout);
            mRvAddress = (RecyclerView) findViewById(R.id.rv_address);

            AddressAadaper addressAadaper = new AddressAadaper(mContext, productAddressList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
            mRvAddress.setLayoutManager(mLayoutManager);
            mRvAddress.setItemAnimator(new DefaultItemAnimator());
            mRvAddress.setAdapter(addressAadaper);
            addressAadaper.notifyDataSetChanged();
        }


    }

    int selectedAddressId;

    public class AddressAadaper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private static final String TAG = "BuySellAdapter";
        private Context mContext;
        private List<UserAddress> productAddressList;

        public AddressAadaper(Context mContext, List<UserAddress> productAddressList) {
            this.mContext = mContext;
            this.productAddressList = productAddressList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.address_item_layout, parent, false);
            return new ItemViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder recyleViewHolder, int position) {
            final ItemViewHolder holder = (ItemViewHolder) recyleViewHolder;
            final UserAddress productAddress = productAddressList.get(position);
            if (productAddress != null) {
                if (productAddress != null) {
                    String address = null;
                    if (productAddress.getAddress1() != null) {
                        address = productAddress.getAddress1();
                    }
                    if (productAddress.getAddress2() != null) {
                        address = address + ", " + productAddress.getAddress2();
                    }
                    if (productAddress.getLandmark() != null) {
                        address = address + ", " + productAddress.getLandmark();
                    }
                    if (productAddress.getPinCode() != null) {
                        address = address + ", " + productAddress.getPinCode();
                    }
                    holder.txtAddress.setText(address);
                }
                holder.bind(productAddress);
                if (productAddress.getAddressId() == selectedAddressId) {
                    holder.txtAddress.setBackgroundColor(getContext().getColor(R.color.grey_color));
                } else {
                    holder.txtAddress.setBackgroundColor(getContext().getColor(R.color.white_color));
                }
            }


        }

        @Override
        public int getItemCount() {
            if (productAddressList != null) {
                return productAddressList.size();
            } else {
                return 0;
            }
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder {
            TextView txtAddress;

            public ItemViewHolder(View view) {
                super(view);
                txtAddress = (TextView) view.findViewById(R.id.txt_address);

            }

            public void bind(final UserAddress productAddress) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectedAddressId = productAddress.getAddressId();
                        if (productAddress != null) {
                            String address = null;
                            if (productAddress.getAddress1() != null) {
                                address = productAddress.getAddress1();
                            }
                            if (productAddress.getAddress2() != null) {
                                address = address + ", " + productAddress.getAddress2();
                            }
                            if (productAddress.getLandmark() != null) {
                                address = address + "\n" + productAddress.getLandmark();
                            }
                            if (productAddress.getPinCode() != null) {
                                address = address + ", " + productAddress.getPinCode();
                            }
                            mTxtViewPinCode.setText("Delivery Address - " + productAddress.getPinCode());
                            mTxtAddress.setText(address);
                        }
                        if (customDialogClass != null) {
                            customDialogClass.dismiss();
                        }
                    }
                });
            }
        }


    }

    public void setAddressValues() {
        mTxtViewPinCode.setText("Delivery Address - " + selectedUserAddress.getPinCode());
        mTxtAddress.setText(selectedUserAddress.getLandmark() + ", " + selectedUserAddress.getAddress1());
    }


}
