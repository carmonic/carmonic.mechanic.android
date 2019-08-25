//package com.camsys.carmonic.mechanic.Service;
//
//
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.location.Address;
//import android.location.Geocoder;
//import android.preference.PreferenceManager;
//import android.support.constraint.ConstraintLayout;
//import android.support.v4.app.FragmentActivity;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.camsys.carmonic.networking.BackEndDAO;
//import com.camsys.carmonic.principals.Mechanic;
//import com.camsys.carmonic.principals.User;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.Marker;
//import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//
//import org.json.JSONObject;
//
//import java.io.IOException;
//import java.net.URISyntaxException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Timer;
//import java.util.TimerTask;
//
//import io.socket.client.IO;
//import io.socket.client.Socket;
//import io.socket.emitter.Emitter;
//import okhttp3.Call;
//import okhttp3.Callback;
//import okhttp3.OkHttpClient;
//import okhttp3.Response;
//
////ToDo: Merge this class with MapsActivity, there is no need for them to be separate
////ToDo: Remove the websocket concerns from here
//public class MapsActivityWithLocationConfirmed extends FragmentActivity implements OnMapReadyCallback {
//
//    private GoogleMap mMap;
//    private String locationAddress;
//    private double longitude;
//    private double latitude;
//    private LatLng customerPosition = null;
//    private Marker customerMarker = null;
//    private LatLng mechanicPosition = null;
//    private Marker mechanicMarker = null;
//
//    private ConstraintLayout popUpConstraintLayout;
//    private ConstraintLayout metadataConstraintLayout;
//    private TextView mechanicName;
//    private TextView mechanicDistanceMessage;
//    private TextView mechanicStarRating;
//    private ImageView mechanicImage; //ToDo: fetch mechanic image from backend
//    private TextView popUpMessage;
//
//    private Socket socket;
//    private List<Mechanic> mechanicList; //list of closest mechanics to user
//    private User user;
//    private boolean mechanicJobAccepted; //true if a mechanic has accepted the job
//    private Gson gson = new Gson();
//
//    private static int MECHANIC_TIME_OUT = 8000;
//    private Timer timer = new Timer();
//    int i = 0;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_maps_with_location_confirmed);
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
//
//        popUpConstraintLayout = findViewById(R.id.networkActivityPopUp);
//        popUpMessage = findViewById(R.id.txtVwScreen2SubTitle);
//        mechanicName = findViewById(R.id.mechanicName);
//        mechanicDistanceMessage = findViewById(R.id.mechanicDistanceMessage);
//        mechanicStarRating = findViewById(R.id.mechanicStarRating);
//        metadataConstraintLayout = findViewById(R.id.metadataConstraint);
//        metadataConstraintLayout.setVisibility(View.INVISIBLE);
//
//        //This will be available if we came from the address confirmation page
//        locationAddress = getIntent().getStringExtra("locationAddress");
//
//        //This will be available if we came from the previous maps page
//        longitude = getIntent().getDoubleExtra("longitude", 0.0);
//        latitude = getIntent().getDoubleExtra("latitude", 0.0);
//
//        mapFragment.getMapAsync(this);
//    }
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//
//        Geocoder geoCoder = new Geocoder(getApplicationContext());
//        List<Address> addresses;
//        customerPosition = null;
//
//        try {
//            if (locationAddress != null) {
//                addresses = geoCoder.getFromLocationName(locationAddress, 5);
//                // ToDo: Handle could not resolve this address, throw error or log
//                if (addresses != null || addresses.size() > 0) {
//                    Address location = addresses.get(0);
//                    customerPosition = new LatLng(location.getLatitude(), location.getLongitude());
//                }
//            } else {
//                customerPosition = new LatLng(latitude, longitude);
//            }
//            customerMarker = mMap.addMarker(new MarkerOptions().position(customerPosition));
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(customerPosition));
//            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
//
//            setupSocket();
//            getMechanics();
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//    }
//
//    private void getMechanics() {
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
//        Gson gson = new Gson();
//        String token = preferences.getString("Authorisation", "");
//        user = gson.fromJson(preferences.getString("User", ""), User.class);
//
//        BackEndDAO.getMechanics(customerPosition.longitude, customerPosition.latitude, token, new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String responseBodyString = response.body().string();
//                Gson gson = new Gson();
//                mechanicList = gson.fromJson(responseBodyString, new TypeToken<ArrayList<Mechanic>>(){}.getType());
//                notifyMechanics();
//            }
//        });
//    }
//
//    private void setupSocket() {
//        try {
//            OkHttpClient okHttpClient = BackEndDAO.getClient();
//
//            IO.setDefaultOkHttpWebSocketFactory(okHttpClient);
//            IO.setDefaultOkHttpCallFactory(okHttpClient);
//
//            IO.Options opts = new IO.Options();
//            opts.callFactory = okHttpClient;
//            opts.webSocketFactory = okHttpClient;
//
//            socket = IO.socket(BackEndDAO.getBackendURL(), opts);
//            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
//
//                @Override
//                public void call(Object... args) {
//                    socket.emit("customer_register", gson.toJson(user));
//                }
//
//            }).on("job_accept", new Emitter.Listener() {
//
//                @Override
//                public void call(Object... args) {
//                    JSONObject jsonObject = (JSONObject) args[0];
//                    Mechanic mechanic = gson.fromJson(jsonObject.toString(), Mechanic.class);
//                    mechanicJobAccepted = true;
//                    mechanicName.setText(mechanic.getName());
//                    BackEndDAO.getEstimatedDistance(mechanic.getLongitude(), mechanic.getLatitude(), customerPosition.longitude, customerPosition.latitude, user.getToken(), new Callback() {
//                        @Override
//                        public void onFailure(Call call, IOException e) {
//                            e.printStackTrace();
//                        }
//
//                        @Override
//                        public void onResponse(Call call, Response response) throws IOException {
//                            mechanicDistanceMessage.setText(generateProximityMessage(mechanic.getName(), response.body().string()));
//                            MapsActivityWithLocationConfirmed.this.runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    popUpConstraintLayout.setVisibility(View.INVISIBLE);
//                                    metadataConstraintLayout.setVisibility(View.VISIBLE);
//                                }
//                            });
//                        }
//                    });
//                }
//
//            }).on("job_start", new Emitter.Listener() {
//
//                @Override
//                public void call(Object... args) {
//                    JSONObject jsonObject = (JSONObject) args[0];
//                    Mechanic mechanic = gson.fromJson(jsonObject.toString(), Mechanic.class);
//                }
//
//            }).on("job_conclude", new Emitter.Listener() {
//
//                @Override
//                public void call(Object... args) {
//                    JSONObject jsonObject = (JSONObject) args[0];
//                    Mechanic mechanic = gson.fromJson(jsonObject.toString(), Mechanic.class);
//                    Intent i = new Intent(getApplicationContext(), edit_mech_complmt.class);
//                    i.putExtra("id", mechanic.getId());
//                    startActivity(i);
//                }
//
//            }).on("update_location", new Emitter.Listener() {
//
//                @Override
//                public void call(Object... args) {
//                    JSONObject jsonObject = (JSONObject) args[0];
//                    Mechanic mechanic = gson.fromJson(jsonObject.toString(), Mechanic.class);
//                    mechanicPosition = new LatLng(mechanic.getLatitude(), mechanic.getLongitude());
//                    MapsActivityWithLocationConfirmed.this.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (mechanicMarker == null) {
//                                mechanicMarker = mMap.addMarker(new MarkerOptions().position(mechanicPosition));
//                            } else {
//                                mechanicMarker.setPosition(mechanicPosition);
//                            }
//                            mMap.moveCamera(CameraUpdateFactory.newLatLng(mechanicPosition));
//                            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
//
//                            user.setLatitude(customerPosition.latitude);
//                            user.setLongitude(customerPosition.longitude);
//
//                            socket.emit("customer_update_location", gson.toJson(mechanic), gson.toJson(user));
//                        }
//                    });
//                }
//
//            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
//
//                @Override
//                public void call(Object... args) {
//                }
//
//            });
//            socket.connect();
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//    }
//
//    // ToDo: Extract this logic from the class
//    private void notifyMechanics() {
//        timer.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                if (i >= mechanicList.size()) {
//                    // Run through all the mechanics and none accepted
//                    timer.cancel();
//                    timer.purge();
//                    i = 0;
//                    MapsActivityWithLocationConfirmed.this.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            popUpMessage.setText("No mechanics available, try again later");
//                        }
//                    });
//                }
//
//                if (!mechanicJobAccepted && i < mechanicList.size()) {
//                    socket.emit("customer_request_job", gson.toJson(mechanicList.get(i)), gson.toJson(user));
//                    i++;
//                } else {
//                    // Job accepted
//                    // ToDo: update UI with details of mechanic
//                    timer.cancel();
//                    timer.purge();
//                    i = 0;
//                }
//            }
//        }, 0, MECHANIC_TIME_OUT);
//    }
//
//    private String generateProximityMessage(String firstname, String distance) {
//        return firstname + " is " + distance + " away";
//    }
//}
