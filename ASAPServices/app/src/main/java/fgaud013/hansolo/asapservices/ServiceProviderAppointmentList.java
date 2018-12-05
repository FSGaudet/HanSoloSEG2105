package fgaud013.hansolo.asapservices;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ServiceProviderAppointmentList extends AppCompatActivity {

    private static final String COL_SPID = "serviceProviderID";
    private static final String COMPANY_NAME = "CompanyName";
    private static final String EMPTY_TEXT_LV_FUTUR = "There is no incoming appointments.";
    private static final String EMPTY_TEXT_LV_PASS = "There is no completed appointments.";

    private int companyID = -1;
    private String companyName;

    private ListView lv_pass, lv_futur;

    private TextView tv_main;


    private ArrayList<Appointment> myPassAppointments = new ArrayList<Appointment>();
    private ArrayList<Appointment> myFuturAppointments = new ArrayList<Appointment>();
    private ArrayList<Day> dayOfTheWeek = new ArrayList<Day>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider_appointment_list);

        Intent i = getIntent();
        Bundle data = i.getExtras();
        companyID = data.getInt(COL_SPID);
        companyName = data.getString(COMPANY_NAME);

        //Error loading the company
        if(companyID == -1 || companyName.equals(""))
            finish();

        init();
    }

    /**
     * == == == == == == == == == == == == Init == == == == == == == == == == == ==
     */
    private void init(){
        lv_pass = (ListView)findViewById(R.id.lv_pass_spal);
        lv_futur = (ListView)findViewById(R.id.lv_incoming_spal);

        tv_main = (TextView)findViewById(R.id.tv_main_spal);

        tv_main.setText("Appointments' for " + companyName);

        callEventListener();
        loadDays();
        loadFuturAppointments();
        loadPassAppointments();
    }

    private void callEventListener(){

        lv_futur.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                String actual = ((TextView)arg1).getText().toString();
                if(!actual.equals(EMPTY_TEXT_LV_FUTUR) )
                    dialogCancel(actual);
                return true;
            }
        });
    }

    /**
     * Load the list view with all the futur appointments
     */
    private void loadFuturAppointments(){
        try {
            MyDBHandler dbHandler = new MyDBHandler(this);
            myFuturAppointments.clear();
            ArrayList<String> myApps = new ArrayList<String>();

            myFuturAppointments.addAll(dbHandler.getAllAppointmentFromServiceProvider(companyID));

            for(Appointment appointment: myFuturAppointments)
                myApps.add(dbHandler.getServiceTypeName(appointment.getServiceType_id()) + " / " +
                        getDayName(appointment.getDay_id()) + " at " +
                        appointment.getStart_time() + " to " +
                        appointment.getEnd_time() + " with " +
                        dbHandler.getHomeOwnerName(appointment.getHome_id())
                );

            if(myApps.size()==0) {
                myApps.add(EMPTY_TEXT_LV_FUTUR);
            }
            ArrayAdapter<String> data = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myApps);
            lv_futur.setAdapter(data);

        }catch(Exception ex){
            Toast.makeText(this,ex.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Load the list view with all the pass appointments
     */
    private void loadPassAppointments(){
        try {
            MyDBHandler dbHandler = new MyDBHandler(this);
            myPassAppointments.clear();
            ArrayList<String> myApps = new ArrayList<String>();

            myPassAppointments.addAll(dbHandler.getCompletedAppointmentFromServiceProvider(companyID));

            for(Appointment appointment: myPassAppointments)
                myApps.add(dbHandler.getServiceTypeName(appointment.getServiceType_id()) + " / " +
                        getDayName(appointment.getDay_id()) + " at " +
                        appointment.getStart_time() + " to " +
                        appointment.getEnd_time() + " with " +
                        dbHandler.getHomeOwnerName(appointment.getHome_id())+ "\n" +
                        "Rating: " + appointment.getRating().getRating() + "\n" +
                        "Comment: " + appointment.getRating().getComment()
                );

            if(myApps.size()==0) {
                myApps.add(EMPTY_TEXT_LV_PASS);
            }
            ArrayAdapter<String> data = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myApps);
            lv_pass.setAdapter(data);

        }catch(Exception ex){
            Toast.makeText(this,ex.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Dialog to cancel an appointments.
     * @param Appointment
     */
    private void dialogCancel(final String Appointment){
        /**
         * Event of the dialog box (found on STACKOVERFLOW)
         */
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Appointment");
        builder.setMessage("Do you want to cancel this Appointment?\n" +
                Appointment);


        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                cancelAppointments(Appointment);
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
     * Cancel an appointments
     * @param text
     */
    private void cancelAppointments(String text){
        Appointment appointment = getAppointmentFromText(text);

        MyDBHandler dbHandler = new MyDBHandler(this);
        boolean result = dbHandler.cancelAppointment(appointment.getAppointment_id());

        if(result){
            Toast.makeText(this, "Appointments cancelled successfully!", Toast.LENGTH_LONG).show();
            loadFuturAppointments();
        }
        else
            Toast.makeText(this, "Error cancelling the appointment.", Toast.LENGTH_LONG).show();
    }

    /**
     * Get the appointment from the text in the list view
     * @param text
     * @return
     */
    private Appointment getAppointmentFromText(String text){
        MyDBHandler dbHandler = new MyDBHandler(this);

        //Compare the complete string in the object listview to find the appointment linked to it
        for(Appointment appointment: myFuturAppointments) {
            String fullChain = dbHandler.getServiceTypeName(appointment.getServiceType_id()) + " / " +
                    getDayName(appointment.getDay_id()) + " at " +
                    appointment.getStart_time() + " to " +
                    appointment.getEnd_time() + " with " +
                    dbHandler.getHomeOwnerName(appointment.getHome_id());
            if(fullChain.equals(text))
                return(appointment);
        }
        return (new Appointment());
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

}
