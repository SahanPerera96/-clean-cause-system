package com.clean.cause.cleancause;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.clean.cause.cleancause.startup.Login;
import com.clean.cause.cleancause.startup.WelcomePage;

public class SplashActivity extends AppCompatActivity {

    Boolean hasRun;
    TextView loadText;
    TextView versionNo;
    ImageView rotateImage;
    private String loggedFlag;
    ViewFlipper flipperImage, flipperText;
    Animation fade_in, fade_out;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        //* Hides Notification Bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        loadText = findViewById(R.id.textView2);

        rotateImage = findViewById(R.id.imageCircle);
        versionNo = findViewById(R.id.version_Name);
        final String version = "v." + BuildConfig.VERSION_NAME ;
        Log.e("App version ---->", version);
        versionNo.setText(version);
        final Animation rotate_animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);


        rotate_animation.reset();
        rotate_animation.setDuration(1000);
        rotateImage.clearAnimation();
        rotateImage.startAnimation(rotate_animation);


        Thread thread = new Thread() {

            @Override
            public void run() {

                try {

                    int waited = 0;
                    while (waited < 1000) {
                        sleep(100);
                        waited += 100;
                    }
                    Intent i = new Intent(SplashActivity.this, Login.class);
                    //i.putExtra("data", result);
                    startActivity(i);
//                    overridePendingTransition(R.anim.push_right_out, R.anim.push_right_in);
                    finish();

                } catch (InterruptedException e) {

                } finally {
//                    SplashActivity.this.finish();
                }
            }
        };
        thread.start();
    }
}
