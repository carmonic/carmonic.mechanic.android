package com.camsys.carmonic.mechanic.Utilities;

import android.content.Context;
import android.util.Log;
import com.camsys.carmonic.mechanic.API.CustomSSLSocketFactory;
import com.camsys.carmonic.mechanic.Model.UserData;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import java.net.URI;


/**
 * Created by macbookpro on 29/06/2017.
 */

public class Connectivity {

//
//public static void  TrustCertificste(){
//
//    TrustManager[] trustAllCerts = new TrustManager[]{
//            new X509TrustManager() {
//
//                public java.security.cert.X509Certificate[] getAcceptedIssuers()
//                {
//                    return null;
//                }
//                public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
//                {
//                    //No need to implement.
//                }
//                public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
//                {
//                    //No need to implement.
//                }
//            }
//    };
//
//// Install the all-trusting trust manager
//    try
//    {
//        SSLContext sc = SSLContext.getInstance("SSL");
//        sc.init(null, trustAllCerts, new java.security.SecureRandom());
//        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
//    }
//    catch (Exception e)
//    {
//        System.out.println(e);
//    }
//}

    // trusting all certificate
//    public static void  doTrustToCertificates() throws Exception {
//        //Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
//        TrustManager[] trustAllCerts = new TrustManager[]{
//                new X509TrustManager() {
//                    public X509Certificate[] getAcceptedIssuers() {
//                        return null;
//                    }
//
//                    public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
//                        return;
//                    }
//
//                    public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
//                        return;
//                    }
//                }
//        };
//
//        SSLContext sc = SSLContext.getInstance("SSL");
//        sc.init(null, trustAllCerts, new SecureRandom());
//        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
//        HostnameVerifier hv = new HostnameVerifier() {
//            public boolean verify(String urlHostName, SSLSession session) {
//                if (!urlHostName.equalsIgnoreCase(session.getPeerHost())) {
//                    System.out.println("Warning: URL host '" + urlHostName + "' is different to SSLSession host '" + session.getPeerHost() + "'.");
//                }
//                return true;
//            }
//        };
//        HttpsURLConnection.setDefaultHostnameVerifier(hv);
//    }
////    public static String Register(UserData userData) {
////        String result = "";
//        try {
//            String fullUrl  =Constants.URL + "/signupMechanic";
//            doTrustToCertificates();
//            HttpClient client = new DefaultHttpClient();
//            HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
//            HttpResponse response = null;
//            JSONObject json = new JSONObject();
//
//
//            HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
//
//            DefaultHttpClient client = new DefaultHttpClient();
//
//            SchemeRegistry registry = new SchemeRegistry();
//            SSLSocketFactory socketFactory = SSLSocketFactory.getDefault();//.getSocketFactory();
//            socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
//            registry.register(new Scheme("https", socketFactory, 443));
//            SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
//            DefaultHttpClient httpClient = new DefaultHttpClient(mgr, client.getParams());
//
//// Set verifier
//            HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
//
//
//            if(fullUrl.toLowerCase().contains("https://")){
//                KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
//                trustStore.load(null, null);
//                CustomSSLSocketFactory sf = new CustomSSLSocketFactory(trustStore);
//               // sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
//
//                HttpParams params = new BasicHttpParams();
//                SchemeRegistry registry = new SchemeRegistry();
//                registry.register(new Scheme("https", sf, 443));
//
//                ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
//                client= new DefaultHttpClient(ccm, params);
//            }
//
//
//
//            HttpPost post = new HttpPost(fullUrl);
//
//            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
//            nameValuePairs.add(new BasicNameValuePair("firstname", userData.getFullName()));
//            nameValuePairs.add(new BasicNameValuePair("lastname", userData.getFullName()));
//            nameValuePairs.add(new BasicNameValuePair("company", userData.getPhoneNumber()));
//            nameValuePairs.add(new BasicNameValuePair("email", userData.getEmailAddress().trim()));
//            nameValuePairs.add(new BasicNameValuePair("password", userData.getPassword()));
//
//
//            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//            response = client.execute(post);
//
//            /*Checking response */
//            if (response != null) {
//                // HttpEntity httpEntity = response.getEntity(); //Get the data in the entity
//                result = EntityUtils.toString(response.getEntity());
//                Log.i("result++:", result);
//
//            }
//        } catch (Exception ex) {
//
//            ex.printStackTrace();
//            result = "";
//        }
//
//        return result;
//    }
//    //
    public static String GETUSERDETAIL_GET(String token) {
        String result = null;
      //  Log.i(Constants.LOG_TAG, "<<<<STARTING LOGIN>>>>");
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            URI website = new URI("http://www.qothooservice.com/api/Settings/GetUserProfile");
            request.setURI(website);
            //request.setContentType("application/json");
            request.addHeader("Content-Type", "application/json");
            request.addHeader("Authorization", "Bearer " + token);  //"Bearer YjYwZDIxZjRmMGE5YzA0NDExOTIyYmE0YTAxYjI4OWU=") ;
            HttpResponse response = httpclient.execute(request);

            if (response != null) {
                // HttpEntity httpEntity = response.getEntity(); //Get the data in the entity
                result = EntityUtils.toString(response.getEntity());
                Log.i("result++:", result);
            }
        } catch (Exception exp) {
            exp.printStackTrace();
           // result = Util.errorCode(Constants.ERROR_CODE, Constants.DOING_BACKGROUND_SERVICE_RESULT);
        }
        System.out.println("background " + result);
        return result;

    }

