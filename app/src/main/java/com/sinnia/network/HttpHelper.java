package com.sinnia.network;

import android.net.Uri;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by AXIOM on 26-02-2018.
 */

public class HttpHelper {


    private static final String TAG = "HttpHelper";

  //  private static final String BASE_URL = "http://52.53.229.4:8080/siniia/api/";

    private static final String BASE_URL = "http://3.17.151.147:8080/siniia/api/";


    private static final String LOGIN_OTP = "getLoginOTP";
    private static final String VERIFY_OTP = "verifyOTP";
    private static final String RESEND_OTP = "resendOTP";
    private static final String UPDATE_USER_TYPE = "updateUserType";
    private static final String UPDATE_USER = "updateUserProfile";
    private static final String GET_PRODUCTS_LIST = "getProductsList";

    private static final String GET_PRODUCTS_BY_TYPE = "getProductByProductType";
    private static final String GET_BUY_DATA = "getBuyData";
    private static final String GET_SELL_DATA = "getSellData";

    private static final String GET_REGISTER_ID = "registerUsers";

    private static final String GET_USER_DETAILS = "getUserDetails";

    private static final String GET_PRODUCT_DETAILS_BY_ID = "getProductsByProductId";

    private static final String GET_ADD_TO_CART = "addToCart";

    private static final String GET_COUNT = "getCounts";

    private static final String GET_CART_DATA = "getCartData";

    private static final String GET_SHIPPERS = "getShippersAvailable";
    private static final String PLACE_AN_ORDER = "placeOrder";
    private static final String UPDATE_PROFILE = "updateUserProfile";
    private static final String POST_A_PRODUCT = "addProduct";
    private static final String ALL_PRODUCT_TYPES = "getProductsCategories";
    private static final String GET_ALL_BANNER_IMAGES = "getAllBanners";
    private static final String GET_MY_POST_DATA = "getMyPostsData";
    private static final String UPDATE_PRODUCT_STATUS = "updateProductStatus";
    private static final String GET_BUY_PRODUCT_DETAILS = "getBuyProductDetails";
    private static final String UPDATE_PRODUCT_DETAILS = "updateProductDetails";
    private static final String GET_USER_ADDRESS = "getAddressByUserId";
    private static final String UPDATE_PRODUCT="updateProduct";
    private static final String DELETE_CART_ID = "deleteFromCart";
    private static final String ADD_NEWS_LETTER="addNewsLetterDetails";
    private static final String ADD_SHIPPER="addShipper";
    private static final String ADD_DONATION="addDonationDetails";
    private static final String ADD_PRODUCT_SEARCH_TO_DATABASE="setSearchData";
    private static final String ADD_PAYPAL_ACCOUNT_DETAILS="addPayPalAccountDetails";
    private static final String SEND_VERSION="getAPKVersion";
    private static final String GET_SHIPPER_DATA="getShippersByOrder";
    private static final String EMAIL_LOGIN="emailLogin";
    private static final String VERIFY_OTP_WITH_EMAIL="verifyEmailOTP";

    public String verifyEmailWithOTP(String OTP,String email){
        String url=BASE_URL+VERIFY_OTP_WITH_EMAIL+"?otp="+OTP+"&email="+email;
        return postHttpResponse(url);
    }

    public String getEmailLogin(String json){
        String url=BASE_URL+EMAIL_LOGIN;
        return postHttpResponse(url,json);

    }

    public String getShipperDataByrder(String json){
        String url=BASE_URL+GET_SHIPPER_DATA;
        return postHttpResponse(url,json);
    }

    public String sendVersion(String json){
        String url=BASE_URL+SEND_VERSION;
        return postHttpResponse(url,json);
    }

    public String addPaypalId(String json){
        String url=BASE_URL+ADD_PAYPAL_ACCOUNT_DETAILS;
        return postHttpResponse(url,json);
    }

    public String addProductSearchKey(String userId,String searchKey){
        String url=BASE_URL+ADD_PRODUCT_SEARCH_TO_DATABASE+"?userId="+userId+"&searchKey="+ Uri.encode(searchKey);
        return postHttpResponse(url);
    }

