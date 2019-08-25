package com.camsys.carmonic.mechanic;

import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;


import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.camsys.carmonic.mechanic.Account.AccountFragment;
import com.camsys.carmonic.mechanic.Help.HelpFragment;
import com.camsys.carmonic.mechanic.History.HistoryFragment;
import com.camsys.carmonic.mechanic.MapView.MapViewFragment;
import com.camsys.carmonic.mechanic.Model.HistoryItem;
import com.camsys.carmonic.mechanic.Model.Users;
import com.camsys.carmonic.mechanic.Profile.ProfileFragment;
import com.camsys.carmonic.mechanic.Service.TestService;
import com.camsys.carmonic.mechanic.Utilities.Constants;
import com.camsys.carmonic.mechanic.Utilities.SharedData;
import com.camsys.carmonic.mechanic.Utilities.Util;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import org.w3c.dom.Text;

import java.util.List;

public class MainActivity extends AppCompatActivity
implements NavigationView.OnNavigationItemSelectedListener, HistoryFragment.OnListFragmentInteractionListener,
                            ProfileFragment.OnFragmentInteractionListener,AccountFragment.OnFragmentInteractionListener,
                            HelpFragment.OnFragmentInteractionListener{

    Gson gson  = null;
    SharedData sharedData =  null;
    int navActiveId;
    DrawerLayout drawer;
    Menu drawerMenu;
    NavigationView navigationView;

    String regID;
    TextView txtBack;
    TextView txtNext;
    TextView title;

    private GoogleMap mMap;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    String  TripReq ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedData =  new SharedData(getApplicationContext());
         String userkey = sharedData.Get(Constants.USER_KEY,"");
         gson  =  new Gson();
//        if(userkey == ""){
//            sharedData.Clear(Constants.USER_KEY);
//            startActivity(new Intent(getApplicationContext(),LandingPage.class));
//            finish();
//            return;
//        }
         Users user  =  gson.fromJson(userkey,Users.class);
         System.out.println("------------------- " + userkey);


        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.TRANSPARENT);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Status bar :: Transparent
        Window window = this.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }





        title = (TextView) toolbar.findViewById(R.id.txtTitle);
        title.setTextSize((float) 16.0);
        title.setText("");
        // title.setText(getResources().getString(R.string.app_name));
        txtBack = (TextView) toolbar.findViewById(R.id.txtBack);
        txtNext = (TextView) toolbar.findViewById(R.id.txtNext);
        txtNext.setVisibility(View.INVISIBLE);
        txtBack.setVisibility(View.INVISIBLE);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();



        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorYellow)));
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.inflateHeaderView(R.layout.nav_header);
        ImageView imageView = (ImageView)headerView.findViewById(R.id.user_picture);
        TextView txtFName = (TextView) headerView.findViewById(R.id.fName);
        TextView txtUsername = (TextView) headerView.findViewById(R.id.username);
        final Switch indicator =  (Switch)headerView.findViewById(R.id.switchAB) ;
//        final TextView txtFName= (TextView)navigationView.findViewById(R.id.fName);
//        final TextView txtUsername= (TextView)navigationView.findViewById(R.id.username);

        System.out.println("$$$$$$$$$$$$" + user.getFirstname());
        txtFName.setText(user.getLastname());
         txtUsername.setText(user.getEmail());

        indicator.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {

                    if(Util.checkConnectivity(getApplicationContext())) {
                        indicator.setText("Online");
                        indicator.setTextColor(getResources().getColor(R.color.colorYellow));
                        startService(new Intent(getApplicationContext(), TestService.class));

                    }else{
                        Toast.makeText(getApplicationContext(), "No internet connection available.", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    if(Util.checkConnectivity(getApplicationContext())) {

                        indicator.setText("Offline");
                        indicator.setTextColor(getResources().getColor(R.color.colorWhite));
                        stopService(new Intent(getApplicationContext(), TestService.class));

                    }else{
                        Toast.makeText(getApplicationContext(), "No internet connection available.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

System.out.println("===getACtion====" + getIntent().getAction());

        loadFragment(getIntent());


    }

    public void loadFragment(Intent intent) {

        Fragment fragment = MapViewFragment.newInstance(intent);     //new MapViewFragment(TripReq);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mapFrame, fragment);
        ft.commit();

    }



    @Override
    protected void onResume() {
        super.onResume();



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.landing_page, menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return true;

    }


    @Override
    protected void onNewIntent(Intent intent) {

        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the

//        int id = item.getItemId();
//        if (id == R.id.p) {
//            //Toast.makeText(getApplicationContext()," iti sfrom the main activty ",Toast.LENGTH_LONG).show();
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Menu menu = navigationView.getMenu();
        navActiveId = item.getItemId();
        displayDrawerItem(navActiveId);
        return true;
    }


    public void displayDrawerItem(int menuItemId) {
        Fragment fragment = null;
        String title = "";
        //boolean hasFab = false;
        boolean noTitle = false;
        switch (menuItemId) {
            case R.id.nav_history: {
                  fragment = HistoryFragment.newInstance();
                  title="Dashboard";
                  break;
              }

            case R.id.nav_account: {
                fragment = AccountFragment.newInstance();
                title="Account";
                break;
            }

            case R.id.nav_profle: {
                fragment = ProfileFragment.newInstance();
                title="Account";
                break;
            }

            case R.id.nav_help: {
                fragment = HelpFragment.newInstance();
                title="Account";
                break;
            }

            default: {
            // fragment = new MapFragment();
            break;
        }
        }


        //check the menu directly
        if (drawerMenu == null) {
            drawerMenu = navigationView.getMenu();
        }

        MenuItem menuItem = drawerMenu.findItem(menuItemId);

        if (TextUtils.isEmpty(title)) {
            try {
                CharSequence titleChars = menuItem.getTitle();

                if (!TextUtils.isEmpty(titleChars)) {
                    title = titleChars.toString();
                }
            } catch (Exception e) {

            }
        }

        if (!noTitle && !TextUtils.isEmpty(title)) {
            // set the toolbar title
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(title);
            }
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mapFrame, fragment);
            ft.commit();

            /*if(hasFab){
                //fabSearch.show();
                fab.show();
            }*/
        }

        //just in case it was called manually
        menuItem.setChecked(true);  //.setCheckedItem(menuItemId);

        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onListFragmentInteraction(HistoryItem item) {

    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//
//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//    }

//    @Override
//    public void onBackPressed() {
//
//        List fragmentList = getSupportFragmentManager().getFragments();
//
//        boolean handled = false;
//        for(Fragment f : fragmentList) {
//            if(f instanceof BaseFragment) {
//                handled = ((BaseFragment)f).onBackPressed();
//
//                if(handled) {
//                    break;
//                }
//            }
//        }
//
//        if(!handled) {
//            super.onBackPressed();
//        }
//    }


}
