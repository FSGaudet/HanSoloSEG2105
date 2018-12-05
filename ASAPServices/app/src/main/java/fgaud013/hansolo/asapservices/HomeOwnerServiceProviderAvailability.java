package fgaud013.hansolo.asapservices;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class HomeOwnerServiceProviderAvailability extends AppCompatActivity {

    private static final String COMPANY_NAME = "CompanyName";
    private static final String EMPTY_TEXT_LV1 = "No availability";
    private static final String EMPTY_TEXT_LV2 = "No Appointment be the first";
    private static final String DAY_TO_MODIFY = "dayToChange";
    private static final String COL_SPID = "serviceProviderID";
    private static final String COMPANY_ID = "CompanyID";
    private static final String HOME_ID = "homeID";

    private ArrayList<String> hoursOfTheDay = new ArrayList<String>();
    private ArrayList<Day> dayOfTheWeek = new ArrayList<Day>();
    private ArrayList<Availability> availabilities = new ArrayList<Availability>();
    private ArrayList<Appointment> appointments = new ArrayList<Appointment>();
    private ArrayList<ServiceType> servicesFromSP = new ArrayList<ServiceType>();

    private Spinner spin_day, spin_start, spin_end, spin_service;

    private Button btn_Appointment;

    private ListView lv_availability, lv_Appointment;

    private TextView tv_main;

    private String companyName;
    private int companyID = -1;
    private int homeID =-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_owner_service_provider_availability);//Get value send from the login activity

        Intent i = getIntent();
        Bundle data = i.getExtras();

        companyName = data.getString(COMPANY_NAME);
        companyID = data.getInt(COMPANY_ID);
        homeID = data.getInt(HOME_ID);

        if(companyName.equals("") || companyID == -1 || homeID == -1)
            finish();

        init();
    }

    @Override
    protected void onResume(){
        super.onResume();
        loadListView();
    }

    /**
     * Initialise all the component
     */
    private void init(){

        //Load component in the good variables
        spin_day = (Spinner)findViewById(R.id.spinner_day_hospa);
        spin_start = (Spinner)findViewById(R.id.spinner_start_hospa);
        spin_end = (Spinner)findViewById(R.id.spinner_end_hospa);
        spin_service = (Spinner)findViewById(R.id.spinner_service_hospa);

                btn_Appointment = (Button)findViewById(R.id.btn_app_hospa);

        lv_availability = (ListView)findViewById(R.id.lv_availability_hospa);
        lv_Appointment = (ListView)findViewById(R.id.lv_Appointment_hospa);

        tv_main = (TextView)findViewById(R.id.tv_main_hospa);

        tv_main.setText("Create Appointment with\n" + this.companyName);

        //Load the days
        loadDays();
        loadHours();
        loadSpinnerDays();
        loadSpinnerHours();
        callEventListener();
        loadListView();
        loadAppointmentList();
        loadSpinnerFromDB();
    }

    /**
     * Load the hours time in the spinner
     */
    private void loadHours(){
        int hours = 0;
        int minutes = 0;
        for(int i=0; i<24;i++){
            for(int j=0; j<4;j++){
                this.hoursOfTheDay.add(String.format("%02d",hours)+":"+ String.format("%02d",minutes));
                minutes+=15;
            }
            hours++;
            minutes = 0;
        }
    }

    /**
     * Call the even listener
     */
    private void callEventListener(){
        //Listener for Button add service
        btn_Appointment.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogCreateAppointment();
                    }
                }
        );

    }

    /**
     * The dialog to create
     */
    private void dialogCreateAppointment(){

        String day = spin_day.getSelectedItem().toString();//Get the text of the spinner
        String time = spin_start.getSelectedItem().toString()+ " to " +
                spin_end.getSelectedItem().toString();//Get the text of the spinner
        String fullTime = day + " at " + time;

        /**
         * Event of the dialog box (found on STACKOVERFLOW)
         */
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Appointment");
        builder.setMessage("You sure you want to create an Appointment:\n" +
                fullTime);


        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                getValidity();
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
     * =================================== LOADS METHOD ======================================
     */

    /**
     * Load days of the week
     */
    private void loadDays(){
        MyDBHandler db = new MyDBHandler(this);
        dayOfTheWeek.clear();
        dayOfTheWeek.addAll(db.getDaysOfTheWeek());
    }

    /**
     * Load the spinner
     */
    private void loadSpinnerDays(){
        try {
            ArrayList<String> days = new ArrayList<String>();

            for(Day day: dayOfTheWeek)
                days.add(day.getDay());

            if (days.size()>0) {
                ArrayAdapter<String> data = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, days);
                data.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spin_day.setAdapter(data);
            }
        }catch(Exception ex){
            Toast.makeText(this,ex.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Load the spinner
     */
    private void loadSpinnerHours(){

        try {
            ArrayAdapter<String> data = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, hoursOfTheDay);
            data.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spin_end.setAdapter(data);
            spin_start.setAdapter(data);
            spin_start.setSelection(32);
            spin_end.setSelection(48);
        }
        catch(Exception ex){
            Toast.makeText(this,ex.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Load the list view
     */
    private void loadListView(){
        try {
            MyDBHandler dbHandler = new MyDBHandler(this);
            availabilities.clear();//Clear the array list
            ArrayList<String> myAvailabilities = new ArrayList<String>();

            availabilities.addAll(dbHandler.getAllAvailabilitiesFromSP(companyID));

            //Concatenate the availability
            for(Availability availability: availabilities)
                myAvailabilities.add(getDayName(availability.getDayID()) + " at " +
                        availability.getStartDate() + " to " +
                        availability.getEndDate());

            if(myAvailabilities.size()==0) {
                myAvailabilities.add(EMPTY_TEXT_LV1);
            }
            ArrayAdapter<String> data = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myAvailabilities);
            lv_availability.setAdapter(data);

        }catch(Exception ex){
            Toast.makeText(this,ex.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Load the list view
     */
    private void loadAppointmentList(){
        try {
            MyDBHandler dbHandler = new MyDBHandler(this);
            appointments.clear();
            ArrayList<String> myApps = new ArrayList<String>();

            appointments.addAll(dbHandler.getAllAppointmentFromServiceProvider(companyID));

            for(Appointment appointment: appointments)
                myApps.add(dbHandler.getServiceTypeName(appointment.getServiceType_id()) + " / " +
                        getDayName(appointment.getDay_id()) + " at " +
                        appointment.getStart_time() + " to " +
                        appointment.getEnd_time()
                        );

            if(myApps.size()==0) {
                myApps.add(EMPTY_TEXT_LV2);
            }
            ArrayAdapter<String> data = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myApps);
            lv_Appointment.setAdapter(data);

        }catch(Exception ex){
            Toast.makeText(this,ex.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Load the spinner of service of the service provider
     */
    private void loadSpinnerFromDB(){

        try {
            MyDBHandler dbHandler = new MyDBHandler(this);
            servicesFromSP.clear();
            ArrayList<String> myServices = new ArrayList<String>();

            servicesFromSP.addAll(dbHandler.getAllServiceTypeOfServiceProvider(companyID));

            for(ServiceType serviceType: servicesFromSP)
                myServices.add(serviceType.getServiceName() + " / " + serviceType.getHourlyRate() + " $/H");

            if(myServices.size()==0)
                Toast.makeText(this,"Cannot load the DB or DB empty",Toast.LENGTH_LONG).show();
            if (myServices.size()>0) {
                ArrayAdapter<String> data = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, myServices);
                data.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spin_service.setAdapter(data);
            }


        }catch(Exception ex){
            Toast.makeText(this,ex.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    /**
     * ================================ APPOINTMENT ======================================
     */
    /**
     * look if Appointment exist
     * @param day
     * @param startTime
     * @param endTime
     * @return
     */
    private boolean isAppointmentExist(String day, String startTime, String endTime){
        String[] start = startTime.split(":");
        String[] end = endTime.split(":");

        //Set the 2 hours
        int hourStart = Integer.parseInt(start[0]);
        int hourEnd = Integer.parseInt(end[0]);

        //Set the minutes
        int minuteStart = Integer.parseInt(start[1]);
        int minuteEnd = Integer.parseInt(end[1]);

        int dayID = getDay(day).getDayID();

        for(Appointment Appointment: appointments){

            int dayIDApp = Appointment.getDay_id();

            Log.d("BUGDATABASEHANDLER","Day:" + dayID + " day 2 : " + dayIDApp);

            //If the same day, make sure it doesnt overlap
            if(dayID == dayIDApp){
                int hourStartApp = Appointment.getStartHour();
                int hourEndApp = Appointment.getEndHour();

                int minuteStartApp = Appointment.getStartMinute();
                int minuteEndApp = Appointment.getEndTimeMinute();

                if(hourStart == hourStartApp && minuteStart >= minuteStartApp)
                    return(false);
                if(hourEnd == hourEndApp && minuteEnd <= minuteEndApp){
                    return(false);
                }
                //If the new hour is between the hourStart and hourEnd of existing availability
                if(hourStart < hourEndApp && hourStart > hourStartApp)
                    return(false);
                //If the new hour is between the hourStart and hourEnd of existing availability
                if(hourEnd > hourStartApp && hourEnd < hourEndApp)
                    return(false);
                //If the new end is the same as the start but the minute overlap
                if(hourEnd == hourStartApp && minuteStartApp <= minuteEnd)
                    return(false);

                if(hourStart == hourEndApp && minuteEndApp >= minuteStart)
                    return(false);

            }
        }

        return(true);
    }

    /**
     * Make sure to not have a duplicate,
     * @return
     */
    private boolean isAvailabilityExist(String day, String startTime, String endTime){
        String full = day + " at " + startTime + " to " + endTime;

        String[] start = startTime.split(":");
        String[] end = endTime.split(":");

        //Set the 2 hours
        int hourStart = Integer.parseInt(start[0]);
        int hourEnd = Integer.parseInt(end[0]);

        //Set the minutes
        int minuteStart = Integer.parseInt(start[1]);
        int minuteEnd = Integer.parseInt(end[1]);

        int dayID = getDay(day).getDayID();

        for(Availability av : availabilities){
            int dayIDAV = av.getDayID();
            if(dayID == dayIDAV){
                //Set the 2 hours
                int hourStartAV = av.getStartHour();
                int hourEndAV = av.getEndHour();

                //Set the minutes
                int minuteStartAV = av.getStartMinute();
                int minuteEndAV = av.getEndTimeMinute();

                if(hourStart<hourStartAV)
                    return(false);

                if(hourEnd>hourEndAV)
                    return(false);

                if(hourEnd == hourEndAV && minuteEnd > minuteEndAV)
                    return(false);

                if(hourStart == hourStartAV && minuteStart<minuteStartAV)
                    return(false);
            }
        }
        return(true);
    }

    /**
     * Return the service Type linked to the name
     * @param serviceTypeName
     * @return
     */
    private ServiceType getServiceType(String serviceTypeName){
        for(ServiceType serviceType: servicesFromSP)
            if(serviceTypeName.equals(serviceType.getServiceName() + " / " + serviceType.getHourlyRate() + " $/H"))
                return(serviceType);
        return(new ServiceType());
    }

    /**
     * Create an Appointment if possible
     */
    private void getValidity(){
        String start_time = spin_start.getSelectedItem().toString();
        String end_time = spin_end.getSelectedItem().toString();
        String day = spin_day.getSelectedItem().toString();

        String[] start = start_time.split(":");
        String[] end = end_time.split(":");

        //Set the 2 hours
        int hourStart = Integer.parseInt(start[0]);
        int hourEnd = Integer.parseInt(end[0]);

        //Set the minutes
        int minuteStart = Integer.parseInt(start[1]);
        int minuteEnd = Integer.parseInt(end[1]);

        boolean valid = true;

        if(hourStart == hourEnd && minuteStart >= minuteEnd)
            valid = false;
        if(hourStart > hourEnd)
            valid = false;
        if(valid) {
            valid = isAvailabilityExist(day, start_time, end_time);
            if (valid) {
                valid = isAppointmentExist(day, start_time, end_time);
                if (valid) {
                    createAppointment(day, start_time, end_time);
                } else
                    Toast.makeText(this, "There is already an appointment at the time you try to use.", Toast.LENGTH_LONG).show();

            } else
                Toast.makeText(this, "Please make sure you select an appointment " +
                        "in the availability of the service provider.", Toast.LENGTH_LONG).show();

        }else
            Toast.makeText(this, "The time you try to get an appointment is not valid.", Toast.LENGTH_LONG).show();
    }

    /**
     * Create the appointment
     * @param day
     * @param end_time
     * @param start_time
     */
    private void createAppointment(String day, String start_time, String end_time){
        String serviceName = spin_service.getSelectedItem().toString();

        int serviceID = getServiceType(serviceName).getSTID();

        MyDBHandler db = new MyDBHandler(this);

        if(db.createAppointment(companyID,homeID,serviceID,getDay(day).getDayID(), start_time,end_time)) {
            loadAppointmentList();

            Toast.makeText(this, "Appointment created:\n" +
                             day + "\nfrom " + start_time + " to " + end_time
                    , Toast.LENGTH_LONG).show();
            finish();
        }
        else
            Toast.makeText(this, "Couldnt create appointment"
                    ,Toast.LENGTH_LONG).show();


    }

}
