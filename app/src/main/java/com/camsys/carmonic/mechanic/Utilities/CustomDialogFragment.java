//package com.camsys.carmonic.mechanic.Utilities;
//
//import android.app.Dialog;
//import android.content.DialogInterface;
//import android.graphics.drawable.ColorDrawable;
//import android.graphics.drawable.Drawable;
//import android.graphics.drawable.GradientDrawable;
//import android.graphics.drawable.StateListDrawable;
//import android.os.Bundle;
//
//import android.text.TextUtils;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//import androidx.annotation.ColorRes;
//import androidx.annotation.DrawableRes;
//import androidx.appcompat.app.AlertDialog;
//import androidx.cardview.widget.CardView;
//import androidx.core.content.ContextCompat;
//import androidx.fragment.app.DialogFragment;
//import androidx.fragment.app.FragmentActivity;
//import com.camsys.carmonic.mechanic.R;
//
///**
// * Created by Francis on 4/25/2016.
// */
//public class CustomDialogFragment extends DialogFragment {
//    private AlertDialog dialog;
//
//    private ResultType type;
//    private CharSequence title;
//    private CharSequence description;
//    private Action primaryAction;
//    private Action secondaryAction;
//    private Action tertiaryAction;
//
//    private View view;
//    private CardView backgroundView;
//    private FragmentActivity activity;
//
//    private Button primary;
//    private Button secondary;
//    private Button tertiary;
//
//    private ImageView iconView;
//
//    private TextView titleView;
//    private TextView descriptionView;
//
//    private Integer backgroundColor;
//
//    private StateListDrawable backgroundDrawable;
//
//    public CustomDialogFragment() {
//
//    }
//
//    public static CustomDialogFragment newInstance(ResultType type,
//                                                   CharSequence title,
//                                                   CharSequence description,
//                                                   Action primaryAction,
//                                                   Action secondaryAction,
//                                                   Action tertiaryAction,
//                                                   Integer backgroundColor) {
//        if(type == null)
//            throw new NullPointerException("type");
//
//        if(title == null)
//            throw new NullPointerException("title");
//
//        if(primaryAction == null)
//            throw new NullPointerException("primaryAction");
//
//        CustomDialogFragment resultDialogFragment = new CustomDialogFragment();
//        resultDialogFragment.type = type;
//        resultDialogFragment.title = title;
//        resultDialogFragment.description = description;
//        resultDialogFragment.primaryAction = primaryAction;
//        resultDialogFragment.secondaryAction = secondaryAction;
//        resultDialogFragment.tertiaryAction = tertiaryAction;
//        resultDialogFragment.backgroundColor = backgroundColor;
//
//        return resultDialogFragment;
//    }
//
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        if(dialog != null)
//            return dialog;
//
//        activity = getActivity();
//        LayoutInflater inflater = activity.getLayoutInflater();
//
//        view = inflater.inflate(R.layout.fragment_result_dialog, null, false);
//        setUpView();
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()); //getContext());
//        builder.setView(view);
//
//        dialog = builder.create();
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        dialog.setCancelable(false);
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//            @Override
//            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                // Prevent dialog close on back press button
//                return keyCode == KeyEvent.KEYCODE_BACK;
//            }
//        });
//        return dialog;
//    }
//
//    public AlertDialog getAlertDialog() {
//        return dialog;
//    }
//
//    private void setUpView(){
//        if(view == null || activity == null)
//            return;
//
//        if(backgroundView == null)
//            backgroundView = (CardView)view.findViewById(R.id.result_card_layout);
//
//        if(backgroundColor == null)
//            backgroundColor = getResources().getColor(type.getColorResId()); //ContextCompat.getColor(activity, type.getColorResId());
//
//        //set the background color
//        backgroundView.setCardBackgroundColor(backgroundColor);
//
//        if(iconView == null)
//            iconView = (ImageView) backgroundView.findViewById(R.id.result_icon);
//
//        if(titleView == null)
//            titleView = (TextView) backgroundView.findViewById(R.id.result_title);
//
//        if(descriptionView == null)
//            descriptionView = (TextView) backgroundView.findViewById(R.id.result_description);
//
//        if(primary == null)
//            primary = (Button) backgroundView.findViewById(R.id.result_primary);
//
//        if(secondary == null)
//            secondary = (Button) backgroundView.findViewById(R.id.result_secondary);
//
//        if(tertiary == null)
//            tertiary = (Button) backgroundView.findViewById(R.id.result_tertiary);
//
//        //set icon
//        iconView.setImageResource(type.getIconResId());
//
//        //set title
//        titleView.setText(title);
//
//        if(!TextUtils.isEmpty(description)){
//            descriptionView.setText(description);
//            descriptionView.setVisibility(View.VISIBLE);
//        }else{
//            descriptionView.setVisibility(View.GONE);
//        }
//
//        //set primary action
//        primary.setText(primaryAction.getText());
//        primary.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //dismiss dialog
//                dialog.dismiss();
//
//                //call listener
//                View.OnClickListener listener = primaryAction.getOnClickListener();
//                if(listener != null){
//                    listener.onClick(v);
//                }
//            }
//        });
//        primary.setTextColor(backgroundColor);
//
//        if(secondaryAction != null){
//            secondary.setText(secondaryAction.getText());
//            secondary.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dialog.dismiss();
//                    //call listener
//                    View.OnClickListener listener = secondaryAction.getOnClickListener();
//                    if(listener != null){
//                        listener.onClick(v);
//                    }
//                }
//            });
//            setNoFillBackground(secondary);
//            secondary.setVisibility(View.VISIBLE);
//        }else{
//            secondary.setVisibility(View.GONE);
//        }
//
//        if(tertiaryAction != null){
//            tertiary.setText(tertiaryAction.getText());
//            tertiary.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dialog.dismiss();
//                    //call listener
//                    View.OnClickListener listener = tertiaryAction.getOnClickListener();
//                    if(listener != null){
//                        listener.onClick(v);
//                    }
//                }
//            });
//            setNoFillBackground(tertiary);
//            tertiary.setVisibility(View.VISIBLE);
//        }else{
//            tertiary.setVisibility(View.GONE);
//        }
//    }
//
//    private void setNoFillBackground(View onView){
//        try{
//            if(backgroundDrawable == null){
//                backgroundDrawable = (StateListDrawable) ContextCompat
//                        .getDrawable(getActivity(), R.drawable.large_button_no_fill)
//                        //.getConstantState()
//                        //.newDrawable()
//                        .mutate();
//                Drawable[] drawables = Utilities.getGradientDrawablesFrom(backgroundDrawable);
//
//                GradientDrawable lastDrawable = (GradientDrawable)drawables[1];
//                lastDrawable.mutate(); // = (GradientDrawable) lastDrawable.getConstantState().newDrawable().mutate();
//                lastDrawable.setColor(backgroundColor);
//
//                drawables[1] = lastDrawable;
//            }
//
//            onView.setBackground(backgroundDrawable);
//        }catch (Exception e){
//
//        }
//    }
////
////    public enum ResultType{
////        success("Success!", R.color.success, R.mipmap.ic_success),
////        failure("Error!", R.color.error, R.mipmap.ic_failure),
////        info("Attention!", R.color.light_blue, R.mipmap.ic_info);
////
////        int colorResId;
////        int iconResId;
////        String text;
////
////        ResultType(String text, @ColorRes int colorResId, @DrawableRes int iconResId){
////            this.colorResId = colorResId;
////            this.iconResId = iconResId;
////            this.text = text;
////        }
////
////        public String getText() {
////            return text;
////        }
////
////        public int getColorResId() {
////            return colorResId;
////        }
////
////        public int getIconResId() {
////            return iconResId;
////        }
////    }
//
//    public interface Action{
//        String getText();
//        View.OnClickListener getOnClickListener();
//    }
//}
