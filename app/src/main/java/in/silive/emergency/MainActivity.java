package in.silive.emergency;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    String MobileNO;
    SharedPreferences sharedPreferences;
    String MyProfile = "Profile";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences(MyProfile, MODE_PRIVATE);
        MobileNO = (sharedPreferences.getString("MobileNO", ""));
        Thread thread = new Thread(){
            @Override
            public void run() {
                try{
                    sleep(2000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                finally {

                    if(MobileNO.isEmpty()){
                        Intent intent = new Intent(MainActivity.this,EnterPersonalDetail.class);
                        intent.putExtra("mobile","no");
                        startActivity(intent);
                    }
                    else {

                            Intent intent = new Intent(MainActivity.this, FragmentCallingActivity.class);
                            startActivity(intent);
                        }

                    }
                }

        };
thread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
