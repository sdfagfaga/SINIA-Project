package com.sinnia.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.sinnia.R;
import com.sinnia.activites.CongratulationsActiviity;
import com.sinnia.activites.MainActivity;
import com.sinnia.activites.RegisterActivity;
import com.sinnia.activites.SelectTypeActivity;
import com.sinnia.adapter.AllProductsAdapter;
import com.sinnia.data.products.Products;
import com.sinnia.data.products.ProductsList;
import com.sinnia.data.products.ProductsResponse;
import com.sinnia.data.selecttype.ApiResponse;
import com.sinnia.fragments.orders.FragmentCartDetails;
import com.sinnia.listeners.ItemClickListener;
import com.sinnia.network.HttpHelper;
import com.sinnia.preferences.SiniiaPreferences;
import com.sinnia.utils.SiniiaConstants;
import com.sinnia.utils.SiniiaUtils;

import java.util.ArrayList;
import java.util.List;

public class FragmentProducts extends Fragment implements ItemClickListener, View.OnClickListener {


    private Context mContext;

    private RecyclerView mRVProducts;
    private static final String TAG = "FragmentProducts";
    private ItemClickListener mItemClickListener;
    private SiniiaPreferences siniiaPreferences;

    private TextView mTxtHerbs, mTxtGrains, mTxtFish, mTxtAll, mTxtVegetables, mTxtFruits;
    private AllProductsAdapter productsAdapter;
    private List<Products> arrayList;
    private List<Products> selectedList;
    private TextView mTxtNoProducts;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_products, container, false);
        mContext = getContext();
        siniiaPreferences = new SiniiaPreferences(mContext);
        mRVProducts = (RecyclerView) rootView.findViewById(R.id.rv_products);

        mTxtAll = (TextView) rootView.findViewById(R.id.txt_all);
        mTxtHerbs = (TextView) rootView.findViewById(R.id.txt_herbs);
        mTxtGrains = (TextView) rootView.findViewById(R.id.txt_grains);
        mTxtFish = (TextView) rootView.findViewById(R.id.txt_fish);
        mTxtVegetables = (TextView) rootView.findViewById(R.id.txt_veg);
        mTxtFruits = (TextView) rootView.findViewById(R.id.txt_fruits);


        mTxtNoProducts = (TextView) rootView.findViewById(R.id.txt_no_products);

        mTxtAll.setOnClickListener(this);
        mTxtHerbs.setOnClickListener(this);
        mTxtGrains.setOnClickListener(this);
        mTxtFish.setOnClickListener(this);
        mTxtVegetables.setOnClickListener(this);
        mTxtFruits.setOnClickListener(this);

        mItemClickListener = this;
        if (SiniiaUtils.checkForNetworkConnectivity(mContext)) {
            new GetProductsList().execute("");
        } else {
            Toast.makeText(mContext, getString(R.string.please_check_internet_connection), Toast.LENGTH_SHORT).show();
        }

        setCartNotification(rootView);

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
            case R.id.txt_all:

                mTxtFish.setBackground(null);
                mTxtHerbs.setBackground(null);
                mTxtGrains.setBackground(null);
                mTxtAll.setBackground(getActivity().getDrawable(R.drawable.text_filled_selected));
                mTxtVegetables.setBackground(null);
                mTxtFruits.setBackground(null);

                if (SiniiaUtils.checkForNetworkConnectivity(mContext)) {
                    new GetProductsList().execute("");
                } else {
                    Toast.makeText(mContext, getString(R.string.please_check_internet_connection), Toast.LENGTH_SHORT).show();
                }

                if (productsAdapter != null) {
                    productsAdapter.notifyDataSetChanged();
                }

                break;
            case R.id.txt_herbs:
                mTxtHerbs.setBackground(getActivity().getDrawable(R.drawable.text_filled_selected));
                mTxtGrains.setBackground(null);
                mTxtAll.setBackground(null);
                mTxtFish.setBackground(null);
                mTxtVegetables.setBackground(null);
                mTxtFruits.setBackground(null);


                if (SiniiaUtils.checkForNetworkConnectivity(mContext)) {
                    new GetProductsList().execute("Herbs");
                } else {
                    Toast.makeText(mContext, getString(R.string.please_check_internet_connection), Toast.LENGTH_SHORT).show();
                }

                if (productsAdapter != null) {
                    productsAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.txt_grains:
                mTxtHerbs.setBackground(null);
                mTxtGrains.setBackground(getActivity().getDrawable(R.drawable.text_filled_selected));
                mTxtAll.setBackground(null);
                mTxtFish.setBackground(null);
                mTxtVegetables.setBackground(null);
                mTxtFruits.setBackground(null);

                if (SiniiaUtils.checkForNetworkConnectivity(mContext)) {
                    new GetProductsList().execute("Grains");
                } else {
                    Toast.makeText(mContext, getString(R.string.please_check_internet_connection), Toast.LENGTH_SHORT).show();
                }

                if (productsAdapter != null) {
                    productsAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.txt_fish:
                mTxtFish.setBackground(getActivity().getDrawable(R.drawable.text_filled_selected));
                mTxtHerbs.setBackground(null);
                mTxtGrains.setBackground(null);
                mTxtAll.setBackground(null);
                mTxtVegetables.setBackground(null);
                mTxtFruits.setBackground(null);

                if (SiniiaUtils.checkForNetworkConnectivity(mContext)) {
                    new GetProductsList().execute("Fish");
                } else {
                    Toast.makeText(mContext, getString(R.string.please_check_internet_connection), Toast.LENGTH_SHORT).show();
                }

                if (productsAdapter != null) {
                    productsAdapter.notifyDataSetChanged();
                }

                break;
            case R.id.txt_veg:
                mTxtFish.setBackground(null);
                mTxtHerbs.setBackground(null);
                mTxtGrains.setBackground(null);
                mTxtAll.setBackground(null);
                mTxtVegetables.setBackground(getActivity().getDrawable(R.drawable.text_filled_selected));
                mTxtFruits.setBackground(null);
                if (SiniiaUtils.checkForNetworkConnectivity(mContext)) {
                    new GetProductsList().execute("Vegetables");
                } else {
                    Toast.makeText(mContext, getString(R.string.please_check_internet_connection), Toast.LENGTH_SHORT).show();
                }

                if (productsAdapter != null) {
                    productsAdapter.notifyDataSetChanged();
                }

                break;
            case R.id.txt_fruits:
                mTxtFish.setBackground(null);
                mTxtHerbs.setBackground(null);
                mTxtGrains.setBackground(null);
                mTxtAll.setBackground(null);
                mTxtVegetables.setBackground(null);
                mTxtFruits.setBackground(getActivity().getDrawable(R.drawable.text_filled_selected));
                if (SiniiaUtils.checkForNetworkConnectivity(mContext)) {
                    new GetProductsList().execute("Fruits");
                } else {
                    Toast.makeText(mContext, getString(R.string.please_check_internet_connection), Toast.LENGTH_SHORT).show();
                }

                if (productsAdapter != null) {
                    productsAdapter.notifyDataSetChanged();
                }

                break;
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

        final EditText mEdtSearch = (EditText) rootView.findViewById(R.id.edt_search);
       /* mEdtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (productsAdapter != null) {
                    productsAdapter.alertFilter(s.toString());
                    productsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/

        mEdtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String name = mEdtSearch.getText().toString();
                Log.d(TAG, "XXXXXXXXXXXXXX name " + name);
                if (name != null) {
                    if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_GO) {
                        if (productsAdapter != null) {
                            productsAdapter.alertFilter(name);
                            productsAdapter.notifyDataSetChanged();
                        }

                        if (SiniiaUtils.checkForNetworkConnectivity(mContext)) {
                            new AddProductSearchToDatabase().execute(siniiaPreferences.getUserId(), name);
                        }
                    }
                } else {
                    Toast.makeText(mContext, "Please enter search key word", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });


    }

    @Override
    public void clickOnRVItem(Products products) {
        Bundle bundle = new Bundle();
        bundle.putString("productId", products.getId() + "");
        ((MainActivity) mContext).launchFragment(new FragmentProductDetails(), getString(R.string.fragment_product_details), bundle);
    }


    public class GetProductsList extends AsyncTask<String, Void, String> {

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
            String result = new HttpHelper().getProductsList(strings[0], siniiaPreferences.getUserId());
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
                    ProductsResponse productsResponse = new Gson().fromJson(s, ProductsResponse.class);
                    if (productsResponse != null) {
                        Integer status = productsResponse.getStatus();
                        if (status == 200) {
                            ProductsList productsList = productsResponse.getProductsList();
                            if (productsList != null) {
                                arrayList = productsList.getProducts();
                                selectedList = new ArrayList<>();
                                selectedList.addAll(arrayList);
                                productsAdapter = new AllProductsAdapter(mContext, selectedList, mItemClickListener);
                                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(mContext, 2);
                                mRVProducts.setLayoutManager(mLayoutManager);
                                mRVProducts.setItemAnimator(new DefaultItemAnimator());
                                mRVProducts.setAdapter(productsAdapter);
                                productsAdapter.notifyDataSetChanged();
                                showRecycleView();
                            } else {
                                hideRecycleView();
                            }
                        } else {
                            hideRecycleView();
                        }
                    } else {
                        hideRecycleView();
                    }

                } else {
                    hideRecycleView();
                }
            } catch (Exception e) {
                hideRecycleView();
                e.printStackTrace();
            }

        }
    }

    public void showRecycleView() {
        if (mRVProducts != null) {
            mRVProducts.setVisibility(View.VISIBLE);
        }
        if (mTxtNoProducts != null) {
            mTxtNoProducts.setVisibility(View.GONE);
        }
    }

    public void hideRecycleView() {
        if (mRVProducts != null) {
            mRVProducts.setVisibility(View.GONE);
        }
        if (mTxtNoProducts != null) {
            mTxtNoProducts.setVisibility(View.VISIBLE);
        }
    }

    public class AddProductSearchToDatabase extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... strings) {
            String result = new HttpHelper().addProductSearchKey(strings[0], strings[1]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {

                Log.d(TAG, "XXXXXXXXXXXXx result " + s);
                if (s != null) {
                    ApiResponse selectTypeResponse = new Gson().fromJson(s, ApiResponse.class);
                    if (selectTypeResponse != null) {
                        int status = selectTypeResponse.getStatus();
                        if (status == 200) {

                        } else {
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
