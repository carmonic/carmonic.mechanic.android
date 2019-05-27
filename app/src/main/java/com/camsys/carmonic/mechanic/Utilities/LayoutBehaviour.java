package com.camsys.carmonic.mechanic.Utilities;

import android.view.View;
import android.view.animation.*;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class LayoutBehaviour {




    public static void performAnimationRelativeLayout(RelativeLayout viewToAnimate) {
        AnimationSet set = new AnimationSet(true);
        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(500);
        set.addAnimation(animation);
        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(500);
        set.addAnimation(animation);

        LayoutAnimationController controller = new LayoutAnimationController(
                set, 0.5f);
        //RelativeLayout loading = (RelativeLayout) findViewById(R.id.loading);
        viewToAnimate.setVisibility(View.VISIBLE);
        viewToAnimate.setLayoutAnimation(controller);
    }
    public static void performAnimation(LinearLayout viewToAnimate) {
        AnimationSet set = new AnimationSet(true);
        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(500);
        set.addAnimation(animation);
        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(500);
        set.addAnimation(animation);
        LayoutAnimationController controller = new LayoutAnimationController(
                set, 0.5f);
        //RelativeLayout loading = (RelativeLayout) findViewById(R.id.loading);
        viewToAnimate.setVisibility(View.VISIBLE);
        viewToAnimate.setLayoutAnimation(controller);
    }

}