    public String addDonation(String json){
        String url=BASE_URL+ADD_DONATION;
        return postHttpResponse(url,json);
    }

    public String addShipper(String json){
        String url=BASE_URL+ADD_SHIPPER;
        return postHttpResponse(url,json);
    }

    public String addNewsLetter(String json){
        String url=BASE_URL+ADD_NEWS_LETTER;
        return postHttpResponse(url,json);
    }

    public String getUserAddress(String userId) {
        String url = BASE_URL + GET_USER_ADDRESS + "?userId=" + userId;
        return postHttpResponse(url);
    }

    public String updateProductDetails(String josn) {
        String url = BASE_URL + UPDATE_PRODUCT_DETAILS;
        return postHttpResponse(url, josn);
    }

    public String getBuyProductDetails(String userId, String productId, String orderId) {
        String url = BASE_URL + GET_BUY_PRODUCT_DETAILS + "?userId=" + userId + "&productId=" + productId + "&orderId=" + orderId;
        return postHttpResponse(url);
    }


    public String updateProductStatus(String productId, String userId, String status) {
        String url = BASE_URL + UPDATE_PRODUCT_STATUS + "?productId=" + productId + "&userId=" + userId + "&status=" + status;
        return postHttpResponse(url);
    }


    public String getMyPostData(String userId) {
        String url = BASE_URL + GET_MY_POST_DATA + "?userId=" + userId;
        return postHttpResponse(url);
    }

    public String getAllBannerImages() {
        String url = BASE_URL + GET_ALL_BANNER_IMAGES;
        return postHttpResponse(url);
    }


    public String getProductTypes(String userId) {
        String url = BASE_URL + ALL_PRODUCT_TYPES + "?userId=" + userId;
        return postHttpResponse(url);
    }



    public String postMultiPartUpdateAProduct(HttpEntity entity) {
        String url = BASE_URL + UPDATE_PRODUCT;
        return postMultipartDataResponse(url, entity);
    }

    public String postMultiPartPostAProduct(HttpEntity entity) {
        String url = BASE_URL + POST_A_PRODUCT;
        return postMultipartDataResponse(url, entity);
    }

    public String postMultiPartUserProfile(HttpEntity entity) {
        String url = BASE_URL + UPDATE_PROFILE;
        return postMultipartDataResponse(url, entity);
    }


    public String deleteFromCart(String json) {
        String url = BASE_URL + DELETE_CART_ID;
        return postHttpResponse(url,json);
    }


    public String placeAnOrder(String json) {
        String url = BASE_URL + PLACE_AN_ORDER;
        return postHttpResponse(url, json);
    }

    public String getResendOT(String json) {
        String url = BASE_URL + RESEND_OTP;
        return postHttpResponse(url, json);
    }

    public String getShipperData(String country) {
        String url = BASE_URL + GET_SHIPPERS+"?country="+country;
        return postHttpResponse(url);
    }

    public String getCartData(String userId) {
        String url = BASE_URL + GET_CART_DATA + "?userId=" + userId;
        return postHttpResponse(url);
    }

    public String getCount(String userId) {
        String url = BASE_URL + GET_COUNT + "?userId=" + userId;
        return postHttpResponse(url);
    }

    public String addToCart(String json) {
        String url = BASE_URL + GET_ADD_TO_CART;
        return postHttpResponse(url, json);
    }


    public String getUserDetails(String userId) {
        String url = BASE_URL + GET_USER_DETAILS + "?userId=" + userId;
        return postHttpResponse(url);
    }


    public String registerUserId(String json) {
        String url = BASE_URL + GET_REGISTER_ID;
        return postHttpResponse(url, json);
    }


    public String getLoginOTP(String json) {
        String url = BASE_URL + LOGIN_OTP;
        return postHttpResponse(url, json);
    }

    public String getVerifyOTP(String emailOrMobileNumber, String otp) {
        String url = BASE_URL + VERIFY_OTP + "?data=" + emailOrMobileNumber + "&otp=" + otp;
        return postHttpResponse(url);
    }

    public String updateUserType(String userType, String userId) {
        String url = BASE_URL + UPDATE_USER_TYPE + "?userType=" + userType + "&userId=" + userId;
        return postHttpResponse(url);
    }

