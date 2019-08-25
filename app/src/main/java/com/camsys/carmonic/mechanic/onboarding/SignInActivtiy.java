package com.camsys.carmonic.mechanic.onboarding;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;


import androidx.room.Room;
import com.camsys.carmonic.mechanic.API.ConnectionController;
////import com.camsys.carmonic.mechanic.DAO.UserDAO;
//import com.camsys.carmonic.mechanic.DAO.UserDAO;
//import com.camsys.carmonic.mechanic.Dasboard.test;
import com.camsys.carmonic.mechanic.DAO.UserDAO;
import com.camsys.carmonic.mechanic.MainActivity;
import com.camsys.carmonic.mechanic.Model.UserData;
import com.camsys.carmonic.mechanic.Model.UserModel;
import com.camsys.carmonic.mechanic.Model.Users;
import com.camsys.carmonic.mechanic.R;
////import com.camsys.carmonic.mechanic.Utilities.AppDatabase;
//import com.camsys.carmonic.mechanic.Utilities.AppDatabase;
import com.camsys.carmonic.mechanic.Utilities.AppDatabase;
import com.camsys.carmonic.mechanic.Utilities.Constants;
import com.camsys.carmonic.mechanic.Utilities.SharedData;
import com.camsys.carmonic.mechanic.Utilities.Util;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.io.IOException;


public class SignInActivtiy  extends AppCompatActivity {

    SharedData   sharedData =  null;
    private UserDAO mUserDAO;
    Gson gson = null;
    EditText txtPassowrd;
    EditText txtEmailAddress;


    TextInputLayout inputLayoutPassword = null;
    TextInputLayout inputLayoutEmailAddress= null;
    LinearLayout wait_icon =  null;
    LinearLayout signlayout =  null;
    private Context mContext;
    private Activity mActivity;

    private static final String TAG = SignInActivtiy.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        mActivity = SignInActivtiy.this;
        sharedData =  new SharedData(getApplicationContext());
       gson  = new Gson();

//        mUserDAO = Room.databaseBuilder(SignInActivtiy.this, AppDatabase.class, Constants.PREF_NAME)
//                .allowMainThreadQueries()   //Allows room to do operation on main thread
//                .fallbackToDestructiveMigration()
//                .build()
//                .getUserDAO();

        setContentView(R.layout.activity_sign_in_activtiy);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        ImageButton imgButton = (ImageButton) findViewById(R.id.appBackButton);
        final Button btnSubmit = (Button) findViewById(R.id.btnSubmit) ;



        txtEmailAddress = (EditText) findViewById(R.id.txtEmailAddress);
        txtPassowrd = (EditText) findViewById(R.id.txtPassword);
        inputLayoutEmailAddress = (TextInputLayout) findViewById(R.id.input_layout_email_address);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);

        txtEmailAddress.addTextChangedListener(new MyTextWatcher(txtEmailAddress));
        txtPassowrd.addTextChangedListener(new MyTextWatcher(txtPassowrd));

        wait_icon = (LinearLayout) findViewById(R.id.wait_icon);
        signlayout = (LinearLayout) findViewById(R.id.signlayout);

        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 finish();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!submitForm()) {

                    Util.hideSoftKeyboard(SignInActivtiy.this,btnSubmit);
                    final UserData user  =  new UserData();
                    user.setEmailAddress(txtEmailAddress.getText().toString());
                    user.setPassword(txtPassowrd.getText().toString());

                    if(Util.checkConnectivity(getApplicationContext())){
                        signlayout.setVisibility(View.INVISIBLE);
                        wait_icon.setVisibility(View.VISIBLE);

                        ConnectionController.loginMechanic(user, new okhttp3.Callback() {
                            @Override
                            public void onFailure(okhttp3.Call call, final IOException e) {
                                mActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        System.out.println("Exception :::: "  +  e.toString());
                                        Toast.makeText(mActivity,e.toString(),Toast.LENGTH_LONG).show();
                                        wait_icon.setVisibility(View.INVISIBLE);
                                        signlayout.setVisibility(View.VISIBLE);

                                    }
                                });
                            }

                            @Override
                            public void onResponse(okhttp3.Call call, final okhttp3.Response response) throws IOException {
                                //wait_icon.setVisibility(View.INVISIBLE);

                                final UserModel model = gson.fromJson(response.body().string(),UserModel.class);      //GetJSonObject(response.body().string());
                                mActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        if(model.getAuthInfo().getMessage().contains("Successfully")){
                                            Users user =  model.getUser();
                                            String json = gson.toJson(user);
                                            sharedData.Set(Constants.USER_KEY,json);

                                            Intent intent =  new Intent(SignInActivtiy.this,MainActivity.class);
                                            intent.setAction(Constants.SetAction.LOGIN);
                                            intent.putExtra("message","isLogin");
                                            startActivity(intent);
                                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                            finish();

                                        }else {

                                            Toast.makeText(mActivity,model.getAuthInfo().getMessage(),Toast.LENGTH_LONG).show();
                                            wait_icon.setVisibility(View.INVISIBLE);
                                            signlayout.setVisibility(View.VISIBLE);
                                        }
                                    }
                                });
                            }
                        });
                    }else {

                        Toast.makeText(SignInActivtiy.this, "Internet connection is not available,kindly try again later.", Toast.LENGTH_LONG).show();
                    }

                }

            }
        });
    }


    private class MyTextWatcher implements TextWatcher {


        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_layout_email_address:
                    validateEmail();
                    break;
                case R.id.input_layout_password:
                    validatePassword();
                    break;
                case R.id.input_layout_confirm_password:
                    validatePassword();
                    break;

            }
        }



    }
    private boolean submitForm() {
        boolean cancel =  false ;

        if (!validateEmail()) {
            cancel = true;
        }

        if (!validatePassword()) {
            cancel = true;
        }
        return  cancel;
    }
    private boolean validateEmail() {
        String Email = txtEmailAddress.getText().toString().trim();
        if (Email.isEmpty() || !isValidEmail(Email)) {
            inputLayoutEmailAddress.setError("Invalid Email");
            requestFocus(txtEmailAddress);
            return false;
        } else {
            inputLayoutEmailAddress.setErrorEnabled(false);
        }
        return true;
    }
    private boolean validatePassword() {
        if (txtPassowrd.getText().toString().trim().isEmpty() ||txtPassowrd.getText().toString().trim().length() < 6 )  {
            inputLayoutPassword.setError("Password must be at least 6 characters long.");
            requestFocus(txtPassowrd);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }
    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public  UserModel  GetJSonObject(String jsonstr){
        final Gson gson = new Gson();
        UserModel user = new UserModel();
        user  = gson.fromJson(jsonstr, UserModel.class);
        return user;
    }

}
