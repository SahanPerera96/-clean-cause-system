package com.clean.cause.cleancause.startup;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.clean.cause.cleancause.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Arrays;

public class Register extends AppCompatActivity implements OnMapReadyCallback {

    Typeface fontRegular, fontLight;
    CheckedTextView r_IndividualUser,r_RotaractClub,r_Company;
    TextView nameTextview, emailTextview, contactNoTextview, passwordTextview,backToLogin;
    EditText nameEditText, emailEditText, numberEditText, passwordEditText, districtEditText;
    LinearLayout rotaractClubView, typeView, mapSelectorLoadClickView;
    CoordinatorLayout registerMainLayout;
    Button signUpButton, addLocationButton;
    Button closeMapIcon;
    RelativeLayout mapSelectorLayout;
    SwipeRefreshLayout registerSwipeRefreshLayout;
    String latitude = "";
    String longitude = "";
    String type = "";

    private GoogleMap viewMap, SelectionMap;
    int count = 0;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private GoogleApiClient mGoogleApiClient;
    Marker mCurrLocationMarker;
    private static final String TAG = "RegisterActivity";
    private Boolean mLocationPermissionsGranted = false;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));
    boolean gps_enabled = false;
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

        r_IndividualUser = (CheckedTextView) findViewById(R.id.r_individual_user);
        r_RotaractClub = (CheckedTextView) findViewById(R.id.r_rotaract_club);
        r_Company = (CheckedTextView) findViewById(R.id.r_company);
        nameTextview = (TextView) findViewById(R.id.r_name_txtview);
        emailTextview = (TextView) findViewById(R.id.r_email_txtview);
        contactNoTextview = (TextView) findViewById(R.id.r_contact_number_txtview);
        passwordTextview = (TextView) findViewById(R.id.r_password_txtview);
        rotaractClubView = (LinearLayout) findViewById(R.id.r_rot_view);
        typeView = (LinearLayout) findViewById(R.id.r_type_selector_view);
        backToLogin = (TextView) findViewById(R.id.r_back_to_login);
        registerMainLayout =(CoordinatorLayout) findViewById(R.id.register_main);
        nameEditText = (EditText) findViewById(R.id.r_name);
        emailEditText = (EditText) findViewById(R.id.r_email_address);
        numberEditText = (EditText) findViewById(R.id.r_contact_number);
        passwordEditText = (EditText) findViewById(R.id.r_password);
        districtEditText = (EditText) findViewById(R.id.r_rot_district);
        mapSelectorLoadClickView = (LinearLayout) findViewById(R.id.r_mapLayout_click);
        signUpButton = (Button) findViewById(R.id.r_btn_register);
        addLocationButton = (Button) findViewById(R.id.r_btn_map_confirm);
        closeMapIcon = (Button) findViewById(R.id.r_close_map_selector);
        mapSelectorLayout = (RelativeLayout) findViewById(R.id.r_map_selector_relative);
        registerSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.r_swipe_refresh);

        typeView.setVisibility(View.INVISIBLE);
        rotaractClubView.setVisibility(View.GONE);
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
        backToLogin.setText(fromHtml("<font color='#8bca3d'>Already have an account? </font><font color='#000000'> Sign In</font>"));
        registerSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (checkInternetConnection(Register.this)) {
                    // when start load gmaps
                    getLocationPermission();
                    initMap();
                    registerSwipeRefreshLayout.setRefreshing(false);

                } else {

                    registerSwipeRefreshLayout.setRefreshing(false);
                }

            }
        });
        // when start load gmaps
        getLocationPermission();
        initMap();

        // all onclick events are called here
        OnclickEventsHandler();

        // on type selection
        typeButtonClickEvents();
    }

    // OnclickEvents
    public void OnclickEventsHandler(){
        // on touch or click the map view
        mapSelectorLoadClickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mapSelectorLayout.setVisibility(View.VISIBLE);
            }
        });
        // on click close on map location selector
        closeMapIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapSelectorLayout.setVisibility(View.GONE);
            }
        });
        // on click confirm button on map location selector
        addLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapSelectorLayout.setVisibility(View.GONE);
            }
        });

        // on click sign up button
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        registerMainLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                return true;
            }
        });
    }

    // type button click
    public void typeButtonClickEvents() {

        r_IndividualUser.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                clickType(view);
                r_RotaractClub.setTextColor(getResources().getColor(R.color.gradient_light_green1));
//                r_RotaractClub.setBackground(getDrawable(R.drawable.button_only_border_one));
                r_RotaractClub.setActivated(false);
                r_Company.setTextColor(getResources().getColor(R.color.gradient_light_green1));
//                r_Company.setBackground(getDrawable(R.drawable.button_only_border_one));
                r_Company.setActivated(false);
                rotaractClubView.setVisibility(View.GONE);
                nameTextview.setText("Name of user");
                emailTextview.setText("User email address");
                contactNoTextview.setText("User contact number");
                passwordTextview.setText("Account password");
                typeView.setVisibility(View.VISIBLE);


            }
        });
        r_RotaractClub.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                clickType(v);
                r_IndividualUser.setTextColor(getResources().getColor(R.color.gradient_light_green1));
