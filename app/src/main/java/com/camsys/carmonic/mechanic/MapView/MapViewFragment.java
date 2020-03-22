package com.camsys.carmonic.mechanic.MapView;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.*;
import android.provider.Settings;

import android.text.TextUtils;
import android.util.Log;
import android.view.*;
import android.widget.*;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.camsys.carmonic.mechanic.API.ConnectionController;
import com.camsys.carmonic.mechanic.Dasboard.AcceptAndDeclineFragment;
import com.camsys.carmonic.mechanic.Dasboard.AlertDialogFragment;
import com.camsys.carmonic.mechanic.Dasboard.CustomerDetailFragment;
import com.camsys.carmonic.mechanic.Dasboard.OrderDetailFragment;
import com.camsys.carmonic.mechanic.MainActivity;
import com.camsys.carmonic.mechanic.Model.Customer;
import com.camsys.carmonic.mechanic.Model.UserModel;
import com.camsys.carmonic.mechanic.Model.Users;
import com.camsys.carmonic.mechanic.R;
import com.camsys.carmonic.mechanic.Service.FetchAddressIntentService;
import com.camsys.carmonic.mechanic.Utilities.Constants;
import com.camsys.carmonic.mechanic.Utilities.DirectionClassUtil;
import com.camsys.carmonic.mechanic.Utilities.NotificationUtil;
import com.camsys.carmonic.mechanic.Utilities.SharedData;
import com.camsys.carmonic.mechanic.Utilities.Util;
import com.camsys.carmonic.mechanic.onboarding.SignInActivtiy;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;

import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import okhttp3.OkHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.camsys.carmonic.mechanic.Utilities.Constants.MAP_TYPES;
import static com.camsys.carmonic.mechanic.Utilities.Util.GetAddressFromLatLong;