//    public static String UPDATEWORKADDRESS(String token, double latitudeAddress, double longitudeAddress, String WorkAddress) {
//        String result = null;
//        Log.i(Constants.LOG_TAG, "<<<<STARTING LOGIN>>>>");
//        try {
//            System.out.println(token + "latitudeAddress:: " + latitudeAddress + "longitudeAddress:: " + longitudeAddress + "WorkAddress:: " + WorkAddress);
//            HttpClient client = new DefaultHttpClient();
//            HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
//            HttpResponse response = null;
//            JSONObject json = new JSONObject();
//            HttpPost post = new HttpPost("http://www.qothooservice.com/api/Settings/UpdateWorkAddress");
//            json.put("workAddress", WorkAddress);
//            json.put("workLattitude", latitudeAddress);//"08028187457");
//            json.put("workLongitude", longitudeAddress);
//
//
//            StringEntity se = new StringEntity(json.toString());
//            se.setContentType("application/json");
//            post.addHeader("Content-Type", "application/json");
//            //post.addHeader("Authorization",  GetAPIKeys(this.MobileNumber).trim());  //"Bearer YjYwZDIxZjRmMGE5YzA0NDExOTIyYmE0YTAxYjI4OWU=") ;
//            post.addHeader("Authorization", "Bearer " + token);
//
//            // Log.i("WorkAddress::: ",WorkAddress);
//            Log.i("json.toString():: ", json.toString());
//
//            post.setEntity(se);
//            response = client.execute(post);
//
//            /*Checking response */
//            if (response != null) {
//                // HttpEntity httpEntity = response.getEntity(); //Get the data in the entity
//                result = EntityUtils.toString(response.getEntity());
//                Log.i("result++:", result);
//            }
//
//        } catch (Exception exp) {
//            exp.printStackTrace();
//            result = Util.errorCode(Constants.ERROR_CODE, Constants.DOING_BACKGROUND_SERVICE_RESULT);
//        }
//        System.out.println("background " + result);
//        return result;
//
//    }
//
//    public static String UPDATEHOMEADDRESS(String token, double latitudeAddress, double longitudeAddress, String WorkAddress) {
//        String result = null;
//        Log.i(Constants.LOG_TAG, "<<<<STARTING LOGIN>>>>");
//        try {
//
//            HttpClient client = new DefaultHttpClient();
//            HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
//            HttpResponse response = null;
//            JSONObject json = new JSONObject();
//            HttpPost post = new HttpPost("http://www.qothooservice.com/api/Settings/UpdateHomeAddress");
//            json.put("homeLattitude", latitudeAddress);//"08028187457");
//            json.put("homeAddress", WorkAddress);
//            json.put("homeLongitude", longitudeAddress);
//
//            StringEntity se = new StringEntity(json.toString());
//            se.setContentType("application/json");
//            post.addHeader("Content-Type", "application/json");
//            //post.addHeader("Authorization",  GetAPIKeys(this.MobileNumber).trim());  //"Bearer YjYwZDIxZjRmMGE5YzA0NDExOTIyYmE0YTAxYjI4OWU=") ;
//            post.addHeader("Authorization", "Bearer " + token);
//            Log.i("json.toString():: ", json.toString());
//
//
//            post.setEntity(se);
//            response = client.execute(post);
//
//            /*Checking response */
//            if (response != null) {
//                // HttpEntity httpEntity = response.getEntity(); //Get the data in the entity
//                result = EntityUtils.toString(response.getEntity());
//                Log.i("result++:", result);
//            }
//
//        } catch (Exception exp) {
//            exp.printStackTrace();
//            result = Util.errorCode(Constants.ERROR_CODE, Constants.DOING_BACKGROUND_SERVICE_RESULT);
//        }
//        System.out.println("background " + result);
//        return result;
//
//    }
//
//
//    public static String GETANOTHERUSERDEATIAL(String token) {
//        String result = null;
//        Log.i(Constants.LOG_TAG, "<<<<STARTING LOGIN>>>>");
//        try {
//
//            //result  = setPreregister(this.MobileNumber,this.Countryname,this.Email); //  this.MobileNumber,this.Email);
//
//            HttpClient client = new DefaultHttpClient();
//            HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
//            HttpResponse response = null;
//            JSONObject json = new JSONObject();
//            //http://www.qothooservice.com/api/Register/PreRegister
//            HttpPost post = new HttpPost("http://www.qothooservice.com/api/Settings/GetUserProfile");
//            json.put("countryId", 1);
//            StringEntity se = new StringEntity(json.toString());
//
//            se.setContentType("application/json");
//            post.addHeader("Content-Type", "application/json");
//            post.addHeader("Authorization", "Bearer" + token);  //"Bearer YjYwZDIxZjRmMGE5YzA0NDExOTIyYmE0YTAxYjI4OWU=") ;
//
//            post.setEntity(se);
//            response = client.execute(post);
//            /*Checking response */
//            if (response != null) {
//                // HttpEntity httpEntity = response.getEntity(); //Get the data in the entity
//                result = EntityUtils.toString(response.getEntity());
//                Log.i("result++:", result);
//            }
//
//        } catch (Exception exp) {
//            exp.printStackTrace();
//            result = Util.errorCode(Constants.ERROR_CODE, Constants.DOING_BACKGROUND_SERVICE_RESULT);
//        }
//        System.out.println("background " + result);
//        return result;
//    }
//
//    public static String PREREGISTER(String bearer, int countryId, String phoneNumber, String Email) {
//        HttpClient client = new DefaultHttpClient();
//        HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
//        HttpResponse response = null;
//        JSONObject json = new JSONObject();
//        String qoothoResponse = "";
//
//        try {
//            HttpPost post = new HttpPost("http://www.qothooservice.com/api/Register/PreRegister");///"http://www.qothooservice.com/api/Register/PreRegister");
//            json.put("mobile", phoneNumber);  //"08028187457");
//            json.put("email", Email);//"oopeniyi@yahoo.com");
//            json.put("countryId", countryId);
//            StringEntity se = new StringEntity(json.toString());
//
//            se.setContentType("application/json");
//            post.addHeader("Content-Type", "application/json");
//            post.addHeader("Authorization", bearer);// "Bearer YjYwZDIxZjRmMGE5YzA0NDExOTIyYmE0YTAxYjI4OWU=") ;
//
//            Log.i("bearer::: ", getB64Auth(bearer));
//            Log.i("json", json.toString());
//
//            post.setEntity(se);
//            response = client.execute(post);
//
//            /*Checking response */
//            if (response != null) {
//                // HttpEntity httpEntity = response.getEntity(); //Get the data in the entity
//                qoothoResponse = cz.msebera.android.httpclient.util.EntityUtils.toString(response.getEntity());
//                Log.i("SSSSSSS", qoothoResponse);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            //createDialog("Error", "Cannot Estabilish Connection");
//            qoothoResponse = "";
//        }
//
//        return qoothoResponse;
//    }
//
//

}
