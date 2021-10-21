package com.sinnia.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.sinnia.R;
import com.sinnia.activites.CongratulationsActiviity;
import com.sinnia.activites.RegisterActivity;
import com.sinnia.data.NewsLetter;
import com.sinnia.data.register.RegisterRequest;
import com.sinnia.data.selecttype.ApiResponse;
import com.sinnia.network.HttpHelper;
import com.sinnia.preferences.SiniiaPreferences;
import com.sinnia.utils.SiniiaConstants;
import com.sinnia.utils.SiniiaUtils;

public class FragmentNewsLetter extends Fragment implements View.OnClickListener {

    private Context mContext;
    private EditText mEdtName, mEdtEmail;
    private Button mBtnSubmit;
    private RelativeLayout mRelativeLayoutNewsLetter;
    private LinearLayout mLinearEmailLayout;
    private SiniiaPreferences siniiaPreferences;
    private static final String TAG = "FragmentNewsLetter";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news_letter, container, false);
        mContext = getContext();

        mEdtName = (EditText) rootView.findViewById(R.id.edt_full_name);
        mEdtEmail = (EditText) rootView.findViewById(R.id.email);
        siniiaPreferences = new SiniiaPreferences(mContext);


        ImageView leftArrow = (ImageView) rootView.findViewById(R.id.ic_left_arrow);
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null && getActivity().getSupportFragmentManager() != null)
                    getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        mRelativeLayoutNewsLetter = (RelativeLayout) rootView.findViewById(R.id.rel_layout_news_description);
        mLinearEmailLayout = (LinearLayout) rootView.findViewById(R.id.linear_email_layout);


        mBtnSubmit = (Button) rootView.findViewById(R.id.btn_submit);
        mBtnSubmit.setOnClickListener(this);

        if (siniiaPreferences.getIsNewsLetterSubmitted()) {
            mLinearEmailLayout.setVisibility(View.GONE);
            mRelativeLayoutNewsLetter.setVisibility(View.VISIBLE);
        } else {
            mLinearEmailLayout.setVisibility(View.VISIBLE);
            mRelativeLayoutNewsLetter.setVisibility(View.GONE);
        }

        return rootView;

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:

                if (SiniiaUtils.checkForNetworkConnectivity(mContext)) {
                    String name = mEdtName.getText().toString();
                    String email = mEdtEmail.getText().toString();
                    if (name != null && !name.isEmpty()) {
                        if (SiniiaUtils.isValidEmail(email)) {
                            NewsLetter newsLetter = new NewsLetter();
                            newsLetter.setName(name);
                            newsLetter.setEmail(email);
                            newsLetter.setUserId(siniiaPreferences.getUserId());
                            new SendNewsLetter().execute(new Gson().toJson(newsLetter).toString());

                        } else {
                            Toast.makeText(mContext, "Please enter valid email", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(mContext, "Please enter name", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(mContext, getString(R.string.please_check_internet_connection), Toast.LENGTH_SHORT).show();
                }


                break;
        }
    }


    public class SendNewsLetter extends AsyncTask<String, Void, String> {

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
            String result = new HttpHelper().addNewsLetter(strings[0]);
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
                    ApiResponse selectTypeResponse = new Gson().fromJson(s, ApiResponse.class);
                    if (selectTypeResponse != null) {
                        int status = selectTypeResponse.getStatus();
                        if (status == 200) {
                            siniiaPreferences.isNewsLetterSubmitted(true);
                            mLinearEmailLayout.setVisibility(View.GONE);
                            mRelativeLayoutNewsLetter.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(mContext, selectTypeResponse.getDescription(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


}
