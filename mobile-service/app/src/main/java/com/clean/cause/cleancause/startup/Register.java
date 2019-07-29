package com.clean.cause.cleancause.startup;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.clean.cause.cleancause.R;

public class Register extends AppCompatActivity {

    Typeface fontRegular, fontLight;

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //* Hides Notification Bar
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        fontLight = Typeface.createFromAsset(getAssets(), "avenirltstd_book_light.ttf");
        fontRegular = Typeface.createFromAsset(getAssets(), "avenirltstd_medium.ttf");
    }
}
