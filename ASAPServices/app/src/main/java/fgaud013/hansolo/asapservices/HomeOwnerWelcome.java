package fgaud013.hansolo.asapservices;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class HomeOwnerWelcome extends AppCompatActivity {

    private static final String EMPTY_TEXT_LV = "There is no Appointment.";

    //Table USER ACCOUNT
    private static final String USERID = "userID";
    private static final String HOME_ID = "homeID";
    private static final String APPOINTMENT_ID = "AppointmentID";
    private static final String COMPANY_ID = "CompanyID";
    private static final String SERVICE_ID = "ServiceID";
    private static final String DAY_NAME = "DayName";

    //Textfield
    private EditText et_firstName, et_lastName, et_birthDate, et_phone, et_address;

    //TextView
    private TextView tv_welcome, tv_firstName, tv_lastName, tv_birthDate, tv_phone, tv_address;

    //List view
    private ListView lv_myListOfAppointment;

    //Button
    private Button btn_spFinder;

    private HomeOwner homeOwner = new HomeOwner();
    private ArrayList<Appointment> myAppointments = new ArrayList<Appointment>();
    private ArrayList<Day> dayOfTheWeek = new ArrayList<Day>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_owner_welcome);

        //Get value send from the login activity
        Intent i = getIntent();
        Bundle data = i.getExtras();
        int userID = data.getInt(USERID);

        boolean result = findHomeOwner(userID);

        if(!result) {
            Toast.makeText(this, "Can't connect to the user.", Toast.LENGTH_LONG).show();
            finish();
        }

        init();
    }
    @Override
    protected void onResume(){
        super.onResume();
        loadAppointmentList();
    }

    /**
     * Initialise the text field and text view
     */
    private void init(){
        //Edit Text ======================================================
        et_firstName = (EditText)findViewById(R.id.firstName_et_how);
        et_lastName = (EditText)findViewById(R.id.lastName_et_how);
        et_address = (EditText)findViewById(R.id.address_et_how);
        et_birthDate = (EditText)findViewById(R.id.dateBirth_et_how);
        et_phone = (EditText)findViewById(R.id.phone_et_how);

        //Text View ======================================================
        tv_firstName = (TextView)findViewById(R.id.firstName_tv_how);
        tv_lastName = (TextView)findViewById(R.id.lastName_tv_how);
        tv_address = (TextView)findViewById(R.id.address_tv_how);
        tv_birthDate = (TextView)findViewById(R.id.dateBirth_tv_how);
        tv_phone = (TextView)findViewById(R.id.phone_tv_how);
        tv_welcome = (TextView)findViewById(R.id.welcomeText_how);

        //List View ======================================================
        lv_myListOfAppointment = (ListView)findViewById(R.id.lv_myListApp_how);

        //Button ======================================================
        btn_spFinder = (Button)findViewById(R.id.btn_searchService_how);

        //Disable all the text Edit Text ==================================
        changeEditText(et_firstName, false);
        changeEditText(et_lastName, false);
        changeEditText(et_address, false);
        changeEditText(et_phone, false);
        changeEditText(et_birthDate, false);

        loadDays();
        callEventListener();
        fillTextField();
        loadAppointmentList();
    }

    /**
     * Fill or edit the text field with a home Owner
     */
    private void fillTextField(){

        et_firstName.setText(this.homeOwner.getFirstName());
        et_lastName.setText(this.homeOwner.getLastName());
        et_birthDate.setText(this.homeOwner.getDateOfBirth());
        et_phone.setText(this.homeOwner.getPhoneNumber());
        et_address.setText(this.homeOwner.getAddress());

        tv_welcome.setText("Welcome, " + this.homeOwner.getUserName() + ", on the home owner page.");
    }

    /**
     * All the event that is needed
     */
    private void callEventListener(){
        btn_spFinder.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openSPFinder();
                    }
                }
        );

        lv_myListOfAppointment.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                String actual = ((TextView)arg1).getText().toString();
                if(!actual.equals(EMPTY_TEXT_LV) )
                    dialogModify(actual);
                return true;
            }
        });
    }

    private void openSPFinder(){
        Intent intent = new Intent(HomeOwnerWelcome.this, ServiceProviderFinder.class);


        Bundle extras = new Bundle();
        extras.putInt(HOME_ID, homeOwner.getHomeID());
        intent.putExtras(extras);

        startActivity(intent);
        loadAppointmentList();
    }

    /**
     * Dialog for deleting a service
     * @param Appointment
     */
    private void dialogModify(final String Appointment){
        /**
         * Event of the dialog box (found on STACKOVERFLOW)
         */
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Appointment");
        builder.setMessage("Do you want to access this Appointment?\n" +
                Appointment);


        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                accessAppointment(Appointment);
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
     * Access an Appointment
     * @param chainListView
     */
    private void accessAppointment(String chainListView){
        Appointment appointment = getAppointment(chainListView);

        if(appointment.getAppointment_id() != -1) {
            Intent intent = new Intent(HomeOwnerWelcome.this, HomeOwnerAppointment.class);

            Bundle extras = new Bundle();
            extras.putInt(APPOINTMENT_ID, appointment.getAppointment_id());
            extras.putInt(COMPANY_ID, appointment.getSp_id());
            extras.putInt(SERVICE_ID, appointment.getServiceType_id());
            extras.putString(DAY_NAME, getDayName(appointment.getDay_id()));

            intent.putExtras(extras);

            startActivity(intent);
            loadAppointmentList();
        }
        else
            Toast.makeText(this, "There was a problem getting the Appointment.\n" +
                    "Please try again", Toast.LENGTH_LONG).show();

    }

    /**
     * change the EditText
     * @param et
     */
    private void changeEditText(EditText et, boolean active){
        et.setEnabled(active);
        et.setFocusable(active);
        et.setCursorVisible(active);
    }

    /**
     * Load the list view
     */
    private void loadAppointmentList(){
        try {
            MyDBHandler dbHandler = new MyDBHandler(this);
            myAppointments.clear();
            ArrayList<String> myApps = new ArrayList<String>();

            myAppointments.addAll(dbHandler.getAllAppointmentFromHomeOwner(homeOwner.getHomeID()));

            Log.d("HOMEOWNERDEBUGGER","" );

            for(Appointment appointment: myAppointments)
                myApps.add(dbHandler.getServiceTypeName(appointment.getServiceType_id()) + " / " +
                        getDayName(appointment.getDay_id()) + " at " +
                        appointment.getStart_time() + " to " +
                        appointment.getEnd_time() + " with " +
                        dbHandler.getServiceProviderCompanyName(appointment.getSp_id())
                );

            if(myApps.size()==0) {
                myApps.add(EMPTY_TEXT_LV);
            }
            ArrayAdapter<String> data = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myApps);
            lv_myListOfAppointment.setAdapter(data);

        }catch(Exception ex){
            Toast.makeText(this,ex.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Find the home Owner connected to the userID send to this activity
     * @param id
     * @return
     */
    private boolean findHomeOwner(int id){
        boolean result = false;
        MyDBHandler dbHandler = new MyDBHandler(this);

        UserAccount userAccount = new UserAccount(dbHandler.getUserAccountFromID(id));
        Log.d("HOMEOWNERDEBUG","METHOD (getting user ID)" + userAccount.getUserID());

        this.homeOwner = new HomeOwner(dbHandler.getHomeOwner(userAccount));
        Log.d("HOMEOWNERDEBUG","METHOD (GETTING HOME OWNER)" + homeOwner.toString());

        if(this.homeOwner.getHomeID() >= 0)
            result = true;
        return(result);
    }

    /**
     * =================================== DAYS METHOD ======================================
     */

    /**
     * Get the day from the day name
     * @param dayName
     * @return
     */
    private Day getDay(String dayName){
        for(Day day: dayOfTheWeek)
            if(dayName.equals(day.getDay()))
                return (day);
        return(new Day());
    }

    /**
     * Get the date name from the ID
     * @param id
     * @return
     */
    private String getDayName(int id){
        for(Day day: dayOfTheWeek)
            if(day.getDayID() == id)
                return (day.getDay());
        return("Error");
    }


    /**
     * Load days of the week
     */
    private void loadDays(){
        MyDBHandler db = new MyDBHandler(this);
        dayOfTheWeek.clear();
        dayOfTheWeek.addAll(db.getDaysOfTheWeek());
    }

    /**
     * Return the appointment the person clicking on
     * @param text
     * @return
     */
    private Appointment getAppointment(String text){
        MyDBHandler dbHandler = new MyDBHandler(this);
        for(Appointment appointment : myAppointments){

            String fullChain = dbHandler.getServiceTypeName(appointment.getServiceType_id()) + " / " +
                    getDayName(appointment.getDay_id()) + " at " +
                    appointment.getStart_time() + " to " +
                    appointment.getEnd_time() + " with " +
                    dbHandler.getServiceProviderCompanyName(appointment.getSp_id());

            if(text.equals(fullChain))
                return(appointment);
        }
        return(new Appointment());
    }
}
