package com.camsys.carmonic.mechanic.Utilities;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;


import java.io.File;
import java.util.ArrayList;

public class QoothoDB extends SQLiteOpenHelper {

    // Logcat tag
    public static final String LOG = "DatabaseHelper";

    // Database Version
    public static final int DATABASE_VERSION = 6;


    // Table Names
    public static final String TABLE_USERS = "Users";
    public static final String TABLE_UTILITY = "Utility";
    public static final String TABLE_LOGIN_DETAIL = "TokenDetail";
    public static final String TABLE_PASSENGER_ACCOUNT = "PassengerAccount";
    public static final String TABLE_RIDER_TYPE = "RiderType";
    //BankName
    public static final String KEY_ID = "Id";
    public static final String KEY_USER_TOKEN = "token";
    public static final String KEY_TIME_EXPIRES = "timeExpires";
    public static final String KEY_EXPIRE_DATE = "expireDate";
    public static final String KEY_EXPIRE_IN = "expireIn";
    public static final String KEY_TOKEN_TYPE = "tokenType";
    public static final String KEY_INSTITUTION_ID = "institutionID";
    public static final String KEY_INSTITUTION_NAME = "institutionName";

    public static final String KEY_RIDER_ID = "riderId";
    public static final String KEY_RIDER_NAME = "rideTypeName";
    public static final String KEY_BASE_FARE = "baseFare";
    public static final String KEY_FARE_PER_KM = "farePerKm";
    public static final String KEY_FARE_PER_MINUTE = "farePerMinute";
    public static final String KEY_CANCELLATION_CHARGE = "cancellationCahrge";
    public static final String KEY_CHARGE_GRACE_PERIOD = "chargeGracePeriod";




    private static final String DATABASE_NAME = "QoothoDB";
    private static final File FILE_DIR = null;
    private static final String KEY_COUNTRY_ID = "countryID";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_DOB = "dob";
    private static final String KEY_FIRST_NAME = "firstName";
    private static final String KEY_HOME_ADDRESS = "homeAddress";
    private static final String KEY_HOME_LATITUDE = "homeLat";
    private static final String KEY_HOME_LONGITUDE = "homeLong";
    private static final String KEY_PERSONAL_REFERAL_CODE = "personalReferralCode";
    private static final String KEY_PHONE_NUMBER = "phoneNumber";
    private static final String KEY_PHOTO = "photo";
    private static final String KEY_REGISTER_DATE = "registeredDate";
    private static final String KEY_SURNAME = "surname";
    private static final String KEY_WORK_ADDRESS = "workAddress";
    private static final String KEY_WORK_LATITUDE = "workLat";
    private static final String KEY_WORK_LONGITUDE = "workLong";
    private static final String KEY_HAS_PAYMENT_CARD ="hasPaymentCard" ; ////"":true,"paymentCardType":null}
    private static final String KEY_PAYMENT_CARD_TYPE = "paymentCardType";
    private static final String KEY_ISSUE_DATE = "issueDate";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_DATE_CREATED = "DateCreated";
    // create registration
    public static final String CREATE_PASSENGER_ACCOUNT = "CREATE TABLE "
            + TABLE_PASSENGER_ACCOUNT + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_USERNAME + " TEXT,"
            + KEY_INSTITUTION_ID + " TEXT,"
            + KEY_INSTITUTION_NAME + " TEXT, "
            + KEY_DATE_CREATED + " DATETIME DEFAULT CURRENT_TIMESTAMP " + ")";

    // create registration
    public static final String CREATE_RIDER_TYPE = "CREATE TABLE "
            + TABLE_RIDER_TYPE + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_RIDER_ID + " TEXT,"
            + KEY_USERNAME + " TEXT,"
            + KEY_RIDER_NAME + " TEXT, "
            + KEY_BASE_FARE + " TEXT,"
            + KEY_FARE_PER_KM + " TEXT,"
            + KEY_FARE_PER_MINUTE + " TEXT, "
            + KEY_CANCELLATION_CHARGE + " TEXT, "
            + KEY_CHARGE_GRACE_PERIOD + " TEXT, "
            + KEY_DATE_CREATED + " DATETIME DEFAULT CURRENT_TIMESTAMP " + ")";

