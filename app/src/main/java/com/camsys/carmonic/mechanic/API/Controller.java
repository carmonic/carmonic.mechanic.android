package com.camsys.carmonic.mechanic.API;


import com.camsys.carmonic.mechanic.Utilities.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by macbookpro on 02/07/2017.
 */

public class Controller {

    static final String BASE_URL = Constants.Base_URL;

    public CarmonicAPI start() {
        Gson gson = new GsonBuilder().setLenient().create();

//        OkHttpClient httpClient = new OkHttpClient();
//
//        httpClient.addInterceptor(new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                Request request = chain.request().newBuilder().addHeader("parameter", "value").build();
//                return chain.proceed(request);
//            }
//        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        CarmonicAPI gerritAPI = retrofit.create(CarmonicAPI.class);

        // Call<RetrofitTest.PreRegister> call = gerritAPI.loadChanges("status:open");
        // call.enqueue(this);
        return gerritAPI;
    }
}
