package com.camsys.carmonic.mechanic.Dasboard;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.camsys.carmonic.mechanic.R;

import java.util.Calendar;


//



public class GenerateBillFragment extends AppCompatActivity {

    TextView txtDateTime  =  null;
    int  currentMonth ;
    int  currentYear ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_generate_bill);


        txtDateTime =  (TextView) findViewById(R.id.txtDateText);

        Calendar instance = Calendar.getInstance();
        currentMonth = instance.get(Calendar.MONTH);
        currentYear = instance.get(Calendar.YEAR);

    }



//    private  String  getFullName(int  month){
//
//        if(month == 1){
//            return "January"
//        }else if(month == 2){
//            return "Feb"
//        }else if(month == 2){
//            return "January"
//        }else if(month == 2) {
//            return "January"
//        }else if(month == 2){
//            return "January"
//        }else if(month == 2) {
//            return "January"
//        }else if(month == 2){
//            return "January"
//        }else if(month == 2){
//            return "January"
//
//        }else if(month == 2){
//            return "January"
//
//        }else if(month == 2) {
//            return "January"
//        }else if(month == 2){
//            return "January"
//        }
//    }

}
