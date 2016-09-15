package in.silive.emergency;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;


import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Shobhit-pc on 8/31/2016.
 */
public class EnterPersonalDetail extends AppCompatActivity implements View.OnClickListener {
    EditText mobile,name,dob,address,bloodgroup,inheriteddiseases,diseases;
    Button submit;
    SharedPreferences sharedPreferences;
    SharedPreferences shared;
    String MyProfile = "Profile";
    Toolbar toolbar;

    TextInputLayout inputLayoutName,inputLayoutMobile,inputLayoutdob;

    String Sname,Smobile,Saddress,Sblood,Sdob,Sinherited,Sdiseases;

    private int year;
    private int month;
    private int day;
    static final int DATE_PICKER_ID = 1111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enterpersonaldetail);

        toolbar = (Toolbar) findViewById(R.id.tbPersonal);
        setSupportActionBar(toolbar);

        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);
        inputLayoutMobile = (TextInputLayout) findViewById(R.id.input_layout_mobile);
        inputLayoutdob = (TextInputLayout) findViewById(R.id.input_layout_dob);

        mobile = (EditText) findViewById(R.id.etmobile);
        name = (EditText) findViewById(R.id.etname);
        dob = (EditText) findViewById(R.id.etdob);
        address = (EditText) findViewById(R.id.etaddress);
        bloodgroup = (EditText) findViewById(R.id.etbloodgroup);
        inheriteddiseases = (EditText) findViewById(R.id.etinheriteddiseases);
        diseases = (EditText) findViewById(R.id.etdiseases);

        submit = (Button) findViewById(R.id.btSubmit);

        shared = getSharedPreferences(MyProfile , MODE_PRIVATE);
        sharedPreferences = getSharedPreferences(MyProfile, MODE_PRIVATE);
        Sname = sharedPreferences.getString("Name", "");
        Smobile = sharedPreferences.getString("MobileNO", "");
        Saddress = sharedPreferences.getString("Address", "");
        Sdob = sharedPreferences.getString("DOB", "");
        Sblood = sharedPreferences.getString("BloodGroup", "");
        Sinherited = sharedPreferences.getString("InheritedDiseases", "");
        Sdiseases = sharedPreferences.getString("Diseases", "");

        if(!Sname.isEmpty())
            name.setText(Sname);

        if(!Smobile.isEmpty())
            mobile.setText(Smobile);

        if(!Saddress.isEmpty())
            address.setText(Saddress);

        if(!Sblood.isEmpty())
            bloodgroup.setText(Sblood);

        if(!Sdob.isEmpty())
            dob.setText(Sdob);

        if(!Sinherited.isEmpty())
            inheriteddiseases.setText(Sinherited);

        if(!Sdiseases.isEmpty())
            diseases.setText(Sdiseases);



        name.addTextChangedListener(new MyTextWatcher(name));
        mobile.addTextChangedListener(new MyTextWatcher(mobile));
        dob.addTextChangedListener(new MyTextWatcher(dob));

        submit.setOnClickListener(this);

       /* dob.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                showDialog(DATE_PICKER_ID);
                return false;
            }
        });


        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DATE_PICKER_ID);
            }
        });*/

        dob.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if(b){
                    showDialog(DATE_PICKER_ID);
                }
            }
        });

        final Calendar c = Calendar.getInstance();
        year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day   = c.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.about){

        }
        return super.onOptionsItemSelected(item);
    }

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public void onClick(View view) {
       if( view.getId() == R.id.btSubmit){
           try  {
               InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
               imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
           } catch (Exception e) {
               e.printStackTrace();
           }
          int mobileNoLength =  mobile.getText().toString().length();
           if(validateName() && validateMobile() && mobileNoLength > 7
                   && !dob.getText().toString().isEmpty()
                   ) {
               sharedPreferences = getSharedPreferences(MyProfile, Context.MODE_PRIVATE);
               SharedPreferences.Editor editor = sharedPreferences.edit();
               editor.putString("Name", name.getText().toString());
               editor.putString("MobileNO", mobile.getText().toString());
               editor.putString("DOB", dob.getText().toString());

               editor.putString("Address", address.getText().toString());
               editor.putString("BloodGroup", bloodgroup.getText().toString());
               editor.putString("InheritedDiseases", inheriteddiseases.getText().toString());
               editor.putString("Diseases", diseases.getText().toString());
               editor.commit();
               AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
               alertDialog.setTitle("Personal Details");
               alertDialog.setMessage("You have successfull enter your details");
               alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                       startnewactivity();
                   }
               });
               alertDialog.setCancelable(false);
               alertDialog.create();
               alertDialog.show();
           }
           else if (dob.getText().toString().isEmpty()){
               validateDob();

           }
       }

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:

                DatePickerDialog datePickerDialog = new DatePickerDialog(this, pickerListener, 1990, month,1);
                datePickerDialog.getDatePicker().setMinDate(new Date().getDate());
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.YEAR , -7);
                long time = calendar.getTimeInMillis();
                datePickerDialog.getDatePicker().setMaxDate(time);
                return datePickerDialog;
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year  = selectedYear;
            month = selectedMonth;
            day   = selectedDay;

            dob.setText(new StringBuilder().append(month + 1)
                    .append("-").append(day).append("-").append(year)
                    .append(" "));


        }
    };
    public void startnewactivity(){
        Intent intent = new Intent(this, FragmentCallingActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    private boolean validateName() {
        if (name.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError("Enter your full name");
            requestFocus(name);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validateMobile() {
        int mobileNoLength =  mobile.getText().toString().length();
        if (mobile.getText().toString().trim().isEmpty()) {
            inputLayoutMobile.setError("Enter your Mobile No.");
            requestFocus(mobile);
            return false;
        }
           else if( mobileNoLength < 7 ){
                inputLayoutMobile.setError("Enter your correct Mobile No.");
                requestFocus(mobile);

            return false;
        } else {
            inputLayoutMobile.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateDob() {
        if (dob.getText().toString().isEmpty()) {
            inputLayoutdob.setError("Enter your Date Of Birth");
            requestFocus(dob);
            return false;

        } else {
            inputLayoutdob.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.etname:
                    validateName();
                    break;
                case R.id.etmobile:
                    validateMobile();
                    break;
                case R.id.etdob:
                    validateDob();
                    break;

            }
        }
    }
}