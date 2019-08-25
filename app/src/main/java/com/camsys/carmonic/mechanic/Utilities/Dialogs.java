package com.camsys.carmonic.mechanic.Utilities;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.widget.Toast;

public class Dialogs extends Activity {

    static Intent i = null;
    static Bundle b = null;
    static ProgressDialog dialog = null;
    static Context context = null;
    @SuppressLint("HandlerLeak")
    private static Handler messageHandler = new Handler() {


        public void handleMessage(Message msg) {
            super.handleMessage(msg);


            if (msg.getData().getString("response").startsWith("code=1")) {
                Dialogs.showDialog(context, "Successful", "OwFar");
            } else if (msg.getData().getString("response").startsWith("desc=You are Already a Friend")) {
                Dialogs.showDialog(context, "You are Already a Friend", "OwFar");
            } else {
                Dialogs.showDialog(context, msg.getData().getString("response"), "OwFar");
            }

        }
    };
    final Handler handler = new Handler();
    //WebserviceCall conn =  null ;
    SharedPreferences shared = null;

    public static void showMessagLocation(final Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) || !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Location Services Not Active");
            builder.setMessage("Please enable Location Services and GPS");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Show location settings when the user acknowledges the alert dialog
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    context.startActivity(intent);
                }
            });
            Dialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }
    }

    public static AlertDialog showMessage(final Context context, String message, String title) {
        AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
        // builder2.setTitle(R.string.dialog_title);
        // builder2.setIcon(R.drawable.ic_launcher);
        builder2.setMessage(message);
        builder2.setPositiveButton(android.R.string.ok, new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        return;
                    }
                });
        builder2.setNegativeButton(android.R.string.cancel, new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context,
                                "Clicked Cancel!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                });
        builder2.create();
        return builder2.show();
    }

    public static void showDialog(Context context, String message, String title) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle(title);//.setIcon(R.drawable.logo);

        // set dialog message
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                        dialog.cancel();
                    }
                });


        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public static void Show(Context context, String message, String title) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle(title);//.setIcon(R.drawable.logo);

        // set dialog message
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                        dialog.cancel();
                    }
                });


        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public static void showIntDialog(Context context, int message, String title) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle(title);

        // set dialog message
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                        dialog.cancel();
                    }
                });


        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public static void showDialog(final Context context, String message, String title, final Intent i) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle(title);

        // set dialog message
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                        dialog.cancel();
                        //Intent i = new Intent(context, StartScreen.class);

                        context.startActivity(i);
                    }
                });


        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public static void showDialog(final Context context, String message, String title, final Intent i, final Activity previous) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle(title);

        // set dialog message
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                        dialog.cancel();
                        //Intent i = new Intent(context, StartScreen.class);

                        context.startActivity(i);
                        previous.finish();
                    }
                });


        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    @SuppressWarnings("deprecation")
    public static void showMessage2(final Context context, String message, String title) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // here you can add functions
            }
        });
        //	alertDialog.setIcon(R.drawable.ic_launcher);
        alertDialog.show();
    }

    public static void showDialogWithButtons(final Context context, String message, String title) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(context, "You clicked yes button", Toast.LENGTH_LONG).show();
            }
        });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public static void shoeMessage(final Context context, String message, String title) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();

            }
        });
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context, "Ow Far", Toast.LENGTH_LONG).show();
            }
        });
        //	alertDialog.setIcon(R.drawable.ic_launcher);
        alertDialog.show();
    }

    public static void Toast(String text, Context context) {
        Toast.makeText(context, text, Toast.LENGTH_LONG);
    }

    public static void showSettingsAlert(final Context context) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // Setting Icon to Dialog
        // alertDialog.setIcon(R.drawable.rounded_corner);

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }


/*
//	Intent  i  =   null; 
	public static AlertDialog showChatMenu(final Context context,final String title, final  Double lat, final Double lng, final String id)
	{
		 AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
	        builder2.setTitle(title);
	        builder2.setIcon(R.drawable.ic_menu_allfriends);
	        
	        builder2.setItems(R.array.item_friends, new  DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					System.out.println(title +  lat +  lng + id);
					
					if(which == 0){
						i = new  Intent(context,Chat.class);
						Bundle  b  =  new  Bundle();
						b.putString("title",title);
						b.putDouble("latitude", lat);
						b.putDouble("longitude", lng);
						b.putString("ID",id);
						i.putExtras(b);
						context.startActivity(i);
					}else if(which == 1){
						i = new Intent(context,ChatPage.class);
						b =  new  Bundle();
						b.putString("title", title);
						b.putDouble("latitude", lat);
						b.putDouble("longitude", lng);
						i.putExtras(b);
						context.startActivity(i);
					}else{
						
					}
				}});
	        builder2.setPositiveButton(android.R.string.ok, new
	DialogInterface.OnClickListener() {
	                  public void onClick(DialogInterface dialog, int which) {
	                        Toast.makeText(context,
	"Clicked OK!", Toast.LENGTH_SHORT).show();
	                      return;
	                } });
	        builder2.setNegativeButton(android.R.string.cancel, new
	DialogInterface.OnClickListener() {
	                  public void onClick(DialogInterface dialog, int which) {
	                        Toast.makeText(context,
	"Cancel", Toast.LENGTH_SHORT).show();
	                      return;
	                } });
	       builder2.create();
	       return  builder2.show();
	}*/

////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////
///////////////////////////////
    ///////////////

    public static void showDialogAndNavigateToMainActivity(final Context context, String message, String title) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle(title);//.setIcon(R.drawable.logo);

        // set dialog message
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Intent  i  =  new  Intent(context,MainActivity.class);
                        // context.startActivity(i);
                        //////dialog.cancel();
                    }
                });


        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    private static String getSharedValue(String value, Context context) {
        SharedPreferences shared = context.getSharedPreferences("PREF_NAME", MODE_PRIVATE);
        String channel = (shared.getString(value, ""));
        if (channel.isEmpty()) {
            channel = "NULL";
        }
        return channel;
    }

}
