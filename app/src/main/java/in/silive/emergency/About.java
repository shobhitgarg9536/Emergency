package in.silive.emergency;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by Shobhit-pc on 9/29/2016.
 */
public class About extends AppCompatActivity {

    Toolbar tbabout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        tbabout = (Toolbar) findViewById(R.id.tbabout);

        setSupportActionBar(tbabout);
        getSupportActionBar().setTitle("About");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
