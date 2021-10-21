package com.sinnia.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;

import com.sinnia.R;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.regex.Pattern;

public class SiniiaUtils {

    private static final String TAG = "SiniiaUtils";

    public static void selectedButton(Context mContext, Button view) {
        view.setBackground(mContext.getDrawable(R.drawable.button_selected));
        view.setTextColor(mContext.getColor(R.color.white_color));
    }

    public static void unSelectedButton(Context mContext, Button view) {
        view.setBackground(mContext.getDrawable(R.drawable.button_unselected));
        view.setTextColor(mContext.getColor(R.color.dark_black_color));
    }

    public static boolean isEmailValid(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    public static boolean checkForNetworkConnectivity(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null && cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public static byte[] convertBitmapToBytes(Bitmap bm) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // compress to specified format (PNG), quality - which is ignored
            // for PNG, and out stream
            bm.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            return baos.toByteArray();
        } catch (Exception e) {
            //Log.v("Gestures", e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    public static void closeKeyboard(Context c, IBinder windowToken) {
        if (windowToken != null) {
            InputMethodManager mgr = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(windowToken, 0);
        }
    }

    public static void loadPicassoImageWithOutCircle(final Context mContext,
                                                     final String userImage,
                                                     final ImageView imageView) {
        try {
            if (userImage != null) {
                Picasso.with(mContext)
                        .load(userImage)
                        .into(imageView);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static final String getCountryName(String countryCode) {
        switch (countryCode) {
            case 91 + "":
                return "INDIA";
            case 254 + "":
                return "AFRICA";
            case 1 + "":
                return "USA";
            case 27 + "":
                return "AFRICA";
        }
        return "USA";
    }


    public static final String getCurrencySymbol(String countryCode) {
        if(countryCode!=null) {
            switch (countryCode) {
                case 91 + "":
                    return "₹";
          /*  case 254+"":
                return "KSh";*/
            }
        }
        return "$";
    }

    public static final String getCurrencyCode(String countryCode) {
       /* switch (countryCode) {
            case 91 + "":
                return "INR";
        }*/
        return "USD";
    }

    public static final BigDecimal convertInDollar(String countryCode, double currency) {
        switch (countryCode) {
            case 91 + "":
                return new BigDecimal(currency / 75.00);
        }
        return new BigDecimal(String.valueOf(currency));
    }

    public static String getCountryDialCode(Context mContext) {
        String contryId = null;
        String contryDialCode = null;
        TelephonyManager telephonyMngr = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        contryId = telephonyMngr.getSimCountryIso().toUpperCase();
        String[] arrContryCode = mContext.getResources().getStringArray(R.array.DialingCountryCode);
        for (int i = 0; i < arrContryCode.length; i++) {
            String[] arrDial = arrContryCode[i].split(",");
            if (arrDial[1].trim().equals(contryId.trim())) {
                contryDialCode = arrDial[0];
                break;
            }
        }
        return contryDialCode;
    }


    public static String getCountryDialogFromCountryCode(Context mContext, String contryId) {
        Log.d(TAG, "XXXXXXXXXXXXXXXXXXXXXx Country Id" + contryId);
        String contryDialCode = null;
        String[] arrContryCode = mContext.getResources().getStringArray(R.array.DialingCountryCode);
        for (int i = 0; i < arrContryCode.length; i++) {
            String[] arrDial = arrContryCode[i].split(",");
            if (arrDial[1].trim().equals(contryId.trim())) {
                contryDialCode = arrDial[0];
                break;
            }
        }
        return contryDialCode;
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
                    .digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public  static void getValues(String hexString){
        byte[] mainArray=hexStringToByteArray(hexString);
        for(int i=0;i<mainArray.length;i++){
            Log.d(TAG,"index "+i+" value "+mainArray[i]);
        }
    }

}
