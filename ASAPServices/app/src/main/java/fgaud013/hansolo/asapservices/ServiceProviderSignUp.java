package fgaud013.hansolo.asapservices;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;

/**
 * @description : Page activity or where service provider can sign up
 * @Author      : Frédérick Gaudet - 8035208
 */

public class ServiceProviderSignUp extends AppCompatActivity {

    private static final String NOTHING_TEXT = "-- NOTHING --";

    private EditText usernameT, passwordT, confirmPasswordT, firstNameT,
            lastNameT,dateOfBirthT,phoneNumberT,hourlyRateT;
    private Spinner serviceTypeT;

    private static Button btnCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider_sign_up);
        init();
    }

    /**
     * Initialisation
     */
    private void init(){
        usernameT = (EditText)findViewById(R.id.usernameText);
        passwordT = (EditText)findViewById(R.id.passwordText);
        confirmPasswordT = (EditText)findViewById(R.id.confirmPasswordText);
        firstNameT = (EditText)findViewById(R.id.firstNameText);
        lastNameT = (EditText)findViewById(R.id.lastNameText);
        dateOfBirthT = (EditText)findViewById(R.id.dateOfBirthText);
        phoneNumberT = (EditText)findViewById(R.id.phoneText);
        serviceTypeT = (Spinner)findViewById(R.id.spinner_signup_sp);
        hourlyRateT = (EditText)findViewById(R.id.hourlyRateText);

        focusEvents();
        loadSpinnerFromDB();
        FinishButtonListener();
    }
    /**
     * ================================ LISTENERS ===================================
     */
    public void FinishButtonListener(){
        btnCreate = (Button)findViewById(R.id.btnCreate);
        btnCreate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Getting values, need some check up
                        boolean error = false;

                        EditText username= (EditText)findViewById(R.id.usernameText);
                        EditText password = (EditText)findViewById(R.id.passwordText);
                        EditText firstName = (EditText)findViewById(R.id.firstNameText);
                        EditText lastName = (EditText)findViewById(R.id.lastNameText);
                        EditText dateOfBirth = (EditText)findViewById(R.id.dateOfBirthText);
                        EditText phoneNumber = (EditText)findViewById(R.id.phoneText);
                        Spinner serviceType = (Spinner)findViewById(R.id.spinner_signup_sp);
                        EditText hourlyRate = (EditText)findViewById(R.id.hourlyRateText);

                        //Make sure that no field are empty
                        error = validateField();

                        //Error = true if there was a error so we dont come here
                        if(!error) {
                            Intent returnIntent = new Intent(ServiceProviderSignUp.this, MainActivity.class);
                            Bundle extras = new Bundle();
                            try {
                                extras.putString("username", username.getText().toString());
                                extras.putString("password", password.getText().toString());
                                extras.putString("firstname", firstName.getText().toString());
                                extras.putString("lastname", lastName.getText().toString());
                                extras.putString("dateOfBirth", dateOfBirth.getText().toString());
                                extras.putString("phoneNumber", phoneNumber.getText().toString());
                                extras.putString("serviceType", serviceType.getSelectedItem().toString());
                                extras.putDouble("hourlyRate", Double.parseDouble(hourlyRate.getText().toString()));
                                extras.putInt("permLevel", 1);

                                returnIntent.putExtras(extras);

                                setResult(RESULT_OK, returnIntent);

                                finish();
                            }
                            catch(Exception ex)
                            {

                            }
                        }
                    }
                }
        );
    }

    /**
     * All the event when the text field receive the focus
     */
    public void focusEvents(){
        //Username text field event
        usernameT.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus && usernameT.getText().toString().equals("Username")){
                    usernameT.setText("");
                }
                else if(!hasFocus && usernameT.getText().toString().equals("")){
                    usernameT.setText("Username");
                }
            }
        });

        //Password text field event
        passwordT.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus && passwordT.getText().toString().equals("Password")){
                    passwordT.setText("");
                }
                else if(!hasFocus && passwordT.getText().toString().equals("")){
                    passwordT.setText("Password");
                }
            }
        });

        //Confirm password
        confirmPasswordT.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus && confirmPasswordT.getText().toString().equals("Confirm password")){
                    confirmPasswordT.setText("");
                }
                else if(!hasFocus && confirmPasswordT.getText().toString().equals("")){
                    confirmPasswordT.setText("Confirm password");
                }
            }
        });

        //Firstname text field event
        firstNameT.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus && firstNameT.getText().toString().equals("First name")){
                    firstNameT.setText("");
                }
                else if(!hasFocus && firstNameT.getText().toString().equals("")){
                    firstNameT.setText("First name");
                }
            }
        });

        //Username text field event
        lastNameT.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus && lastNameT.getText().toString().equals("Last name")){
                    lastNameT.setText("");
                }
                else if(!hasFocus && lastNameT.getText().toString().equals("")){
                    lastNameT.setText("Last name");
                }
            }
        });

        //Date Of birth text field event
        dateOfBirthT.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus && dateOfBirthT.getText().toString().equals("Date of Birth (yyyy-mm-dd)")){
                    dateOfBirthT.setText("yyyy-mm-dd");
                    dateOfBirthT.selectAll();
                }
                else if(!hasFocus && dateOfBirthT.getText().toString().equals("yyyy-mm-dd")){
                    dateOfBirthT.setText("Date of Birth (yyyy-mm-dd)");
                }
            }
        });

        //Phone number event
        phoneNumberT.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus && phoneNumberT.getText().toString().equals("Phone (###-###-####)")){
                    phoneNumberT.setText("###-###-####");
                    phoneNumberT.selectAll();
                }
                else if(!hasFocus && phoneNumberT.getText().toString().equals("###-###-####")){
                    phoneNumberT.setText("Phone (###-###-####)");
                }
            }
        });

        //Hourly rate event
        hourlyRateT.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus && hourlyRateT.getText().toString().equals("Hourly rate")){
                    hourlyRateT.setText("");
                }
                else if(!hasFocus && hourlyRateT.getText().toString().equals("")){
                    hourlyRateT.setText("Hourly rate");
                }
            }
        });

    }

    /**
     * Load the spinnerfrom the database
     */
    private void loadSpinnerFromDB(){

        try {
            Spinner mySpinner = (Spinner)findViewById(R.id.spinner_signup_sp);
            MyDBHandler dbHandler = new MyDBHandler(this);

            ArrayList<String> myServices = new ArrayList<String>();
            myServices.add(NOTHING_TEXT);
            myServices.addAll(dbHandler.getAllServiceType());

            if (myServices.size()>0) {
                ArrayAdapter<String> data = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, myServices);
                data.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mySpinner.setAdapter(data);
            }
        }catch(Exception ex){
            Toast.makeText(this,ex.getMessage(),Toast.LENGTH_LONG).show();
        }
    }


    /**
     * ================================ others ===================================
     */
    /**
     * Make sure that all field are valid
     * @return
     */
    private boolean validateField(){
        //Get the elements


        //Get the string out of elements
        String username = usernameT.getText().toString();
        String password = passwordT.getText().toString();
        String confirmPassword = confirmPasswordT.getText().toString();
        String firstname = firstNameT.getText().toString();
        String lastname = lastNameT.getText().toString();
        String dateOfBirth = dateOfBirthT.getText().toString();
        String phoneNumber = phoneNumberT.getText().toString();
        String serviceType = serviceTypeT.getSelectedItem().toString();
        String hourlyRate = hourlyRateT.getText().toString();

        //Create a validator
        Validator v = new Validator();
        String error = "";

        /**
         * Validate all the field
         */
        if(!v.isUsernameValid(username))
            error = error + "\n- username";

        if(!v.isPasswordValid(password))
            error = error + "\n- password";

        if(!v.isPasswordValid(confirmPassword))
            error = error + "\n- confirm password";

        if(!password.equals(confirmPassword))
            error = error + "\n- password's doesnt match";

        if(!v.isFirstnameValid(firstname))
            error = error + "\n- first name";

        if(!v.isLastnameValid(lastname))
            error = error + "\n- last name";

        if(!v.isDateOfBirthValid(dateOfBirth))
            error = error + "\n- date of birth(yyyy-mm-dd)(between 1900 and 2018)";

        if(!v.isPhoneNumberValid(phoneNumber))
            error = error + "\n- phone number(###-###-####)";

        if(serviceType.equals(NOTHING_TEXT))
            error = error + "\n- serviceType";

        if(!v.isHourlyRateValid(hourlyRate))
            error = error + "\n- hourly rate";

        if(error.isEmpty())
            return false;
        else
            Toast.makeText(this,"There invalid field(s)..." + error,Toast.LENGTH_LONG).show();
        return true;
    }
}
