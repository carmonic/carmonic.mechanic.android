package com.camsys.carmonic.mechanic.Utilities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.*;
import android.media.ExifInterface;
import android.media.FaceDetector;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;


import androidx.annotation.IdRes;
import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.camsys.carmonic.mechanic.Model.UserModel;
import com.camsys.carmonic.mechanic.Model.Users;
import com.camsys.carmonic.mechanic.R;
import com.google.android.gms.location.ActivityTransition;
import com.google.gson.Gson;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by macbookpro on 07/06/2017.
 */

public class Util {


    public static final int photoMaxLength = 640;




    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @author paulburke
     */
    @SuppressLint("NewApi")
    public static String getFilePath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }



    public static boolean checkConnectivity(Context context) {

        ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();

    }

    public static void hideSoftKeyboard(Activity activity,View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);

        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void performAnimationRelativeLayout(RelativeLayout viewToAnimate) {
        AnimationSet set = new AnimationSet(true);
        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(500);
        set.addAnimation(animation);
        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(500);
        set.addAnimation(animation);

        LayoutAnimationController controller = new LayoutAnimationController(
                set, 0.5f);
        //RelativeLayout loading = (RelativeLayout) findViewById(R.id.loading);
        viewToAnimate.setVisibility(View.VISIBLE);
        viewToAnimate.setLayoutAnimation(controller);
    }

    public static void performAnimation(LinearLayout viewToAnimate) {
        AnimationSet set = new AnimationSet(true);
        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(500);
        set.addAnimation(animation);
        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(500);
        set.addAnimation(animation);
        LayoutAnimationController controller = new LayoutAnimationController(
                set, 0.5f);
        //RelativeLayout loading = (RelativeLayout) findViewById(R.id.loading);
        viewToAnimate.setVisibility(View.VISIBLE);
        viewToAnimate.setLayoutAnimation(controller);
    }


    @SuppressLint("MissingPermission")
    public static String GetIME1(Context context) {
        String deviceID = String.valueOf(System.currentTimeMillis());
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (telephonyManager != null)
                deviceID = telephonyManager.getDeviceId();
            if (deviceID == null || deviceID.length() == 0)
                deviceID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e) {

        }
        return deviceID;
    }

    public static String GetDeviceCountryCode(Context context) {
        String mPhoneNumber = String.valueOf(System.currentTimeMillis());
        try {
            TelephonyManager tMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            mPhoneNumber = tMgr.getSimCountryIso();

        } catch (Exception e) {
            mPhoneNumber = "";
        }
        return mPhoneNumber;
    }

    public static String GetNetworkCountryIso(Context context) {
        String mPhoneNumber = String.valueOf(System.currentTimeMillis());
        try {
            TelephonyManager tMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            mPhoneNumber = tMgr.getNetworkCountryIso();
        } catch (Exception e) {
            mPhoneNumber = "";
        }
        return mPhoneNumber;
    }

    @SuppressLint("MissingPermission")
    public static String GetDevicePhoneNumber(Context context) {
        String mPhoneNumber = "N/A";
        try {
            TelephonyManager tMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            mPhoneNumber = tMgr.getLine1Number();
            if (mPhoneNumber.length() == 0)
                mPhoneNumber = "N/A";
        } catch (Exception e) {
            mPhoneNumber = "N/A";
        }
        return mPhoneNumber;
    }

    public static String GetSimOperatorName(Context context) {
        String mPhoneNumber = "N/A";
        try {
            TelephonyManager tMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            mPhoneNumber = tMgr.getSimOperatorName();
        } catch (Exception e) {
            mPhoneNumber = "N/A";
        }
        return mPhoneNumber;
    }

    @SuppressLint("MissingPermission")
    public static String GetCellLocation(Context context) {
        String mPhoneNumber = "N/A";
        try {
            TelephonyManager tMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            mPhoneNumber = tMgr.getCellLocation().toString();
            if (mPhoneNumber.length() == 0)
                mPhoneNumber = "N/A";
        } catch (Exception e) {
            mPhoneNumber = "N/A";
        }
        return mPhoneNumber;
    }

    @SuppressLint("MissingPermission")
    public static String GetSIMSerialNumber(Context context) {
        String mPhoneNumber = "N/A";
        try {
            TelephonyManager tMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            mPhoneNumber = tMgr.getSimSerialNumber();
            if (mPhoneNumber.length() == 0)
                mPhoneNumber = "N/A";
        } catch (Exception e) {
            mPhoneNumber = "N/A";
        }
        return mPhoneNumber;
    }

    @SuppressLint("MissingPermission")
    public static String GetDeviceSoftwareVersion(Context context) {
        String mPhoneNumber = "N/A";
        try {
            TelephonyManager tMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            mPhoneNumber = tMgr.getDeviceSoftwareVersion();
            if (mPhoneNumber.length() == 0)
                mPhoneNumber = "N/A";
        } catch (Exception e) {
            mPhoneNumber = "N/A";
        }
        return mPhoneNumber;
    }

    public static String getB64Auth(String content) {
        String source = content;
        String ret = "Bearer " + content; //Base64.encodeToString(source.getBytes(),Base64.URL_SAFE|Base64.NO_WRAP);
        Log.i("ret:: ", ret);
        return ret;
    }

