package com.camsys.carmonic.mechanic.Utilities;

import android.os.SystemClock;
import com.google.android.gms.maps.GoogleMap;

/**
 * Created by macbookpro on 10/06/2017.
 */

public class Constants {

    public static final String DOING_BACKGROUND_SERVICE_RESULT = "Operation not successfull, try again later";
    public static final String INTERNET_CONNECTION_ERROR = "Internet connection is not available at moment";
    public static final String ERROR_CODE = "104";
    public static final long SESSION_TIMOUT_PERIOD = 300000; //5mins
    public static final long ALARM_TRIGGER_AT_TIME = SystemClock.elapsedRealtime() + 30000;
    public static final int CONNECTION_TIMEOUT = 60000;  //1min
    public static final int SOCKET_CONNECTION_TIMEOUT = 90000;  //2min
    public static String LOG_TAG = "WEMAMOBILE";

    public static String API_KEYS = "AIzaSyCrNr-TwLxRsDV2obUiVRvrpYecomSfDcc";
    public static String SERVICE_KEYS = "256c33bcc75b45bb95041b51bdd6fedf";

    public  static  String URL = "https://ec2-35-177-219-101.eu-west-2.compute.amazonaws.com:8443";
    public  static  String  Base_URL = "http://ec2-35-177-219-101.eu-west-2.compute.amazonaws.com:3000/";



    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";
    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";
    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;
    public static final String SHARED_PREF = "ah_firebase";
    public static final String BOOK_RIDE_DESTINATION = "bookRideDestination";
    public static final String BOOK_RIDE_SOURCE = "bookRideSource";

    // Shared pref mode
    public static final int PRIVATE_MODE = 0;
    public static final String PREF_NAME = "Mechanic";
    public static final String IS_LOG_IN = "IsLoggedIn";
    public static final String IS_LOG_OUT = "IsLoggedOut";


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static int[] MAP_TYPES = {GoogleMap.MAP_TYPE_SATELLITE,
            GoogleMap.MAP_TYPE_NORMAL,
            GoogleMap.MAP_TYPE_HYBRID,
            GoogleMap.MAP_TYPE_TERRAIN,
            GoogleMap.MAP_TYPE_NONE};


    public class LocationConstants {
        public static final int SUCCESS_RESULT = 0;

        public static final int FAILURE_RESULT = 1;

        public static final String PACKAGE_NAME = "com.sample.sishin.maplocation";

        public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";

        public static final String RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY";

        public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_DATA_EXTRA";

        public static final String LOCATION_DATA_AREA = PACKAGE_NAME + ".LOCATION_DATA_AREA";
        public static final String LOCATION_DATA_CITY = PACKAGE_NAME + ".LOCATION_DATA_CITY";
        public static final String LOCATION_DATA_STREET = PACKAGE_NAME + ".LOCATION_DATA_STREET";


    }


}
