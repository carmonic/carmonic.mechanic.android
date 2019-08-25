package com.camsys.carmonic.mechanic.Service;

import android.app.Activity;
import android.app.Dialog;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import com.camsys.carmonic.mechanic.API.ConnectionController;
import com.camsys.carmonic.mechanic.Dasboard.AcceptAndDeclineFragment;
import com.camsys.carmonic.mechanic.MapView.MapViewFragment;
import com.camsys.carmonic.mechanic.Model.Customer;
import com.camsys.carmonic.mechanic.Model.Users;
import com.camsys.carmonic.mechanic.R;
import com.camsys.carmonic.mechanic.Utilities.Constants;
import com.camsys.carmonic.mechanic.Utilities.NotificationUtil;
import com.camsys.carmonic.mechanic.Utilities.Util;
import com.google.gson.Gson;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import okhttp3.OkHttpClient;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;
import static io.socket.client.Socket.EVENT_MESSAGE;

public class TestService extends Service  {


    private final int INTERVAL = 60 * 1000;
    private Timer timer = new Timer();
    private Socket socket;
    ScheduledExecutorService scheduleTaskExecutor =  null;
    Gson gson  =  null;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Service#onCreate() Called by the system when the service
     * is first created. Do not call this method directly.
     */
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

        scheduleTaskExecutor = Executors.newScheduledThreadPool(5);
        gson =  new Gson();
    }



    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        Log.i("TAG", "onStartCommand");

        Toast.makeText(getApplicationContext(),"onStartCommand",Toast.LENGTH_LONG).show();



// This schedule a runnable task every 2 minutes
        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            public void run() {

                Customer customer  =  new  Customer();
                customer.setFirstname("Ademola");
                customer.setDestination("Ikorodu Garage");
                customer.setLastname("Isola");
                customer.setSource("Ikorodu");
                customer.setUsername("biola.gold@gmail.com");

               final String  customerJSON  = gson.toJson(customer);

                NotificationUtil notificationUtil = new NotificationUtil(getApplicationContext(),customerJSON);
                notificationUtil.showNotificationMessage(" Carmonic","A customer needs your help 5km away from your location","","");

                try{

                    OkHttpClient okHttpClient = ConnectionController.client;
                    IO.setDefaultOkHttpWebSocketFactory(okHttpClient);
                    IO.setDefaultOkHttpCallFactory(okHttpClient);
                    IO.Options opts = new IO.Options();
                    opts.callFactory = okHttpClient;
                    opts.webSocketFactory = okHttpClient;
                    socket = IO.socket(Constants.Base_URL, opts);
                    socket.on(Constants.CUSTOMER_REQUEST, new Emitter.Listener() {
                        @Override
                        public void call(Object... args) {

                            System.out.println("A customer needs your help 5km away from your location");

                            Gson gson  =  new Gson();
                            JSONObject jsonObject = (JSONObject) args[0];

                            Toast.makeText(getApplicationContext(),args[0] + "",Toast.LENGTH_LONG).show();
                            NotificationUtil notificationUtil = new NotificationUtil(getApplicationContext(),customerJSON);
                            notificationUtil.showNotificationMessage(" Carmonic","A customer needs your help 5km away from your location","","");

                        }
                    });
                    socket.connect();
                } catch (URISyntaxException e) {

                    Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, 0, 1, TimeUnit.MINUTES);


        return START_STICKY;

    }


    public void showRequest(final TestService activity, String message){
        final Dialog dialog =new Dialog(activity);
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

        TextView txtTopic  = (TextView)dialog.findViewById(R.id.txtTopic);
        TextView cancelButton  = (TextView)dialog.findViewById(R.id.cancel_button);

        txtTopic.setVisibility(View.GONE);
        cancelButton.setVisibility(View.GONE);


        TextView txtNotificationText  = (TextView)dialog.findViewById(R.id.txtNotification);
        TextView txtSeeDetail  = (TextView)dialog.findViewById(R.id.txtSeeDetail);
        TextView txtDecline  = (TextView)dialog.findViewById(R.id.txtDecline);

        txtNotificationText.setText("A customer needs your help 5km away from your location");

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        txtSeeDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



//                final AcceptAndDeclineFragment myBottomSheet = AcceptAndDeclineFragment.newInstance("", new AcceptAndDeclineFragment.MyDialogFragmentListener() {
//                    @Override
//                    public void onReturnValue(boolean indicator) {
//
//                        if(indicator) {
//                            //ShowingAcceptRequest(getActivity());
//
//                        }else{
//
//                           // showingRejectRequest(getActivity());
//                        }
//
//                    }
//                });
//                myBottomSheet.show(activity, myBottomSheet.getTag());
//                dialog.dismiss();
            }
        });

        txtDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   showingRejectRequest(getActivity());
                dialog.dismiss();
            }
        });
        dialog.show();
    }



    private boolean isSocketConnected() {
        if (null == socket) {
            return false;
        }
        if (!socket.connected()) {
            socket.connect();
            Log.i(Constants.TAG, "reconnecting socket...");
            return false;
        }

        return true;
    }

    @Override
    public void onDestroy() {
        // Display the Toast Message
        Toast.makeText(this, "Stop Service", Toast.LENGTH_SHORT).show();
        super.onDestroy();
        scheduleTaskExecutor.shutdownNow();
    }


}