//    public static String GetAPIKeys(String mobileNumber) {
//        String base64 = null;
//        try {
//            mobileNumber = rightPadZeros(mobileNumber, 24);
//            Log.i("mobileNumber", mobileNumber);
//            String paddedUserId = Constants.SERVICE_KEYS.concat(mobileNumber);
//            paddedUserId = paddedUserId.concat(Constants.API_SECRETE);
//            System.out.println("paddedUserId::: " + paddedUserId);
//            base64 = "Bearer " + ProcessToken(paddedUserId);
//            Log.i("base64:: ", base64);
//        } catch (Exception e) {
//            e.printStackTrace();
//            base64 = "";
//
//        }
//
//        return base64;
//
//    }

    public static String hmacSha256(String KEY, byte[] VALUE) {
        return hmacSha(KEY, VALUE, "HmacSHA256");
    }

    private static String hmacSha(String KEY, byte[] VALUE, String SHA_TYPE) {
        try {
            SecretKeySpec signingKey = new SecretKeySpec(KEY.getBytes("UTF-8"), SHA_TYPE);
            Mac mac = Mac.getInstance(SHA_TYPE);
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(VALUE); //.getBytes("UTF-8")
            byte[] hexArray = {
                    (byte) '0', (byte) '1', (byte) '2', (byte) '3',
                    (byte) '4', (byte) '5', (byte) '6', (byte) '7',
                    (byte) '8', (byte) '9', (byte) 'a', (byte) 'b',
                    (byte) 'c', (byte) 'd', (byte) 'e', (byte) 'f'
            };
            byte[] hexChars = new byte[rawHmac.length * 2];
            for (int j = 0; j < rawHmac.length; j++) {
                int v = rawHmac[j] & 0xFF;
                hexChars[j * 2] = hexArray[v >>> 4];
                hexChars[j * 2 + 1] = hexArray[v & 0x0F];
            }


            return new String(hexChars);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


    private static String rightPadZeros(String str, int num) {
        return String.format("%1$-" + num + "s", str).replace(' ', '0');
    }

    private static String ProcessToken(String mobile) {
        try {
            //String mobile = "256c33bcc75b45bb95041b51bdd6fedf080281874560000000000000W@laBwa!at3@2o17";
            MessageDigest md = MessageDigest.getInstance("MD5");

            StringBuilder hexString = new StringBuilder();
            for (byte digestByte : md.digest(mobile.getBytes()))
                hexString.append(String.format("%02x", digestByte));

            byte[] data = new byte[0];
            try {
                data = hexString.toString().getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            String base64 = Base64.encodeToString(data, Base64.DEFAULT);

            System.out.println("WHY ME :: " + base64);
            return base64;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String GetStringMD5(String password) throws Exception {

        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());

        byte byteData[] = md.digest();

    /*//convert the byte to hex format method 1
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < byteData.length; i++) {
     sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
    }

    System.out.println("Digest(in hex format):: " + sb.toString());*/

        //convert the byte to hex format method 2
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            String hex = Integer.toHexString(0xff & byteData[i]);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        System.out.println("Digest(in hex format):: " + hexString.toString());
        return hexString.toString();

    }


    public static String GetHASHString(String password) {
        StringBuffer hexString = new StringBuffer();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(password.getBytes());
            byte byteData[] = md.digest();
            for (int i = 0; i < byteData.length; i++) {
                String hex = Integer.toHexString(0xff & byteData[i]);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            System.out.println("Digest(in hex format):: " + hexString.toString());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return hexString.toString();

    }


    public static String GetStringSHA512(String password) {
        MessageDigest md = null;
        String base64 = null;
        try {
            md = MessageDigest.getInstance("SHA-512");
            md.update(password.getBytes());
            byte byteData[] = md.digest();
            base64 = Base64.encodeToString(byteData, Base64.NO_WRAP);
        } catch (NoSuchAlgorithmException e) {
            Log.w("ACC OPENING", "Could not load MessageDigest: SHA-512");
            e.printStackTrace();
            //return false;
        }
        return base64;
    }




    public static int getInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static float dpToPx(float dp, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float px = dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    public static Bitmap getRectangularBitmap(Bitmap source, int desiredLength, boolean recycleSource) {
        if(source != null) {

            int width = source.getWidth();
            int height = source.getHeight();

        /*Log.d("faces", Integer.toString(width));
        Log.d("faces", Integer.toString(height));*/

            int sourceY = 0;

            int noToDetect = 10;
            FaceDetector.Face[] myFaces = new FaceDetector.Face[noToDetect];
            Bitmap _source = source.copy(Bitmap.Config.RGB_565, true);

        /*Log.d("faces", _source.getConfig().toString());

        Log.d("faces", Integer.toString(_source.getWidth()));
        Log.d("faces", Integer.toString(_source.getHeight()));*/

            FaceDetector myFaceDetect = new FaceDetector(_source.getWidth(), _source.getHeight(), noToDetect);
            int numberOfFaceDetected = myFaceDetect.findFaces(_source, myFaces);
            _source.recycle();

        /*Log.d("faces", Integer.toString(numberOfFaceDetected));
        Log.d("faces", Integer.toString(myFaces.length));*/

            if (numberOfFaceDetected > 0 && myFaces.length > 0) {
                PointF midPoint = new PointF(Float.MAX_VALUE, Float.MAX_VALUE);
                float eyesDistance = 0;

                for (int i = 0, l = myFaces.length; i < l; i++) {
                    FaceDetector.Face face = myFaces[i];

                    if (face == null) continue;

                    PointF _midPoint = new PointF();
                    face.getMidPoint(_midPoint);

                    if (_midPoint.y < midPoint.y) {
                        //this person should be the taller one in the picture since face detection starts from below the pic
                        midPoint = _midPoint;
                        eyesDistance = face.eyesDistance();
                    }
                }

            /*Log.d("faces mid", midPoint.toString());
            Log.d("face eyes", Float.toString(eyesDistance));*/

                float faceTop = (midPoint.y - eyesDistance * 2);

            /*Log.d("face eyes", Float.toString(faceTop));*/

                if (faceTop < 0)
                    faceTop = 0;

                if (faceTop > (height / 2))
                    faceTop = 0;

                sourceY = Math.round(faceTop);
                height = height - sourceY; //take the difference from the height to get better rectangle
            }

            //make circular thumbnail
            int imageLength = width < height ? width : height;

            int sourceX = (width - imageLength) / 2;

            float scale = ((float) desiredLength) / imageLength;

            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);

            Bitmap newBitmap = Bitmap.createBitmap(source, sourceX, sourceY, imageLength, imageLength, matrix, true);
            if (recycleSource)
                source.recycle();

            return newBitmap;
        }else{
            return null;
        }
    }

    public static Bitmap getInSampledBitmap(String src, Integer width, Integer height)
    {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(src, options);

        //setting max values ensures no sampling would be done

        if(width == null && height == null){
            //make fit
            width = options.outWidth;
            height = options.outHeight;

            int larger = width > height ? width : height;

            int maxLength = photoMaxLength;

            double zoomRatio = ((double)maxLength) / ((double)larger);

            width = ((Double)Math.ceil(width * zoomRatio)).intValue();
            height = ((Double)Math.ceil(height * zoomRatio)).intValue();
        }

        if(width == null)
            width = Integer.MAX_VALUE - 1;

        if(height == null)
            height = Integer.MAX_VALUE - 1;

        // Calculate inSampleSize
        options.inSampleSize = getInSampleSize(options, width, height);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(src, options);
    }

    public static Bitmap getRotatedBitmap(String src, Bitmap source, boolean recycleSource) {
        try {
            int orientation = getExifOrientation(src);

            if (orientation == 1) {
                return source;
            }

            Matrix matrix = new Matrix();
            /*switch (orientation) {
                case 2:
                    matrix.setScale(-1, 1);
                    break;
                case 3:
                    matrix.setRotate(180);
                    break;
                case 4:
                    matrix.setRotate(180);
                    matrix.postScale(-1, 1);
                    break;
                case 5:
                    matrix.setRotate(90);
                    matrix.postScale(-1, 1);
                    break;
                case 6:
                    matrix.setRotate(90);
                    break;
                case 7:
                    matrix.setRotate(-90);
                    matrix.postScale(-1, 1);
                    break;
                case 8:
                    matrix.setRotate(-90);
                    break;
                default:
                    return source;
            }*/

            switch (orientation) {
                case ExifInterface.ORIENTATION_NORMAL:
                    return source;
                case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                    matrix.setScale(-1, 1);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    matrix.setRotate(180);
                    break;
                case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                    matrix.setRotate(180);
                    matrix.postScale(-1, 1);
                    break;
                case ExifInterface.ORIENTATION_TRANSPOSE:
                    matrix.setRotate(90);
                    matrix.postScale(-1, 1);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    matrix.setRotate(90);
                    break;
                case ExifInterface.ORIENTATION_TRANSVERSE:
                    matrix.setRotate(-90);
                    matrix.postScale(-1, 1);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    matrix.setRotate(-90);
                    break;
                default:
                    return source;
            }

            try {
                Bitmap oriented = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);

                /*if(oriented == null)
                    return source;*/

                if (recycleSource)
                    source.recycle();

                return oriented;
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                return source;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return source;
    }

    private static int getExifOrientation(String src) throws IOException {
        int orientation = 1;

        try {
            /**
             * if your are targeting only api level >= 5
             * ExifInterface exif = new ExifInterface(src);
             * orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
             */
            if (Build.VERSION.SDK_INT >= 5) {
                Class<?> exifClass = Class.forName("android.media.ExifInterface");
                Constructor<?> exifConstructor = exifClass.getConstructor(new Class[] { String.class });
                Object exifInstance = exifConstructor.newInstance(new Object[] { src });
                Method getAttributeInt = exifClass.getMethod("getAttributeInt", new Class[] { String.class, int.class });
                Field tagOrientationField = exifClass.getField("TAG_ORIENTATION");
                String tagOrientation = (String) tagOrientationField.get(null);
                orientation = (Integer) getAttributeInt.invoke(exifInstance, new Object[] { tagOrientation, 1});
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        return orientation;
    }




    public static File saveBitmapToTempFile(Context context, Bitmap bitmap){

        File outputFile = null;
        FileOutputStream fOut = null;

        try{
            File outputDir = context.getCacheDir(); // context being the Activity pointer
            outputFile = File.createTempFile("profile_pic", "png", outputDir);

            fOut = new FileOutputStream(outputFile);
            //picture
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, fOut);

            fOut.flush();
            fOut.close();

            return outputFile;
        }catch (Exception e){
            try{
                fOut.flush();
                fOut.close();
            }catch (Exception ee){

            }
            //deleteFile(context, outputFile);
        }

        return null;
    }


    public static String ImageToString(ImageView image) {
        BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] byteArrayImage = baos.toByteArray();
        String imagetobyte = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
        return imagetobyte;
    }

    public static String BitMapToString(Bitmap bitmap) {
        if(bitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] byteArrayImage = baos.toByteArray();
            String imagetobyte = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);

/*
        View drawingView = get_your_view_for_render;
        drawingView.buildDrawingCache(true);
        Bitmap bitmap = drawingView.getDrawingCache(true).copy(Config.RGB_565, false);
        drawingView.destroyDrawingCache(); */

            return imagetobyte;
        }else{
            return "";
        }
    }

    public static byte[] ImageToByte(ImageView image) {
        BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] byteArrayImage = baos.toByteArray();
        // String imagetobyte = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
        return byteArrayImage;
    }

    public static byte[] ImageToByte(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] byteArrayImage = baos.toByteArray();
        // String imagetobyte = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
        return byteArrayImage;
    }
    public static Bitmap StringToImage(String image) {
        //  byte[] imageAsBytes = Base64.decode(image.getBytes(), Base64.DEFAULT);
        //   ImageView image = (ImageView)this.findViewById(R.id.ImageView);
        //image.setImageBitmap(
        //        BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length)
        // );

        return null;
    }

    public  static Bitmap GetImageFromString(String imageString){
        try {
            if (imageString != "" || imageString != null) {
                byte[] imageBytes = Base64.decode(imageString, Base64.DEFAULT);
                Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                return decodedImage;
            }
        }catch (Exception ex){

            return  null;
        }
       return null;
    }

//    public static  String ConvertDateToString(String dateToString){
//
//        //"2017-07-16T15:54:45.0331047-07:00"
//            boolean catalog_outdated = false;
//        String inputPattern = "yyyy-MM-ddTHH:mm:ss zzz"; //Wed, 06 Sep 2017 12:29:25 GMT"
//            //String inputPattern = "EEE,dd MMM yyyy HH:mm:ss zzz"; //Wed, 06 Sep 2017 12:29:25 GMT"
//            String outputPattern = "dd-MM-yyyy";
//            SimpleDateFormat sdf = new SimpleDateFormat(inputPattern, Locale.ENGLISH);
//            SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
//            Date strDate = null;
//            Date today = new Date();
//            try {
//                strDate = sdf.parse(valid_until);
//                // today = outputFormat.format(today);
//
//                if (new Date().after(strDate)) {
//                    catalog_outdated = true;
//                }
//
//            } catch (ParseException e) {
//                e.printStackTrace();
//                catalog_outdated = false;
//            }
//            return catalog_outdated;
//
//    }

    public static void releaseBitmap(Bitmap bitmap)
    {
        try{
            bitmap.recycle();
        }catch (Exception e){

        }
    }

    public static String setDateFromCalendar_1(int year,int month,int day) {
        Date dt=new Date(year,month+1,day);
        return FormatDate_ddMMMYYYY(dt);
        //return (day+"-"+(month+1)+"-"+year);
    }
    public static String setDateFromCalendar(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        return FormatDate_ddMMYYHHMMSS(cal.getTime());
    }


    public static String setCalenedar(int year, int month, int day,int hour,int min,int sec) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR,hour);
        cal.set(Calendar.MINUTE,min);
        cal.set(Calendar.SECOND,sec);
       // cal.set(Calendar.AM_PM,)
        //cal.set(Calendar.)
        return FormatDate_ddMMYY(cal.getTime());
    }


    public static String setCalenedar(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        return FormatDate_ddMMMYYYY(cal.getTime());
    }
    public static String setDOBFromCalendar(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        return FormatDate_ddMMYYYY(cal.getTime());
    }
    public static String setCurrentDate() {
        final Calendar calendar = Calendar.getInstance();
        String today="";
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        // set current date into textview
        today=(day+"-"+(month+1)+"-"+year);
        return today;
    }
    public static String getTodaysDate(){
        Date today=new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat(
                "dd-MMM-yyyy");
        return sdf.format(today);
    }
    public static String getMiniStatementDate(){
        Date today=new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat(
                "dd-MMM-yyyy");
        return sdf.format(today);
    }
    public static String getRelativeDate(String dateInMillis){
        return android.text.format.DateUtils.getRelativeTimeSpanString(Long.parseLong(dateInMillis)).toString();
    }
    @SuppressWarnings("deprecation")
    public static String IsTodayFormatLongDate(long date){
        Date d = new Date(date);
        Date today=new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat(
                "EEE MMM dd yyyy");

        String dateString = sdf.format(d);
//Calendar cal=Calendar.getInstance();
        if(today.getDate()==d.getDate() && today.getMonth()==d.getMonth() && today.getYear()==d.getYear())
            return "Today";
        else
        {
            return dateString;

        }

    }


    public static String FormatLongDate(long date){
        Date d = new Date(date);
        SimpleDateFormat sdf = new SimpleDateFormat(
                "EEE MMM dd yyyy HH:mm:ss");


        //d = sdf.parse(cDate);
        //SimpleDateFormat sdf = new SimpleDateFormat(
        //"MMMM dd, yyyy hh:mm a");
        String dateString = sdf.format(d);
        return dateString;
    }
    public static String FormatDateMMMddYYYY(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat(
                "EEE MMM dd yyyy");
        String dateString = sdf.format(date);
        return dateString;
    }
    public static String FormatDate_ddMMMYYYY(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat(
                "dd-MMM-yyyy");
        String dateString = sdf.format(date);
        return dateString;
    }

    public static String FormatDate_ddMMYY(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat(
                "dd-MMM-yyyy HH:mm:ss");
        String dateString = sdf.format(date);
        return dateString;
    }

    public static String FormatDate_ddMMYYHHMMSS(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat(
                "dd-MM-yy HH:mm:ss");
        String dateString = sdf.format(date);
        return dateString;
    }
    public static String getDateTimeStamp(){
        SimpleDateFormat sdf = new SimpleDateFormat(
                "dd-MM-yy HH:mm:ss");
        String dateString = sdf.format(new Date(System.currentTimeMillis()));
        return dateString.replace("-", "").replace(":", "").replace(" ", "").trim();
    }
    public static String FormatDate_ddMMYYYY(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat(
                "dd/mm/yyyy");
        String dateString = sdf.format(date);
        return dateString;
    }
