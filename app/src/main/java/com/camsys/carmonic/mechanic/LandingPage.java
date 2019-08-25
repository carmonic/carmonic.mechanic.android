package com.camsys.carmonic.mechanic;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.camsys.carmonic.mechanic.Model.UserModel;
import com.camsys.carmonic.mechanic.Model.Users;
import com.camsys.carmonic.mechanic.Service.TestService;
import com.camsys.carmonic.mechanic.Utilities.Constants;
import com.camsys.carmonic.mechanic.Utilities.SharedData;
import com.camsys.carmonic.mechanic.onboarding.SignInActivtiy;
import com.camsys.carmonic.mechanic.onboarding.SignUpActivity;
import com.google.gson.Gson;
import io.socket.client.IO;
import io.socket.client.Socket;

import java.net.URISyntaxException;


public class LandingPage extends AppCompatActivity {




    SharedData sharedData =  null;
    Gson gson =  null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_landing_page);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        sharedData =  new SharedData(getApplicationContext());
        gson  = new Gson();
        Button  btnSignIn  = (Button) findViewById(R.id.btn_sign_in);
        Button  btnSignUp  = (Button) findViewById(R.id.btn_register);

        String storedData =  sharedData.Get(Constants.USER_KEY,"");
//        if(storedData == ""){
//            Intent intent  =  new Intent(getApplicationContext(), MainActivity.class);
//            startActivity(intent);
//            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//            finish();
//        }
        Users users  =  gson.fromJson(storedData,Users.class);





       btnSignIn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Bundle  b  =  new Bundle();
               b.putString("test","test");
               Intent intent  =  new Intent(getApplicationContext(), SignInActivtiy.class);
               //intent.putExtras(b);
               //startService(intent);
               startActivity(intent);
               overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
           }
       });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent  =  new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }


}