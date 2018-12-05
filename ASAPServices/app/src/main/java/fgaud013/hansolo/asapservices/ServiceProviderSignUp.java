package fgaud013.hansolo.asapservices;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.content.Intent;
import android.widget.Toast;

/**
 * @description : Page activity or where service provider can sign up
 * @Author      : Frédérick Gaudet - 8035208
 */

public class ServiceProviderSignUp extends AppCompatActivity {

    private static final String NOTHING_TEXT = "-- NOTHING --";

    private EditText usernameT, passwordT, confirmPasswordT, addressT,
            descriptionT,phoneNumberT,companyNameT;
    private CheckBox licensedCB;

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
        usernameT = findViewById(R.id.usernameText);
        passwordT = findViewById(R.id.passwordText);
        confirmPasswordT = findViewById(R.id.confirmPasswordText);
        phoneNumberT = findViewById(R.id.phoneText);
        companyNameT = findViewById(R.id.companyName_spsu);
        addressT = findViewById(R.id.address_spsu);
        licensedCB = findViewById(R.id.licensedCB_spsu);
        descriptionT = findViewById(R.id.description_spsu);
        btnCreate = findViewById(R.id.btnCreate);

        focusEvents();
        //loadSpinnerFromDB();
        FinishButtonListener();
    }
    /**
     * ================================ LISTENERS ===================================
     */
    public void FinishButtonListener(){
        btnCreate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Getting values, need some check up
                        boolean error = false;

                        //Make sure that no field are empty
                        error = validateField();

                        //Error = true if there was a error so we dont come here
                        if(!error) {
                            Intent returnIntent = new Intent(ServiceProviderSignUp.this, MainActivity.class);
                            Bundle extras = new Bundle();
                            try {
                                String description = descriptionT.getText().toString();
                                description = (description.equals("Description (optional)"))?"":description;

                                boolean licensed = licensedCB.isChecked();

                                extras.putString("username", usernameT.getText().toString());
                                extras.putString("description", description);
                                extras.putBoolean("licensed", licensed);
                                extras.putString("companyName", companyNameT.getText().toString());
                                extras.putString("address", addressT.getText().toString());
                                extras.putString("password", passwordT.getText().toString());
                                extras.putString("phoneNumber", phoneNumberT.getText().toString());
                                extras.putInt("permLevel", 1);

                                returnIntent.putExtras(extras);

                                setResult(RESULT_OK, returnIntent);

                                finish();
                            }
                            catch(Exception ex)
                            {
                                Log.d("DEBUGSIGNUPSP","METHOD (BUTTON)" + ex.getMessage());
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
        companyNameT.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus && companyNameT.getText().toString().equals("Company name")){
                    companyNameT.setText("");
                }
                else if(!hasFocus && companyNameT.getText().toString().equals("")){
                    companyNameT.setText("Company name");
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
        addressT.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus && addressT.getText().toString().equals("Address")){
                    addressT.setText("");
                }
                else if(!hasFocus && addressT.getText().toString().equals("")){
                    addressT.setText("Address");
                }
            }
        });
        try {
            descriptionT.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus && descriptionT.getText().toString().equals("Description (optional)")) {
                        descriptionT.setText("");
                    } else if (!hasFocus && addressT.getText().toString().equals("")) {
                        addressT.setText("Description (optional)");
                    }
                }
            });
        }
        catch(Exception ex)
        {
            Log.d("DEBUGSIGNUPSP","METHOD (BUTTON)" + ex.getMessage());
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
        //Get the string out of elements
        String username = usernameT.getText().toString();
        String password = passwordT.getText().toString();
        String confirmPassword = confirmPasswordT.getText().toString();
        String address = addressT.getText().toString();
        String companyName = companyNameT.getText().toString();
        String phoneNumber = phoneNumberT.getText().toString();

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

        if(!v.isCompanyNameValid(companyName))
            error = error + "\n- company name";

        if(!v.isAddressValid(address))
            error = error + "\n- address";

        if(!v.isPhoneNumberValid(phoneNumber))
            error = error + "\n- phone number(###-###-####)";

        if(error.isEmpty())
            return false;
        else
            Toast.makeText(this,"There invalid field(s)..." + error,Toast.LENGTH_LONG).show();
        return true;
    }
}