//    public static String FormatShortDate(Date date){
//            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
//        //d = sdf.parse(cDate);
//        //SimpleDateFormat sdf = new SimpleDateFormat(
//        //"MMMM dd, yyyy hh:mm a");
//        String dateString = format.format(d);
//        return dateString;
//    }

//    public static void LogOutSession(Context context,Boolean isExpired){
//        SessionManager sessionMgr= new SessionManager(context);
//        sessionMgr.logoutUser(isExpired);
//    }


    public static String  ConvertToDateTime(String valid_until) {
        boolean catalog_outdated = false;
        String inputPattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        String outputPattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        SimpleDateFormat sdf = new SimpleDateFormat(inputPattern, Locale.ENGLISH);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
        String  strDate = null;

        try {

            strDate = sdf.format(valid_until);

        } catch (Exception e) {
            e.printStackTrace();
            catalog_outdated = false;
        }
        return strDate;
    }


    public static String CheckDateTime(Date valid_until) {
        boolean catalog_outdated = false;
        String inputPattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"; //Wed, 06 Sep 2017 12:29:25 GMT"
        SimpleDateFormat sdf = new SimpleDateFormat(inputPattern, Locale.ENGLISH);
       // SimpleDateFormat outputFormat = new SimpleDateFormat(inputPattern);
        String strDate = null;
        try {

            strDate = sdf.format(valid_until);

        } catch (Exception e) {
            e.printStackTrace();
            catalog_outdated = false;
        }
        return strDate.toString();
    }




    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }


    public static int mainActivityLayoutHeight;

    private static int waitIconViewTagKey = 0x2344334;

    public static void makeFullScreen(AppCompatActivity activity){
        View decorView = activity.getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        // Remember that you should never show the action bar if the
        // status bar is hidden, so hide that too if necessary.
        ActionBar actionBar = activity.getSupportActionBar();
        if(actionBar != null)
            actionBar.hide();
    }

    public static void showWaitIcon(Activity context,
                                    @IdRes int waitIconLayoutId,
                                    @StringRes Integer waitTextId,
                                    boolean showProgress,
                                    boolean showText) {
        Resources resources = context.getResources();
        showWaitIcon(context, context.findViewById(waitIconLayoutId), resources.getString(waitTextId), showProgress, showText);
    }

    public static void showWaitIcon(Activity context,
                                    View waitIconLayout,
                                    String waitText,
                                    boolean showProgress,
                                    boolean showText) {

        showWaitIcon(context, waitIconLayout, waitText, showProgress, showText, //null, null,
                null, null);
    }

    public static void showWaitIcon(Activity context,
                                    View waitIconLayout,
                                    String waitText,
                                    boolean showProgress,
                                    boolean showText,
                                    /*Integer progressStartColor,
                                    Integer progressEndColor,*/
                                    Integer textColor,
                                    Integer backgroundColor) {
        ProgressBar progressBar = (ProgressBar)waitIconLayout.findViewById(R.id.wait_progress);
        TextView textView = (TextView)waitIconLayout.findViewById(R.id.wait_text);

        if(!showProgress){
            progressBar.setVisibility(View.GONE);
        }else{
            progressBar.setIndeterminate(true);

            /*if(progressStartColor != null || progressEndColor != null){
                progressStartColor = progressStartColor != null? progressStartColor
                        : ContextCompat.getColor(context, android.R.color.transparent);
                progressEndColor = progressEndColor != null? progressEndColor
                        : ContextCompat.getColor(context, R.color.light_blue);

                //change colors
                RotateDrawable drawable = (RotateDrawable) ContextCompat.getDrawable(context, R.drawable.progress).mutate();
                GradientDrawable shapeDrawable = (GradientDrawable) drawable.getDrawable();
                shapeDrawable.setColors(new int[] { progressStartColor, progressEndColor });
                progressBar.setIndeterminateDrawable(null);
                progressBar.setIndeterminateDrawable(drawable);
            }*/

            progressBar.setVisibility(View.VISIBLE);
            progressBar.invalidate();
        }

        if(!showText){
            textView.setVisibility(View.GONE);
        }else{
            if(!TextUtils.isEmpty(waitText)){
                textView.setText(waitText);
            }

            if(textColor != null)
                textView.setTextColor(textColor);

            textView.setVisibility(View.VISIBLE);
        }

        if(backgroundColor != null){
            Integer lastBackgroundColor = (Integer)waitIconLayout.getTag(waitIconViewTagKey);
            if(lastBackgroundColor == null || !backgroundColor.equals(lastBackgroundColor)){
                Resources resources = context.getResources();
                StateListDrawable stateListDrawable = (StateListDrawable) //ContextCompat.getDrawable(context, R.drawable.wait_icon_background)//.getConstantState().newDrawable();
                        waitIconLayout.getBackground().getConstantState().newDrawable(resources).mutate();
                Drawable[] drawables = getGradientDrawablesFrom(stateListDrawable);
                drawables[0] = drawables[0].mutate(); //getConstantState().newDrawable(resources).mutate();
                ((GradientDrawable)drawables[0]).setColor(backgroundColor);
                waitIconLayout.setBackground(stateListDrawable);

                waitIconLayout.setTag(waitIconViewTagKey, backgroundColor);
            }
        }

        waitIconLayout.setVisibility(View.VISIBLE);
        waitIconLayout.invalidate();
    }


    public static Fragment addFragment(Fragment fragment,
                                       FragmentManager fragmentManager,
                                       @IdRes Integer containerId,
                                       ViewGroup container,
                                       Bundle savedInstanceState,
                                       boolean makeContainerVisible){
        Fragment originalFragment = fragment;
        if (savedInstanceState != null) {
            try{
                fragment = fragmentManager.findFragmentById(containerId);
            }catch (Exception e){
                fragment = null;
            }
        }

        if ((savedInstanceState == null || fragment == null) && containerId != null) {
            fragment = originalFragment;

            fragmentManager.beginTransaction()
                    .add(containerId, fragment)
                    .commit();
        }

        if(fragment != null){
            if(makeContainerVisible){
                container.setVisibility(View.VISIBLE);
            }else{
                container.setVisibility(View.INVISIBLE);
            }
        }

        return fragment;
    }

    public static Drawable[] getGradientDrawablesFrom(StateListDrawable gradientDrawable){
        DrawableContainer.DrawableContainerState drawableContainerState =
                (DrawableContainer.DrawableContainerState) gradientDrawable.getConstantState();

        Drawable[] children = drawableContainerState.getChildren();

        return children;


    }

    public static void setEnable(boolean enable, ViewGroup vg){
        for (int i = 0; i < vg.getChildCount(); i++){
            View child = vg.getChildAt(i);
            child.setEnabled(enable);
            if (child instanceof ViewGroup){
                setEnable(enable, (ViewGroup)child);
            }
        }
    }

    public static boolean isLongNumeric(String str)
    {
        try
        {
            Long.parseLong(str, 10);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }



    public static Users GetUserObjectFromJson(Activity activity){
        SharedData sharedData =  new SharedData(activity);
        Gson gson  =  new Gson();
        Users user = gson.fromJson(sharedData.Get(Constants.USER_KEY,""),Users.class);
        return  user;
    }
}
