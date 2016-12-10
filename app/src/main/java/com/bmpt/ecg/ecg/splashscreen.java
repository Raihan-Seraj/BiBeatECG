package com.bmpt.ecg.ecg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


/**
 * Created by Raihan on 10-Mar-16.
 */

/**
 * in this class we declare a splash screen to the app. We use custom animation xml in anim, which rotates a spinner
 */
public class splashscreen extends Activity {



    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);// layout of the splashscreen

        final ImageView iv = (ImageView) findViewById(R.id.spinnerimg);//background effect
        final Animation an = AnimationUtils.loadAnimation(getBaseContext(), R.anim.rotate);// annimation the spinner will rotate
        final Animation an2 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.abc_fade_out);// annimation for fading out
        iv.startAnimation(an);
        an.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                iv.startAnimation(an2);// starting the animation
                finish();
                Intent i = new Intent(getBaseContext(), patientform.class);//starting the login class
                startActivity(i);//starting activity
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
}