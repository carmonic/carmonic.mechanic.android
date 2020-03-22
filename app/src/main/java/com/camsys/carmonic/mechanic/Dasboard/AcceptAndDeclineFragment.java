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

import com.camsys.carmonic.mechanic.API.ConnectionController;
import com.camsys.carmonic.mechanic.MainActivity;
import com.camsys.carmonic.mechanic.Model.Customer;
import com.camsys.carmonic.mechanic.Model.Users;
import com.camsys.carmonic.mechanic.R;
import com.camsys.carmonic.mechanic.Utilities.Constants;
import com.camsys.carmonic.mechanic.Utilities.Util;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import okhttp3.OkHttpClient;

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
    TextView txtText ;


    MyDialogFragmentListener listener =  null;
    int minute;
    long min;
    Gson gson  =  null;
    Location mLastLocation =  null;
    double userLat,userLong;
    private Socket socket;





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
        txtText  = (TextView) dialog.findViewById(R.id.txtText);

        txtCustomerName =  (TextView) dialog.findViewById(R.id.txtCustomerName);
        txtDescription = (TextView)dialog.findViewById(R.id.txtDescription);
        txtRating = (TextView) dialog.findViewById(R.id.txtRating);

        txtCustomerName.setText(customer.getFirstname());
       // txtDescription.setText(customer.);
       String txtDesc =  txtDescription.getText().toString().replace("XXXX",customer.getFirstname()).replace("YYYY",Math.round(distance)+"");
       String txtTxt = txtText.getText().toString().replace("his", customer.getFirstname());

       txtText.setText(txtTxt);


       // String msg = String.format("A customer needs your help, %dkm away from your location", Math.round(distance));

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

                rejectRequestSocketConnection(userLat,userLong,mString);

                dialog.dismiss();

            }
        });

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                acceptRequestSocketConnection(userLat,userLong,mString);


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




    private void acceptRequestSocketConnection(double lat,double longitude,final String customer) {
        try {



            final Users mechanic = Util.GetUserObjectFromJson(getActivity());
            mechanic.setLatitude(lat);
            mechanic.setLongitude(longitude);

            OkHttpClient okHttpClient = ConnectionController.client;
            IO.setDefaultOkHttpWebSocketFactory(okHttpClient);
            IO.setDefaultOkHttpCallFactory(okHttpClient);
            IO.Options opts = new IO.Options();
            opts.callFactory = okHttpClient;
            opts.webSocketFactory = okHttpClient;
            socket = IO.socket(Constants.Base_URL, opts);
            socket.connect();
//            socket.emit(Constants.MECHANIC_ACCEPT_JOB,gson.toJson(mechanic),customer);
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {


                }
            }).on(Constants.MECHANIC_ACCEPT_JOB, new Emitter.Listener() {
                @Override
                public void call(Object... args) {

                    JSONObject jsonMechanic = (JSONObject) args[0];
                    String responseString  = jsonMechanic.toString();

                    System.out.println("-responseString-" + responseString);
                }
            });
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    listener.onReturnValue(true);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.toString());
        }
    }


    private void rejectRequestSocketConnection(double lat,double longitude,final String customer) {
        try {

            Users mechanic = Util.GetUserObjectFromJson(getActivity());
            mechanic.setLatitude(lat);
            mechanic.setLongitude(longitude);

            OkHttpClient okHttpClient = ConnectionController.client;
            IO.setDefaultOkHttpWebSocketFactory(okHttpClient);
            IO.setDefaultOkHttpCallFactory(okHttpClient);
            IO.Options opts = new IO.Options();
            opts.callFactory = okHttpClient;
            opts.webSocketFactory = okHttpClient;
            socket = IO.socket(Constants.Base_URL, opts);
            socket.connect();
            System.out.println("before - Rejecting");
            socket.emit(Constants.MECHANIC_REJECT_JOB, gson.toJson(mechanic),customer);
            System.out.println("after - Rejecting");
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    listener.onReturnValue(false);
                }
            });

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
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
