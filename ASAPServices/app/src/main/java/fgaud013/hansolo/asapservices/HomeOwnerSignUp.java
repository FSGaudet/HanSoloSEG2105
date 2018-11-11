package fgaud013.hansolo.asapservices;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;//package for deitText
import android.widget.Button;//package for button
import android.view.View;
import android.content.Intent;
import android.widget.Toast;

/**
 * @description : Page activity or where home owner can sign up
 * @Author      : Frédérick Gaudet - 8035208
 */

public class HomeOwnerSignUp extends AppCompatActivity {

    private static Button b_finish;
    private static EditText usernameT, passwordT, confirmPasswordT, firstNameT,
            lastNameT, dateOfBirthT, phoneNumberT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_owner_sign_up);
        init();
    }

    /**
     * Initialisation of textfield
     */
    private void init(){
        usernameT = (EditText)findViewById(R.id.usernameText1);
        passwordT = (EditText)findViewById(R.id.passwordText1);
        confirmPasswordT = (EditText)findViewById(R.id.confirmPasswordText1);
        firstNameT = (EditText)findViewById(R.id.firstnameText1);
        lastNameT = (EditText)findViewById(R.id.lastnameText1);
        dateOfBirthT = (EditText)findViewById(R.id.dateOfBirthText1);
        phoneNumberT = (EditText)findViewById(R.id.phoneNumberText1);
        b_finish = (Button)findViewById(R.id.btnCreate1);
        FinishButtonListener();
        focusEvents();
    }

    /**
     * ================================ LISTENERS ===================================
     */

    public void FinishButtonListener(){
        b_finish.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        boolean error = false;

                        EditText username= (EditText)findViewById(R.id.usernameText1);
                        EditText password = (EditText)findViewById(R.id.passwordText1);
                        EditText firstName = (EditText)findViewById(R.id.firstnameText1);
                        EditText lastName = (EditText)findViewById(R.id.lastnameText1);
                        EditText dateOfBirth = (EditText)findViewById(R.id.dateOfBirthText1);
                        EditText phoneNumber = (EditText)findViewById(R.id.phoneNumberText1);


                        //Make sure that no field are empty
                        error = validateField();

                        //Error = true if there was a error so we dont come here
                        if(!error) {
                            Intent returnIntent = new Intent(HomeOwnerSignUp.this, MainActivity.class);
                            Bundle extras = new Bundle();

                            extras.putString("username", username.getText().toString());
                            extras.putString("password", password.getText().toString());
                            extras.putString("firstname", firstName.getText().toString());
                            extras.putString("lastname", lastName.getText().toString());
                            extras.putString("dateOfBirth", dateOfBirth.getText().toString());
                            extras.putString("phoneNumber", phoneNumber.getText().toString());
                            extras.putInt("permLevel", 2);

                            returnIntent.putExtras(extras);

                            setResult(RESULT_OK, returnIntent);

                            finish();
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
    }

    /**
     * ================================ other ===================================
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

        if(error.isEmpty())
            return false;
        else
            Toast.makeText(this,"There invalid field(s)..." + error,Toast.LENGTH_LONG).show();
        return true;
    }
}
