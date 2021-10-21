package com.sinnia.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.sinnia.R;

public class SiniiaPreferences {

    private Context mContext;
    private SharedPreferences prefs;

    public SiniiaPreferences(Context context) {
        this.mContext = context;
        prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    public void setRegisterStatus(int registerStatus) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(mContext.getResources().getString(R.string.prefs_register_status), registerStatus);
        editor.commit();
    }

    public int getRegisterStatus() {
        return prefs.getInt(mContext.getResources().getString(R.string.prefs_register_status), 0);
    }

    public void setCountryCode(String countryCode) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(mContext.getResources().getString(R.string.prefs_country_code), countryCode);
        editor.commit();
    }

    public String getCountryCode() {
        return prefs.getString(mContext.getResources().getString(R.string.prefs_country_code), null);
    }

    public void setCountryName(String countryCode) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(mContext.getResources().getString(R.string.prefs_country_name), countryCode);
        editor.commit();
    }

    public String getCountryName() {
        return prefs.getString(mContext.getResources().getString(R.string.prefs_country_name), null);
    }

    public void setMobileNumber(String mobileNumber) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(mContext.getResources().getString(R.string.prefs_mobile_number), mobileNumber);
        editor.commit();
    }

    public String getMobileNumber() {
        return prefs.getString(mContext.getResources().getString(R.string.prefs_mobile_number), null);
    }

    public void setEmail(String email) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(mContext.getResources().getString(R.string.prefs_email), email);
        editor.commit();
    }

    public String getEmail() {
        return prefs.getString(mContext.getResources().getString(R.string.prefs_email), null);
    }

    public void setUserId(String userId) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(mContext.getResources().getString(R.string.prefs_user_id), userId);
        editor.commit();
    }

    public String getUserId() {
        return prefs.getString(mContext.getResources().getString(R.string.prefs_user_id), null);
    }

    public void setUserType(String userId) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(mContext.getResources().getString(R.string.prefs_user_type), userId);
        editor.commit();
    }


    public String getUserType() {
        return prefs.getString(mContext.getResources().getString(R.string.prefs_user_type), null);
    }

    public String getUserName() {
        return prefs.getString(mContext.getResources().getString(R.string.prefs_user_name), null);
    }

    public void setUserName(String userName) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(mContext.getResources().getString(R.string.prefs_user_name), userName);
        editor.commit();
    }

    public void setCartCount(int cartCount) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(mContext.getResources().getString(R.string.prefs_cart_count), cartCount);
        editor.commit();
    }

    public int getCartCount() {
        return prefs.getInt(mContext.getResources().getString(R.string.prefs_cart_count), 0);
    }

    public void setNotificationCount(int cartCount) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(mContext.getResources().getString(R.string.prefs_notification_count), cartCount);
        editor.commit();
    }

    public int getNotificationCount() {
        return prefs.getInt(mContext.getResources().getString(R.string.prefs_notification_count), 0);
    }


    public void isNewsLetterSubmitted(boolean newsletter) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(mContext.getResources().getString(R.string.prefs_news_letter), newsletter);
        editor.commit();
    }

    public boolean getIsNewsLetterSubmitted() {
        return prefs.getBoolean(mContext.getResources().getString(R.string.prefs_news_letter), false);
    }

    public String getPayPalId() {
        return prefs.getString(mContext.getResources().getString(R.string.pref_paypal_id), null);
    }

    public void setPayPalId(String payPalId) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(mContext.getResources().getString(R.string.pref_paypal_id), payPalId);
        editor.commit();
    }

}
