package fgaud013.hansolo.asapservices;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;//package for deitText
import android.widget.Button;//package for button
import android.view.View;
import android.content.Intent;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * @description : Main activity, or the ''Log in page''
 * @Author      : Frédérick Gaudet - 8035208
 */

public class MainActivity extends AppCompatActivity {

    //Table USER ACCOUNT
    private static final String COL_USERID = "userID";
    private static final String COL_USERNAME = "username";
    private static final String COL_PASSWORD = "password";
    private static final String COL_ADDRESS = "address";
    private static final String COL_PHONE = "phoneNumber";
    private static final String COL_PERMLVL = "permLevel";

    //Table SERVICE PROVIDER
    private static final String COL_SPID = "serviceProviderID";
    private static final String COL_COMPANYNAME = "companyName";
    private static final String COL_DESCRIPTION = "description";
    private static final String COL_LICENSED = "licensed";
    private static final String COL_ST_ID = "serviceTypeID";
    private static final String COL_ST_NAME= "serviceTypeName";

    //Table HOME OWNER
    private static final String COL_HOMEID = "homeOwnerID";
    private static final String COL_FIRSTNAME = "firstname";
    private static final String COL_LASTNAME = "lastname";
    private static final String COL_BIRTH = "dateOfBirth";


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

    @Override
    protected void onResume(){
        super.onResume();
        usernameT.setText("");
        passwordT.setText("");
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

                    serviceProviderTMP = new ServiceProvider(data.getString(COL_USERNAME),
                            -1,
                            data.getString(COL_PASSWORD),
                            data.getString(COL_ADDRESS),
                            data.getString(COL_PHONE),
                            1,//Mean service provider
                            -1,//We dont know yet
                            new ArrayList<ServiceType>(),
                            data.getString(COL_COMPANYNAME),
                            data.getString(COL_DESCRIPTION),
                            data.getBoolean(COL_LICENSED));

                    //Insert the service provider in the data base, maybe of use later
                    ServiceProvider serviceProvider = addServiceProvider(serviceProviderTMP);
                }
                catch(Exception ex){
                    Toast.makeText(getApplicationContext(),
                            "Something went wrong.\n Please try again!\n",
                            Toast.LENGTH_LONG).show();
                }

            }//When its from the Home owner sign up

            else if(permLevel == 2){
            //Create a user account with the data coming from the owner signup
                HomeOwner homeOwnerTMP = new HomeOwner(data.getString(COL_USERNAME),
                        -1,
                        data.getString(COL_PASSWORD),
                        data.getString(COL_ADDRESS),
                        data.getString(COL_PHONE),
                        2,
                        -1,
                        data.getString(COL_FIRSTNAME),
                        data.getString(COL_LASTNAME),
                        data.getString(COL_BIRTH));


                //Insert the home owner in the database
                HomeOwner homeOwner = addHomeOwner(homeOwnerTMP);

                int myID = homeOwner.getUserID();
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

        usernameT = findViewById(R.id.usernameTextMain);
        passwordT = findViewById(R.id.passwordTextMain);

        createOwnerListener();
        createProviderListener();
        createAccount();
        signInListener();
    }

    /**
     * Listener for the button create service type(have to be implement in another activity)
     */
    private void createAccount(){
        Button btn = findViewById(R.id.btn_createAccount);
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
        button_signup = findViewById(R.id.btnCreateOwner);
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
        button_signup1 = findViewById(R.id.btnCreateProvider);
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
        Button btnSignIn = findViewById(R.id.btnSignIn);
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

        UserAccount userAccount = new UserAccount(serviceProvider.getUserAccount());

        //Push the USERACCOUNT
        if(serviceProvider.getUserID() == -1)//If user not being created yet
            userAccount = dbHandler.insertUserAccount(userAccount);

        int userID = userAccount.getUserID();
        serviceProvider.setUserAccount(userAccount);
        Toast.makeText(this, userAccount.toString(), Toast.LENGTH_LONG).show();

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
     * Insert an homeowner in the database
     * @param homeOwner
     * @return
     */
    private HomeOwner addHomeOwner(HomeOwner homeOwner) {
        MyDBHandler dbHandler = new MyDBHandler(this);

        UserAccount userAccount = new UserAccount(homeOwner.getUserAccount());

        //Push the USERACCOUNT
        if(homeOwner.getUserID() == -1)//If user not being created yet
            userAccount = dbHandler.insertUserAccount(userAccount);

        int userID = userAccount.getUserID();
        homeOwner.setUserAccount(userAccount);
        Toast.makeText(this, userAccount.toString(), Toast.LENGTH_LONG).show();

        if(userID >= 0) {

            HomeOwner hoTmp = dbHandler.insertHomeOwner(homeOwner);

            int hoID = hoTmp.getHomeID();

            if (hoID >= 0) {
                Toast.makeText(this, "Home Owner created!" + hoID, Toast.LENGTH_LONG).show();
                return (hoTmp);
            } else {
                if (hoID == -1)
                    Toast.makeText(this, "Cant find the home owner ID!" + hoID, Toast.LENGTH_LONG).show();
                else if (hoID == -5)
                    Toast.makeText(this, "Generic error!" + hoID, Toast.LENGTH_LONG).show();
                else if (hoID == -6)
                    Toast.makeText(this, "Error searching ID HomeOwner!" + hoID, Toast.LENGTH_LONG).show();
            }
            return (hoTmp);
        }
        else {
            Toast.makeText(this, "Couldn't create the Home Owner!", Toast.LENGTH_LONG).show();
            return (homeOwner);
        }
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
            Toast.makeText(this, "Wrong username or password.\nPlease try again!\n" +
                    "The username is CASE SENSITIVE.", Toast.LENGTH_LONG).show();
        else{//Have found the user
            try {
                int permLevel = theUser.getPermLevel();

                //Prepare the bundle to send data to the next INTENT
                Intent intent;

                if(permLevel == 0)
                    intent = new Intent(MainActivity.this, AdminWelcome.class);
                else if(permLevel == 1)
                    intent = new Intent(MainActivity.this, ServiceProviderWelcome.class);
                else
                    intent = new Intent(MainActivity.this, HomeOwnerWelcome.class);

                Bundle extras = new Bundle();
                extras.putInt(COL_USERID, theUser.getUserID());
                intent.putExtras(extras);

                startActivity(intent);
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
        builder.setMessage("It will clear the database and create.\n1 admin.\n10 Service Type.\n2 home owner\n2 Service provider.\nAre you sure?");
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
        dbHandler.insertServiceType(
                new ServiceType(-1,"Window Cleaning",20.45));
        dbHandler.insertServiceType(
                new ServiceType(-1,"Floor Cleaning",15.23));
        dbHandler.insertServiceType(
                new ServiceType(-1,"Plumbing",25.76));
        dbHandler.insertServiceType(
                new ServiceType(-1,"Electricity",28.90));
        dbHandler.insertServiceType(
                new ServiceType(-1,"Light bulb changer",14.75));
        dbHandler.insertServiceType(
                new ServiceType(-1,"Domotic",24.89));
        dbHandler.insertServiceType(
                new ServiceType(-1,"Floor waxing",18.72));
        dbHandler.insertServiceType(
                new ServiceType(-1,"Painting",22.22));
        dbHandler.insertServiceType(
                new ServiceType(-1,"Door fixing",19.50));
        dbHandler.insertServiceType(
                new ServiceType(-1,"Wall cleaning",16.56));

        dbHandler.insertAdmin();


    /**
    * ================================ INSERT SERVICE PROVIDER ===================================
    */
        ServiceProvider serviceProvider = new ServiceProvider("usersp1",
                -1,
                "pass",
                "456 MyCompanyStreet street",
                "888-123-1234",
                1,
                -1,
                new ArrayList<ServiceType>(),
                "Bob inc",
                "We are a company that doe bunch of different thing unlicensed",
                false);

        serviceProvider = this.addServiceProvider(serviceProvider);
        int spid1 = serviceProvider.getServiceProviderID();
        dbHandler.addServiceTypeToServiceProvider(1, serviceProvider.getServiceProviderID());
        dbHandler.addServiceTypeToServiceProvider(2, serviceProvider.getServiceProviderID());
        dbHandler.addServiceTypeToServiceProvider(3, serviceProvider.getServiceProviderID());
        dbHandler.addServiceTypeToServiceProvider(4, serviceProvider.getServiceProviderID());
        dbHandler.addServiceTypeToServiceProvider(7, serviceProvider.getServiceProviderID());

        //Add all availability
        dbHandler.addAvailability(serviceProvider.getServiceProviderID(),
                1,
                "08:00",
                "18:00");

        dbHandler.addAvailability(serviceProvider.getServiceProviderID(),
                2,
                "08:00",
                "18:00");

        dbHandler.addAvailability(serviceProvider.getServiceProviderID(),
                3,
                "08:00",
                "18:00");

        dbHandler.addAvailability(serviceProvider.getServiceProviderID(),
                4,
                "08:00",
                "18:00");

        dbHandler.addAvailability(serviceProvider.getServiceProviderID(),
                5,
                "08:00",
                "18:00");

        dbHandler.addAvailability(serviceProvider.getServiceProviderID(),
                6,
                "12:00",
                "17:00");

        dbHandler.addAvailability(serviceProvider.getServiceProviderID(),
                7,
                "12:00",
                "17:00");


        serviceProvider = new ServiceProvider("usersp2",
                -1,
                "pass",
                "555 ottawa Boulvard",
                "666-666-6666",
                1,
                -1,
                new ArrayList<ServiceType>(),
                "Constructel ltm",
                "Here a test of how much description can be written." +
                        "There is actual no limit. The limit could be added but w.e",
                true);

        serviceProvider = this.addServiceProvider(serviceProvider);
        int spid2 = serviceProvider.getServiceProviderID();
        dbHandler.addServiceTypeToServiceProvider(4, serviceProvider.getServiceProviderID());
        dbHandler.addServiceTypeToServiceProvider(8, serviceProvider.getServiceProviderID());
        dbHandler.addServiceTypeToServiceProvider(6, serviceProvider.getServiceProviderID());

        //Add all availability
        dbHandler.addAvailability(serviceProvider.getServiceProviderID(),
                1,
                "07:00",
                "20:00");

        dbHandler.addAvailability(serviceProvider.getServiceProviderID(),
                2,
                "07:00",
                "20:00");

        dbHandler.addAvailability(serviceProvider.getServiceProviderID(),
                3,
                "07:00",
                "20:00");

        dbHandler.addAvailability(serviceProvider.getServiceProviderID(),
                4,
                "07:00",
                "20:00");

        dbHandler.addAvailability(serviceProvider.getServiceProviderID(),
                5,
                "07:00",
                "20:00");

        /**
         * ================================ appointment ===================================
         */


        /**
         * ================================ CASE USER (2 USER) ===================================
         */
        HomeOwner homeOwner1 = new HomeOwner("homeuser1",
                -1,
                "pass",
                "123 Address home user 1",
                "345-666-5454",
                2,
                -1,
                "Bob home",
                "User",
                "1960-05-01"
        );

        homeOwner1 = this.addHomeOwner(homeOwner1);


        HomeOwner homeOwner2 = new HomeOwner("homeuser2",
                -1,
                "pass",
                "987 Address home user 2",
                "819-664-3333",
                2,
                -1,
                "Alfonce",
                "the home user",
                "1989-12-31"
        );

        homeOwner2 = this.addHomeOwner(homeOwner2);

        /**
         * ================================ appointment ===================================
         */

        int homeid1 = homeOwner1.getHomeID();
        int homeid2 = homeOwner2.getHomeID();

        //Appointment service provider 1
        dbHandler.createAppointment(spid1,homeid1,1,1,"08:00", "12:00");//ID: 1
        dbHandler.createAppointment(spid1,homeid2,2,1,"12:15", "14:00");//ID: 2
        dbHandler.createAppointment(spid1,homeid1,2,2,"08:00", "12:00");//ID: 3
        dbHandler.createAppointment(spid1,homeid1,3,2,"12:15", "14:00");//ID: 4
        dbHandler.createAppointment(spid1,homeid2,4,1,"14:15", "16:00");//ID: 5
        dbHandler.createAppointment(spid1,homeid1,3,4,"12:15", "14:00");//ID: 6
        dbHandler.createAppointment(spid1,homeid2,4,5,"14:15", "16:00");//ID: 7
        dbHandler.createAppointment(spid1,homeid1,3,6,"12:15", "14:00");//ID: 8
        dbHandler.createAppointment(spid1,homeid2,4,7,"14:15", "16:00");//ID: 9

        //Complete appointment
        Rating r1 = new Rating(-1,1,3,"Average service");
        Rating r2 = new Rating(-1,3,4,"More than average");
        Rating r3 = new Rating(-1,5,4,"More than average");
        Rating r4 = new Rating(-1,7,3,"Average service");

        //Average for service provider 1 = 3.5
        dbHandler.completeAppointmentWithRating(r1);
        dbHandler.completeAppointmentWithRating(r2);
        dbHandler.completeAppointmentWithRating(r3);
        dbHandler.completeAppointmentWithRating(r4);

        //Appointment service provider 2
        dbHandler.createAppointment(spid2,homeid1,4,1,"08:00", "12:00");//ID: 10
        dbHandler.createAppointment(spid2,homeid2,4,1,"12:15", "14:00");//ID: 11
        dbHandler.createAppointment(spid2,homeid1,4,2,"08:00", "12:00");//ID: 12
        dbHandler.createAppointment(spid2,homeid2,4,2,"12:15", "14:00");//ID: 13
        dbHandler.createAppointment(spid2,homeid1,4,3,"08:00", "12:00");//ID: 14
        dbHandler.createAppointment(spid2,homeid2,4,3,"12:15", "14:00");//ID: 15
        dbHandler.createAppointment(spid2,homeid1,4,4,"08:00", "12:00");//ID: 16
        dbHandler.createAppointment(spid2,homeid2,4,4,"12:15", "14:00");//ID: 17
        dbHandler.createAppointment(spid2,homeid1,4,5,"08:00", "12:00");//ID: 18
        dbHandler.createAppointment(spid2,homeid2,4,5,"12:15", "14:00");//ID: 19

        Rating rr1 = new Rating(-1,10,1,"Bad");
        Rating rr2 = new Rating(-1,13,2,"Below average");
        Rating rr3 = new Rating(-1,15,1,"Bad");
        Rating rr4 = new Rating(-1,17,2,"Below average");

        //Average for service provider 2 = 1.5
        dbHandler.completeAppointmentWithRating(rr1);
        dbHandler.completeAppointmentWithRating(rr2);
        dbHandler.completeAppointmentWithRating(rr3);
        dbHandler.completeAppointmentWithRating(rr4);

    }
}
