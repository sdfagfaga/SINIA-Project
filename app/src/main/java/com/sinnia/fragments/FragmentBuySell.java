package com.sinnia.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.Api;
import com.google.gson.Gson;
import com.sinnia.R;
import com.sinnia.activites.MainActivity;
import com.sinnia.activites.RegisterActivity;
import com.sinnia.activites.SelectTypeActivity;
import com.sinnia.adapter.BuySellAdapter;
import com.sinnia.adapter.MyPostsAdapter;
import com.sinnia.adapter.SellAdapter;
import com.sinnia.data.products.EditProduct;
import com.sinnia.data.products.Products;
import com.sinnia.data.selecttype.ApiResponse;
import com.sinnia.data.sell.SellResponse;
import com.sinnia.fragments.orders.FragmentCartDetails;
import com.sinnia.listeners.ItemClickListener;
import com.sinnia.listeners.MyPostListeners;
import com.sinnia.network.HttpHelper;
import com.sinnia.preferences.SiniiaPreferences;
import com.sinnia.utils.BuySellItem;
import com.sinnia.utils.SiniiaConstants;
import com.sinnia.utils.SiniiaUtils;

import java.util.ArrayList;

public class FragmentBuySell extends Fragment implements View.OnClickListener, ItemClickListener, MyPostListeners {


    private static final String TAG = "FragmentBuySell";
    private RecyclerView mRvBuySell;
    private Context mContext;
    private Button mBtnBuy, mBtnSell, mBtnMyPosts;
    private SiniiaPreferences siniiaPreferences;
    private RelativeLayout mRelMyPostsLayout;
    private ImageView mImgPostProduct;
    private ItemClickListener itemClickListener;
    private MyPostListeners myPostListeners;
    public static final int IN_ACTIVE = 0;
    public static final int ACTIVE = 1;
    private TextView mTxtAll, mTxtActive;
    private ArrayList<Products> myPostProductArrayList;
    private ArrayList<Products> selectedMyPostList;
    private MyPostsAdapter myPostsAdapter;
    private TextView mTxtNoDataFound;
    private BuySellAdapter buySellAdapter;
    private SellAdapter sellAdapter;
    private int selectedButton;

