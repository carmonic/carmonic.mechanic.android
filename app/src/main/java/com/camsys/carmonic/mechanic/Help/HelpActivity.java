package com.camsys.carmonic.mechanic.Help;

import android.content.Context;
import android.os.Bundle;

import com.camsys.carmonic.mechanic.Profile.ProfileActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;

import com.camsys.carmonic.mechanic.R;

public class HelpActivity extends AppCompatActivity {

    Context mContext =  null;
    private   Toolbar toolbar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_help);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        mContext =  HelpActivity.this;

        TextView txtTitle  =  (TextView) toolbar.findViewById(R.id.txtTitle);
        txtTitle.setText("Help");


        AppCompatImageButton button  =  (AppCompatImageButton) toolbar.findViewById(R.id.appBackButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
