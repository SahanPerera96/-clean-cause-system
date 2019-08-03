package com.clean.cause.cleancause.main;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.clean.cause.cleancause.R;
import com.clean.cause.cleancause.addReport.AddReport;
import com.clean.cause.cleancause.startup.Login;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;

public class ApplicationMain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        AddReport.OnFragmentInteractionListener{

    NavigationView sideNavigationView;
    BottomNavigationView bottomNavigationView;
    Typeface fontLight,fontRegular;
    ActionBarDrawerToggle actionBarDrawerToggle;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ImageView openNavigationDrawerBtn, closeNavigationDrawerBtn;
    BottomNavigationViewEx bnve;
    FrameLayout frameLayout;
    String base64String = "";

    //Styling for double press back button
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

        setContentView(R.layout.activity_application_main);

        sideNavigationView = (NavigationView) findViewById(R.id.side_navigation_drawer);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_view);
        fontLight = Typeface.createFromAsset(getAssets(), "avenirltstd_book_light.ttf");
        fontRegular = Typeface.createFromAsset(getAssets(), "avenirltstd_medium.ttf");
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        openNavigationDrawerBtn = (ImageView) findViewById(R.id.open_navigation_drawer);
        closeNavigationDrawerBtn = (ImageView) findViewById(R.id.close_navigation_drawer);
        bnve = (BottomNavigationViewEx) findViewById(R.id.bnve);
        frameLayout = (FrameLayout) findViewById(R.id.application_main_fragment_view);
        //navigation drawer All oncreate and click events handled here
        sideNavigationOnCreateEvents();

        //bottom navigation drawer All oncreate and click events handled here
        bottomNavigationOnCreateEvents();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch(id){
            case R.id.nav_your_reports:
                removeSelectedItemFromsideNavigationDrawer();
                removeSelectedItemFrombottomNavigationDrawer();
                menuItem.setChecked(true);
                break;
            case R.id.nav_your_completed_reports:
                removeSelectedItemFromsideNavigationDrawer();
                removeSelectedItemFrombottomNavigationDrawer();
                menuItem.setChecked(true);
                break;
            case R.id.nav_guide:
                removeSelectedItemFromsideNavigationDrawer();
                removeSelectedItemFrombottomNavigationDrawer();
                menuItem.setChecked(true);
                break;
            case R.id.nav_faq:
                removeSelectedItemFromsideNavigationDrawer();
                removeSelectedItemFrombottomNavigationDrawer();
                menuItem.setChecked(true);
                break;
            case R.id.nav_about_us:
                removeSelectedItemFromsideNavigationDrawer();
                removeSelectedItemFrombottomNavigationDrawer();
                menuItem.setChecked(true);
                break;
            case R.id.nav_contact_us:
                removeSelectedItemFromsideNavigationDrawer();
                removeSelectedItemFrombottomNavigationDrawer();
                menuItem.setChecked(true);
                break;
            case R.id.nav_logout:
                menuItem.setChecked(false);
                alertDialogForLogout("Do you wish to logout?");
                break;

//            default:
//                return true;
        }
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        initBottomViewAndLoadFragments(bnve);
        return true;

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            if (resultCode == RESULT_OK) {
//                Uri resultUri = result.getUri();
//                Log.e("resultUri ---------->", String.valueOf(resultUri));
//                AddReport addReport = new AddReport();
//                Bundle arguments = new Bundle();
//                arguments.putString("resultUri", String.valueOf(resultUri));
//
//                addReport.setArguments(arguments);
//                FragmentTransaction fragmentTransactionDiscover = getSupportFragmentManager().beginTransaction();
//                fragmentTransactionDiscover.replace(R.id.application_main_fragment_view, addReport);
//                fragmentTransactionDiscover.commit();
//
//            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                Exception error = result.getError();
//                Log.e("error ->", String.valueOf(error));
//            }
//        }
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    if (imageReturnedIntent.getData() == null) {
                        Bitmap bitmap = (Bitmap) imageReturnedIntent.getExtras().get("data");
                        base64String = convertBitmapToBase64String(bitmap);
//                        iv.setImageBitmap(bitmap);

                    } else {
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageReturnedIntent.getData());
//                            iv.setImageBitmap(bitmap);
                            base64String = convertBitmapToBase64String(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.d("selectedImage: ", imageReturnedIntent.getData() + "");
                    AddReport addReport = new AddReport();
                Bundle arguments = new Bundle();
                arguments.putString("base64String", base64String );

                addReport.setArguments(arguments);
                FragmentTransaction fragmentTransactionDiscover = getSupportFragmentManager().beginTransaction();
                fragmentTransactionDiscover.replace(R.id.application_main_fragment_view, addReport);
                fragmentTransactionDiscover.commit();

                }

                break;
        }
    }

    // convert Bitmap To Base64 String
    public String convertBitmapToBase64String(Bitmap bitmap) {
        byte[] b = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, baos); //bm is the bitmap object
        b = baos.toByteArray();
        String encodedImage = null;
        encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
