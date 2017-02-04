package in.silive.emergency;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Shobhit-pc on 9/25/2016.
 */
public class StartAppService extends Service {

int currentVolume=0,previousVolume=0;
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        AudioManager audio = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        //getting current volume
        currentVolume = audio.getStreamVolume(AudioManager.STREAM_RING);

        if((currentVolume - previousVolume) == 2) {
            //if condition is true then open chat head
            Intent i = new Intent(getBaseContext(),Profile.class);
            //to make app2 starts on app1
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            //setting class of app2
            i.setClass(getBaseContext(), FragmentCallingActivity.class);
            previousVolume = currentVolume;
            startActivity(i);
        }
        else if(previousVolume - currentVolume == 2){
            //if condition is true then open chat head
            Intent i = new Intent(getBaseContext(),Profile.class);
            //to make app2 starts on app1
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            //setting class of app2
            i.setClass(getBaseContext(), FragmentCallingActivity.class);
            previousVolume = currentVolume;
            startActivity(i);
        }

        else{
            previousVolume = currentVolume;
        }
        return 1 ;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
