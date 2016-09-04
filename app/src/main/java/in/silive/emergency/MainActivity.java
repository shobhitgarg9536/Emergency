package in.silive.emergency;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {


    SharedPreferences sharedPreferences;
    String MyProfile = "Profile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences(MyProfile, MODE_PRIVATE);
        String MobileNO = (sharedPreferences.getString("MobileNO", ""));
        if(MobileNO.isEmpty()){
            Intent intent = new Intent(this,EnterPersonalDetail.class);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(this,TypeOfEmergency.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
