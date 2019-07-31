package com.clean.cause.cleancause.startup;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.clean.cause.cleancause.R;

public class Register extends AppCompatActivity {

    Typeface fontRegular, fontLight;
    EditText r_Password;
    TextView r_IndividualUser,r_RotaractClub,r_Company;
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
        r_Password = (EditText) findViewById(R.id.r_password);
        r_IndividualUser = (TextView) findViewById(R.id.r_individual_user);
        r_RotaractClub = (TextView) findViewById(R.id.r_rotaract_club);
        r_Company = (TextView) findViewById(R.id.r_company);


        // set password hint when no txt
        r_Password.setHint("PASSWORD");
        r_Password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (r_Password.getText().length() > 0)
                    r_Password.setHint("");
                else
                    r_Password.setHint("PASSWORD");
            }
        });
    }
}