public class MapViewFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraMoveCanceledListener,
        GoogleMap.OnCameraIdleListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, AcceptAndDeclineFragment.MyDialogFragmentListener {


    boolean  showRequest  =  true  ;
    // private AddressResultReceiver mResultReceiver;
    protected LatLng start;
    protected LatLng mCenterLatLong;
    protected GoogleApiClient mGoogleApiClient;
    MapView mMapView;
    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    Location mLastLocation;
    Switch switch1;
    String[] permissionsRequired = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    int rideType = 0;
    //UserAccount user;
    private LocationRequest mLocationRequest;
    private PolylineOptions currPolylineOptions;
    String regID;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    SharedData sharedData = null;
    EditText txtMyLocation = null;

    private AddressResultReceiver mResultReceiver;
    protected String mAddressOutput;
    protected String mAreaOutput;
    protected String mCityOutput;
    protected String mStateOutput;

    LinearLayout mLinearLayout;
    TextView txtOrderStatus;
    AppCompatButton btnContact;
    private Socket socket;

    Gson gson = null;
    String mcustomerString =  "";
    String action = "";
    String message = null;
    boolean showMarker =  false;
    ArrayList<LatLng> markerPoints;

    TextView txtOrderCustomerName = null,txtTimeToReach = null;

    AppCompatButton btnCallCustomer = null;
    String  customerPhoneNumber =  "";


    public void showRequest(final Activity activity, final String customerJSON) {
        final Dialog dialog = new Dialog(activity);



        dialog.setContentView(R.layout.fragment_result_dialog);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.7f;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

//        LinearLayout layout = dialog.findViewById(R.id.root);
//        ViewGroup.LayoutParams params = layout.getLayoutParams();
//        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//        params.width =  ViewGroup.LayoutParams.WRAP_CONTENT;
//        layout.setLayoutParams(params);


        System.out.println("customerJSON::::  " + customerJSON);

        TextView txtTopic = (TextView) dialog.findViewById(R.id.txtTopic);
        TextView cancelButton = (TextView) dialog.findViewById(R.id.cancel_button);

        txtTopic.setVisibility(View.GONE);
        cancelButton.setVisibility(View.GONE);


        TextView txtNotificationText = (TextView) dialog.findViewById(R.id.txtNotification);
        TextView txtSeeDetail = (TextView) dialog.findViewById(R.id.txtSeeDetail);
        TextView txtDecline = (TextView) dialog.findViewById(R.id.txtDecline);

        final Users customer = gson.fromJson(customerJSON, Users.class);
        String customerName = customer.getFirstname();
        double lat = customer.getLatitude();

        double distance = Util.distance(customer.getLatitude(), customer.getLongitude(), mLastLocation.getLatitude(), mLastLocation.getLongitude());

        String msg = String.format("A customer needs your help, %dkm away from your location", Math.round(distance));

        txtNotificationText.setText(msg.replace("xxx", distance + ""));

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        txtSeeDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final AcceptAndDeclineFragment myBottomSheet = AcceptAndDeclineFragment.newInstance(customerJSON, mLastLocation.getLatitude(), mLastLocation.getLongitude(), new AcceptAndDeclineFragment.MyDialogFragmentListener() {
                    @Override
                    public void onReturnValue(boolean indicator) {
                        if (indicator) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    ShowingAcceptRequest(getActivity(), customerJSON);
                                }
                            });

                        } else {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    showingRejectRequest(getActivity(), customerJSON);
                                }
                            });
                        }

                    }
                });
                myBottomSheet.show(getActivity().getSupportFragmentManager(), myBottomSheet.getTag());
                dialog.dismiss();



            }
        });

        txtDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // showingRejectRequest(getActivity(), customerJSON);
                rejectRequestSocketConnection(mLastLocation,customerJSON);
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    public void ShowingAcceptRequest(final Activity activity, final String  customerJSON) {

        final Users customer = gson.fromJson(customerJSON, Users.class);

        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.fragment_result_dialog);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.7f;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        LinearLayout layout = dialog.findViewById(R.id.root);
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        layout.setLayoutParams(params);

        TextView txtTopic = (TextView) dialog.findViewById(R.id.txtTopic);
        TextView cancelButton = (TextView) dialog.findViewById(R.id.cancel_button);
        TextView txtNotificationText = (TextView) dialog.findViewById(R.id.txtNotification);
        TextView txtSeeDetail = (TextView) dialog.findViewById(R.id.txtSeeDetail);
        TextView txtDecline = (TextView) dialog.findViewById(R.id.txtDecline);

        txtTopic.setVisibility(View.VISIBLE);
        cancelButton.setVisibility(View.GONE);

        txtTopic.setText("You accepted the request!");


        String msg = String.format("%s is waiting %s", customer.getFirstname(), Util.GetAddressFromLatLong(getContext(), customer.getLatitude(), customer.getLongitude()));

        txtNotificationText.setText(msg);    //("Fikayo is waiting for you at 789 Oriole Pkway");
        txtNotificationText.setTextColor(getResources().getColor(R.color.dialog_text));

        txtDecline.setTextColor(getResources().getColor(R.color.dialog_text));

        txtDecline.setText("Get Direction");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        txtSeeDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CustomerDetailFragment myBottomSheet = CustomerDetailFragment.newInstance(customerJSON);
                myBottomSheet.show(getActivity().getSupportFragmentManager(), myBottomSheet.getTag());

            }
        });

        txtDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                txtOrderCustomerName.setText(customer.getFirstname());
                String  timeToReach =  txtTimeToReach.getText().toString().replace("Fikayo",customer.getFirstname());
               txtTimeToReach.setText(timeToReach);
                customerPhoneNumber =  customer.getPhoneNumber();

                mLinearLayout.setVisibility(View.VISIBLE);
                dialog.dismiss();
                //show the marker on the map;
                showMarker =  true ;
                mcustomerString  = customerJSON;

              //  createMarker(customer.getLatitude(), customer.getLongitude(), "user", "user");

            }
        });
        dialog.show();
    }


    public void showingRejectRequest(final Activity activity, String customerJSON) {
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.fragment_result_dialog);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.7f;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        View view = (View) dialog.findViewById(R.id.line1);
        TextView txtTopic = (TextView) dialog.findViewById(R.id.txtTopic);
        TextView cancelButton = (TextView) dialog.findViewById(R.id.cancel_button);
        LinearLayout container = (LinearLayout) dialog.findViewById(R.id.container);
        txtTopic.setVisibility(View.VISIBLE);
        cancelButton.setVisibility(View.VISIBLE);
        container.setVisibility(View.GONE);
        view.setVisibility(View.GONE);

        TextView txtNotificationText = (TextView) dialog.findViewById(R.id.txtNotification);

        txtTopic.setText("You rejected the request!");
        txtNotificationText.setText("The request has been transferred to another mechanic.");
        txtNotificationText.setTextColor(getResources().getColor(R.color.dialog_text));

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