    public void callMethode() {
        Log.d(TAG, "XXXXXXXXXXXXXXXXX selected button " + selectedButton);
        switch (selectedButton) {
            case R.id.btn_buy:
                if (SiniiaUtils.checkForNetworkConnectivity(mContext)) {
                    new GetBuyData().execute();
                } else {
                    Toast.makeText(mContext, getString(R.string.please_check_internet_connection), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_sell:
                if (SiniiaUtils.checkForNetworkConnectivity(mContext)) {
                    new GetSellData().execute();
                } else {
                    Toast.makeText(mContext, getString(R.string.please_check_internet_connection), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_my_posts:
                if (SiniiaUtils.checkForNetworkConnectivity(mContext)) {
                    new GetMyPostData().execute();
                } else {
                    Toast.makeText(mContext, getString(R.string.please_check_internet_connection), Toast.LENGTH_SHORT).show();
                }
                break;


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_buy_sell, container, false);
        mContext = getContext();
        siniiaPreferences = new SiniiaPreferences(mContext);
        myPostListeners = this;

        mRvBuySell = (RecyclerView) rootView.findViewById(R.id.rv_buy_sell);
        mTxtNoDataFound = (TextView) rootView.findViewById(R.id.txt_no_products);

        mBtnBuy = (Button) rootView.findViewById(R.id.btn_buy);
        mBtnSell = (Button) rootView.findViewById(R.id.btn_sell);
        mBtnMyPosts = (Button) rootView.findViewById(R.id.btn_my_posts);
        mRelMyPostsLayout = (RelativeLayout) rootView.findViewById(R.id.relative_mypost_layout);

        mTxtAll = (TextView) rootView.findViewById(R.id.txt_all);
        mTxtActive = (TextView) rootView.findViewById(R.id.txt_active);

        mBtnBuy.setOnClickListener(this);
        mBtnSell.setOnClickListener(this);
        mBtnMyPosts.setOnClickListener(this);

        selectedButton = R.id.btn_buy;

        mImgPostProduct = (ImageView) rootView.findViewById(R.id.img_add_product);
        mImgPostProduct.setOnClickListener(this);

        setCartNotification(rootView);
        if (SiniiaUtils.checkForNetworkConnectivity(mContext)) {
            new GetBuyData().execute();
        } else {
            Toast.makeText(mContext, getString(R.string.please_check_internet_connection), Toast.LENGTH_SHORT).show();
        }
        itemClickListener = this;

        mTxtAll.setOnClickListener(this);
        mTxtActive.setOnClickListener(this);

        return rootView;
    }

    public void setCartNotification(View rootView) {
        View cartView = (View) rootView.findViewById(R.id.ic_cart_layout);
        View notificationView = (View) rootView.findViewById(R.id.ic_notification_layout);
        TextView mTxtNotificationCount = (TextView) notificationView.findViewById(R.id.txt_notification_count);
        TextView mTxtCartCount = (TextView) cartView.findViewById(R.id.txt_cart_count);

        ImageView mImgLogo = (ImageView) rootView.findViewById(R.id.ic_logo);

        TextView mTxtStall14=(TextView)rootView.findViewById(R.id.txt_stall_14);
        mTxtStall14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SelectTypeActivity.class);
                startActivity(intent);
            }
        });

        mImgLogo.setOnClickListener(new View.OnClickListener() {
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

        EditText mEdtSearch = (EditText) rootView.findViewById(R.id.edt_search);
        mEdtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                switch (selectedButton) {
                    case R.id.btn_buy:
                        if (buySellAdapter != null) {
                            buySellAdapter.alertFilter(s.toString());
                            buySellAdapter.notifyDataSetChanged();
                        }
                        break;
                    case R.id.btn_sell:
                        if (buySellAdapter != null) {
                            sellAdapter.alertFilter(s.toString());
                            sellAdapter.notifyDataSetChanged();
                        }
                        break;
                    case R.id.btn_my_posts:
                        if (buySellAdapter != null) {
                            myPostsAdapter.alertFilter(s.toString());
                            myPostsAdapter.notifyDataSetChanged();
                        }
                        break;

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_buy:
                SiniiaUtils.selectedButton(mContext, mBtnBuy);
                SiniiaUtils.unSelectedButton(mContext, mBtnSell);
                SiniiaUtils.unSelectedButton(mContext, mBtnMyPosts);
                mRelMyPostsLayout.setVisibility(View.GONE);
                if (SiniiaUtils.checkForNetworkConnectivity(mContext)) {
                    new GetBuyData().execute();
                } else {
                    hideRv();
                    Toast.makeText(mContext, getString(R.string.please_check_internet_connection), Toast.LENGTH_SHORT).show();
                }
                selectedButton = R.id.btn_buy;
                break;
            case R.id.btn_sell:
                selectedButton = R.id.btn_sell;
                SiniiaUtils.unSelectedButton(mContext, mBtnBuy);
                SiniiaUtils.selectedButton(mContext, mBtnSell);
                SiniiaUtils.unSelectedButton(mContext, mBtnMyPosts);
                mRelMyPostsLayout.setVisibility(View.GONE);
                if (SiniiaUtils.checkForNetworkConnectivity(mContext)) {
                    new GetSellData().execute();
                } else {
                    hideRv();
                    Toast.makeText(mContext, getString(R.string.please_check_internet_connection), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_my_posts:
                selectedButton = R.id.btn_my_posts;
                SiniiaUtils.unSelectedButton(mContext, mBtnBuy);
                SiniiaUtils.unSelectedButton(mContext, mBtnSell);
                SiniiaUtils.selectedButton(mContext, mBtnMyPosts);
                mRelMyPostsLayout.setVisibility(View.VISIBLE);
                if (SiniiaUtils.checkForNetworkConnectivity(mContext)) {
                    new GetMyPostData().execute();
                } else {
                    hideRv();
                    Toast.makeText(mContext, getString(R.string.please_check_internet_connection), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.img_add_product:
                ((MainActivity) mContext).launchFragment(new FragmentPostAProduct(), getString(R.string.post_a_product_fragment));
                break;
            case R.id.ic_notification_layout:
                break;
            case R.id.ic_cart_layout:
                if (siniiaPreferences.getCartCount() != 0) {
                    ((MainActivity) mContext).launchFragment(new FragmentCartDetails(), getString(R.string.fragment_product_details));
                } else {
                    Toast.makeText(mContext, getString(R.string.add_products_cart), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.txt_all:
                mTxtAll.setBackground(getContext().getDrawable(R.drawable.button_selected));
                mTxtAll.setTextColor(getContext().getColor(R.color.white_color));
                mTxtActive.setBackground(getContext().getDrawable(R.drawable.capulse_filled_selected));
                mTxtActive.setTextColor(getContext().getColor(R.color.dark_black_color));
                if (myPostsAdapter != null) {
                    selectedMyPostList.clear();
                    selectedMyPostList.addAll(myPostProductArrayList);
                    myPostsAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.txt_active:
                mTxtAll.setBackground(getContext().getDrawable(R.drawable.capulse_filled_selected));
                mTxtAll.setTextColor(getContext().getColor(R.color.dark_black_color));
                mTxtActive.setBackground(getContext().getDrawable(R.drawable.button_selected));
                mTxtActive.setTextColor(getContext().getColor(R.color.white_color));
                if (myPostsAdapter != null) {
                    selectedMyPostList.clear();
                    for (Products products : myPostProductArrayList) {
                        if (products.getProductStatus() == 1) {
                            selectedMyPostList.add(products);
                        }
                    }
                    myPostsAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    @Override
    public void clickOnActiveOrInactiveButton(int status, String productId) {
        if (SiniiaUtils.checkForNetworkConnectivity(mContext)) {

            /*Chnageing Status*/
            if (status == IN_ACTIVE) {
                status = ACTIVE;
            } else {
                status = IN_ACTIVE;
            }
            new GetUpdateMyPostStatus().execute(productId, status + "");
        } else {
            Toast.makeText(mContext, getString(R.string.please_check_internet_connection), Toast.LENGTH_SHORT).show();
        }
    }

    CustomDialogClass customDialogClass;

    @Override
    public void clickOnEdit(Products products) {
      /*  if (SiniiaUtils.checkForNetworkConnectivity(mContext)) {
            customDialogClass = new CustomDialogClass(getActivity(), productId);
            customDialogClass.show();
            Window window = customDialogClass.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        } else {
            Toast.makeText(mContext, getString(R.string.please_check_internet_connection), Toast.LENGTH_SHORT).show();
        }*/

        Bundle bundle = new Bundle();
        bundle.putSerializable("product", products);
        ((MainActivity) mContext).launchFragment(new FragmentUpdateProduct(), getString(R.string.fragment_product_details), bundle);
    }


    public class GetBuyData extends AsyncTask<String, Void, String> {

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
            String result = new HttpHelper().GetBuyData(siniiaPreferences.getUserId());
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
                    SellResponse sellResponse = new Gson().fromJson(s, SellResponse.class);
                    if (sellResponse != null) {
                        int status = sellResponse.getStatus();
                        if (status == 200) {

                            buySellAdapter = new BuySellAdapter(mContext, sellResponse.getProducts(), itemClickListener);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                            mRvBuySell.setLayoutManager(mLayoutManager);
                            mRvBuySell.setItemAnimator(new DefaultItemAnimator());
                            mRvBuySell.setAdapter(buySellAdapter);
                            buySellAdapter.notifyDataSetChanged();
                            showRv();

                        } else {
                            hideRv();
                            Toast.makeText(mContext, sellResponse.getDescription(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        hideRv();
                    }
                } else {
                    hideRv();
                }
            } catch (Exception e) {
                hideRv();
                e.printStackTrace();
            }

        }
    }

    public class GetSellData extends AsyncTask<String, Void, String> {

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
            String result = new HttpHelper().GetSellData(siniiaPreferences.getUserId());
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
                    SellResponse sellResponse = new Gson().fromJson(s, SellResponse.class);
                    if (sellResponse != null) {
                        int status = sellResponse.getStatus();
                        if (status == 200) {
                            sellAdapter = new SellAdapter(mContext, sellResponse.getProducts(), itemClickListener);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                            mRvBuySell.setLayoutManager(mLayoutManager);
                            mRvBuySell.setItemAnimator(new DefaultItemAnimator());
                            mRvBuySell.setAdapter(sellAdapter);
                            sellAdapter.notifyDataSetChanged();
                            showRv();
                        } else {
                            hideRv();
                            Toast.makeText(mContext, sellResponse.getDescription(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        hideRv();
                    }
                } else {
                    hideRv();
                }
            } catch (Exception e) {
                hideRv();
                e.printStackTrace();
            }

        }
    }

    public class GetMyPostData extends AsyncTask<String, Void, String> {

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
            String result = new HttpHelper().getMyPostData(siniiaPreferences.getUserId());
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
                    SellResponse sellResponse = new Gson().fromJson(s, SellResponse.class);
                    if (sellResponse != null) {
                        int status = sellResponse.getStatus();
                        if (status == 200) {
                            myPostProductArrayList = new ArrayList<>();
                            selectedMyPostList = new ArrayList<>();
                            selectedMyPostList.addAll(sellResponse.getProducts());
                            myPostProductArrayList.addAll(sellResponse.getProducts());
                            myPostsAdapter = new MyPostsAdapter(mContext, selectedMyPostList, myPostListeners);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                            mRvBuySell.setLayoutManager(mLayoutManager);
                            mRvBuySell.setItemAnimator(new DefaultItemAnimator());
                            mRvBuySell.setAdapter(myPostsAdapter);
                            myPostsAdapter.notifyDataSetChanged();
                            showRv();
                        } else {
                            hideRv();
                            Toast.makeText(mContext, sellResponse.getDescription(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        hideRv();
                    }
                } else {
                    hideRv();
                }
            } catch (Exception e) {
                hideRv();
                e.printStackTrace();
            }

        }
    }

    @Override
    public void clickOnRVItem(Products products) {
        if (products.getOrderId() != null && products.getOrderId() != 0) {
            Bundle bundle = new Bundle();
            bundle.putString("productId", products.getProductId() + "");
            bundle.putString("orderId", products.getOrderId() + "");
            ((MainActivity) mContext).launchFragment(new FragmentBuySellProductDeatils(), getString(R.string.fragment_buy_sell_product_details), bundle);
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("productId", products.getProductId() + "");
            ((MainActivity) mContext).launchFragment(new FragmentProductDetails(), getString(R.string.fragment_product_details), bundle);
        }
    }

    public class GetUpdateMyPostStatus extends AsyncTask<String, Void, String> {

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
            String result = new HttpHelper().updateProductStatus(strings[0], siniiaPreferences.getUserId(), strings[1]);
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
                    ApiResponse sellResponse = new Gson().fromJson(s, ApiResponse.class);
                    if (sellResponse != null) {
                        int status = sellResponse.getStatus();
                        if (status == 200) {
                            if (SiniiaUtils.checkForNetworkConnectivity(mContext)) {
                                new GetMyPostData().execute();
                            } else {
                                Toast.makeText(mContext, getString(R.string.please_check_internet_connection), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(mContext, sellResponse.getDescription(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public class CustomDialogClass extends Dialog implements
            android.view.View.OnClickListener {

        public Activity c;
        public Dialog d;
        public Button btnEdit;
        private EditText mEdtMinQty, mEdtAvaQty, mEdtPricePerUnit;
        private String productId;

        public CustomDialogClass(Activity a, String productId) {
            super(a);
            this.c = a;
            this.productId = productId;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.edit_my_post_dialog);

            mEdtMinQty = (EditText) findViewById(R.id.edt_minm_qty);
            mEdtAvaQty = (EditText) findViewById(R.id.edt_ava_quantity);
            mEdtPricePerUnit = (EditText) findViewById(R.id.edt_price_per_unit);

            btnEdit = (Button) findViewById(R.id.btn_edit);
            btnEdit.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_edit:

                    String minQty = mEdtMinQty.getText().toString();
                    String avaQty = mEdtAvaQty.getText().toString();
                    String pricePerUnit = mEdtPricePerUnit.getText().toString();
                    if (minQty != null && !minQty.isEmpty()) {
                        if (avaQty != null && !avaQty.isEmpty()) {
                            if (pricePerUnit != null && !pricePerUnit.isEmpty()) {

                                EditProduct editProduct = new EditProduct();
                                editProduct.setMinQuantity(minQty);
                                editProduct.setQuantityAvailable(avaQty);
                                editProduct.setPricePerUnit(pricePerUnit);
                                editProduct.setProductId(productId);
                                editProduct.setUserId(siniiaPreferences.getUserId() + "");

                                if (SiniiaUtils.checkForNetworkConnectivity(mContext)) {
                                    new GetEditMyPost().execute(new Gson().toJson(editProduct).toString());
                                } else {
                                    Toast.makeText(mContext, getString(R.string.please_check_internet_connection), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(mContext, "Please enter " + getString(R.string.price_per_unit), Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(mContext, "Please enter " + getString(R.string.quantity_ava), Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(mContext, "Please enter " + getString(R.string.minm_oty), Toast.LENGTH_SHORT).show();
                    }

                    break;
                default:
                    break;
            }
            dismiss();
        }
    }

    public class GetEditMyPost extends AsyncTask<String, Void, String> {

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
            String result = new HttpHelper().updateProductDetails(strings[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (customDialogClass != null) {
                    customDialogClass.dismiss();
                }
                Log.d(TAG, "XXXXXXXXXXXXx result " + s);
                if (s != null) {

                    ApiResponse sellResponse = new Gson().fromJson(s, ApiResponse.class);
                    if (sellResponse != null) {
                        int status = sellResponse.getStatus();
                        Toast.makeText(mContext, sellResponse.getDescription(), Toast.LENGTH_SHORT).show();
                        if (status == 200) {
                            if (SiniiaUtils.checkForNetworkConnectivity(mContext)) {
                                new GetMyPostData().execute();
                            } else {
                                Toast.makeText(mContext, getString(R.string.please_check_internet_connection), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(mContext, sellResponse.getDescription(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void showRv() {
        if (mRvBuySell != null) {
            mRvBuySell.setVisibility(View.VISIBLE);
        }
        if (mTxtNoDataFound != null) {
            mTxtNoDataFound.setVisibility(View.GONE);
        }
    }

    public void hideRv() {
        if (mRvBuySell != null) {
            mRvBuySell.setVisibility(View.GONE);
        }
        if (mTxtNoDataFound != null) {
            mTxtNoDataFound.setVisibility(View.VISIBLE);
        }
    }


}


