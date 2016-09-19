package in.silive.emergency;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.IBinder;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Shobhit-pc on 9/16/2016.
 */
public class ChatHeadService extends Service implements View.OnClickListener,View.OnKeyListener
{


    private WindowManager windowManager;
    private Button chatHead;
    WindowManager.LayoutParams params;
    float screenWidth = 0, screenHeight = 0;
    GestureDetector gestureDetector;
    View popupView;
    LinearLayout layout;

    String type = "";

    @Override
    public void onCreate() {
        super.onCreate();
        //getting screen height and width
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        screenHeight = getResources().getDisplayMetrics().heightPixels;

        gestureDetector = new GestureDetector(this, new SingleTapConfirm());

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        //custom layout
        layout = new LinearLayout(this);
        //taking button
        chatHead = new Button(this);
        chatHead.setBackgroundResource(R.drawable.logo);
        chatHead.setClickable(true);
        chatHead.setEnabled(true);
        chatHead.setFocusable(true);
        chatHead.setFocusableInTouchMode(true);
        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        //programmiticaly generating layout
        popupView = layoutInflater.inflate(R.layout.chatheadlayout, null);
        popupView.setVisibility(View.GONE);

        Button hospital = (Button) popupView.findViewById(R.id.bhospital);
        Button pharmacy = (Button) popupView.findViewById(R.id.bphar);
        Button police = (Button) popupView.findViewById(R.id.bpolic);
        Button contact = (Button) popupView.findViewById(R.id.bcontac);
        Button delete = (Button) popupView.findViewById(R.id.bdel);
       //adding onClick Listerner to hospital,pharmacy,etc
        hospital.setOnClickListener(this);
        pharmacy.setOnClickListener(this);
        police.setOnClickListener(this);
        contact.setOnClickListener(this);
        delete.setOnClickListener(this);
        //adding chathead and popupview to our programmiticaly generated layout
        layout.addView(chatHead);
        layout.addView(popupView);
        //generating parameters for our layout
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;

        //setting initial x and y cordinate of our layout
        params.x = (int) screenWidth;
        params.y = 100;

        //setting chathead on touch listener
        chatHead.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (gestureDetector.onTouchEvent(event)) {
                    //getting visibility of chat head layout
                    if (popupView.getVisibility() == View.VISIBLE)
                        //setting visibility of chat head layout
                        popupView.setVisibility(View.GONE);
                    //getting visibility of chat head layout
                    if (popupView.getVisibility() == View.GONE)
                        //setting visibility of chat head layout
                        popupView.setVisibility(View.VISIBLE);
                    windowManager.updateViewLayout(layout, params);
                    return true;
                } else {
                    switch (event.getAction()) {

                        case MotionEvent.ACTION_DOWN:
                            initialX = params.x;
                            initialY = params.y;
                            initialTouchX = event.getRawX();
                            initialTouchY = event.getRawY();
                            //setting visibility of chat head layout to gone
                            popupView.setVisibility(View.GONE);
                            return true;

                        case MotionEvent.ACTION_UP:
                            popupView.setVisibility(View.GONE);
                            return false;

                        case MotionEvent.ACTION_MOVE:
                            params.x = initialX
                                    + (int) (event.getRawX() - initialTouchX);
                            params.y = initialY
                                    + (int) (event.getRawY() - initialTouchY);
                            windowManager.updateViewLayout(layout, params);
                            return true;


                    }
                    return false;
                }

                }

        });

        //adding aur programmiticaly generated layout to windows manager
        windowManager.addView(layout, params);


    }

    @Override
    public void onStart(Intent intent, int startId) {


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return 1 ;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.bhospital:
                type = "hospital";
                emergency();
                break;
            case R.id.bphar:
                type = "pharmacy";
                emergency();
                break;
            case R.id.bpolic:
                type = "police";
                emergency();
                break;
            case R.id.bcontac:
                contacts();
                break;
            case R.id.bdel:
                stopSelf();
                break;
        }
    }

    private void contacts() {
        //passing intent
        Intent i = new Intent(getBaseContext(),FragmentCallingActivity.class);
        //to make app2 starts on app1
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        //setting class of app2
        i.setClass(getBaseContext(), FragmentCallingActivity.class);
        i.putExtra("type" , type);
        startActivity(i);
        //setting class of app2
        popupView.setVisibility(View.GONE);
        //removing layout from windows  manager
        windowManager.removeView(layout);
        windowManager.addView(layout, params);

    }

    public void emergency(){
        //passing intent
        Intent i = new Intent(getBaseContext(),MapsActivity.class);
        //to make app2 starts on app1
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        //setting class of app2
        i.setClass(getBaseContext(), MapsActivity.class);
        i.putExtra("type" , type);
        startActivity(i);
        //setting class of app2
        popupView.setVisibility(View.GONE);
        //removing layout from windows  manager
        windowManager.removeView(layout);
        windowManager.addView(layout, params);
    }
    @Override
    public void onDestroy() {
        if (chatHead != null)
            windowManager.removeView(layout);
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {
            Toast.makeText(getApplicationContext() , "asss" ,Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;

        }

    public class SingleTapConfirm extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            return true;
        }
    }

}