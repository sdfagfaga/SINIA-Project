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
import com.sinnia.adapter.OrderSummaryAdapter;
import com.sinnia.data.cart.UserAddress;
import com.sinnia.data.products.AddressDetails;
import com.sinnia.data.products.Products;
import com.sinnia.network.HttpHelper;
import com.sinnia.preferences.SiniiaPreferences;
import com.sinnia.utils.SiniiaUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FragmentOrderSummery extends Fragment implements View.OnClickListener, LocationListener {

    private static final String TAG = "FragmentOrderSummery";
    private Context mContext;
    private SiniiaPreferences siniiaPreferences;
    private RecyclerView mRvCart;
    private TextView mTxtTotalPrice;
    private ArrayList<Products> orderProductsList;
    private int selectedAddressId;
    private TextView mTxtViewPinCode, mTxtAddress;

    private Button mBtnChange;
    private CustomDialogClass customDialogClass;
    private List<UserAddress> productAddressList;
    private UserAddress selectedAddress;

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
        View rootView = inflater.inflate(R.layout.fragment_order_summary, container, false);
        mContext = getActivity();
        productAddressList = new ArrayList<>();
        siniiaPreferences = new SiniiaPreferences(mContext);
        mRvCart = (RecyclerView) rootView.findViewById(R.id.rv_recycler);
        mTxtTotalPrice = (TextView) rootView.findViewById(R.id.txt_total);
        ImageView leftArrow = (ImageView) rootView.findViewById(R.id.ic_left_arrow);
        leftArrow.setOnClickListener(this);

        Button mBtnPlaceOrder = (Button) rootView.findViewById(R.id.btn_continue);
        mBtnPlaceOrder.setOnClickListener(this);

        if (getArguments() != null) {
            orderProductsList = (ArrayList<Products>) getArguments().getSerializable(FragmentCartDetails.SUMMARY);
            productAddressList = (ArrayList<UserAddress>) getArguments().getSerializable(FragmentCartDetails.ADDRESS);
            selectedAddressId = getArguments().getInt("selectedAddressId");
            selectedAddress = (UserAddress) getArguments().getSerializable(FragmentCartDetails.SELECTED_ADDRESS);

            if (orderProductsList != null) {
                double totalPrice = 0;
                for (Products products : orderProductsList) {
                    totalPrice = totalPrice + products.getSelectedTotal();
                }
                mTxtTotalPrice.setText(SiniiaUtils.getCurrencySymbol(siniiaPreferences.getCountryCode()) + totalPrice);
            }
            OrderSummaryAdapter orderSummaryAdapter = new OrderSummaryAdapter(mContext, orderProductsList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
            mRvCart.setLayoutManager(mLayoutManager);
            mRvCart.setItemAnimator(new DefaultItemAnimator());
            mRvCart.setAdapter(orderSummaryAdapter);
            orderSummaryAdapter.notifyDataSetChanged();

            mTxtViewPinCode = (TextView) rootView.findViewById(R.id.txt_delivery_pincode);
            mTxtAddress = (TextView) rootView.findViewById(R.id.txt_address_value);


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

        if (selectedAddress == null) {
            if (SiniiaUtils.checkForNetworkConnectivity(mContext)) {
                new GetUserAddress().execute();
            } else {
                Toast.makeText(mContext, getString(R.string.please_check_internet_connection), Toast.LENGTH_SHORT).show();
            }
        } else {
            mTxtViewPinCode.setText("Delivery Address - " + selectedAddress.getPinCode());
            String address = selectedAddress.getLandmark() + ", " + selectedAddress.getAddress1();
            if (selectedAddress.getAddress2() != null) {
                address = address + ", " + selectedAddress.getAddress2();
            }
            mTxtAddress.setText(address);


            mTxtNamePinCode.setText(siniiaPreferences.getUserName() + ", " + selectedAddress.getPinCode());
            mTxtChangeAddress.setText(address);


        }

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
            case R.id.ic_left_arrow:
                if (getActivity() != null && getActivity().getSupportFragmentManager() != null)
                    getActivity().getSupportFragmentManager().popBackStack();
                closeOrOpenBottomSheet();
                break;
            case R.id.btn_continue:
                if (selectedAddressId != 0) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(FragmentCartDetails.SUMMARY, orderProductsList);
                    Log.d(TAG, "XXXXXXXXXXXXXx address " + selectedAddress);
                    // bundle.putSerializable(FragmentCartDetails.ADDRESS, selectedAddress);
                    bundle.putInt(FragmentCartDetails.SELECT_ADDRESS_ID, selectedAddressId);
                    bundle.putSerializable(FragmentCartDetails.SELECTED_ADDRESS, selectedAddress);
                    ((MainActivity) mContext).launchFragment(new FragmentShipping(), getString(R.string.fragment_shipping), bundle);
                } else {
                    Toast.makeText(mContext, "Please change Address", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_change:
               /* if (SiniiaUtils.checkForNetworkConnectivity(mContext)) {
                    customDialogClass = new CustomDialogClass(getActivity(), productAddressList);
                    customDialogClass.show();
                    Window window = customDialogClass.getWindow();
                    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                } else {
                    Toast.makeText(mContext, getString(R.string.please_check_internet_connection), Toast.LENGTH_SHORT).show();
                }*/

                closeOrOpenBottomSheet();

                break;

        }

    }

    @Override
    public void onLocationChanged(Location location) {

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
                        selectedAddress = productAddress;
                        Log.d(TAG, "XXXXXXXXXXXXXXXxx selected Address " + selectedAddress);
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
                Log.d(TAG, "XXXXXXXXXXXXx result " + s);
                if (s != null) {
                    AddressDetails productsResponse = new Gson().fromJson(s, AddressDetails.class);
                    if (productsResponse != null) {
                        Integer status = productsResponse.getStatus();
                        if (status == 200) {
                            productAddressList = new ArrayList<>();
                            productAddressList.addAll(productsResponse.getData());

                            if (productAddressList != null && productAddressList.size() != 0) {

                                UserAddress userAddress = productAddressList.get(0);
                                selectedAddressId = userAddress.getAddressId();
                                selectedAddress = userAddress;

                                mTxtViewPinCode.setText("Delivery Address - " + userAddress.getPinCode());
                                mTxtAddress.setText(userAddress.getLandmark() + ", " + userAddress.getAddress1() + ", " + userAddress.getAddress2());

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

    public void setAddressValues() {
        mTxtViewPinCode.setText("Delivery Address - " + selectedUserAddress.getPinCode());
        mTxtAddress.setText(selectedUserAddress.getLandmark() + ", " + selectedUserAddress.getAddress1());
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
                    loc = address.getSubLocality() + ", " + address.getSubAdminArea() + ", " + address.getLocality();
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
}
