package com.camsys.carmonic.mechanic.API;


import com.camsys.carmonic.mechanic.Model.UserData;
import com.camsys.carmonic.mechanic.Model.UserModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by macbookpro on 02/07/2017.
 */

public interface CarmonicAPI {

    @Headers("Content-Type: application/json")
    @POST("signupMechanic")
    Call<UserModel> SignUp(@Query("firstname") String firstname,@Query("lastname") String lastname,@Query("company") String phoneNumber,@Query("email") String emailAddress, @Query("password") String password); //(@Body String fullName,@Body String email,@Body String phoneNumber,@Body String passWord);

    @Headers("Content-Type: application/json")
    @POST("loginMechanic")
    Call<UserModel> SignIn(@Body UserData user); //login
}
