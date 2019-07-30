package com.clean.cause.cleancause.startup;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.clean.cause.cleancause.R;
import com.clean.cause.cleancause.SplashActivity;
import com.clean.cause.cleancause.main.ApplicationMain;

public class Login extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView registerTextView, forgotPasswordTextView;

    private CoordinatorLayout coordinatorLayout;
    Typeface fontRegular, fontLight;
    private static long back_pressed;
    @Override
    public void onBackPressed() {

        if (back_pressed + 2000 > System.currentTimeMillis()) {
            //super.onBackPressed();
            finishAffinity();
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
            back_pressed = System.currentTimeMillis();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //* Hides Notification Bar
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        fontLight = Typeface.createFromAsset(getAssets(), "avenirltstd_book_light.ttf");
        fontRegular = Typeface.createFromAsset(getAssets(), "avenirltstd_medium.ttf");


        emailEditText = (EditText) findViewById(R.id.input_user_email);
        passwordEditText = (EditText) findViewById(R.id.input_password);
        loginButton = (Button) findViewById(R.id.btn_login);
        registerTextView = (TextView) findViewById(R.id.signUp);
        forgotPasswordTextView = (TextView) findViewById(R.id.forgotPass);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.login_main);
        // set colour of the textboxs with different coloursat the botton
        forgotPasswordTextView.setText(fromHtml("<font color='#8bca3d'>Forgot Passsword? </font><font color='#ffffff'> Reset</font>"));
        registerTextView.setText(fromHtml("<font color='#8bca3d'>Not Registered? </font><font color='#000000'> Register with Us</font>"));

        // set password hint when no txt
        passwordEditText.setHint("PASSWORD");
        passwordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (passwordEditText.getText().length() > 0)
                    passwordEditText.setHint("");
                else
                    passwordEditText.setHint("PASSWORD");
            }
        });

        // remove keyboard when press outside the keyboard
        coordinatorLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                return true;
            }
        });

        // on login button click
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, ApplicationMain.class);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                finish();
                startActivity(intent);
            }
        });
        // register button click
        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                finish();
                startActivity(intent);
            }
        });
    }

    // to get colours using html css
    public static Spanned fromHtml(String html) {
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }
}
