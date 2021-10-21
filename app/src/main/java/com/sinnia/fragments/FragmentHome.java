package com.sinnia.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.sinnia.BuildConfig;
import com.sinnia.R;
import com.sinnia.activites.MainActivity;
import com.sinnia.activites.SelectTypeActivity;
import com.sinnia.adapter.SectionRecyclerViewAdapter;
import com.sinnia.data.banner.Banner;
import com.sinnia.data.banner.BannerList;
import com.sinnia.data.count.CountResponse;
import com.sinnia.data.login.LoginRequest;
import com.sinnia.data.products.ProductsList;
import com.sinnia.data.products.ProductsResponse;
import com.sinnia.fragments.orders.FragmentCartDetails;
import com.sinnia.listeners.ItemClickListener;
import com.sinnia.listeners.SeeAllLisentenrs;
import com.sinnia.network.HttpHelper;
import com.sinnia.data.products.Products;
import com.sinnia.preferences.SiniiaPreferences;
import com.sinnia.utils.SectionModel;
import com.sinnia.utils.SiniiaConstants;
import com.sinnia.utils.SiniiaUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class FragmentHome extends Fragment implements ItemClickListener, View.OnClickListener, SeeAllLisentenrs {


    private Context mContext;
    private RecyclerView mRV;
    private static final String TAG = "FragmentHome";
    private ItemClickListener itemClickListener;
    private SiniiaPreferences siniiaPreferences;


    private TextView mTxtNotificationCount, mTxtCartCount;
    private SeeAllLisentenrs seeAllLisentenrs;
    ViewPager pager;
    TabLayout tabLayout;
    View rootView;
    private EditText mEdtSearch;

    List<Products> arrayList;
    SectionRecyclerViewAdapter adapter;

    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000; // time in milliseconds between successive task executions.

    String currentVersion;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        mContext = getContext();
        siniiaPreferences = new SiniiaPreferences(mContext);
        mRV = (RecyclerView) rootView.findViewById(R.id.rv_recycler);
        itemClickListener = this;
        seeAllLisentenrs = this;
        if (SiniiaUtils.checkForNetworkConnectivity(mContext)) {
            new GetProductsList().execute();
        } else {
            Toast.makeText(mContext, getString(R.string.please_check_internet_connection), Toast.LENGTH_SHORT).show();
        }

        pager = (ViewPager) rootView.findViewById(R.id.pager);
        tabLayout = (TabLayout) rootView.findViewById(R.id.tabDots);

        setHasOptionsMenu(true);
        if (SiniiaUtils.checkForNetworkConnectivity(mContext)) {
            new GetBannerImages().execute();
            new GetCountTask().execute();
        } else {
            Toast.makeText(mContext, getString(R.string.please_check_internet_connection), Toast.LENGTH_SHORT).show();
        }
        setCartNotification(rootView);


        Log.d(TAG, " XXXXXXXXXXx FragmentHome " + siniiaPreferences.getCountryCode());

        /*After Selecting buyer and we should to FragmentBuySell*/
        if (siniiaPreferences.getUserType() == SiniiaConstants.BUYER) {
            ((MainActivity) mContext).launchFragment(new FragmentBuySell(), getResources().getString(R.string.buy_sell_fragment));
        }

        if (SiniiaUtils.checkForNetworkConnectivity(mContext)) {
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setVersionCode(BuildConfig.VERSION_CODE);
            loginRequest.setVersionName(BuildConfig.VERSION_NAME);
            loginRequest.setDevice("Android");
            loginRequest.setUserId(siniiaPreferences.getUserId());
            new SendVersionCode().execute(new Gson().toJson(loginRequest).toString());
        }


        try {
            currentVersion = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (SiniiaUtils.checkForNetworkConnectivity(mContext)) {
            new GetVersionCode().execute();
        }

        return rootView;

    }

    public void setCartNotification(View rootView) {
        View cartView = (View) rootView.findViewById(R.id.ic_cart_layout);
        View notificationView = (View) rootView.findViewById(R.id.ic_notification_layout);
        mTxtNotificationCount = (TextView) notificationView.findViewById(R.id.txt_notification_count);
        mTxtCartCount = (TextView) cartView.findViewById(R.id.txt_cart_count);
        ImageView mImgLogo = (ImageView) rootView.findViewById(R.id.ic_logo);

        mImgLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SelectTypeActivity.class);
                startActivity(intent);
            }
        });

        TextView mTxtStall14 = (TextView) rootView.findViewById(R.id.txt_stall_14);
        mTxtStall14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SelectTypeActivity.class);
                startActivity(intent);
            }
        });


        EditText mEdtSearch = (EditText) rootView.findViewById(R.id.edt_search);
        mEdtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "XXXXXXXXXXXXXXXXX Edit Search " + s);
                if (adapter != null) {
                    adapter.alertFilter(s.toString());
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

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


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void clickOnRVItem(Products products) {
        Bundle bundle = new Bundle();
        bundle.putString("productId", products.getId() + "");
        ((MainActivity) mContext).launchFragment(new FragmentProductDetails(), getString(R.string.fragment_product_details), bundle);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ic_notification_layout:
                break;
            case R.id.ic_cart_layout:
                ((MainActivity) mContext).launchFragment(new FragmentCartDetails(), getString(R.string.fragment_product_details));
                break;
        }
    }

    @Override
    public void seeAll() {
        ((MainActivity) mContext).launchFragment(new FragmentProducts(), getString(R.string.products_fragment));
        ((MainActivity) mContext).setBottomNavigationItemClicked(2);

    }

    public void setCallCartMethodes() {
        if (SiniiaUtils.checkForNetworkConnectivity(mContext)) {
            new GetBannerImages().execute();
            new GetCountTask().execute();
        } else {
            Toast.makeText(mContext, getString(R.string.please_check_internet_connection), Toast.LENGTH_SHORT).show();
        }
        setCartNotification(rootView);
    }

    public class GetCountTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String result = new HttpHelper().getCount(siniiaPreferences.getUserId());
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                if (s != null) {
                    CountResponse countResponse = new Gson().fromJson(s, CountResponse.class);
                    if (countResponse != null && countResponse.getStatus() == 200) {
                        if (countResponse.getCounts() != null) {
                            siniiaPreferences.setCartCount(Integer.parseInt(countResponse.getCounts().getBasketOrdersCount()));
                            if (siniiaPreferences.getCartCount() != 0) {
                                mTxtCartCount.setText(siniiaPreferences.getCartCount() + "");
                                mTxtCartCount.setVisibility(View.VISIBLE);
                            } else {
                                mTxtCartCount.setVisibility(View.GONE);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
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
            String result = new HttpHelper().getProductsList(null, siniiaPreferences.getUserId());
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
                    ProductsResponse productsResponse = new Gson().fromJson(s, ProductsResponse.class);
                    if (productsResponse != null) {
                        Integer status = productsResponse.getStatus();
                        if (status == 200) {
                            ProductsList productsList = productsResponse.getProductsList();
                            if (productsList != null) {
                                arrayList = productsList.getProducts();

                                ArrayList<SectionModel> sectionModelArrayList = new ArrayList<>();
                                SectionModel sectionModel = new SectionModel("Production Near By", arrayList);
                                sectionModelArrayList.add(sectionModel);

                                SectionModel sectionModel2 = new SectionModel("My Products", arrayList);
                                sectionModelArrayList.add(sectionModel2);

                                SectionModel sectionModel3 = new SectionModel("All Products", arrayList);
                                sectionModelArrayList.add(sectionModel3);

                                adapter = new SectionRecyclerViewAdapter(mContext, sectionModelArrayList,
                                        itemClickListener, seeAllLisentenrs);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                                mRV.setLayoutManager(mLayoutManager);
                                mRV.setItemAnimator(new DefaultItemAnimator());
                                mRV.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
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
        //private int[] images = {R.mipmap.ic_sample, R.mipmap.ic_sample, R.mipmap.ic_sample, R.mipmap.ic_sample};

        private List<Banner> bannerList;
        LayoutInflater mLayoutInflater;

        ImageAdapter(Context context, List<Banner> bannerList) {
            this.context = context;
            this.bannerList = bannerList;
            mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            if (bannerList != null) {
                return bannerList.size();
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

            final Banner banner = bannerList.get(position);

            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
            //   imageView.setImageDrawable(mContext.getDrawable(images[position]));
            SiniiaUtils.loadPicassoImageWithOutCircle(mContext, banner.getURL(), imageView);
            container.addView(itemView);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SiniiaUtils.checkForNetworkConnectivity(mContext)) {
                        Bundle bundle = new Bundle();
                        bundle.putString("productId", banner.getProductId() + "");
                        ((MainActivity) mContext).launchFragment(new FragmentProductDetails(), getString(R.string.fragment_product_details), bundle);
                    } else {
                        Toast.makeText(mContext, getString(R.string.please_check_internet_connection), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }

    public class GetBannerImages extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String result = new HttpHelper().getAllBannerImages();
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "XXXXXXXXXXXXXXx result " + s);
            try {
                if (s != null) {
                    final BannerList bannerList = new Gson().fromJson(s, BannerList.class);
                    if (bannerList != null && bannerList.getStatus() == 200) {
                        if (bannerList.getBanners() != null) {
                            PagerAdapter adapter = new ImageAdapter(mContext, bannerList.getBanners());
                            pager.setAdapter(adapter);
                            tabLayout.setupWithViewPager(pager, true);

                            /*After setting the adapter use the timer */
                            final Handler handler = new Handler();
                            final Runnable Update = new Runnable() {
                                public void run() {
                                    if (currentPage == bannerList.getBanners().size() - 1) {
                                        currentPage = 0;
                                    }
                                    pager.setCurrentItem(currentPage++, true);
                                }
                            };

                            timer = new Timer(); // This will create a new Thread
                            timer.schedule(new TimerTask() { // task to be scheduled
                                @Override
                                public void run() {
                                    handler.post(Update);
                                }
                            }, DELAY_MS, PERIOD_MS);

                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class SendVersionCode extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {

            String result = new HttpHelper().sendVersion(strings[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "Verison  " + s);
        }
    }

    class GetVersionCode extends AsyncTask<Void, String, String> {

        @Override

        protected String doInBackground(Void... voids) {

            String newVersion = null;

            try {
                Document document = Jsoup.connect("https://play.google.com/store/apps/details?id=" + mContext.getPackageName() + "&hl=en")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get();
                if (document != null) {
                    Elements element = document.getElementsContainingOwnText("Current Version");
                    for (Element ele : element) {
                        if (ele.siblingElements() != null) {
                            Elements sibElemets = ele.siblingElements();
                            for (Element sibElemet : sibElemets) {
                                newVersion = sibElemet.text();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return newVersion;
        }


        @Override

        protected void onPostExecute(String onlineVersion) {
            super.onPostExecute(onlineVersion);
            try {
                Log.d(TAG, " currentVersion   " + currentVersion + " online version" + onlineVersion);
                if (onlineVersion != null && !onlineVersion.isEmpty() &&
                        currentVersion != null && !currentVersion.isEmpty()) {
                    if (!onlineVersion.equalsIgnoreCase(currentVersion)) {
                        showUpdateAlert();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void showUpdateAlert() {
        try {
            new AlertDialog.Builder(mContext)
                    .setIcon(R.drawable.ic_app_icon)
                    .setTitle("Update available")
                    .setMessage("An update for Live App is available on Play Store.")
                    .setNegativeButton("Update now", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            /* User clicked OK so do some stuff */
                            Intent intent = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("https://play.google.com/store/apps/details?id=" + mContext.getPackageName()));
                            startActivity(intent);
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton("Later", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            /* User clicked Cancel */
                            dialog.dismiss();
                        }
                    })
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
