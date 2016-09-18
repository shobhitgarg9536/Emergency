package in.silive.emergency;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.InstrumentationInfo;
import android.media.AudioManager;
import android.support.v4.media.VolumeProviderCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Shobhit-pc on 9/17/2016.
 */
public class VolumeReceiver extends BroadcastReceiver {
    AudioManager audio;
    Context mcontext;
    int currentVolume;

    @Override
    public void onReceive(Context context, Intent intent) {
        mcontext=context;

        audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        //getting maximum volume
         int max = audio.getStreamMaxVolume(AudioManager.STREAM_RING);
       //getting current volume
        currentVolume = audio.getStreamVolume(AudioManager.STREAM_RING);
        if(max == currentVolume) {
            //if condition is true then open chat head
            Intent in = new Intent(mcontext, ChatHeadService.class);
            mcontext.startService(in);
        }


    }


}
