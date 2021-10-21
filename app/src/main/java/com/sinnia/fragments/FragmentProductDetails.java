package com.sinnia.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.sinnia.R;
import com.sinnia.activites.MainActivity;
import com.sinnia.adapter.AllProductsAdapter;
import com.sinnia.adapter.SectionRecyclerViewAdapter;
import com.sinnia.data.productdetails.AddToCart;
import com.sinnia.data.productdetails.ProductDetails;
import com.sinnia.data.products.Products;
import com.sinnia.data.products.ProductsList;
import com.sinnia.data.products.ProductsResponse;
import com.sinnia.data.selecttype.ApiResponse;
import com.sinnia.fragments.orders.FragmentCartDetails;
import com.sinnia.fragments.orders.FragmentOrderSummery;
import com.sinnia.network.HttpHelper;
import com.sinnia.preferences.SiniiaPreferences;
import com.sinnia.utils.SectionModel;
import com.sinnia.utils.SiniiaUtils;

import java.util.ArrayList;
import java.util.List;

public class FragmentProductDetails extends Fragment implements View.OnClickListener {

    private Context mContext;
    private static final String TAG = "FragmentProductDetails";
    private SiniiaPreferences siniiaPreferences;
    private TextView mTxtProductName, mTxtProductType, mTxtProductCost, mTxtQuantityTypeAndNumber,
            mTxtMinimumQuantity, mTxtGrade, mTxtHighlight, mTxtPostedBy;

    private Button mBtnAddToCart, mBtnBuyNow;
    private String productId;

