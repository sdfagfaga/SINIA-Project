package com.sinnia.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.gson.Gson;
import com.sinnia.R;
import com.sinnia.data.AddPayPalId;
import com.sinnia.data.AddShipper;
import com.sinnia.data.selecttype.ApiResponse;
import com.sinnia.fragments.orders.FragmentShipperDialog;
import com.sinnia.listeners.TypeListener;
import com.sinnia.network.HttpHelper;
import com.sinnia.preferences.SiniiaPreferences;
import com.sinnia.utils.SiniiaUtils;

public class FragmentPaypalIdDialog extends DialogFragment implements View.OnClickListener {

    private static final String TAG = "FragmentPaypalIdDialog";
    private Button mBtnAdd, mBtnCancel;
    private EditText mEdtPayPalId, mEdtReEnterPayPalId;
    private Context mContext;
    private SiniiaPreferences siniiaPreferences;

    private TypeListener typeListener;
    String payPalId;

    public void setListener(TypeListener typeListener) {
        this.typeListener = typeListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_paypal_id_layout, container, false);

        mContext = getActivity();

        siniiaPreferences = new SiniiaPreferences(mContext);

        mBtnAdd = (Button) rootView.findViewById(R.id.btn_add);
        mBtnCancel = (Button) rootView.findViewById(R.id.btn_cancel);

        mEdtPayPalId = (EditText) rootView.findViewById(R.id.edt_enter_paypal_id);
        mEdtReEnterPayPalId = (EditText) rootView.findViewById(R.id.edt_reenter_paypal_id);

        mBtnAdd.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);

        return rootView;

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_add:


                if (SiniiaUtils.checkForNetworkConnectivity(mContext)) {

                    String payPalId = mEdtPayPalId.getText().toString();
                    String enterPayPalId = mEdtReEnterPayPalId.getText().toString();
                    if (payPalId != null && !payPalId.isEmpty()) {
                        if (enterPayPalId != null && !enterPayPalId.isEmpty()) {
                            if (payPalId.equals(enterPayPalId)) {
                                this.payPalId=payPalId;
                                AddPayPalId addPayPalId = new AddPayPalId();
                                addPayPalId.setUserId(siniiaPreferences.getUserId());
                                addPayPalId.setPayPalAccountId(payPalId);
                                new SavePayPalId().execute(new Gson().toJson(addPayPalId).toString());
                            } else {
                                Toast.makeText(mContext, "Both should match", Toast.LENGTH_SHORT).show();
                            }
                        } else {

                            Toast.makeText(mContext, "Please Re-enter PayPal account Id", Toast.LENGTH_SHORT).show();


                        }
                    } else {
                        Toast.makeText(mContext, "Please enter PayPal account Id", Toast.LENGTH_SHORT).show();

                    }


                } else {
                    Toast.makeText(mContext, getString(R.string.please_check_internet_connection), Toast.LENGTH_SHORT).show();
                }


                break;
            case R.id.btn_cancel:
                dismiss();
                break;
        }
    }

    public class SavePayPalId extends AsyncTask<String, Void, String> {

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
            String result = new HttpHelper().addPaypalId(strings[0]);
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
                            if (typeListener != null) {
                                typeListener.clickOnListener();
                            }
                            siniiaPreferences.setPayPalId(payPalId);
                            dismiss();

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
