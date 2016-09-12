package in.silive.emergency;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;


public class TypeOfEmergency extends Fragment implements View.OnClickListener {
Button hospital,police,pharmacy,contacts;



    public TypeOfEmergency() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        LinearLayout linearLayout = (LinearLayout)inflater.inflate(R.layout.typeofemergency, container, false);

        hospital = (Button) linearLayout.findViewById(R.id.bthospital);
        police = (Button) linearLayout.findViewById(R.id.btpolice);
        pharmacy = (Button) linearLayout.findViewById(R.id.btpharmacy);

        contacts = (Button) linearLayout.findViewById(R.id.btcontacts);

        hospital.setOnClickListener(this);
        police.setOnClickListener(this);
        pharmacy.setOnClickListener(this);

        contacts.setOnClickListener(this);
        return linearLayout;

    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.bthospital:

                Intent intent = new Intent(getContext(), MapsActivity.class);
                intent.putExtra("type","hospital");

                startActivity(intent);

                break;
            case R.id.btpharmacy:

                intent = new Intent(getContext(), MapsActivity.class);
                intent.putExtra("type","pharmacy");

                startActivity(intent);

                break;
            case R.id.btpolice:

                intent = new Intent(getContext(), MapsActivity.class);
                intent.putExtra("type","police");

                startActivity(intent);

                break;
            case R.id.btcontacts:



                break;

        }
    }


}
