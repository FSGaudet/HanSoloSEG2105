package fgaud013.hansolo.asapservices;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;//package for deitText
import android.widget.Button;//package for button
import android.view.View;
import android.content.Intent;
import android.widget.Toast;

/**
 * @description : Main activity, or the ''Log in page''
 * @Author      : Frédérick Gaudet - 8035208
 */

public class MainActivity extends AppCompatActivity {

    //Table USER ACCOUNT
    private static final String COL_USERID = "userID";
    private static final String COL_USERNAME = "username";
    private static final String COL_PASSWORD = "password";
    private static final String COL_FIRSTNAME = "firstname";
    private static final String COL_LASTNAME = "lastname";
    private static final String COL_BIRTH = "dateOfBirth";
    private static final String COL_PHONE = "phoneNumber";
    private static final String COL_PERMLVL = "permLevel";

    //Table SERVICE PROVIDER
    private static final String COL_SPID = "serviceProviderID";
    private static final String COL_SERVICETYPE = "serviceType";
    private static final String COL_HOURLYRATE = "hourlyRate";
    private static final String COL_ST_ID = "serviceTypeID";
    private static final String COL_ST_NAME= "serviceTypeName";


    private static Button button_signup;
    private static Button button_signup1;

    private static EditText usernameT, passwordT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    /**
     * Do different thing if the user sign up as provider or home owner
     * @param requestCode
     * @param resultCode
     * @param data2
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data2) {
        if (resultCode == RESULT_CANCELED) return;
        {
            Bundle data = data2.getExtras();//Receive the value of the bundle
            int permLevel = data.getInt(COL_PERMLVL,-1);

            Toast.makeText(getApplicationContext(), "Perm level :" + permLevel, Toast.LENGTH_LONG).show();
            //If there is a problem with anything
            if(permLevel == -1){

            }
            //If it is a ServiceProvider that try to sign up
            else if(permLevel == 1){
                ServiceProvider serviceProviderTMP;
                //Create the new service provider from the data.extra
                try {
                    ServiceType servType = new ServiceType(-1, data.getString(COL_SERVICETYPE));

                    serviceProviderTMP = new ServiceProvider(data.getString(COL_USERNAME),
                            -1,
                            data.getString(COL_PASSWORD),
                            data.getString(COL_FIRSTNAME),
                            data.getString(COL_LASTNAME),
                            data.getString(COL_BIRTH),
                            data.getString(COL_PHONE),
                            1,
                            -1,
                            servType,
                            Double.valueOf(data.getDouble(COL_HOURLYRATE, -1)));

                    //Insert the service provider in the data base
                    ServiceProvider serviceProvider = addServiceProvider(serviceProviderTMP);
                    int myID = serviceProvider.getServiceProviderID();
                }
                catch(Exception ex){
                    Toast.makeText(getApplicationContext(),
                            "Something went wrong.\n Please try again!\n",
                            Toast.LENGTH_LONG).show();
                }

            }//When its from the Home owner sign up

            else if(permLevel == 2){
            //Create a user account with the data coming from the owner signup
                UserAccount userAccountTMP = new UserAccount(  data.getString(COL_USERNAME),
                        -1,
                        data.getString(COL_PASSWORD),
                        data.getString(COL_FIRSTNAME),
                        data.getString(COL_LASTNAME),
                        data.getString(COL_BIRTH),
                        data.getString(COL_PHONE),
                        2);


                //Insert the home owner in the database
                UserAccount userAccount = addUserAccount(userAccountTMP);
                int myID = userAccount.getUserID();
            }
        }
    }

    /**
     * Initialisation
     */
    private void init(){
        callListener();

        //Look if database is empty
        MyDBHandler dbHandler = new MyDBHandler(this);

        if(dbHandler.isDBEmpty())
            Toast.makeText(this, "The database is empty, please click on test case to fill it", Toast.LENGTH_LONG).show();
    }

    /**
     * =================================== LISTENERS ======================================
     */

    /**
     * Call all the listener
     */
    private void callListener(){

        usernameT = (EditText) findViewById(R.id.usernameTextMain);
        passwordT = (EditText) findViewById(R.id.passwordTextMain);

        createOwnerListener();
        createProviderListener();
        createAccount();
        signInListener();
        onFocusListener();

        //Clean the table
        //MyDBHandler dbHandler = new MyDBHandler(this);
        //dbHandler.cleanUser();
        //dbHandler.clearAll();
    }

