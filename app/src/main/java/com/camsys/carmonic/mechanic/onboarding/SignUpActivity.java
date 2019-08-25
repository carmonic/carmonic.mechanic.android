package com.camsys.carmonic.mechanic.onboarding;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.*;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.camsys.carmonic.mechanic.API.ConnectionController;
import com.camsys.carmonic.mechanic.Model.UserData;
import com.camsys.carmonic.mechanic.Model.UserModel;
import com.camsys.carmonic.mechanic.R;
import com.camsys.carmonic.mechanic.Utilities.LayoutBehaviour;
import com.camsys.carmonic.mechanic.Utilities.Util;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.io.IOException;

public class SignUpActivity extends AppCompatActivity {

    private Context mContext;
    private Activity mActivity;

    private static final String TAG = SignUpActivity.class.getSimpleName();

    LinearLayout infoPanelLayout = null;
    LinearLayout passwordPanelLayout =  null;
    LinearLayout wait_icon = null;

    EditText txtFullName;
    EditText txtPassowrd;
    EditText txtConfirmPassowrd;
    EditText txtPhoneNumber;
    EditText txtEmailAddress;

    TextInputLayout inputLayoutFullname = null;
    TextInputLayout inputLayoutPassword = null;
    TextInputLayout inputLayoutConfirmPassword = null;
    TextInputLayout inputLayoutPhoneNumber = null;
    TextInputLayout inputLayoutEmailAddress= null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mContext = getApplicationContext();
        mActivity = SignUpActivity.this;

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        ImageButton imgButton = (ImageButton) findViewById(R.id.backbutton);
        Button btnInfoPanel = (Button) findViewById(R.id.btnInfoPanel) ;
        final Button btnPassWordPanel = (Button) findViewById(R.id.btnPasswordPanel) ;
        infoPanelLayout = (LinearLayout) findViewById(R.id.infoPanel);
        passwordPanelLayout = (LinearLayout) findViewById(R.id.passwordPanel);
        wait_icon = (LinearLayout) findViewById(R.id.wait_icon);

        getVisibility(true,false,false);


        txtFullName = (EditText) findViewById(R.id.txtFullName);
        txtEmailAddress = (EditText) findViewById(R.id.txtEmailAddress);
        txtConfirmPassowrd = (EditText) findViewById(R.id.txtConfirmPassword);
        txtPhoneNumber = (EditText) findViewById(R.id.txtPhoneNumber);
        txtPassowrd = (EditText) findViewById(R.id.txtPassword);


        inputLayoutConfirmPassword = (TextInputLayout) findViewById(R.id.input_layout_confirm_password);
        inputLayoutEmailAddress = (TextInputLayout) findViewById(R.id.input_layout_email_address);
        inputLayoutFullname = (TextInputLayout) findViewById(R.id.input_layout_full_name);
        inputLayoutPhoneNumber = (TextInputLayout) findViewById(R.id.input_layout_phone_number);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);


        txtFullName.addTextChangedListener(new MyTextWatcher(txtFullName));
        txtEmailAddress.addTextChangedListener(new MyTextWatcher(txtEmailAddress));
        txtPhoneNumber.addTextChangedListener(new MyTextWatcher(txtPhoneNumber));
        txtPassowrd.addTextChangedListener(new MyTextWatcher(txtPassowrd));
        txtConfirmPassowrd.addTextChangedListener(new MyTextWatcher(txtConfirmPassowrd));




        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(infoPanelLayout.getVisibility() == View.VISIBLE){
                   finish();

               }else if(passwordPanelLayout.getVisibility() == View.VISIBLE){
                   getVisibility(true,false,false);
               }
            }
        });

        btnInfoPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if(validateFullName() && validateEmail() && validatePhone()){

                    getVisibility(false,true,false);

                //}

            }
        });

        btnPassWordPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Handler handler = new Handler();
                if(!submitForm()) {
                    Util.hideSoftKeyboard(SignUpActivity.this,btnPassWordPanel);
                   // Util.showWaitIcon(SignUpActivity.this, wait_icon, null, true, true);
                    getVisibility(false,false,true);

                   final UserData user  =  new UserData();
                    user.setFullName(txtFullName.getText().toString());
                    user.setEmailAddress(txtEmailAddress.getText().toString());
                    user.setPhoneNumber(txtPhoneNumber.getText().toString());
                    user.setPassword(txtPassowrd.getText().toString());

                    if(Util.checkConnectivity(getApplicationContext())){


                        ConnectionController.signupMechanic(user, new okhttp3.Callback() {
                            @Override
                            public void onFailure(okhttp3.Call call, final IOException e) {

                                mActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        Toast.makeText(mActivity,e.getMessage(),Toast.LENGTH_LONG).show();
                                        wait_icon.setVisibility(View.INVISIBLE);
                                        passwordPanelLayout.setVisibility(View.VISIBLE);

                                    }
                                });


                            }

                            @Override
                            public void onResponse(okhttp3.Call call, final okhttp3.Response response) throws IOException {


                               final UserModel model = GetJSonObject(response.body().string());

                                mActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        if(model.getAuthInfo().getMessage().contains("Successfully")){

                                            Intent signIn =  new Intent(SignUpActivity.this,SignInActivtiy.class);
                                            startActivity(signIn);
                                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                                        }else {

                                            Toast.makeText(mActivity,model.getAuthInfo().getMessage(),Toast.LENGTH_LONG).show();
                                            wait_icon.setVisibility(View.INVISIBLE);
                                            passwordPanelLayout.setVisibility(View.VISIBLE);

                                        }
                                    }
                                });

