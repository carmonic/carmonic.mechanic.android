package com.camsys.carmonic.mechanic.API;


import com.camsys.carmonic.mechanic.Model.UserData;
import com.camsys.carmonic.mechanic.Model.UserModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by macbookpro on 02/07/2017.
 */

public interface CarmonicAPI {



    @Headers("Content-Type: application/json")
    @POST("/SignUp")
    Call<UserModel> SignUp(@Body UserData user);

    @Headers("Content-Type: application/json")
    @POST("/SignUp")
    Call<UserModel> SignIn(@Body UserData user); //login
}