//                r_IndividualUser.setBackground(getDrawable(R.drawable.button_only_border_one));
                r_IndividualUser.setActivated(false);
                r_Company.setTextColor(getResources().getColor(R.color.gradient_light_green1));
//                r_Company.setBackground(getDrawable(R.drawable.button_only_border_one));
                r_Company.setActivated(false);
                nameTextview.setText("Name of rotaract club");
                emailTextview.setText("Rotaract club email address");
                contactNoTextview.setText("Rotaract club contact number");
                passwordTextview.setText("Account password");
                rotaractClubView.setVisibility(View.VISIBLE);
                typeView.setVisibility(View.VISIBLE);
                // when start load gmaps
                getLocationPermission();
                initMap();
            }
        });
        r_Company.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                clickType(v);
                r_IndividualUser.setTextColor(getResources().getColor(R.color.gradient_light_green1));
//                r_IndividualUser.setBackground(getDrawable(R.drawable.button_only_border_one));
                r_IndividualUser.setActivated(false);
                r_RotaractClub.setTextColor(getResources().getColor(R.color.gradient_light_green1));
//                r_RotaractClub.setBackground(getDrawable(R.drawable.button_only_border_one));
                r_RotaractClub.setActivated(false);
                nameTextview.setText("Name of company");
                emailTextview.setText("Company email address");
                contactNoTextview.setText("Company contact number");
                passwordTextview.setText("Account password");
                rotaractClubView.setVisibility(View.GONE);
                typeView.setVisibility(View.VISIBLE);
            }
        });

    }

    // check change colour and background make modifications on the ui on click type
    public void clickType(View v) {

        if (v.isActivated()) {
            v.setBackgroundResource(R.drawable.button_only_border_one);
            ((TextView) v).setTextColor(ContextCompat.getColor(this, R.color.gradient_light_green1));
            v.setActivated(false);
        } else {
            v.setActivated(true);
            v.setBackgroundResource(R.drawable.button_only_border_two);
            ((TextView) v).setTextColor(ContextCompat.getColor(this, R.color.white_font));
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


        viewMap = googleMap;
        SelectionMap = googleMap;
        count++;
        if (count == 1) {
            Log.e(TAG, count + "  ...........................................");
            if (latitude.equals("") || longitude.equals("") ) {
                Log.e(TAG, "  nill...........................................");
                // check if location is enabled
                gps_enabled = isLocationEnabled(this);
                if (gps_enabled) {
                    if (mLocationPermissionsGranted) {
                        getDeviceLocation();

                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        viewMap.setMyLocationEnabled(true);
                        viewMap.getUiSettings().setMyLocationButtonEnabled(false);


                    }
                } else {
                Toast.makeText(this, "Location OFF", Toast.LENGTH_SHORT).show();
                showDialogSettings();
//                alertDialogLocationEnabling();
                }


            } else {
                Log.e(TAG, " NOT nill...........................................");


                Double Lanti = Double.valueOf(latitude);
                Double Longi = Double.valueOf(longitude);
                moveCamera(new LatLng(Lanti, Longi), DEFAULT_ZOOM, "");


            }
        }


//        viewMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            public void onMapClick(LatLng point) {
////                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
////                imm.hideSoftInputFromWindow(Register.this.getCurrentFocus().getWindowToken(), 0);
////                Toast.makeText(ge(), point.latitude + ", " + point.longitude, Toast.LENGTH_SHORT).show();
//                Log.e("latitude ->", String.valueOf(point.latitude));
//                Log.e("longitude ->", String.valueOf(point.longitude));
//
//                if (mCurrLocationMarker != null) {
//                    mCurrLocationMarker.remove();
//                }
//                LatLng sydney = new LatLng(point.latitude, point.longitude);
//                mCurrLocationMarker = viewMap.addMarker(new MarkerOptions().position(sydney).title("Your selection"));
////                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//                viewMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, DEFAULT_ZOOM));
//                latitude = String.valueOf(point.latitude);
//                longitude = String.valueOf(point.longitude);
//            }
//        });
    }

    private void getDeviceLocation() {
        Log.e(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (mLocationPermissionsGranted) {

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.e(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();
                            if (currentLocation == null) {
//                                alertDialogLocationEnabling();
                                Toast.makeText(Register.this, "Unable to get location", Toast.LENGTH_SHORT).show();
                            } else {
                                moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM, "My Location");
                            }


                        } else {
                            Log.e(TAG, "onComplete: current location is null");
                            Toast.makeText(Register.this, "Unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }

    private void moveCamera(LatLng latLng, float zoom, String title) {
        Log.e(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        viewMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        if (!title.equals("My Location")) {
            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(title);
            mCurrLocationMarker = viewMap.addMarker(options);
        }


    }

    private void initMap() {
        Log.e(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.r_map);
        mapFragment.getMapAsync(this);
    }

    private void getLocationPermission() {
        Log.e(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this,
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this,
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
                count = 0;
                initMap();
            } else {
                count = 0;
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            count = 0;
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.e(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false;
                            Log.e(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.e(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }

    // popup if no location enabled message
    public void showDialogSettings() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Please enable location services from your settings.")
                .setCancelable(true)
                .setPositiveButton("SETTINGS", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);

                        dialog.dismiss();
                    }
                }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {

                dialog.dismiss();

            }
        });
        AlertDialog alert = builder.create();

        //Change background color of dialog
        alert.getWindow().setBackgroundDrawableResource(R.color.gradient_light_green1);
        alert.show();
//
        //Change positive button style and color
        Button buttonPositive = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        buttonPositive.setTextColor(ContextCompat.getColor(this, R.color.white_font));
        buttonPositive.setTypeface(fontRegular);

        //Change positive button style and color
        Button buttonNegative = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        buttonNegative.setTextColor(ContextCompat.getColor(this, R.color.gradient_light_green2));
        buttonNegative.setTypeface(fontRegular);


        //Change dialog message style and color
        TextView textView = (TextView) alert.findViewById(android.R.id.message);
        textView.setTextColor(ContextCompat.getColor(this, R.color.white_font));
        textView.setTypeface(fontLight);
    }


    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html) {
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    // check if location GPS is enabled
    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    // check connectivity
    public boolean checkInternetConnection(Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();

        if (activeNetworkInfo != null) { // connected to the internet
            //Toast.makeText(context, activeNetworkInfo.getTypeName(), Toast.LENGTH_SHORT).show();

            if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                return true;
            } else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                return true;
            }
        }
        return false;
    }
}