//
                            }
                        });




                    }else {

                        Toast.makeText(SignUpActivity.this, "Internet connection is not available,kindly try again later.", Toast.LENGTH_LONG).show();
                    }

                }

            }
        });
    }




    public  UserModel  GetJSonObject(String jsonstr){
        final Gson gson = new Gson();
        UserModel user = new UserModel();
        user  = gson.fromJson(jsonstr, UserModel.class);
        return user;
    }


    private  void    getVisibility(boolean infoPanelFLag,boolean passwordPanelFlag,boolean waitIcon){

        if(infoPanelFLag){
            infoPanelLayout.setVisibility(View.VISIBLE);
            passwordPanelLayout.setVisibility(View.GONE);
            wait_icon.setVisibility(View.GONE);
            LayoutBehaviour.performAnimation(infoPanelLayout);
        }

        if(passwordPanelFlag){
            infoPanelLayout.setVisibility(View.GONE);
            passwordPanelLayout.setVisibility(View.VISIBLE);
            wait_icon.setVisibility(View.GONE);
            LayoutBehaviour.performAnimation(passwordPanelLayout);
        }

        if(waitIcon){

            infoPanelLayout.setVisibility(View.GONE);
            passwordPanelLayout.setVisibility(View.GONE);
            wait_icon.setVisibility(View.VISIBLE);
        }
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

                case R.id.input_layout_full_name:
                    validateFullName();
                    break;
                case R.id.input_layout_email_address:
                    validateEmail();
                    break;

                case R.id.input_layout_phone_number:
                    validatePhone();
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

        if (!validateFullName()) {
            cancel = true;
        }
        if (!validateEmail()) {
            cancel = true;
        }

        if (!validatePhone()) {
            cancel = true;
        }
        if (!validatePassword()) {
            cancel = true;
        }

        if(!validateComfirmPassword()){
            cancel = true;
        }
        return  cancel;
    }
    private boolean validateFullName() {
        if (txtFullName.getText().toString().trim().isEmpty()) {
            inputLayoutFullname.setError("Required");
            requestFocus(txtFullName);
            return false;
        } else {
            inputLayoutFullname.setErrorEnabled(false);
        }

        return true;
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

    private boolean validatePhone() {
        if (txtPhoneNumber.getText().toString().trim().isEmpty() ||txtPhoneNumber.getText().toString().trim().length() != 11 ) {
            inputLayoutPhoneNumber.setError("Enter valid phone number");
            requestFocus(txtPhoneNumber);
            return false;
        }else if(TextUtils.isEmpty(txtPhoneNumber.getText().toString().trim())){
            inputLayoutPhoneNumber.setError("Required");
            requestFocus(txtPhoneNumber);
            return false;
        } else {
            inputLayoutPhoneNumber.setErrorEnabled(false);
        }

        return true;
    }


    private boolean validateComfirmPassword() {
        if (!txtPassowrd.getText().toString().trim().equals(txtConfirmPassowrd.getText().toString().trim())){
            inputLayoutPassword.setError("Password mismatch ");
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


    public class Register extends AsyncTask<Void, Void, UserModel> {
       UserData userData;
        UserModel model =  null;
        public Register(UserData userData) {
            this.userData = userData;
        }

//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            wait_icon.setVisibility(View.VISIBLE);
//
//        }
        @Override
        protected UserModel doInBackground(Void... params) {
            String result = null;
            try {




            } catch (Exception ex) {


            }

            System.out.println("---------------- " + model.getAuthInfo());
            return model;
        }
        @Override
        protected void onPostExecute(final UserModel model) {

           if(model != null && model.getAuthInfo().getResponseCode() != "01"){
                if(model.getAuthInfo().getResponseCode() == "00"){

                    Toast.makeText(getApplicationContext(),model.getAuthInfo().getMessage(),Toast.LENGTH_LONG).show();


                }
            }else{



              // Toast.makeText(getApplicationContext(),model.getAuthInfo().getMessage(),Toast.LENGTH_LONG).show();

            }
        }

        @Override
        protected void onCancelled() {

        }
    }


}