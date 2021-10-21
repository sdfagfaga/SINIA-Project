package com.sinnia.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.sinnia.R;

public class FragmentWeb extends Fragment {

    private Context mContext;
    private WebView mWebView;

    public static final String HELP_DESK="help_desk";
    public static final String FAQ="faq";
    public static final String TERMS_CONDITIONS="terms_conditions";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_web, container, false);
        mContext=getActivity();
        mWebView = (WebView) rootView.findViewById(R.id.dis_webview);
        mWebView.setWebViewClient(new MyBrowser());
        ImageView leftArrow = (ImageView) rootView.findViewById(R.id.ic_left_arrow);
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null && getActivity().getSupportFragmentManager() != null)
                    getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        if(getArguments()!=null){
            String fileName =getArguments().getString("htmlName");
            String file ="file:///android_asset/static_content/"+fileName+".htm";
            openBrowser(file);
        }

        return rootView;
    }

    public void openBrowser(String file) {

        mWebView.getSettings().setLoadsImagesAutomatically(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.getSettings().setBuiltInZoomControls(false);
        mWebView.getSettings().setSupportZoom(false);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.loadUrl(file);
    }
    // load the urls or assets files
    private class MyBrowser extends WebViewClient {

        // ProgressDialog prDialog = ProgressDialog.show(mContext, null, "Loading, Please Wait...");

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("mailto:") || url.startsWith("tel:")) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(url));
                startActivity(intent);
            } else if (url.endsWith(".pdf")) {
                String googleDocs = "https://docs.google.com/viewer?url=";
                //  prDialog.show();
                view.loadUrl(googleDocs + url);
            } else {
                //  prDialog.show();
                view.loadUrl(url);
            }
            return true;
        }

        @Override
        public void onPageFinished(WebView view, final String url) {
            // prDialog.dismiss();
            super.onPageFinished(view, url);
        }

    }
}