    // create registration
    public static final String CREATE_TABLE_TOKEN = "CREATE TABLE "
            + TABLE_LOGIN_DETAIL + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_USER_TOKEN + " TEXT,"
            + KEY_TIME_EXPIRES + " TEXT,"
            + KEY_ISSUE_DATE + " DATETIME,"
            + KEY_EXPIRE_DATE + " DATETIME,"
            + KEY_USERNAME + " TEXT,"
            + KEY_EXPIRE_IN + " TEXT, "
            + KEY_TOKEN_TYPE + " TEXT,"
            + KEY_DATE_CREATED + " DATETIME DEFAULT CURRENT_TIMESTAMP " + ")";
    // create registration
    public static final String CREATE_TABLE_USER = "CREATE TABLE "
            + TABLE_USERS + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_COUNTRY_ID + " TEXT,"
            + KEY_EMAIL + " TEXT,"
            + KEY_DOB + " TEXT,"
            + KEY_FIRST_NAME + " TEXT,"
            + KEY_HOME_ADDRESS + " TEXT,"
            + KEY_HOME_LATITUDE + " TEXT, "
            + KEY_HOME_LONGITUDE + " TEXT,"
            + KEY_PERSONAL_REFERAL_CODE + " TEXT,"
            + KEY_PHONE_NUMBER + " TEXT,"
            + KEY_PHOTO + " TEXT,"
            + KEY_REGISTER_DATE + " TEXT,"
            + KEY_SURNAME + " TEXT,"
            + KEY_WORK_ADDRESS + " TEXT,"
            + KEY_WORK_LATITUDE + " TEXT,"
            + KEY_WORK_LONGITUDE + " TEXT,"
            + KEY_HAS_PAYMENT_CARD + " TEXT,"
            + KEY_PAYMENT_CARD_TYPE + " TEXT,"
            + KEY_USERNAME + " TEXT,"
            + KEY_DATE_CREATED + " DATETIME DEFAULT CURRENT_TIMESTAMP " + ")";
    private static final String KEY_COUNTRY_CODE = "CountryCode";
    // create registration
    public static final String CREATE_UTILITY = "CREATE TABLE "
            + TABLE_UTILITY + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_USERNAME + " TEXT,"
            + KEY_COUNTRY_CODE + " TEXT, "
            + KEY_DATE_CREATED + " DATETIME DEFAULT CURRENT_TIMESTAMP " + ")";
    private Context ctx;


    public QoothoDB(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //SQLiteDatabase.openOrCreateDatabase(DATABASE_NAME,null);
        /*super(context, Environment.getExternalStorageDirectory()
                + File.separator + FILE_DIR
	            + File.separator + DATABASE_NAME, null, DATABASE_VERSION);
		  SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory()
		            + File.separator + FILE_DIR
		            + File.separator + DATABASE_NAME,null);
		*/
        //	insertRole();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_TOKEN);
        db.execSQL(CREATE_UTILITY);
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_PASSENGER_ACCOUNT);
        db.execSQL(CREATE_RIDER_TYPE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.i("DATABASE_UPGRADE", "oldVersion=" + oldVersion + " newVersion=" + newVersion);
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_UTILITY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN_DETAIL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PASSENGER_ACCOUNT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RIDER_TYPE);

        onCreate(db);
    }


    ////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////CRUD OPERATION //////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////

    private SQLiteDatabase getDatabaseContext() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db;
    }



    public boolean saveCountryCode(Context ctx, String token, String username) {
        SQLiteDatabase db = getDatabaseContext();
        try {
            //db = context.openOrCreateDatabase(DATABASE_NAME, 0, null);
            ContentValues values = new ContentValues();
            values.put(KEY_COUNTRY_CODE, token);
            values.put(KEY_USERNAME, username);
            boolean returnValue = db.insert(TABLE_UTILITY, null, values) > 0;
            db.close();
            return (returnValue);
        } catch (Exception ex) {
            db.close();
            return false;
        }
    }



    /////////////GET QUERY /////////////


    public Cursor getUSerByUsername(String profileId) {
        Cursor cursor = null;
        //String selectQuery = "SELECT  * FROM " + TABLE_CARDS +" WHERE ProfileId ="+ profileId ;
        String selectQuery = "SELECT  * FROM " + TABLE_USERS + " WHERE username ='" + profileId + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Log.e(LOG, selectQuery);
        cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }



    public Cursor getCountryCodeByUSername(String profileId) {
        Cursor cursor = null;
        //String selectQuery = "SELECT  * FROM " + TABLE_CARDS +" WHERE ProfileId ="+ profileId ;
        String selectQuery = "SELECT  * FROM " + TABLE_UTILITY + " WHERE username ='" + profileId + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Log.e(LOG, selectQuery);
        cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }


    public Cursor getToken() {
        Cursor cursor;
        String selectQuery = "SELECT  * FROM " + TABLE_LOGIN_DETAIL;
        SQLiteDatabase db = this.getReadableDatabase();
        Log.e(LOG, selectQuery);
        cursor = db.rawQuery(selectQuery, null);
        Log.i("getCount- Token ", cursor.getCount() + "");
        return cursor;
    }

    public void delete(Context ctx, String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.i("DELETE", "<< DELETE IN PROGRESSSSSSSSSSSSS>>> " + tableName);
        try {
            //db = ctx.openOrCreateDatabase(DATABASE_NAME, 0, null);
            int rowsAffected = db.delete(tableName, "", null);
            boolean returnValue = false;
            if (rowsAffected > 0) {
                returnValue = true;
            }

            db.close();
        } catch (Exception ex) {
            db.close();
            ex.printStackTrace();
        }
    }

    public void delete(Context ctx, String tableName, String empty) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.i("DELETE", "<< DELETE IN PROGRESSSSSSSSSSSSS>>> " + tableName);
        try {
            int rowsAffected = db.delete(tableName, "", null);
            boolean returnValue = false;
            if (rowsAffected > 0) {
                returnValue = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
