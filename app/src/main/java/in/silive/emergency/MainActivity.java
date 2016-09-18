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

        //accessing sharedPreferences file
        sharedPreferences = getSharedPreferences(MyProfile, MODE_PRIVATE);

        //accessing mobile no. sharedPreferences file
        MobileNO = (sharedPreferences.getString("MobileNO", ""));

        Thread thread = new Thread(){
            @Override
            public void run() {
                try{
                    //sleep activity for 2 seconds
                    sleep(2000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                finally {
                    //if mobile no is empty , go to personal detail activity
                    if(MobileNO.isEmpty()){
                        Intent intent = new Intent(MainActivity.this,EnterPersonalDetail.class);
                        intent.putExtra("mobile","no");
                        startActivity(intent);
                    }
                    //else goto emegency acivity
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
