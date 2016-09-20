package in.silive.emergency;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Shobhit-pc on 8/31/2016.
 */
public class EnterPersonalDetail extends AppCompatActivity implements View.OnClickListener {
    EditText mobile,name,dob,address,inheriteddiseases,diseases;
    Button submit;
    SharedPreferences sharedPreferences;
    String MyProfile = "Profile";
    Toolbar toolbar;
    Spinner bloodgroup;

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
        bloodgroup = (Spinner) findViewById(R.id.sp_bloodgroup);
        inheriteddiseases = (EditText) findViewById(R.id.etinheriteddiseases);
        diseases = (EditText) findViewById(R.id.etdiseases);


        submit = (Button) findViewById(R.id.btSubmit);

        //accessing MyProfile file
        sharedPreferences = getSharedPreferences(MyProfile, MODE_PRIVATE);
        Sname = sharedPreferences.getString("Name", "");
        Smobile = sharedPreferences.getString("MobileNO", "");
        Saddress = sharedPreferences.getString("Address", "");
        Sdob = sharedPreferences.getString("DOB", "");
        Sblood = sharedPreferences.getString("BloodGroup", "");
        Sinherited = sharedPreferences.getString("InheritedDiseases", "");
        Sdiseases = sharedPreferences.getString("Diseases", "");

        //if name is not null
        if(!Sname.isEmpty())
            name.setText(Sname);
        //if mobile is not null
        if(!Smobile.isEmpty())
            mobile.setText(Smobile);
        //if address is not null
        if(!Saddress.isEmpty())
            address.setText(Saddress);
        //if date of birth is not null
        if(!Sdob.isEmpty())
            dob.setText(Sdob);
        //if inherited diseases is not null
        if(!Sinherited.isEmpty())
            inheriteddiseases.setText(Sinherited);
        //if diseases is not null
        if(!Sdiseases.isEmpty())
            diseases.setText(Sdiseases);


        name.addTextChangedListener(new MyTextWatcher(name));
        mobile.addTextChangedListener(new MyTextWatcher(mobile));
        dob.addTextChangedListener(new MyTextWatcher(dob));

        submit.setOnClickListener(this);

        dob.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if(b){
                    showDialog(DATE_PICKER_ID);
                }
            }
        });

        //getting year,month,day from calender
        final Calendar c = Calendar.getInstance();
        year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day   = c.get(Calendar.DAY_OF_MONTH);
    }


    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public void onClick(View view) {
        //on submit button click
       if( view.getId() == R.id.btSubmit){
           try  {
               //disabling keyboard
               InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
               imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

           } catch (Exception e) {
               e.printStackTrace();
           }
           //getting mobile length
          int mobileNoLength =  mobile.getText().toString().length();
           //if validations are true
           if(validateName() && validateMobile() && mobileNoLength > 7
                   && !dob.getText().toString().isEmpty()
                   ) {
             //putting values through sharedPreference
               sharedPreferences = getSharedPreferences(MyProfile, Context.MODE_PRIVATE);
              //getting sharedPreferences editor
               SharedPreferences.Editor editor = sharedPreferences.edit();
             //putting values
               editor.putString("Name", name.getText().toString());
               editor.putString("MobileNO", mobile.getText().toString());
               editor.putString("DOB", dob.getText().toString());
               editor.putString("Address", address.getText().toString());
               editor.putString("BloodGroup", bloodgroup.getSelectedItem().toString());
               editor.putString("InheritedDiseases", inheriteddiseases.getText().toString());
               editor.putString("Diseases", diseases.getText().toString());

               editor.commit();
               //shoeing alert dialog
               AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
               alertDialog.setTitle("Personal Details");
               alertDialog.setMessage("You have successfull entered your details");
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
                //adding date picker
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, pickerListener, 1990, month,1);
                datePickerDialog.getDatePicker().setMinDate(new Date().getDate());
                Calendar calendar = Calendar.getInstance();
                //going 7 years back from current calender
                calendar.add(Calendar.YEAR , -7);
                //converting date to millisecond
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
        String edit = "";
        //getting intent
        Intent i=getIntent();
        edit =  i.getStringExtra("mobile");

        if(edit.equals("no")){

            Intent intent = new Intent(this, SelectContacts.class);
            intent.putExtra("contact" , "ba");
            finish();
            startActivity(intent);
        }

        else if(edit.equals("edit")) {
            Intent intent = new Intent(this, FragmentCallingActivity.class);
            finish();
            startActivity(intent);
        }
    }


    private boolean validateName() {
        if (name.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError("Enter your full name");
            //requsting focus on editText
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
            //requsting focus on editText
            requestFocus(mobile);
            return false;
        }
           else if( mobileNoLength <= 7 ){
                inputLayoutMobile.setError("Enter your correct Mobile No.");
            //requsting focus on editText
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
            //requsting focus on editText
            requestFocus(dob);
            return false;

        } else {
            inputLayoutdob.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            //showing keyboard
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

    public void openPic(View view){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
       // intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_CODE);
    }
    final int REQUEST_CODE = 1;
Bitmap bitmap;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK)
            try {
                // We need to recyle unused bitmaps
                if (bitmap != null) {
                    bitmap.recycle();
                }
                InputStream stream = getContentResolver().openInputStream(
                        data.getData());
                bitmap = BitmapFactory.decodeStream(stream);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, byteArrayOutputStream);
                byte[] image = byteArrayOutputStream.toByteArray();
                String Simage = Base64.encodeToString(image, Base64.DEFAULT);
                //putting values through sharedPreference
                sharedPreferences = getSharedPreferences(MyProfile, Context.MODE_PRIVATE);
                //getting sharedPreferences editor
                SharedPreferences.Editor editor = sharedPreferences.edit();
                //putting values
                editor.putString("icon" , Simage);
                editor.commit();
                stream.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        super.onActivityResult(requestCode, resultCode, data);
    }

}