    ViewPager pager;
    TabLayout tabLayout;
    ArrayList<Products> productsList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_product_details, container, false);
        mContext = getActivity();


        ImageView leftArrow = (ImageView) rootView.findViewById(R.id.ic_left_arrow);
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });

        siniiaPreferences = new SiniiaPreferences(mContext);

        mTxtProductName = (TextView) rootView.findViewById(R.id.txt_product_name);
        mTxtProductType = (TextView) rootView.findViewById(R.id.txt_product_type);
        mTxtProductCost = (TextView) rootView.findViewById(R.id.txt_product_cost);
        mTxtQuantityTypeAndNumber = (TextView) rootView.findViewById(R.id.txt_quantity_type_number);
        mTxtMinimumQuantity = (TextView) rootView.findViewById(R.id.txt_minmum_oty);
        mTxtGrade = (TextView) rootView.findViewById(R.id.txt_grade);
        mTxtHighlight = (TextView) rootView.findViewById(R.id.txt_highlight);
        mTxtPostedBy = (TextView) rootView.findViewById(R.id.txt_posted_by);

        mBtnAddToCart = (Button) rootView.findViewById(R.id.btn_add_cart);
        mBtnBuyNow = (Button) rootView.findViewById(R.id.btn_buy_now);

        mBtnAddToCart.setOnClickListener(this);
        mBtnBuyNow.setOnClickListener(this);

        pager = (ViewPager) rootView.findViewById(R.id.pager);
        tabLayout = (TabLayout) rootView.findViewById(R.id.tabDots);


        if (getArguments() != null) {
            productId = getArguments().getString("productId");
            if (SiniiaUtils.checkForNetworkConnectivity(mContext)) {
                new GetProductsDetails().execute(siniiaPreferences.getUserId(), productId);
            } else {
                Toast.makeText(mContext, getString(R.string.please_check_internet_connection), Toast.LENGTH_SHORT).show();
            }
        }

        setCartNotification(rootView);
        return rootView;
    }

    public void setCartNotification(View rootView) {
        View cartView = (View) rootView.findViewById(R.id.ic_cart_layout);
        TextView mTxtCartCount = (TextView) cartView.findViewById(R.id.txt_cart_count);
        if (siniiaPreferences.getCartCount() != 0) {
            mTxtCartCount.setText(siniiaPreferences.getCartCount() + "");
            mTxtCartCount.setVisibility(View.VISIBLE);
        } else {
            mTxtCartCount.setVisibility(View.GONE);
        }
        cartView.setOnClickListener(this);
    }

    public class GetProductsDetails extends AsyncTask<String, Void, String> {

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
            String result = new HttpHelper().GetProductsById(strings[0], strings[1]);
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
                    ProductDetails productDetails = new Gson().fromJson(s, ProductDetails.class);
                    if (productDetails != null) {
                        productsList = productDetails.getProducts();
                        if (productsList != null && !productsList.isEmpty()) {
                            Products products = productsList.get(0);
                            if (products != null) {
                                mTxtProductName.setText(products.getProductName());
                                mTxtProductType.setText("(" + products.getProductType() + ")");
                                mTxtProductCost.setText("R " + products.getPricePerUnit());
                                mTxtQuantityTypeAndNumber.setText("(" + products.getQuantityType() + " - " + products.getQuantityPerUnit() + ")");
                                mTxtMinimumQuantity.setText("Minm. Qnty : " + products.getMinQuantity());
                                mTxtGrade.setText("Grade : " + products.getProductGrade());
                                mTxtPostedBy.setText("Posted By : " + products.getProductOwnerName());
                                mTxtHighlight.setText(products.getHighlight());

                                List<String> imagesList = new ArrayList<>();
                                String url = products.getThumbImageURL();
                                if (url != null) {
                                    if (url.contains(",")) {
                                        String[] items = url.split(",");
                                        for (String item : items) {
                                            imagesList.add(item);
                                        }
                                    } else {
                                        imagesList.add(url);
                                    }
                                }
                                PagerAdapter adapter = new ImageAdapter(mContext, imagesList);
                                pager.setAdapter(adapter);
                                tabLayout.setupWithViewPager(pager, true);


                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    public class ImageAdapter extends PagerAdapter {
        Context context;
        private List<String> images;

        LayoutInflater mLayoutInflater;

        ImageAdapter(Context context, List<String> images) {
            this.context = context;
            this.images = images;
            mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            if (!images.isEmpty()) {
                return images.size();
            }
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
            SiniiaUtils.loadPicassoImageWithOutCircle(mContext, images.get(position), imageView);
            container.addView(itemView);
            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_cart:
                if (SiniiaUtils.checkForNetworkConnectivity(mContext)) {

                    AddToCart addToCart = new AddToCart();
                    addToCart.setProductId(productId);
                    addToCart.setUserId(siniiaPreferences.getUserId());

                  /*  addToCart.setAvailableAddressId();
                    addToCart.setDeliveryAddressID();
                    addToCart.setShipmentId();
                    addToCart.setQuantity();
                    addToCart.setQuantityTypeId();
                    addToCart.setQuantityPrice();
                    addToCart.setDeliveryStatus();*/

                    new AddToCartTask().execute(new Gson().toJson(addToCart).toString());
                } else {
                    Toast.makeText(mContext, getString(R.string.please_check_internet_connection), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_buy_now:

                if (productsList != null) {
                    ArrayList<Products> latestArrayList = new ArrayList<>();
                    for (Products products : productsList) {
                        Products products1 = new Products();
                        products1.setSelectedQuantity(products.getMinQuantity());
                        products1.setSelectedTotal(products.getMinQuantity() * products.getPricePerUnit());
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
                    if (latestArrayList != null && latestArrayList.size() != 0) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("summary", latestArrayList);
                        bundle.putSerializable("address", null);
                        ((MainActivity) mContext).launchFragment(new FragmentOrderSummery(), getString(R.string.fragment_order_summary), bundle);
                    } else {
                        Toast.makeText(mContext, getString(R.string.please_check_internet_connection), Toast.LENGTH_SHORT).show();
                    }

                }


                break;
            case R.id.ic_cart_layout:
                if (siniiaPreferences.getCartCount() != 0) {
                    ((MainActivity) mContext).launchFragment(new FragmentCartDetails(), getString(R.string.fragment_product_details));
                } else {
                    Toast.makeText(mContext, getString(R.string.add_products_cart), Toast.LENGTH_SHORT).show();
                }
        }
    }

    public class AddToCartTask extends AsyncTask<String, Void, String> {

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
            String result = new HttpHelper().addToCart(strings[0]);
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
                    ApiResponse apiResponse = new Gson().fromJson(s, ApiResponse.class);
                    if (apiResponse != null) {
                        if (apiResponse.getStatus() == 200) {
                            siniiaPreferences.setCartCount(siniiaPreferences.getCartCount() + 1);
                        }
                        Toast.makeText(mContext, apiResponse.getDescription(), Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


}
