package com.camsys.carmonic.mechanic.Dasboard;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.camsys.carmonic.mechanic.API.ConnectionController;
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

public class OrderDetailFragment extends BottomSheetDialogFragment {

    String mString;
    Activity activtiy;
    private BottomSheetBehavior mBehavior;
    Button btnAccept =  null;
    Button btnDecline =  null;
    TextView cancel_action= null;
    TextView txtTimer =  null;
    MyDialogFragmentListener listener =  null;
    int minute;
    long min;

    Gson  gson = null;
    Users user   = null;
    private Socket socket;


    TextView txtCustomer;
    TextView txtDescription;
    TextView txtMoreNote;
    TextView txtNote;





    public static OrderDetailFragment newInstance(String string, MyDialogFragmentListener listener) {
        OrderDetailFragment f = new OrderDetailFragment(listener);
        Bundle args = new Bundle();
        args.putString("string", string);

        f.setArguments(args);
        return f;
    }

    public OrderDetailFragment(MyDialogFragmentListener listener) {
        this.listener= listener;
    }



    public void showOperationBill(final Activity activity, final String  customerJSON) {

        final Users customer = gson.fromJson(customerJSON, Users.class);

        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.fragment_result_dialog);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.7f;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        LinearLayout layout = dialog.findViewById(R.id.root);
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        layout.setLayoutParams(params);

        TextView txtTopic = (TextView) dialog.findViewById(R.id.txtTopic);
        TextView cancelButton = (TextView) dialog.findViewById(R.id.cancel_button);
        TextView txtNotificationText = (TextView) dialog.findViewById(R.id.txtNotification);
        TextView txtSeeDetail = (TextView) dialog.findViewById(R.id.txtSeeDetail);
        TextView txtDecline = (TextView) dialog.findViewById(R.id.txtDecline);

        txtTopic.setVisibility(View.VISIBLE);
        cancelButton.setVisibility(View.GONE);

        txtTopic.setText("Thanks for helping out!");


        String msg = String.format("%s is waiting %s", customer.getFirstname(), Util.GetAddressFromLatLong(getContext(), customer.getLatitude(), customer.getLongitude()));

        txtNotificationText.setText("Would you like to generate the customer’s bill?");    //("Fikayo is waiting for you at 789 Oriole Pkway");
        txtNotificationText.setTextColor(getResources().getColor(R.color.dialog_text));
        txtTopic.setTextColor(getResources().getColor(R.color.colorYellow));
        txtDecline.setTextColor(getResources().getColor(R.color.dialog_text));

        txtDecline.setText("Yes,generate bill");
        txtSeeDetail.setText("No,see order summary");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        txtSeeDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent  =  new Intent(getActivity(),ThankYouActivity.class);
                startActivity(intent);
            }
        });

        txtDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             Intent  intent  =  new Intent(getActivity(),GenerateBillFragment.class);
             startActivity(intent);


            }
        });
        dialog.show();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mString = getArguments().getString("string");
        gson  =  new Gson();
        user = gson.fromJson(mString, Users.class);
//        if(user == null){
//            dismiss();
//        }

        System.out.println("---------------onCreate------------  " + user.getFirstname().toString());
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
        System.out.println("---------------onCreateDialog------------");
        final BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.activity_order_detail, null);
        dialog.setContentView(view);


        LinearLayout linearLayout = view.findViewById(R.id.root);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
        params.height = getScreenHeight();
        linearLayout.setLayoutParams(params);

        txtCustomer = (TextView) dialog.findViewById(R.id.txtCustomerName);
        txtMoreNote = (TextView) dialog.findViewById(R.id.txtMoreNote);
        txtNote = (TextView) dialog.findViewById(R.id.txtNote);
        txtDescription = (TextView) dialog.findViewById(R.id.txtDescription);

        txtCustomer.setText(user.getFirstname());

        String address = Util.GetAddressFromLatLong(getContext(), user.getLatitude(), user.getLongitude());


        String  locationDesc =  getResources().getString(R.string.location_descr);
        locationDesc =locationDesc.replace("XXX",user.getFirstname()).replace("YYY",address);
        txtDescription.setText(locationDesc);


        Spinner spinner  = (Spinner) dialog.findViewById(R.id.dropdown);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
              //  setupSocket(item,mString);
                moqStatus(item,mString);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mBehavior = BottomSheetBehavior.from((View) view.getParent());
        return dialog;

    }


    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public interface MyDialogFragmentListener {
        void onReturnValue(boolean indicator);
    }


    private void setupSocket(final String status,final String customerJSON) {
        try {
            OkHttpClient okHttpClient = ConnectionController.client;
            IO.setDefaultOkHttpWebSocketFactory(okHttpClient);
            IO.setDefaultOkHttpCallFactory(okHttpClient);
            IO.Options opts = new IO.Options();
            opts.callFactory = okHttpClient;
            opts.webSocketFactory = okHttpClient;
            socket = IO.socket(Constants.Base_URL, opts);
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    Users mechanic = Util.GetUserObjectFromJson(getActivity());

                    Toast.makeText(activtiy, status, Toast.LENGTH_SHORT).show();

                    if(status.equalsIgnoreCase("Arrived")){
                        socket.emit(Constants.MECHANIC_START_JOB, gson.toJson(mechanic),customerJSON);

                    }else if(status.equalsIgnoreCase("In Progress")){
                        socket.emit(Constants.MECHANIC_IN_PROGRESS_JOB, gson.toJson(mechanic),customerJSON);

                    }else if(status.equalsIgnoreCase("Completed")) {
                        socket.emit(Constants.MECHANIC_COMPLETE_JOB, gson.toJson(mechanic),customerJSON);

                       // Move to Thank you  page
                        showOperationBill(getActivity(),customerJSON);

                        Toast.makeText(activtiy, status, Toast.LENGTH_SHORT).show();

                    }
            }

            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {

                    System.out.println("========= 1 ===========");
                }

            });
            socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void  moqStatus(String status,String  customerJSON){
        if(status.equalsIgnoreCase("Arrived")){


        }else if(status.equalsIgnoreCase("In Progress")){


        }else if(status.equalsIgnoreCase("Completed")) {

            // Move to Thank you  page
            showOperationBill(getActivity(),customerJSON);

            //Toast.makeText(activtiy, status, Toast.LENGTH_SHORT).show();

        }
    }


}
