package in.silive.emergency;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Gravity;
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
public class ChatHeadService extends Service implements View.OnClickListener {


    private WindowManager windowManager;
    private Button chatHead;
    WindowManager.LayoutParams params,params2;
    float screenWidth = 0, screenHeight = 0;
    private Button chatHead2;
    View popupView;
    LinearLayout layout;
    String ChatService = "Chat";
    String type = "";

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public void onStart(Intent intent, int startId) {
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        screenHeight = getResources().getDisplayMetrics().heightPixels;

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        layout = new LinearLayout(this);
        chatHead = new Button(this);
        chatHead.setBackgroundResource(R.drawable.logo);
        chatHead.setClickable(true);
        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        popupView = layoutInflater.inflate(R.layout.chatheadlayout, null);
        popupView.setVisibility(View.GONE);

        Button hospital = (Button) popupView.findViewById(R.id.bhospital);
        Button pharmacy = (Button) popupView.findViewById(R.id.bphar);
        Button police = (Button) popupView.findViewById(R.id.bpolic);
        Button contact = (Button) popupView.findViewById(R.id.bcontac);
        hospital.setOnClickListener(this);
        pharmacy.setOnClickListener(this);
        police.setOnClickListener(this);
        contact.setOnClickListener(this);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ChatHeadService.this , "snvjnf" ,Toast.LENGTH_SHORT).show();
            }
        });
      /*  layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupView.setVisibility(View.GONE);
                windowManager.removeView(layout);
                windowManager.addView(layout , params);
            }
        });*/
        // chatHead2 = new Button(this);
        //  chatHead2.setBackgroundResource(R.mipmap.ic_launcher);
        //  chatHead2.setVisibility(View.GONE);
        layout.addView(chatHead);
        layout.addView(popupView);
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        params2 = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 100;

        //this code is for dragging the chat head
        chatHead.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        //    chatHead2.setVisibility(View.VISIBLE);
                        popupView.setVisibility(View.GONE);

                        return true;
                    case MotionEvent.ACTION_UP:
                        popupView.setVisibility(View.VISIBLE);
                        return true;
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
        });
        windowManager.addView(layout, params);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY ;
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
                emergency();
                break;
        }
    }

    public void emergency(){
        Intent i = new Intent(getBaseContext(),MainActivity.class);
        SharedPreferences sharedPreferences = getSharedPreferences(ChatService, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("chat", type);
        editor.commit();
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        i.setClass(getBaseContext(), MainActivity.class);
        startActivity(i);
        popupView.setVisibility(View.GONE);
        windowManager.removeView(layout);
        windowManager.addView(layout, params);
    }
    @Override
    public void onDestroy() {
        if (chatHead != null)
            windowManager.removeView(layout);
    }
}