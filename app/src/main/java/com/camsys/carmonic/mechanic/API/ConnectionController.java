package com.camsys.carmonic.mechanic.API;

import com.camsys.carmonic.mechanic.Model.UserData;
import com.camsys.carmonic.mechanic.Model.UserModel;
import com.camsys.carmonic.mechanic.Utilities.Constants;
import okhttp3.*;
import okio.Buffer;
import retrofit2.http.Query;

import javax.net.ssl.*;
import java.security.cert.CertificateException;

public class ConnectionController {

    //private static OkHttpClient client = new OkHttpClient();
    private static OkHttpClient client = getUnsafeOkHttpClient();
    public static void signupMechanic(UserData userData, Callback callback) {
        String  url  = Constants.URL + "signupMechanic";

        RequestBody requestBody = new FormBody.Builder()
                .add("firstname", userData.getFullName())
                .add("lastname", "Me")
                .add("company", "Carmonic")
                .add("password", userData.getPassword())
                .add("email",userData.getEmailAddress())
                .build();

        Request request = new Request.Builder()
                .url(url)
                .method("POST",requestBody)
                .header("Content-Type","application/json")
                .build();

        try{

            final Buffer buffer = new Buffer();
            request.body().writeTo(buffer);
            System.out.println("----------------------------------------");
            System.out.println("Buffer::: "+buffer.readUtf8());

        }catch (Exception ex){
            System.out.println("Buffer::: "+ex.toString());
        }



        client.newCall(request).enqueue(callback);
    }

    public static void loginMechanic(UserData userData, Callback callback) {
        String  url  = Constants.Base_URL + "/loginMechanic";

        RequestBody requestBody = new FormBody.Builder()
                .add("email", userData.getEmailAddress())
                .add("password", userData.getPassword())
                .build();

        Request request = new Request.Builder()
                .url(url)
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

            OkHttpClient okHttpClient = builder.build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
