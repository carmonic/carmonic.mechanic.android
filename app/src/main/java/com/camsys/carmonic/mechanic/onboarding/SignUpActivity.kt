package com.camsys.carmonic.mechanic.onboarding

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import com.camsys.carmonic.mechanic.R

import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlin.math.log

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        val infoPanel = findViewById(R.id.infoPanel) as LinearLayout
        val passwordPanel = findViewById(R.id.passwordPanel) as LinearLayout


        backbutton.setOnClickListener(View.OnClickListener {
            showInfoPanel(infoPanel,passwordPanel)
        })
        btn_sign_up.setOnClickListener {

            var btnSignInText = btn_sign_up.text
            println( btnSignInText)



            if(btnSignInText == "Next" ){

                showInfoPanel(infoPanel,passwordPanel)

            }else{

                showInfoPanel(infoPanel,passwordPanel)

            }

        }

    }

    fun showInfoPanel(view1:View,view2:View) {
         if (view1.visibility == View.VISIBLE  ){

             view1.visibility = View.INVISIBLE
             view2.visibility = View.VISIBLE
             btn_sign_up.text = "Submit"

        } else if (view2.visibility == View.VISIBLE ){

             view1.visibility = View.VISIBLE
             view2.visibility = View.INVISIBLE
             btn_sign_up.text = "Next"
        }
    }

    fun showPasswordPanel(view:View) {
         if (view.visibility == View.VISIBLE){
             view.visibility =  View.VISIBLE
        } else{
             view.visibility =  View.INVISIBLE
        }
    }




}