    /**
     * On focus listener of textfield username & password
     */
    public void onFocusListener(){

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

        passwordT.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus && passwordT.getText().toString().equals("Password")){
                    passwordT.setText("");
                }
            }
        });
    }

    /**
     * Listener for the button create service type(have to be implement in another activity)
     */
    private void createAccount(){
        Button btn = (Button)findViewById(R.id.btn_createAccount);
        btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogCreation();
                    }
                }
        );
    }

    //Listener for the create owner button
    private void createOwnerListener(){
        button_signup = (Button)findViewById(R.id.btnCreateOwner);
        button_signup.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this,HomeOwnerSignUp.class);
                        startActivityForResult(intent,0);
                    }
                }
        );

    }

    //Listener for Create provider button
    private void createProviderListener(){
        button_signup1 = (Button)findViewById(R.id.btnCreateProvider);
        button_signup1.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this,ServiceProviderSignUp.class);
                        startActivityForResult(intent,0);
                    }
                }
        );

    }

    //Sign in button listener
    private void signInListener(){
        Button btnSignIn = (Button)findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        tryConnection();
                    }
                }
        );
    }

    /**
     * =================================== Data base handler ======================================
     */

    /**
     * Add a service provider by calling addUserAcount; to generate an ID and return that ID to put in
     * the table ServiceProvider
     * @param serviceProvider
     * @return Service Provider
     */
    private ServiceProvider addServiceProvider(ServiceProvider serviceProvider) {
        MyDBHandler dbHandler = new MyDBHandler(this);

        UserAccount userAccount = new UserAccount(serviceProvider.getUserName(),
                serviceProvider.getUserID(),
                serviceProvider.getPassword(),
                serviceProvider.getFirstName(),
                serviceProvider.getLastName(),
                serviceProvider.getDateOfBirth(),
                serviceProvider.getPhoneNumber(),
                serviceProvider.getPermLevel()
        );

        //Push the USERACCOUNT
        if(serviceProvider.getUserID() == -1)//If user not being created yet
            userAccount = addUserAccount(userAccount);

        int userID = userAccount.getUserID();
        serviceProvider.setUserAccount(userAccount);
        Toast.makeText(this, userAccount.toString(), Toast.LENGTH_LONG).show();

        //Toast.makeText(this, "Cant find the service provider ID!" + serviceProvider.getUserID(), Toast.LENGTH_LONG).show();

        if(userID >= 0) {

            ServiceProvider spTmp = dbHandler.insertServiceProvider(serviceProvider);

            int spID = spTmp.getServiceProviderID();

            if (spID >= 0) {
                Toast.makeText(this, "ServiceProvider created!" + spID, Toast.LENGTH_LONG).show();
                return (spTmp);
            } else {
                if (spID == -1)
                    Toast.makeText(this, "Cant find the service provider ID!" + spID, Toast.LENGTH_LONG).show();
                else if (spID == -4)
                    Toast.makeText(this, "Cant find the service Type!" + spID, Toast.LENGTH_LONG).show();
                else if (spID == -5)
                    Toast.makeText(this, "Generic error!" + spID, Toast.LENGTH_LONG).show();
                else if (spID == -6)
                    Toast.makeText(this, "Error searching ID SP!" + spID, Toast.LENGTH_LONG).show();
            }
            return (spTmp);
        }
        else {
            Toast.makeText(this, "Couldn't create service provider!", Toast.LENGTH_LONG).show();
            return (serviceProvider);
        }
    }

    /**
     * Method to add a user
     * @param userAccount
     * @return userAccount
     */
    private UserAccount addUserAccount(UserAccount userAccount) {

        MyDBHandler dbHandler = new MyDBHandler(this);

        //String userAccountTmp = dbHandler.insertUserAccount(userAccount);
        //Toast.makeText(this,"Here the message: \n" + userAccountTmp,Toast.LENGTH_LONG).show();

        UserAccount userAccountTmp = dbHandler.insertUserAccount(userAccount);
        int userID = userAccountTmp.getUserID();

        if(userID >= 0) {
            Toast.makeText(this,"User created!" + userID,Toast.LENGTH_LONG).show();
            return (userAccountTmp);
        }
        else{
            //-1 = error getting ID, -2 = Already exist, -3 = error
            if(userID == -1)
                Toast.makeText(this,"Error getting ID!" + userID,Toast.LENGTH_LONG).show();
            else if(userID == -2)
                Toast.makeText(this,"User already exist!" + userID,Toast.LENGTH_LONG).show();
            else if(userID == -3)
                Toast.makeText(this,"Error!" + userID,Toast.LENGTH_LONG).show();
        }

        return(userAccount);
    }

    /**
     * Method to connect a user, has to be changed for something with AUTH instead of having a List
     * @return UserAccount
     */
    private void tryConnection(){
        String username = ((EditText)findViewById(R.id.usernameTextMain)).getText().toString();
        String password = ((EditText)findViewById(R.id.passwordTextMain)).getText().toString();

        MyDBHandler dbHandler = new MyDBHandler(this);

        UserAccount theUser = dbHandler.getUserAccount(username,password);

        //Cant find the user

        if(theUser.getUserID()<0)
            Toast.makeText(this, "Wrong username or password.\nPlease try again!", Toast.LENGTH_LONG).show();
        else{//Have found the user
            try {
                int permLevel = theUser.getPermLevel();
                ServiceProvider spTmp = new ServiceProvider(theUser);

                //Prepare the bundle to send data to the next INTENT
                Intent intent = new Intent(MainActivity.this, WelcomePage.class);
                Bundle extras = new Bundle();

                extras.putString(COL_USERNAME, theUser.getUserName());
                extras.putString(COL_PASSWORD, theUser.getPassword());
                extras.putInt(COL_USERID, theUser.getUserID());
                extras.putString(COL_FIRSTNAME, theUser.getFirstName());
                extras.putString(COL_LASTNAME, theUser.getLastName());
                extras.putString(COL_BIRTH, theUser.getDateOfBirth());
                extras.putString(COL_PHONE, theUser.getPhoneNumber());
                extras.putInt(COL_PERMLVL, theUser.getPermLevel());

                //Look the user permission level, 0 = admin, 1 = service provider, 2 = home owner
                if(permLevel == 1) {
                    spTmp = dbHandler.getServiceProvider(theUser);//Get the rest if a service provider
                    extras.putInt(COL_SPID, spTmp.getServiceProviderID());
                    extras.putInt(COL_ST_ID, spTmp.getServiceType().getSTID());
                    extras.putString(COL_ST_NAME, spTmp.getServiceType().getServiceName());
                    extras.putDouble(COL_HOURLYRATE, spTmp.getHourlyRate());
                }

                //If perm level is 1 and service Provider ID is not set
                if(spTmp.getServiceProviderID() < 0 && permLevel == 1)
                    Toast.makeText(this, "Couldnt connect!", Toast.LENGTH_LONG).show();

                //Ready to connect and go to welcome page
                else {

                    //Toast.makeText(this, spTmp.toString(), Toast.LENGTH_LONG).show();
                    intent.putExtras(extras);
                    startActivity(intent);
                }
            }
            catch(Exception ex){//Error connecting
                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();

            }
        }

    }

    /**
     * The dialog at the isertion of data
     */
    private void dialogCreation(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name);
        builder.setMessage("It will clear the database and create.\n1 admin.\n2 Service Type.\n2 home owner\n2 Service provider.\nAre you sure?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                testCase();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * All the test case, creating 2 service type, 1 admin, 2 user, 2 service providers
     */
    private void testCase(){
        MyDBHandler dbHandler = new MyDBHandler(this);
        dbHandler.clearAll();

        /**
         * ================================ SERVICE TYPE ===================================
         */
        dbHandler.insertServiceType("Window Cleaning");
        dbHandler.insertServiceType("Floor Cleaning");

        /**
         * ================================ INSERT ADMIN ===================================
         */
        UserAccount adminAccount = new UserAccount("admin",
                -1,
                "admin",
                "adminFirstName",
                "adminLastName",
                "1980-02-05",
                "888-888-8888",
                0);

        adminAccount = dbHandler.insertUserAccount(adminAccount);

        /**
         * ================================ CASE USER (2 USER) ===================================
         */
        UserAccount userAccount1 = new UserAccount("bob",
                -1,
                "pass1",
                "Bob",
                "Gratton",
                "1985-12-21",
                "819-661-5690",
                2);

        userAccount1 = dbHandler.insertUserAccount(userAccount1);

        UserAccount userAccount2 = new UserAccount("drake",
                -1,
                "pass1",
                "Drake",
                "DeRouyn",
                "1965-06-15",
                "613-123-1234",
                2);

        userAccount2 = dbHandler.insertUserAccount(userAccount2);

        /**
         * ================================ CASE SERVICE TYPE ===================================
         */
        //First service provider
        UserAccount userAccount3 = new UserAccount("siphon",
                -1,
                "1234",
                "Jean-Louis",
                "Premier",
                "1945-01-09",
                "819-656-3456",
                1);

        //Insert the user part
        //userAccount3 = dbHandler.insertUserAccount(userAccount3);
        //Create the type
        ServiceType st1 = new ServiceType(1,"");
        st1.setServiceName(dbHandler.getServiceTypeName(st1.getSTID()));//Get the name from the ID

        ServiceProvider serviceProvider1 = new ServiceProvider(userAccount3,userAccount3.getUserID(),st1,20);
        serviceProvider1 = addServiceProvider(serviceProvider1);

        UserAccount userAccount4 = new UserAccount("Pierro",
                -1,
                "0112",
                "Pierre",
                "Lacasse",
                "1989-09-22",
                "123-123-1234",
                1);

        //Insert the user part
        //userAccount4 = dbHandler.insertUserAccount(userAccount4);
        //Create the type

        //Get the service type name
        ServiceType st2 = new ServiceType(2,"");
        st2.setServiceName(dbHandler.getServiceTypeName(st2.getSTID()));//Get the name from the ID

        //Push the service Provider in database
        ServiceProvider serviceProvider2 = new ServiceProvider(userAccount4,userAccount4.getUserID(),st2,23);
        serviceProvider2 = addServiceProvider(serviceProvider2);

        //Toast.makeText(this,userAccount4.toString(),Toast.LENGTH_LONG).show();
        //Toast.makeText(this,serviceProvider2.toString(),Toast.LENGTH_LONG).show();

        Toast.makeText(this,"Completed, please refer to code for the username/password of each",Toast.LENGTH_LONG).show();

    }
}
