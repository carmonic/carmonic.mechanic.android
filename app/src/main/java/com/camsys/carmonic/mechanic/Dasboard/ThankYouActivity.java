package com.camsys.carmonic.mechanic.Dasboard;

import android.content.Intent;
import android.os.Bundle;

import com.camsys.carmonic.mechanic.MainActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import com.camsys.carmonic.mechanic.R;

public class ThankYouActivity extends AppCompatActivity {


    AppCompatButton btnDone ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_thank_you);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnDone  = (AppCompatButton) findViewById(R.id.btnDone);


        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent  =  new  Intent(ThankYouActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

}
