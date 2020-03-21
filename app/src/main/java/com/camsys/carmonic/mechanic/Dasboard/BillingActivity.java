package com.camsys.carmonic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

import com.camsys.carmonic.networking.BackEndDAO;
import com.camsys.carmonic.principals.User;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class BillingActivity extends AppCompatActivity {

    int mechanicId;
    int starRating;
    String feedback;
    String compliment;
    SeekBar seekBar;
    TextView starRatingValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing_main);
        starRatingValue = findViewById(R.id.starRatingValue);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        seekBar = findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(seekBarChangeListener);

        mechanicId = getIntent().getIntExtra("mechanicId", -1);
        feedback = getIntent().getStringExtra("feedback");
        compliment = getIntent().getStringExtra("compliment");
    }

    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // updated continuously as the user slides the thumb
            starRating = progress;
            BillingActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    starRatingValue.setText(String.valueOf(progress));
                }
            });
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    };

    public void onclick_bill_done(View view) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        User user = gson.fromJson(preferences.getString("User", ""), User.class);

        BackEndDAO.postFeedback(starRating, compliment, feedback, String.valueOf(user.getId()), String.valueOf(mechanicId), user.getToken(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Intent i = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(i);
            }
        });
    }

    public void onclick_edit_complmt(View view) {
        Intent i = new Intent(getApplicationContext(), FeedbackActivity.class);
        i.putExtra("mechanicId", mechanicId);
        startActivity(i);
    }
}