//        Log.e(" encodedImage  ---->", encodedImage);
        return encodedImage;
    }

    //bottom navigation drawer All oncreate and click events handled here
    private  void bottomNavigationOnCreateEvents(){

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.home:
//                                bottomNavigationView.setItemBackground(getDrawable(R.drawable.checked_state_nav_drawer_colour_changer));
                        removeSelectedItemFromsideNavigationDrawer();
                        removeSelectedItemFrombottomNavigationDrawer();
                        item.setChecked(true);
                        break;
                    case R.id.reports:
                        removeSelectedItemFromsideNavigationDrawer();
                        removeSelectedItemFrombottomNavigationDrawer();
                        item.setChecked(true);
                        break;
                    case R.id.camera:
                        removeSelectedItemFromsideNavigationDrawer();
                        removeSelectedItemFrombottomNavigationDrawer();
                        item.setChecked(true);
//                        Intent intent = CropImage.activity()
//                                .getIntent(ApplicationMain.this);
//                        startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);

                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, 0);
                        break;
                    case R.id.calender:
                        removeSelectedItemFromsideNavigationDrawer();
                        removeSelectedItemFrombottomNavigationDrawer();
                        item.setChecked(true);
                        break;
                    case R.id.profile:
                        removeSelectedItemFromsideNavigationDrawer();
                        removeSelectedItemFrombottomNavigationDrawer();
                        item.setChecked(true);
                        break;
                }

                return true;
            }
        });
        disableShiftMode(bottomNavigationView);
        initBottomViewAndLoadFragments(bnve);
    }

    //navigation drawer All oncreate and click events handled here
    private  void sideNavigationOnCreateEvents(){
        actionBarDrawerToggle = new ActionBarDrawerToggle(ApplicationMain.this, drawerLayout, toolbar, R.string.open, R.string.close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // Do whatever you want here
            }
        };

        // navigation header open naavigation drawer on icon click
        openNavigationDrawerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);

            }
        });


        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


        sideNavigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) ApplicationMain.this);
        disableNavigationViewScrollbars(sideNavigationView);

        //remove all selected items
        removeSelectedItemFromsideNavigationDrawer();

        // navigation header close naavigation drawer on icon click
        closeNavigationDrawerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(Gravity.LEFT);


            }
        });
    }

    // deselect all items that are selected in the side navigation bar
    private void removeSelectedItemFromsideNavigationDrawer() {
        int size = sideNavigationView.getMenu().size();
        for (int i = 0; i < size; i++) {
            sideNavigationView.getMenu().getItem(i).setChecked(false);
        }
    }

    // deselect all items that are selected in the bottom navigation bar
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void removeSelectedItemFrombottomNavigationDrawer() {

        int size = bottomNavigationView.getMenu().size();
        for (int i = 0; i < size; i++) {
//            System.out.println( bottomNavigationView.getMenu().getItem(i).getTitle() );
            bottomNavigationView.getMenu().getItem(i).setChecked(false);
//            bottomNavigationView.getMenu().getItem(1).setChecked(false);
//            bottomNavigationView.getMenu().getItem(2).setChecked(false);
//            bottomNavigationView.getMenu().getItem(3).setChecked(false);
//            bottomNavigationView.getMenu().getItem(4).setChecked(false);
        }
//        bottomNavigationView.getMenu().findItem(R.id.profile).setChecked(false);
//        bottomNavigationView.setItemBackground(getDrawable(R.drawable.checked_state_nav_drawer_colour_changer_two));
    }

    // remove scrollbar in navigation drawer
    private void disableNavigationViewScrollbars(NavigationView navigationView) {
        if (navigationView != null) {
            NavigationMenuView navigationMenuView = (NavigationMenuView) navigationView.getChildAt(0);
            if (navigationMenuView != null) {
                navigationMenuView.setVerticalScrollBarEnabled(false);
            }
        }
    }

    // popup message for logout
    public void alertDialogForLogout(String Message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(Message);
        builder.setCancelable(false);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {


                Intent intent = new Intent(ApplicationMain.this, Login.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();



            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alert = builder.create();

        //Change background color of dialog
        alert.getWindow().setBackgroundDrawableResource(R.color.gradient_light_green1);
        alert.show();

        //Change positive button style and color
        Button buttonPositive = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        buttonPositive.setTextColor(ContextCompat.getColor(this, R.color.white_font));
        buttonPositive.setTypeface(fontRegular);

        //Change positive button style and color
        Button buttonNegative = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        buttonNegative.setTextColor(ContextCompat.getColor(this, R.color.white_font));
        buttonNegative.setTypeface(fontRegular);

        //Change dialog message style and color
        TextView textView = (TextView) alert.findViewById(android.R.id.message);
        textView.setTextColor(ContextCompat.getColor(this, R.color.white_font));
        textView.setTypeface(fontLight);
    }

    @SuppressLint("RestrictedApi")
    public static void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShifting(false);

                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {

        } catch (IllegalAccessException e) {

        }
    }

    private void initBottomViewAndLoadFragments(final BottomNavigationViewEx bnve) {
        bnve.enableAnimation(false);
        bnve.enableShiftingMode(false);
        bnve.enableItemShiftingMode(false);

        // use the unchecked color for first item
        bnve.setIconTintList(0, getResources().getColorStateList(R.color.black_color));
        bnve.setTextTintList(0, getResources().getColorStateList(R.color.black_color));
        bnve.setItemBackground(0,getResources().getColor(R.color.transparent));

        bnve.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            private boolean firstClick = true;
            private int lastItemId = -1;

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // restore the color when click
                if (firstClick) {
                    firstClick = false;
                    bnve.setIconTintList(0, getResources().getColorStateList(R.color.white_color));
                    bnve.setTextTintList(0, getResources().getColorStateList(R.color.white_color));
                    bnve.setItemBackground(0,getResources().getColor(R.color.transparent));

                }

                if (firstClick || lastItemId == -1 || lastItemId != item.getItemId()) {
                    lastItemId = item.getItemId();
                } else {
                    return false;
                }

                // do stuff
                return true;
            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
