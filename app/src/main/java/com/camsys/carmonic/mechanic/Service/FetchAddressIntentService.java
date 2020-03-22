package com.camsys.carmonic.mechanic.Service;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;
import com.camsys.carmonic.mechanic.R;
import com.camsys.carmonic.mechanic.Utilities.Constants;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Asynchronously handles an intent using a worker thread. Receives a ResultReceiver object and a
 * location through an intent. Tries to fetch the address for the location using a Geocoder, and
 * sends the result to the ResultReceiver.
 */
public class FetchAddressIntentService extends IntentService {
    private static final String TAG = "FetchAddressIS";
    public  static final String Action = "com.carmonic.get.address";
    /**
     * The receiver where results are forwarded from this service.
     */
    protected ResultReceiver mReceiver;

    /**
     * This constructor is required, and calls the super IntentService(String)
     * constructor with the name for a worker thread.
     */
    public FetchAddressIntentService() {
        // Use the TAG to name the worker thread.
        super(TAG);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        String errorMessage = "";
       try {
           mReceiver = intent.getParcelableExtra(Constants.LocationConstants.RECEIVER);
           // Check if receiver was properly registered.
           if (mReceiver == null) {
               Log.wtf(TAG, "No receiver received. There is nowhere to send the results.");
               return;
           }
           // Get the location passed to this service through an extra.
           Location location = intent.getParcelableExtra(Constants.LocationConstants.LOCATION_DATA_EXTRA);

           System.out.println(":::::::::::::::::::::::::::::::::::::::::::::::::");
           System.out.println(":::::::I:::am:::here:::to:::start:::the:::::SERVICE::::");
           System.out.println(":::::::::::::::::::::::::::::::::::::::::::::::::");
           System.out.println("::Latitude:::::::" + location.getLatitude() + "::::::::Longitude:::::::::" + location.getLongitude() + ":::::::::::::::::::::::");


           if (location == null) {
               errorMessage = getString(R.string.no_location_data_provided);
               Log.wtf(TAG, errorMessage);
               deliverResultToReceiver(Constants.LocationConstants.FAILURE_RESULT, errorMessage, null);
               return;
           }

           // Errors could still arise from using the Geocoder (for example, if there is no
           // connectivity, or if the Geocoder is given illegal location data). Or, the Geocoder may
           // simply not have an address for a location. In all these cases, we communicate with the
           // receiver using a resultCode indicating failure. If an address is found, we use a
           // resultCode indicating success.

           // The Geocoder used in this sample. The Geocoder's responses are localized for the given
           // Locale, which represents a specific geographical or linguistic region. Locales are used
           // to alter the presentation of information such as numbers or dates to suit the conventions
           // in the region they describe.
           Geocoder geocoder = new Geocoder(this, Locale.getDefault());

           // Address found using the Geocoder.
           List<Address> addresses = null;

           try {
               // Using getFromLocation() returns an array of Addresses for the area immediately
               // surrounding the given latitude and longitude. The results are a best guess and are
               // not guaranteed to be accurate.
               addresses = geocoder.getFromLocation(
                       location.getLatitude(),
                       location.getLongitude(),
                       // In this sample, we get just a single address.
                       1);
               // System.out.println("addresses:: " + addresses.size()+ " addresses:: "  + addresses.get(0).getLongitude());

           } catch (IOException ioException) {
               // Catch network or other I/O problems.
               errorMessage = getString(R.string.service_not_available);
               Log.e(TAG, errorMessage, ioException);
           } catch (IllegalArgumentException illegalArgumentException) {
               // Catch invalid latitude or longitude values.
               errorMessage = getString(R.string.invalid_lat_long_used);
               Log.e(TAG, errorMessage + ". " +
                       "Latitude = " + location.getLatitude() +
                       ", Longitude = " + location.getLongitude(), illegalArgumentException);
           }

           // Handle case where no address was found.
           if (addresses == null || addresses.size() == 0) {
               if (errorMessage.isEmpty()) {
                   errorMessage = getString(R.string.no_address_found);
                   Log.e(TAG, errorMessage);
               }
               deliverResultToReceiver(Constants.LocationConstants.FAILURE_RESULT, errorMessage, null);
           } else {
               Address address = addresses.get(0);
               ArrayList<String> addressFragments = new ArrayList<String>();

               for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                   addressFragments.add(address.getAddressLine(i));

               }
               deliverResultToReceiver(Constants.LocationConstants.SUCCESS_RESULT,
                       TextUtils.join(System.getProperty("line.separator"), addressFragments), address);
               //TextUtils.split(TextUtils.join(System.getProperty("line.separator"), addressFragments), System.getProperty("line.separator"));

           }
       }catch (Exception ex){

           System.out.println(ex.toString());

       }
    }

    /**
     * Sends a resultCode and message to the receiver.
     */
    private void deliverResultToReceiver(int resultCode, String message, Address address) {
        try {
            Bundle bundle = new Bundle();

            if (address != null) {

                bundle.putString(Constants.LocationConstants.RESULT_DATA_KEY, message);
                bundle.putString(Constants.LocationConstants.LOCATION_DATA_AREA, address.getSubLocality());
                bundle.putString(Constants.LocationConstants.LOCATION_DATA_CITY, address.getLocality());
                bundle.putString(Constants.LocationConstants.LOCATION_DATA_STREET, address.getAddressLine(0));
                bundle.putString("countryName", address.getCountryName());
                bundle.putDouble("Latitude", address.getLatitude());
                bundle.putDouble("Longitude", address.getLongitude());

                System.out.print("getLongitude :: " + address.getLongitude() + "getLatitude:: " + address.getLatitude() + "getAddressLine:  " + address.getAddressLine(0));


            } else {
                bundle.putString("Error", "Error");

            }

            mReceiver.send(resultCode, bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
