package in.silive.emergency;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.InstrumentationInfo;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.os.Handler;
import android.support.v4.media.VolumeProviderCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Shobhit-pc on 9/17/2016.
 */
public class VolumeReceiver extends BroadcastReceiver {
    Context mcontext;

    @Override
    public void onReceive(Context context, Intent intent) {
        mcontext=context;


            Intent in = new Intent(mcontext, StartAppService.class);
            mcontext.startService(in);



    }

}
