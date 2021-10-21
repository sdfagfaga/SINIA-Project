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
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.sinnia.R;
import com.sinnia.activites.MainActivity;
import com.sinnia.data.buyproductdetails.BuyProduct;
import com.sinnia.data.buyproductdetails.BuyProductResponse;
import com.sinnia.data.cart.UserAddress;
import com.sinnia.fragments.orders.FragmentCartDetails;
import com.sinnia.network.HttpHelper;
import com.sinnia.preferences.SiniiaPreferences;
import com.sinnia.utils.SiniiaUtils;

import java.util.ArrayList;
import java.util.List;

public class FragmentBuySellProductDeatils extends Fragment implements View.OnClickListener {

    private Context mContext;
    private static final String TAG = "FragmentProductDetails";
    private SiniiaPreferences siniiaPreferences;
    private TextView mTxtProductName, mTxtProductType, mTxtProductCost, mTxtQuantityTypeAndNumber,
            mTxtGrade, mTxtHighlight, mTxtPostedBy, mTxtAddress, mTxtPurQunatity, mTxtPurPrice, mTxtStatus;

    private String productId;

    ViewPager pager;
    TabLayout tabLayout;
    private Button mBtnStatus;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.buy_sell_product_details_layout, container, false);
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
        mTxtGrade = (TextView) rootView.findViewById(R.id.txt_grade);
        mTxtHighlight = (TextView) rootView.findViewById(R.id.txt_highlight);

        mTxtAddress = (TextView) rootView.findViewById(R.id.txt_address);
        mTxtPurQunatity = (TextView) rootView.findViewById(R.id.txt_pur_qunatity);
        mTxtPurPrice = (TextView) rootView.findViewById(R.id.txt_pur_price);

        mTxtPostedBy = (TextView) rootView.findViewById(R.id.txt_posted_by);
        mTxtAddress = (TextView) rootView.findViewById(R.id.txt_address);

        pager = (ViewPager) rootView.findViewById(R.id.pager);
        tabLayout = (TabLayout) rootView.findViewById(R.id.tabDots);
        mBtnStatus = (Button) rootView.findViewById(R.id.btn_status);
        mTxtStatus = (TextView) rootView.findViewById(R.id.txt_status);

        if (getArguments() != null) {
            productId = getArguments().getString("productId");
            String orderId = getArguments().getString("orderId");
            if (SiniiaUtils.checkForNetworkConnectivity(mContext)) {
                new GetProductsDetails().execute(siniiaPreferences.getUserId(), productId, orderId);
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
            String result = new HttpHelper().getBuyProductDetails(strings[0], strings[1], strings[2]);
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
                    BuyProductResponse productDetails = new Gson().fromJson(s, BuyProductResponse.class);
                    if (productDetails != null && productDetails.getStatus() == 200) {
                        BuyProduct products = productDetails.getData();
                        if (products != null) {
                            mTxtProductName.setText(products.getProductName());
                            mTxtProductType.setText("(" + products.getProductType() + ")");
                            mTxtProductCost.setText("R " + products.getPricePerUnit());
                            mTxtQuantityTypeAndNumber.setText("(" + products.getQuantityType() + " - " + products.getQuantityPerUnit() + ")");
                            mTxtGrade.setText(products.getProductGrade());
                            mTxtPostedBy.setText("Posted By : " + products.getProductOwenerName());
                            //  mTxtHighlight.setText(products.getHighlight());
                            mTxtPurPrice.setText("$ " + products.getQuantityPrice());
                            mTxtPurQunatity.setText(products.getQuantity() + " " + products.getQuantityType());
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
                            UserAddress productAddress = products.getAddress();
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
                                mTxtAddress.setText(address);
                            }

                            mBtnStatus.setText(products.getDeliveryStatus());
                            mTxtStatus.setText(products.getDeliveryStatus());
                            PagerAdapter adapter = new ImageAdapter(mContext, imagesList);
                            pager.setAdapter(adapter);
                            tabLayout.setupWithViewPager(pager, true);


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
            case R.id.ic_cart_layout:
                if (siniiaPreferences.getCartCount() != 0) {
                    ((MainActivity) mContext).launchFragment(new FragmentCartDetails(), getString(R.string.fragment_product_details));
                } else {
                    Toast.makeText(mContext, getString(R.string.add_products_cart), Toast.LENGTH_SHORT).show();
                }
        }
    }


}
