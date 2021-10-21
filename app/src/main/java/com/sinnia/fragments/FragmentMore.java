package com.sinnia.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.sinnia.R;
import com.sinnia.activites.LoginActivity;
import com.sinnia.activites.MainActivity;
import com.sinnia.activites.SelectTypeActivity;
import com.sinnia.fragments.orders.FragmentCartDetails;
import com.sinnia.preferences.SiniiaPreferences;
import com.sinnia.utils.SiniiaUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class FragmentMore extends Fragment implements View.OnClickListener {

    private SiniiaPreferences siniiaPreferences;
    private Context mContext;
    private TextView mTxtTermsConditions, mTxtFAQ, mTxtHelpDesk, mTxtGmail,
            mTxtChatOnWeb, mTxtNewsLetter, mTxtDonate, mTxtCheckUpdate;
    public static final String DEVELOPER_MAIL = "aliwigs@gmail.com";

    /*siniia@123*/

    String currentVersion;

    private static final String TAG = "FragmentMore";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_more, container, false);
        mContext = getContext();
        siniiaPreferences = new SiniiaPreferences(mContext);
        TextView mTxtLogout = (TextView) rootView.findViewById(R.id.txt_logout);
        mTxtLogout.setOnClickListener(this);
        setCartNotification(rootView);

        mTxtTermsConditions = (TextView) rootView.findViewById(R.id.txt_terms_conditions);
        mTxtFAQ = (TextView) rootView.findViewById(R.id.txt_faq);
        mTxtHelpDesk = (TextView) rootView.findViewById(R.id.txt_help);
        mTxtGmail = (TextView) rootView.findViewById(R.id.txt_email);
        mTxtChatOnWeb = (TextView) rootView.findViewById(R.id.txt_chat_on_web);
        mTxtNewsLetter = (TextView) rootView.findViewById(R.id.txt_news_letter);
        mTxtDonate = (TextView) rootView.findViewById(R.id.txt_donate);
        mTxtCheckUpdate = (TextView) rootView.findViewById(R.id.txt_update);

        TextView mTxtStall14 = (TextView) rootView.findViewById(R.id.txt_stall);


        mTxtTermsConditions.setOnClickListener(this);
        mTxtFAQ.setOnClickListener(this);
        mTxtHelpDesk.setOnClickListener(this);
        mTxtGmail.setOnClickListener(this);
        mTxtChatOnWeb.setOnClickListener(this);
        mTxtNewsLetter.setOnClickListener(this);
        mTxtDonate.setOnClickListener(this);
        mTxtStall14.setOnClickListener(this);
        mTxtCheckUpdate.setOnClickListener(this);


        return rootView;
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();

        switch (v.getId()) {
            case R.id.txt_terms_conditions:
                bundle.putString("htmlName", FragmentWeb.TERMS_CONDITIONS);
                ((MainActivity) mContext).launchFragment(new FragmentWeb(), getString(R.string.fragment_order_summary), bundle);
                break;
            case R.id.txt_faq:
                bundle.putString("htmlName", FragmentWeb.FAQ);
                ((MainActivity) mContext).launchFragment(new FragmentWeb(), getString(R.string.fragment_order_summary), bundle);
                break;
            case R.id.txt_help:
                bundle.putString("htmlName", FragmentWeb.HELP_DESK);
                ((MainActivity) mContext).launchFragment(new FragmentWeb(), getString(R.string.fragment_order_summary), bundle);
                break;
            case R.id.txt_logout:
                showAlertDialog(mContext);
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
            case R.id.txt_email:
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + DEVELOPER_MAIL));
                  /*  intent.putExtra(Intent.EXTRA_SUBJECT, "your_subject");
                    intent.putExtra(Intent.EXTRA_TEXT, "your_text");*/
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    //TODO smth
                }


                break;
            case R.id.txt_chat_on_web:
                break;
            case R.id.txt_news_letter:
                ((MainActivity) mContext).launchFragment(new FragmentNewsLetter(), getString(R.string.fragment_news_letter));
                break;
            case R.id.txt_donate:
                ((MainActivity) mContext).launchFragment(new FragmentDonate(), getString(R.string.fragment_donate));
                break;
            case R.id.txt_stall:
                ((MainActivity) mContext).launchFragment(new FragmentStall14(), getString(R.string.fragment_stall_14));
                break;
            case R.id.txt_update:
                try {
                    currentVersion = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                if (SiniiaUtils.checkForNetworkConnectivity(mContext)) {
                    new GetVersionCode().execute();
                } else {
                    Toast.makeText(mContext, getString(R.string.please_check_internet_connection), Toast.LENGTH_SHORT).show();
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

        TextView mTxtStall14 = (TextView) rootView.findViewById(R.id.txt_stall_14);
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

    public void showAlertDialog(final Context mContext) {
        Dialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("");
        builder.setMessage("Do you want logout ?");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                siniiaPreferences.setRegisterStatus(0);
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
                getActivity().finishAffinity();
            }
        });
        dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }

    class GetVersionCode extends AsyncTask<Void, String, String> {


        private ProgressDialog dialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Please wait.....");
            dialog.show();

        }

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
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                Log.d(TAG, " currentVersion   " + currentVersion + " online version" + onlineVersion);
                if (onlineVersion != null && !onlineVersion.isEmpty() &&
                        currentVersion != null && !currentVersion.isEmpty()) {
                    if (!onlineVersion.equalsIgnoreCase(currentVersion)) {
                        showUpdateAlert();
                    } else {
                        Toast.makeText(mContext, "No updates available ", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, "No updates available ", Toast.LENGTH_SHORT).show();
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
