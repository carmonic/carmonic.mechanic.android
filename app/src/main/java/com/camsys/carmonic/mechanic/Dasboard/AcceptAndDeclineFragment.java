package com.camsys.carmonic.mechanic.Dasboard;


import android.app.Activity;
import android.app.Dialog;

import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.camsys.carmonic.mechanic.MainActivity;
import com.camsys.carmonic.mechanic.Model.Customer;
import com.camsys.carmonic.mechanic.Model.Users;
import com.camsys.carmonic.mechanic.R;
import com.camsys.carmonic.mechanic.Utilities.Util;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;

public class AcceptAndDeclineFragment extends BottomSheetDialogFragment {

    String mString;
    Activity activtiy;
    private BottomSheetBehavior mBehavior;
    Button btnAccept =  null;
    Button btnDecline =  null;
    TextView cancel_action= null;
    TextView txtTimer =  null;

    TextView txtCustomerName;
    TextView txtRating;
    TextView txtDescription;


    MyDialogFragmentListener listener =  null;
    int minute;
    long min;
    Gson gson  =  null;
    Location mLastLocation =  null;
    double userLat,userLong;





    public static AcceptAndDeclineFragment newInstance(String string,double userLat,double userLong, MyDialogFragmentListener listener) {
        AcceptAndDeclineFragment f = new AcceptAndDeclineFragment(listener);
        Bundle args = new Bundle();
        args.putString("string", string);
        args.putDouble("userLat",userLat);
        args.putDouble("userLong",userLong);

        f.setArguments(args);
        return f;
    }

    public AcceptAndDeclineFragment (MyDialogFragmentListener listener) {
        this.listener= listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mString = getArguments().getString("string");
        userLat = getArguments().getDouble("userLat");
        userLong = getArguments().getDouble("userLong");
        gson =  new Gson();


    }

    @Override
    public void onStart() {
        super.onStart();
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //final Dialog dialog = (Dialog) super.onCreateDialog(savedInstanceState);
        final BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.activity_customer_detail, null);
        dialog.setContentView(view);

        LinearLayout linearLayout = view.findViewById(R.id.root);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
        params.height = getScreenHeight();
        linearLayout.setLayoutParams(params);

        //Customer customer1 = gson.fromJson(mString,Customer.class);
        Users customer = gson.fromJson(mString, Users.class);



        double distance  =  Util.distance(customer.getLatitude(),customer.getLongitude(),userLat,userLong);


        txtTimer = (TextView) dialog.findViewById(R.id.txtTimer);
        btnAccept = (Button) dialog.findViewById(R.id.btnAccept);
        btnDecline = (Button) dialog.findViewById(R.id.btnDecline);
        cancel_action = (TextView) dialog.findViewById(R.id.cancel_action);


        txtCustomerName =  (TextView) dialog.findViewById(R.id.txtCustomerName);
        txtDescription = (TextView)dialog.findViewById(R.id.txtDescription);
        txtRating = (TextView) dialog.findViewById(R.id.txtRating);

        txtCustomerName.setText(customer.getFirstname());
       // txtDescription.setText(customer.);
       String txtDesc =  txtDescription.getText().toString().replace("XXXX",customer.getFirstname()).replace("YYYY",Math.round(distance)+"");

        txtDescription.setText(txtDesc);
        cancel_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onReturnValue(false);
                dialog.dismiss();

            }
        });

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               listener.onReturnValue(true);
                dialog.dismiss();

            }
        });


        min= 10*60*1000;
        counter(min,dialog);
        if(min == 0){

            dismiss();
        }

        mBehavior = BottomSheetBehavior.from((View) view.getParent());
        return dialog;

    }

    private void counter(long min,final Dialog dialog) {
        CountDownTimer timer = new CountDownTimer(min, 1000) {
            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000) % 60;
                int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
                int hours = (int) ((millisUntilFinished / (1000 * 60 * 60)) % 24);
                txtTimer.setText(String.format("%d:%d:%d", hours, minutes, seconds));
            }
            public void onFinish() {
               dialog.dismiss();
            }
        };
        timer.start();
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public interface MyDialogFragmentListener {
        void onReturnValue(boolean indicator);
    }
}