//        txtSeeDetail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                final CustomerDetailFragment myBottomSheet = CustomerDetailFragment.newInstance("");
//                myBottomSheet.show(getActivity().getSupportFragmentManager(), myBottomSheet.getTag());
//
//            }
//        });
//
//        txtDecline.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//            }
//        });
        dialog.show();
    }


    public static MapViewFragment newInstance(Intent intent) {
        MapViewFragment fragment = new MapViewFragment();
        Bundle args = new Bundle();
        args.putString("action", intent.getAction());
        args.putString("message", intent.getStringExtra("message"));
        args.putString("customerJSON", intent.getStringExtra("customerJSON"));
        fragment.setArguments(args);
        return fragment;
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map_view, container, false);
        setHasOptionsMenu(true);
        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        txtMyLocation = (EditText) rootView.findViewById(R.id.txtMyLocation);
        txtOrderStatus = (TextView) rootView.findViewById(R.id.txtOrderStatus);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        sharedData = new SharedData(getActivity());
        String showPopUp = sharedData.Get(Constants.IS_LOG_IN, null);
        mLinearLayout = (LinearLayout) rootView.findViewById(R.id.bottomContainer);
        mLinearLayout.setVisibility(View.GONE);




        txtOrderCustomerName = (TextView) rootView.findViewById(R.id.txtOrderCustomerName);
        txtTimeToReach = (TextView) rootView.findViewById(R.id.txtTimeAway);

        btnCallCustomer = (AppCompatButton) rootView.findViewById(R.id.btnContact);

        checkPlayService(getActivity());

        mMapView.getMapAsync(this);
        mResultReceiver = new AddressResultReceiver(new Handler());

        btnCallCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(customerPhoneNumber != ""  && customerPhoneNumber.length() > 10){

                    String phoneNumber  = String.format("tel:%s",customerPhoneNumber);

                    Intent intent =new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse(phoneNumber));
                    startActivity(intent);

                }
            }
        });
        txtOrderStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // Toast.makeText(getActivity(),mcustomerString,Toast.LENGTH_LONG).show();

                 try{

                     final OrderDetailFragment orderDetailFragment = OrderDetailFragment.newInstance(mcustomerString, new OrderDetailFragment.MyDialogFragmentListener() {
                    @Override
                    public void onReturnValue(boolean indicator) {

                        //Toast.makeText(getActivity(), "Hi text", Toast.LENGTH_LONG).show();
                    }
                   });
                     orderDetailFragment.show(getActivity().getSupportFragmentManager(), orderDetailFragment.getTag());


                 }catch (Exception ex){

                     System.out.println(ex.toString());

                 }
            }
        });


        return rootView;
    }

    private void checkPlayService(Context context) {

        if (checkPlayServices()) {
            // If this check succeeds, proceed with normal processing.
            // Otherwise, prompt user to get valid Play Services APK.
            if (!Util.isLocationEnabled(getActivity())) {
                // notify user
                android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(context);
                dialog.setMessage("Location not enabled!");
                dialog.setPositiveButton("Open location settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        // TODO Auto-generated method stub

                    }
                });
                dialog.show();
            }
            buildGoogleApiClient();
        } else {
            Toast.makeText(context, "Location not supported in this device", Toast.LENGTH_SHORT).show();
        }

    }



    private void changeCamera(CameraUpdate update, GoogleMap.CancelableCallback callback, LatLng latlng, String Address, int drawable) {
        MarkerOptions markerOptions = new MarkerOptions();
        mGoogleMap.animateCamera(update, callback);
        markerOptions.position(latlng);
        markerOptions.title(Address);
        markerOptions.draggable(true).visible(true);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(drawable));
        //  mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        mGoogleMap.addMarker(markerOptions);

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        buildGoogleApiClient();
        gson = new Gson();
