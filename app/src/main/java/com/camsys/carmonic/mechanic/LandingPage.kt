package com.camsys.carmonic.mechanic

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.camsys.carmonic.mechanic.onboarding.SignInActivtiy
import com.camsys.carmonic.mechanic.onboarding.SignUpActivity
import kotlinx.android.synthetic.main.activity_landing_page.*

class LandingPage : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_page)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.statusBarColor = Color.TRANSPARENT

        btn_register.setOnClickListener {

            val intent = Intent(applicationContext, SignUpActivity::class.java)
            startActivity(intent)
        }



        btn_sign_in.setOnClickListener(View.OnClickListener {

            val intent = Intent(applicationContext, SignInActivtiy::class.java)
            startActivity(intent)

        })

    }
}
