package com.camsys.carmonic.mechanic.API;

import android.util.Log;
import com.camsys.carmonic.mechanic.R;
import com.camsys.carmonic.mechanic.Utilities.Constants;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

public class RetrofitClientInstance {

    private static Retrofit retrofit;
    private static final String BASE_URL = Constants.URL;
    static OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();

    public static Retrofit getRetrofitInstance() {

        OkHttpClient client = new OkHttpClient.Builder()

                .connectTimeout(100, TimeUnit.SECONDS)
                .connectTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(15, TimeUnit.SECONDS)
                .readTimeout(100,TimeUnit.SECONDS).build();
        client.sslSocketFactory();

        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }




}