//        if (getArguments() != null && getArguments().getString("action") == Constants.SetAction.MECHANIC_REQUEST) {
//            message = getArguments().getString("message");
//            customerJSON  = getArguments().getString("customerJSON");
//
//            showRequest(getActivity(), customerJSON);
//        }else if(showMarker){
//
//
//            initCamera(mLastLocation);
//
//        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.clear();    //remove all items
        getActivity().getMenuInflater().inflate(R.menu.main, menu);
        switch1 = (Switch) menu.findItem(R.id.myswitch).getActionView().findViewById(R.id.switchAB);
        switch1.setVisibility(View.INVISIBLE);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        mMapView.getMapAsync(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
        //stop location updates when Activity is no longer active
        try {
            if (mGoogleApiClient != null) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
        mGoogleApiClient.connect();
        //mMapView.getMapAsync(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        //mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        //  mGoogleMap.setMyLocationEnabled(true);
        // mGoogleMap.setMy
        //Initialize Google Play Services
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    permissionsRequired[0])
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);
                mGoogleMap.setMapType(MAP_TYPES[1]);
                // mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(startDirection));
                mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14));
                mGoogleMap.setBuildingsEnabled(true);
                mGoogleMap.setOnCameraIdleListener(this);
                mGoogleMap.setOnCameraMoveStartedListener(this);
                mGoogleMap.setOnCameraMoveListener(this);
                mGoogleMap.setOnCameraMoveCanceledListener(this);

                // We will provide our own zoom controls.
                mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
                mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);

                //  startIntentService(mLastLocation);

                //initCamera(mLastLocation);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.setMapType(MAP_TYPES[1]);
            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14));
            //mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
            //initCamera(mLastLocation);
            mGoogleMap.setBuildingsEnabled(true);
            mGoogleMap.setOnCameraIdleListener(this);
            mGoogleMap.setOnCameraMoveStartedListener(this);
            mGoogleMap.setOnCameraMoveListener(this);
            mGoogleMap.setOnCameraMoveCanceledListener(this);

            // We will provide our own zoom controls.
            mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
            mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);

            // startIntentService(mLastLocation);
        }

        setupSocket();


        mGoogleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                Log.d("Camera postion change" + "", cameraPosition + "");
                mCenterLatLong = cameraPosition.target;


                mGoogleMap.clear();

                try {

                    Location mLocation = new Location("");// Location("");
                    mLocation.setLatitude(mCenterLatLong.latitude);
                    mLocation.setLongitude(mCenterLatLong.longitude);

                    startIntentService(mLocation);

//                    if(showRequest) {
//                        showRequest(getActivity(), gson.toJson(setCustomer()));
//                    }

                    showRequest =  false;
                    try {
                        //socketLocationUpdate(mLocation);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        try {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }

            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (mLastLocation != null) {
                // changeMap(mLastLocation);
                initCamera(mLastLocation);
                //Log.d("TAG", "ON connected");
                // Toast.makeText(getActivity(),"Longitude = " +mLastLocation.getLongitude()+ " Latyitude = " + mLastLocation.getLatitude() + "" ,Toast.LENGTH_LONG).show();
                startIntentService(mLastLocation);
              //  socketLocationUpdate(mLastLocation);

            } else
                try {
                    LocationServices.FusedLocationApi.removeLocationUpdates(
                            mGoogleApiClient, this);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            try {
                mLocationRequest = new LocationRequest();
                mLocationRequest.setInterval(60000);
                mLocationRequest.setFastestInterval(10000);
                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                LocationServices.FusedLocationApi.requestLocationUpdates(
                        mGoogleApiClient, mLocationRequest, this);
                //Toast.makeText(getActivity(),"Longitude = " +mLastLocation.getLongitude()+ " Latyitude = " + mLastLocation.getLatitude() + "" ,Toast.LENGTH_LONG).show();
                startIntentService(mLastLocation);
            //    socketLocationUpdate(mLastLocation);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();

        }

    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        initCamera(mLastLocation);
    }


    /**
     * Creates an intent, adds location data to it as an extra, and starts the intent service for
     * fetching an address.
     */
    protected void startIntentService(Location mLocation) {
        // Create an intent for passing to the intent service responsible for fetching the address.
        try {

            Intent intent = new Intent(getActivity(), FetchAddressIntentService.class);
            intent.putExtra(Constants.LocationConstants.RECEIVER, mResultReceiver);
            intent.putExtra(Constants.LocationConstants.LOCATION_DATA_EXTRA, mLocation);
            getActivity().startService(intent);

        } catch (Exception ex) {
            System.out.println(ex.toString());
        }

    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), permissionsRequired[0])
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissionsRequired[0])) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getActivity())
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{permissionsRequired[0]},
                                        Constants.MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{permissionsRequired[0]},
                        Constants.MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Constants.MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getActivity(), permissionsRequired[0])
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

        }
    }

    private void initCamera(Location location) {

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        CameraPosition position = CameraPosition.builder()
                .target(latLng)
                .zoom(15.5f)
                .bearing(300)
                .tilt(50)
                .build();

        mGoogleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(position), null);

        markerOptions.position(latLng);
        markerOptions.title("Pick Up Point");
        markerOptions.draggable(true).visible(true);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        // mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        mGoogleMap.setMapType(MAP_TYPES[1]);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        //startIntentService(location);
        if(showMarker){
           // createMarker(location.getLatitude(),location.getLongitude(),"mechanic","mechanic");
        }
    }




    @Override
    public void onCameraMoveStarted(int reason) {
//        if (!isCanceled) {
//            mGoogleMap.clear();
//        }

        String reasonText = "UNKNOWN_REASON";
        //currPolylineOptions = new PolylineOptions().width(5);
        switch (reason) {
            case GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE:
                //currPolylineOptions.color(Color.BLUE);
                reasonText = "GESTURE";
                break;
            case GoogleMap.OnCameraMoveStartedListener.REASON_API_ANIMATION:
                //currPolylineOptions.color(Color.RED);
                reasonText = "API_ANIMATION";
                break;
            case GoogleMap.OnCameraMoveStartedListener.REASON_DEVELOPER_ANIMATION:
                //currPolylineOptions.color(Color.GREEN);
                reasonText = "DEVELOPER_ANIMATION";
                break;
        }
        Log.i("mGoogleMap", "onCameraMoveStarted(" + reasonText + ")");
        //  addCameraTargetToPath();
    }

    @Override
    public void onCameraMove() {
        // When the camera is moving, add its target to the current path we'll draw on the map.
        // if (currPolylineOptions != null) {
        addCameraTargetToPath();
        // }
        Log.i("mGoogleMap", "onCameraMove");
    }

    @Override
    public void onCameraMoveCanceled() {
        // When the camera stops moving, add its target to the current path, and draw it on the map.
        if (currPolylineOptions != null) {
            // addCameraTargetToPath();
            //  mGoogleMap.addPolyline(currPolylineOptions);
        }
        //isCanceled = true;  // Set to clear the map when dragging starts again.
        currPolylineOptions = null;
        Log.i("mGoogleMap", "onCameraMoveCancelled");
    }

    @Override
    public void onCameraIdle() {
        if (currPolylineOptions != null) {

            //  mGoogleMap.addPolyline(currPolylineOptions);
        }
        currPolylineOptions = null;
        //isCanceled = false;  // Set to *not* clear the map when dragging starts again.
        Log.i("mGoogleMap", "onCameraIdle");
    }

    private void addCameraTargetToPath() {
        mCenterLatLong = mGoogleMap.getCameraPosition().target;
        // currPolylineOptions.add(mCenterLatLong);
        // mGoogleMap.clear();
        try {

            //System.out.println("mCenterLatLong:: " + mCenterLatLong.latitude + "mCenterLatLong:: " + mCenterLatLong.longitude);
            Location mLocation = new Location("");// Location("");
            mLocation.setLatitude(mCenterLatLong.latitude);
            mLocation.setLongitude(mCenterLatLong.longitude);
            //startIntentService(mLocation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Listener that handles selections from suggestions from the AutoCompleteTextView that
     * displays Place suggestions.
     * Gets the place id of the selected item and issues a request to the Places Geo Data API
     * to retrieve more details about the place.
     */


    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(),
                        Constants.PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                //finish();
            }
            return false;
        }
        return true;
    }


    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }

    /**
     * Updates the address in the UI.
     */
