package com.camsys.carmonic.mechanic.API;

import com.camsys.carmonic.mechanic.Model.UserData;
import com.camsys.carmonic.mechanic.Model.UserModel;
import com.camsys.carmonic.mechanic.Utilities.Constants;
import okhttp3.*;


import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;

import javax.net.ssl.*;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

public class ConnectionController {

    //private static OkHttpClient client = new OkHttpClient();
    public static OkHttpClient client = getUnsafeOkHttpClient();
    public static void signupMechanic(UserData userData, Callback callback) {
        String  url  = Constants.Base_URL + "signupMechanic";

        RequestBody requestBody = new FormBody.Builder()
                .add("firstname", userData.getFullName())
                .add("lastname", userData.getFullName())
                .add("company", "Carmonic")
                .add("password", userData.getPassword())
                .add("email",userData.getEmailAddress())
                .build();
        Request request = new Request.Builder()
                .url(url)
                .header("Connection","close")
               .post(requestBody)
                .build();

        client.newCall(request).enqueue(callback);
    }

    public static void loginMechanic(UserData userData, Callback callback) {
        String  url  = Constants.Base_URL +"loginMechanic"; // "loginMechanic";
        RequestBody requestBody = new FormBody.Builder()
                .add("email", userData.getEmailAddress())
                .add("password", userData.getPassword())
                .build();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Connection","close")
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(callback);
    }

    //ToDo: Configure proper certificate settings when we buy one
    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            builder.connectTimeout(5, TimeUnit.MINUTES);
            builder.readTimeout(5, TimeUnit.MINUTES);
            builder.writeTimeout(5, TimeUnit.MINUTES);
            builder.retryOnConnectionFailure(true);

            OkHttpClient okHttpClient = builder.build();

            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
