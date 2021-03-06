package at.str.buzzmovies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * Controls activity that happens on Profile page of the App
 * @author Delicous 3.14
 */
public class ProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    protected User user;
    protected String name;
    protected String interestStr;
    protected String major;
    protected String newPass;
    protected String newPassC;
    protected EditText nameText;
    protected EditText majorText;
    protected EditText interests;
    protected EditText changePass;
    protected EditText confirmPass;
    protected Spinner majorSpinner;

    /**
     * onCreate method
     * @param savedInstanceState Instance state of type Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        user = (User) LocalStore.getCurrentAccount();

        TextView mEmail = (TextView) findViewById(R.id.email_textView);
        mEmail.setText(user.getEmail());
        String email = mEmail.getText().toString();
        nameText = (EditText) findViewById(R.id.name_editText);
        TextView majorPromp = (TextView) findViewById(R.id.major_TextView);

        //majorText = (EditText) findViewById(R.id.major_editText);
        interests = (EditText) findViewById(R.id.interests_editText);

        changePass = (EditText) findViewById(R.id.changePass_editText);
        confirmPass = (EditText) findViewById(R.id.confirmPass_editText);

        //majorText.setText(user.getMajor());
        interests.setText(user.getInterest());
        nameText.setText(user.getName());
        changePass.setText("");
        confirmPass.setText("");

        majorSpinner = (Spinner) findViewById(R.id.major_spinner);
        //String[] majorChoices = new String[]{" ", "Computer Science", "Electrical Engineering", "Mechanical Engineering", "Computer Engineering"};
        ArrayList<String> majorChoices = LocalStore.getMajors();
        ArrayAdapter<String> majorAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, majorChoices);
        majorSpinner.setAdapter(majorAdapter);

        int pos = majorAdapter.getPosition(user.getMajor());
        majorSpinner.setSelection(pos);


        majorSpinner.setOnItemSelectedListener(this);

        Button mHomeButton = (Button) findViewById(R.id.home_button);
        mHomeButton.setOnClickListener(new View.OnClickListener() {
            /**
             * onClick method
             * @param view view of type View
             */
            @Override
            public void onClick(View view) {

                    // Set all of the fields in user
                    //retrieving name, might use in user.java
                    name = nameText.getText().toString();
                    //retrieving major
                    //major = majorText.getText().toString();
                    //retrieving interest
                    interestStr = interests.getText().toString();
                    changeInformation();
                    toHome();

            }
        });

        Button mChangePasswordButton = (Button) findViewById(R.id.change_password_button);
        mChangePasswordButton.setOnClickListener(new View.OnClickListener() {
            /**
             * onClick method
             * @param view view of type view
             */
            @Override
            public void onClick(View view) {
                //TODO: Actually make this method check the passwords then change them in the database. Below is some of the old code
                newPass = changePass.getText().toString();
                newPassC = confirmPass.getText().toString();
                if (!(newPass.equals(newPassC))) {
                    Context context = getApplicationContext();
                    CharSequence text = "Passwords do not match";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                } else if (newPass.length() > 0 && newPass.length() < 4) {
                    Context context = getApplicationContext();
                    CharSequence text = "Password must be at least 4 characters";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                } else {

                }
            }
        });


    }


    /**
     * Change Information method of interest, major, and name.
     */
    protected void changeInformation() {
        user.setInterest(interestStr);
        user.setMajor(major);
        user.setName(name);
        //if (newPass.length() > 0) {
          //  user.setPassword(newPass);
       // }
        LocalStore.setCurrentAccount(user);


        // Now we have to go to ProfileController to also update the API
        //Pass Credentials to profile controller for authentication


        ProfileController.updateProfile(this.getApplicationContext(), this, interestStr, major, name, LocalStore.getCurrentAccount().getToken());

    }

    /**
     * private method to go to Home activity.
     */
    private void toHome() {
        Intent toHomeActivity = new Intent(this, HomeActivity.class);
        startActivity(toHomeActivity);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        major = (String) parent.getItemAtPosition(pos);
        Log.d("Major String", major);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // This isn't really important
    }
}