    public String getProductsList(String productType,String userId) {
        String url = BASE_URL;
        if(productType==null||productType.isEmpty()) {
          url= url + GET_PRODUCTS_LIST+"?userId="+userId;
        }else{
            url=url+GET_PRODUCTS_BY_TYPE+"?productType="+productType+"&userId="+userId;
        }
        return postHttpResponse(url);
    }

    public String GetSellData(String userId) {
        String url = BASE_URL + GET_SELL_DATA + "?userId=" + userId;
        return postHttpResponse(url);
    }

    public String GetBuyData(String userId) {
        String url = BASE_URL + GET_BUY_DATA + "?userId=" + userId;
        return postHttpResponse(url);
    }

    public String GetProductsById(String userId, String productId) {
        String url = BASE_URL + GET_PRODUCT_DETAILS_BY_ID + "?userId=" + userId + "&productId=" + productId;
        return postHttpResponse(url);
    }

    public String getHttpResponse(String url) {
        Log.d(TAG, "request Url:" + url);
        InputStream is = null;
        try {
            URL connectionUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) connectionUrl
                    .openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setReadTimeout(100000);
            conn.setConnectTimeout(100000);
            conn.connect();
            int response = conn.getResponseCode();
            is = conn.getInputStream();
            ByteArrayOutputStream data = new ByteArrayOutputStream();
            int readBytes = 0;
            byte[] sBuffer = new byte[512];
            while ((readBytes = is.read(sBuffer)) != -1) {
                data.write(sBuffer, 0, readBytes);
            }
            String dataAsString = new String(data.toByteArray());
            return dataAsString;
        } catch (IOException e) {
            Log.e(TAG, "getHttpStream Exception: " + e.getMessage());
            return null;
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    public String postHttpResponse(String url) {
        Log.d(TAG, "post request Url:" + url);
        InputStream is = null;
        try {
            URL connectionUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) connectionUrl
                    .openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            conn.connect();
            int response = conn.getResponseCode();
            is = conn.getInputStream();
            ByteArrayOutputStream data = new ByteArrayOutputStream();
            int readBytes = 0;
            byte[] sBuffer = new byte[512];
            while ((readBytes = is.read(sBuffer)) != -1) {
                data.write(sBuffer, 0, readBytes);
            }
            String dataAsString = new String(data.toByteArray());
            Log.d(TAG, "response:" + dataAsString);
            return dataAsString;
        } catch (IOException e) {
            Log.e(TAG, "postHttpStream Exception: " + e.getMessage());
            return null;
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private String postMultipartDataResponse(String url, HttpEntity entity) {
        try {
            Log.d(TAG, "Req url:" + url);
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost(url);
            postRequest.setEntity(entity);
            HttpResponse response = httpClient.execute(postRequest);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent(), "UTF-8"));
            String sResponse;
            StringBuilder s = new StringBuilder();

            while ((sResponse = reader.readLine()) != null) {
                s = s.append(sResponse);
            }
            Log.d(TAG, "upload response:" + s.toString());
            return s.toString();
        } catch (Exception e) {
            // handle exception here
            Log.e(e.getClass().getName(), e.getMessage());
            return null;
        } finally {
        }
    }


    public String postHttpResponse(String url, String json) {
        Log.d(TAG, "post request Url:" + url + " json " + json);
        InputStream is = null;
        try {
            URL connectionUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) connectionUrl
                    .openConnection();
            conn.setRequestMethod("POST");
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(10000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            /*conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Accept","application/json");*/
            conn.setRequestProperty("Content-Type", "application/json");
            conn.connect();

            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(json);
            wr.flush();
            wr.close();
            int response = conn.getResponseCode();
            is = conn.getInputStream();
            ByteArrayOutputStream data = new ByteArrayOutputStream();

            int readBytes = 0;
            byte[] sBuffer = new byte[512];
            while ((readBytes = is.read(sBuffer)) != -1) {
                data.write(sBuffer, 0, readBytes);
            }
            String dataAsString = new String(data.toByteArray());
            return dataAsString;
        } catch (IOException e) {
            Log.e(TAG, "post Exception: " + e.getMessage());
            return null;
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
