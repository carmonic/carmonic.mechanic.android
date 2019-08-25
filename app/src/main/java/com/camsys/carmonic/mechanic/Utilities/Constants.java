package com.camsys.carmonic.mechanic.Utilities;

import android.os.SystemClock;
import com.google.android.gms.maps.GoogleMap;


public class Constants {


    public static final String USER_KEY = "userKey";
    public static  String TAG = "CarmonicLog";

    public static String API_KEYS = "AIzaSyCrNr-TwLxRsDV2obUiVRvrpYecomSfDcc";
    public static String SERVICE_KEYS = "256c33bcc75b45bb95041b51bdd6fedf";
//192.168.43.237

    public  static  String URL =  "https://ec2-35-177-219-101.eu-west-2.compute.amazonaws.com:8443/";
    public  static  String  Base_URL = "https://192.168.43.237:8443/";// "https://ec2-35-177-219-101.eu-west-2.compute.amazonaws.com:8443/";



    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;


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

    public  static final String Get_Mechanic_Request = "";
    public  static final String MECHANIC_UPDATE_LOCATION = "mechanic_update_location";

    public  static final String CUSTOMER_REQUEST = "job_request";
    public  static  final String mechanic_accept_job = "mechanic_accept_job";


    public class LocationConstants {
        public static final int SUCCESS_RESULT = 0;
        public static final int FAILURE_RESULT = 1;
        public static final String PACKAGE_NAME = "com.camsys.carmonic.mechanic";
        public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
        public static final String RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY";
        public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_DATA_EXTRA";
        public static final String LOCATION_DATA_AREA = PACKAGE_NAME + ".LOCATION_DATA_AREA";
        public static final String LOCATION_DATA_CITY = PACKAGE_NAME + ".LOCATION_DATA_CITY";
        public static final String LOCATION_DATA_STREET = PACKAGE_NAME + ".LOCATION_DATA_STREET";

    }


    public class SetAction {

        public static final String PACKAGE_NAME = "com.camsys.carmonic.mechanic";
        public static final String MECHANIC_REQUEST = PACKAGE_NAME + ".request";
        public static final String LOGIN = PACKAGE_NAME + ".login";



    }


}
