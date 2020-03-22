package com.camsys.carmonic.mechanic.Dasboard;


import android.app.Dialog;
import android.content.res.Resources;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.camsys.carmonic.mechanic.Model.Users;
import com.camsys.carmonic.mechanic.R;
import com.camsys.carmonic.mechanic.Utilities.Util;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;

public class CustomerDetailFragment extends BottomSheetDialogFragment {

    String mString;
    private BottomSheetBehavior mBehavior;

    ImageView cancel_action= null;
    ImageView imgBook = null;
    Gson gson =  null;
    Users user = null;

    TextView txtSourceLocation;
    TextView txtDestinationLocation;
    TextView txtCustomerName;
    TextView  txtExtraNote;
    TextView txtNote;
    TextView  txtTextHeader ;


    public static CustomerDetailFragment newInstance(String string) {
        CustomerDetailFragment f = new CustomerDetailFragment();
        Bundle args = new Bundle();
        args.putString("string", string);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mString = getArguments().getString("string");
        gson  =  new Gson();
        user = gson.fromJson(mString,Users.class);
        if(user == null){
            dismiss();
        }

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
        View view = View.inflate(getContext(), R.layout.activity_customer_location, null);
        dialog.setContentView(view);

        LinearLayout linearLayout = view.findViewById(R.id.root);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
        params.height = getScreenHeight();
        linearLayout.setLayoutParams(params);

       txtCustomerName =  (TextView) dialog.findViewById(R.id.txtCustomerName);
       txtDestinationLocation = (TextView)dialog.findViewById(R.id.txtCustomerLocation);
       txtSourceLocation = (TextView)dialog.findViewById(R.id.txtSourceLocation);
       txtExtraNote = (TextView)dialog.findViewById(R.id.txtExtraNote);
       txtNote = (TextView)dialog.findViewById(R.id.txtNote);
       txtTextHeader =  (TextView)dialog.findViewById(R.id.txtTextHeader);
       txtCustomerName.setText(user.getFirstname());

        String  customerLocation  =  Util.GetAddressFromLatLong(getContext(), user.getLatitude(), user.getLongitude());
        txtDestinationLocation.setText(customerLocation);

        String  descNote  =  getResources().getString(R.string.location_descr).replace("XXX",user.getFirstname()).replace("YYY",Util.GetAddressFromLatLong(getContext(), user.getLatitude(), user.getLongitude()));

        txtNote.setText(descNote);
        String textHeader = txtTextHeader.getText().toString().replace("FIKAYO",user.getFirstname());



        cancel_action = (ImageView) dialog.findViewById(R.id.close);
        imgBook = (ImageView) dialog.findViewById(R.id.book);

        cancel_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        imgBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        mBehavior = BottomSheetBehavior.from((View) view.getParent());
        return dialog;

    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public interface MyDialogFragmentListener {
        void onReturnValue(String foo);
    }
}