//    protected void displayAddressOutput() {
//        try {
//
//                txtMyLocation.setText("");
//                txtMyLocation.setText(mAddressOutput);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    @Override
    public void onReturnValue(boolean indicator) {

    }


    /**
     * Receiver for data sent from FetchAddressIntentService.
     */
    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        /**
         * Receives data sent from FetchAddressIntentService and updates the UI in MainActivity.
         */
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            // Display the address string or an error message sent from the intent service.
            mAddressOutput = resultData.getString(Constants.LocationConstants.RESULT_DATA_KEY);
            mAreaOutput = resultData.getString(Constants.LocationConstants.LOCATION_DATA_AREA);
            mCityOutput = resultData.getString(Constants.LocationConstants.LOCATION_DATA_CITY);
            mStateOutput = resultData.getString(Constants.LocationConstants.LOCATION_DATA_STREET);
            // Show a toast message if an address was found.
            if (resultCode == Constants.LocationConstants.SUCCESS_RESULT) {
                txtMyLocation.setText(mStateOutput);
            }
        }
    }


    private void socketLocationUpdate(Location mLastLocation) {
        try {

            Users mechanic = Util.GetUserObjectFromJson(getActivity());
            mechanic.setLatitude(mLastLocation.getLatitude());
            mechanic.setLongitude(mLastLocation.getLongitude());

            OkHttpClient okHttpClient = ConnectionController.client;
            IO.setDefaultOkHttpWebSocketFactory(okHttpClient);
            IO.setDefaultOkHttpCallFactory(okHttpClient);
            IO.Options opts = new IO.Options();
            opts.callFactory = okHttpClient;
            opts.webSocketFactory = okHttpClient;
            socket = IO.socket(Constants.Base_URL, opts);
            socket.connect();
            //System.out.println("After - Emit" + gson.toJson(mechanic));
           socket.emit(Constants.MECHANIC_UPDATE_LOCATION, gson.toJson(mechanic));


        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void setupSocket() {
        try {
            OkHttpClient okHttpClient = ConnectionController.client;

            IO.setDefaultOkHttpWebSocketFactory(okHttpClient);
            IO.setDefaultOkHttpCallFactory(okHttpClient);

            IO.Options opts = new IO.Options();
            opts.callFactory = okHttpClient;
            opts.webSocketFactory = okHttpClient;
            socket = IO.socket(Constants.Base_URL, opts);

            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    Users mechanic = Util.GetUserObjectFromJson(getActivity());
                    socket.emit("mechanic_register", gson.toJson(mechanic));

                }

            }).on(Constants.CUSTOMER_REQUEST, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    JSONObject jsonMechanic = (JSONObject) args[0];
                    Users mechanic = gson.fromJson(jsonMechanic.toString(), Users.class);
                    final JSONObject jsonCustomer = (JSONObject) args[1];
                    Users customer = gson.fromJson(jsonCustomer.toString(), Users.class);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            showRequest(getActivity(), jsonCustomer.toString());
                        }
                    });

                    //System.out.println("Received job request  " + jsonCustomer.toString());
                    //Toast.makeText(getContext(),"mapview" + jsonCustomer.toString() ,Toast.LENGTH_LONG).show();

                    // NotificationUtil notificationUtil = new NotificationUtil(getActivity(),jsonCustomer.toString());
                    // notificationUtil.showNotificationMessage(" Carmonic","A customer needs your help 5km away from your location","","");


                    //Notify the mechanic
                }

            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {

                    System.out.println("========= 1 ===========");
                }

            });
            socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void acceptRequestSocketConnection(final Location mLastLocation,final String customer) {
        try {



           final Users mechanic = Util.GetUserObjectFromJson(getActivity());
            mechanic.setLatitude(mLastLocation.getLatitude());
            mechanic.setLongitude(mLastLocation.getLongitude());

            OkHttpClient okHttpClient = ConnectionController.client;
            IO.setDefaultOkHttpWebSocketFactory(okHttpClient);
            IO.setDefaultOkHttpCallFactory(okHttpClient);
            IO.Options opts = new IO.Options();
            opts.callFactory = okHttpClient;
            opts.webSocketFactory = okHttpClient;
            System.out.println("Before - Accepting");

            socket.emit(Constants.MECHANIC_ACCEPT_JOB);//, gson.toJson(mechanic),customer);
            System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
            System.out.println("after - Accepting");

            if(socket.connected()){

                System.out.println("0000000000000000000000");
            }else{

                System.out.println("NOOOOOOOONOOOOOON");
            }
            socket.connect();

//            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
//
//                @Override
//                public void call(Object... args) {
//                    System.out.println("about to - Accepting");
//
//                    //socketLocationUpdate(mLastLocation);
//                    System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
//                    System.out.println("after - Accepting");
//
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            ShowingAcceptRequest(getActivity(), customer);
//                        }
//                    });
//                }
//
//            });



            System.out.println("after - Accepting");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.toString());
        }
    }


    private void rejectRequestSocketConnection(Location mLastLocation,final String customer) {
        try {

            Users mechanic = Util.GetUserObjectFromJson(getActivity());
            mechanic.setLatitude(mLastLocation.getLatitude());
            mechanic.setLongitude(mLastLocation.getLongitude());

            OkHttpClient okHttpClient = ConnectionController.client;
            IO.setDefaultOkHttpWebSocketFactory(okHttpClient);
            IO.setDefaultOkHttpCallFactory(okHttpClient);
            IO.Options opts = new IO.Options();
            opts.callFactory = okHttpClient;
            opts.webSocketFactory = okHttpClient;
            socket = IO.socket(Constants.Base_URL, opts);
            socket.connect();
            System.out.println("before - Rejecting");
            socket.emit(Constants.MECHANIC_REJECT_JOB, gson.toJson(mechanic),customer);
            System.out.println("after - Rejecting");
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    showingRejectRequest(getActivity(), customer);
                }
            });

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }


    private Users  setCustomer(){
     Users user  =  new Users();
        user.setFirstname("Abiola");
        user.setLastname("Isola");
        user.setPassword("");
        user.setEmail("biola.gold@gmail.com");
        user.setId(1234);
        user.setPhoneNumber("08070987420");
        user.setLatitude(6.63241637947631);
        user.setLongitude(3.51389698684216);
       return  user ;
    }




    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "&mode=DRIVING";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor +"&key=" + Constants.API_KEYS;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        System.out.println("Direction url :::: " + url);

        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }


    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            System.out.println("Result :::: " + result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionClassUtil parser = new DirectionClassUtil();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();
            String distance = "";
            String duration = "";

            if (result.size() < 1) {
                Toast.makeText(getActivity(), "No Points", Toast.LENGTH_SHORT).show();
                return;
            }

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    if (j == 0) {    // Get distance from the list
                        distance = (String) point.get("distance");
                        continue;
                    } else if (j == 1) { // Get duration from the list
                        duration = (String) point.get("duration");
                        continue;
                    }

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(2);
                lineOptions.color(Color.RED);
            }

           // tvDistanceDuration.setText("Distance:" + distance + ", Duration:" + duration);

            // Drawing polyline in the Google Map for the i-th route
            mGoogleMap.addPolyline(lineOptions);
        }
    }


    public  void  createMarker(LatLng source,LatLng destination){



        mGoogleMap.addMarker(new MarkerOptions().position(source).title("Mechanic").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        mGoogleMap.addMarker(new MarkerOptions().position(destination).title("User").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        mGoogleMap.setMapType(MAP_TYPES[1]);
        mGoogleMap.setTrafficEnabled(false);



        /**
         * For the start location, the color of marker is GREEN and
         * for the end location, the color of marker is RED.
         */
//        if (markerPoints.size() == 1) {
//            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
//        } else if (markerPoints.size() == 2) {
//            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//        }



        // Checks, whether start and end locations are captured
        //if (markerPoints.size() >= 2) {
            LatLng origin = source ; //markerPoints.get(0);
            LatLng dest =  destination ;//markerPoints.get(1);

            // Getting URL to the Google Directions API
            String url = getDirectionsUrl(origin, dest);

            DownloadTask downloadTask = new DownloadTask();

            // Start downloading json data from Google Directions API
            downloadTask.execute(url);
       // }
    }

}