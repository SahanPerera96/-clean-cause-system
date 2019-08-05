package com.clean.cause.cleancause.addReport;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
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
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddReport.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddReport#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddReport extends Fragment implements OnMapReadyCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String base64String = "";
    ImageView reportImageView;

    private GoogleMap mMap;

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private GoogleApiClient mGoogleApiClient;

    Marker mCurrLocationMarker;

    private static final String TAG = "MapActivity";

    private Boolean mLocationPermissionsGranted = false;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));

    Typeface fontLight, fontRegular;
    String Lan = "";
    String Long = "";
    int count = 0;
    boolean gps_enabled = false;

    Button addReportButton;
    private OnFragmentInteractionListener mListener;

    public AddReport() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddReport.
     */
    // TODO: Rename and change types and number of parameters
    public static AddReport newInstance(String param1, String param2) {
        AddReport fragment = new AddReport();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_add_report, container, false);
        View view = inflater.inflate(R.layout.fragment_add_report, container, false);
        reportImageView = view.findViewById(R.id.r_image_taken);
        addReportButton = view.findViewById(R.id.btn_add_report);

        fontLight = Typeface.createFromAsset(getActivity().getAssets(), "avenirltstd_book_light.ttf");
        fontRegular = Typeface.createFromAsset(getActivity().getAssets(), "avenirltstd_medium.ttf");

        getLocationPermission();
        if (getArguments() != null) {
            base64String = getArguments().getString("base64String");


            Log.e("base64String ---->", base64String);

//            Uri uri = Uri.parse(resultUri);
//            System.out.println(uri);
//            if (base64String == null) {


            reportImageView.setImageBitmap(ConvertBase64Tobitmap(base64String));
//            reportImageView.setImageURI(Uri.parse(base64String));
            reportImageView.setBackgroundResource(0);
//            }

        }
        // on click add report button
        addReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                System.out.println("Lantitude "+ Lan);
                System.out.println("Longitude "+ Long);


            }
        });

        // on click report image if wrong orrientation
        reportImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                System.out.println("Lantitude "+ Lan);
                System.out.println("Longitude "+ Long);
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 0);

            }
        });
        return view;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
//        googleMapv = googleMap;
//        Toast.makeText(getActivity(), "Map is Ready", Toast.LENGTH_SHORT).show();

        mMap = googleMap;
        count++;
//        if (count == 1) {
//            Log.e(TAG, count + "  ...........................................");
//            if (Lan.equals("") || Long.equals("") || Lan == null || Long == null) {
//                Log.e(TAG, "  nill...........................................");
                // check if location is enabled
                gps_enabled = isLocationEnabled(getActivity());
                if (gps_enabled) {
                    if (mLocationPermissionsGranted) {
                        getDeviceLocation();

                        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        mMap.setMyLocationEnabled(true);
                        mMap.getUiSettings().setMyLocationButtonEnabled(true);


                    }
                }
//                else {
////                Toast.makeText(getActivity(), "Location OFF", Toast.LENGTH_SHORT).show();
////                showDialogSettings();
////                    alertDialogLocationEnabling();
//                }


//            } else {
//                Log.e(TAG, " NOT nill...........................................");
//
//
//                Double Lanti = Double.valueOf(Lan);
//                Double Longi = Double.valueOf(Long);
//                moveCamera(new LatLng(Lanti, Longi), DEFAULT_ZOOM, "");
//
//
//            }
//        }


//        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            public void onMapClick(LatLng point) {
//                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
////                Toast.makeText(ge(), point.latitude + ", " + point.longitude, Toast.LENGTH_SHORT).show();
//                Log.e("latitude ->", String.valueOf(point.latitude));
//                Log.e("longitude ->", String.valueOf(point.longitude));
//
//                if (mCurrLocationMarker != null) {
//                    mCurrLocationMarker.remove();
//                }
//                LatLng sydney = new LatLng(point.latitude, point.longitude);
//                mCurrLocationMarker = mMap.addMarker(new MarkerOptions().position(sydney).title("Your selection"));
////                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, DEFAULT_ZOOM));
//                Lan = String.valueOf(point.latitude);
//                Long = String.valueOf(point.longitude);
//            }
//        });
    }

    private void getDeviceLocation() {
        Log.e(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

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
                            } else {
                                Lan = String.valueOf(currentLocation.getLatitude());
                                Long = String.valueOf(currentLocation.getLongitude());
                                moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM, "My Location");
                            }


                        } else {
                            Log.e(TAG, "onComplete: current location is null");
                            Toast.makeText(getActivity(), "Unable to get current location", Toast.LENGTH_SHORT).show();
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
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        if (!title.equals("My Location")) {
            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(title);
            mCurrLocationMarker = mMap.addMarker(options);
        }


    }

    private void initMap() {
        Log.e(TAG, "initMap: initializing map");
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.r_add_report_map);
        supportMapFragment.getMapAsync(this);
    }

    private void getLocationPermission() {
        Log.e(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(getActivity(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    if (imageReturnedIntent.getData() == null) {
                        Bitmap bitmap = (Bitmap) imageReturnedIntent.getExtras().get("data");
                        base64String = convertBitmapToBase64String(bitmap);
                        reportImageView.setImageBitmap(bitmap);
                        reportImageView.setBackgroundResource(0);

                    } else {
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageReturnedIntent.getData());
                            base64String = convertBitmapToBase64String(bitmap);
                            reportImageView.setImageBitmap(bitmap);
                            reportImageView.setBackgroundResource(0);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

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

    // convert base 64 string to bitmap
    public Bitmap ConvertBase64Tobitmap(String BaseString) {
        byte[] b = null;
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();// Create object of bitmapfactory's option method for further option use
        options.inPurgeable = true; // inPurgeable is used to free up memory while required
        if (BaseString != null) {
            b = Base64.decode(BaseString, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(b, 0, b.length, options);
        }
        return bitmap;
    }
    // check if locatin GPS is enabled
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
