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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ServiceProviderAvailability extends AppCompatActivity {

    private static final String EMPTY_TEXT_LV = "Empty, please add a availability.";
    private static final String DAY_TO_MODIFY = "dayToChange";
    private static final String COL_SPID = "serviceProviderID";

    private ArrayList<Day> dayOfTheWeek = new ArrayList<Day>();
    private ArrayList<String> hoursOfTheDay = new ArrayList<String>();
    private ArrayList<Availability> availabilities = new ArrayList<Availability>();

    private Spinner spinner, spinnerST, spinnerET;

    private Button btn_availability;

    private ListView lv_availability;

    private int spID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider_availability);

        //Get value send from the login activity
        Intent i = getIntent();
        Bundle data = i.getExtras();

        spID = data.getInt(COL_SPID);

        if(spID < 0)
            finish();

        init();
    }
    @Override
    protected void onResume(){
        super.onResume();
        loadListView();
    }

    private void init(){

        //Load component in the good variables
        spinner = (Spinner)findViewById(R.id.spinner_spa);
        spinnerST = (Spinner)findViewById(R.id.spinnerStartTime_spa);
        spinnerET = (Spinner)findViewById(R.id.spinnerEndTime_spa);
        btn_availability = (Button)findViewById(R.id.btn_availability_spa);
        lv_availability = (ListView)findViewById(R.id.lv_availability_spa);

        //Load the days
        loadDays();
        loadHours();
        loadSpinnerDays();
        loadSpinnerHours();
        callEventListener();
        loadListView();
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
        btn_availability.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogAdd();
                    }
                }
        );

        //Listener for the list view
        lv_availability.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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


    /**
     * The dialog for the add button
     */
    private void dialogAdd(){

        String day = spinner.getSelectedItem().toString();//Get the text of the spinner
        String time = spinnerST.getSelectedItem().toString()+ " to " +
                        spinnerET.getSelectedItem().toString();//Get the text of the spinner
        String fullTime = day + " at " + time;

        /**
         * Event of the dialog box (found on STACKOVERFLOW)
         */
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Availability");
        builder.setMessage("Take note:\n" +
                "The app won't let you have the same Day + Time but your time can overlap.\n" +
                "Please do no overlap if possible.\n" +
                "Exemple: Monday from 7:00 to 9:00 and Monday from 7:30 to 9:00 shouldn't be inserted.\n\n" +
                "Do you really want to add this availability?\n" +
                fullTime +
                " to your services provided?");


        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                addAvailability();
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
     * Delete a availability
     * @param day
     */
    private void dialogModify(final String day){
        /**
         * Event of the dialog box (found on STACKOVERFLOW)
         */
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Availability");
        builder.setMessage("Do you really want to modify/delete this availability\n" +
                day + "from your services provided?");


        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                modifyAvailability(day);
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
     * Load the spinner
     */
    private void loadSpinnerDays(){

        try {
            MyDBHandler dbHandler = new MyDBHandler(this);
            ArrayList<String> days = new ArrayList<String>();

            for(Day day: dayOfTheWeek)
                days.add(day.getDay());

            if (days.size()>0) {
                ArrayAdapter<String> data = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, days);
                data.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(data);
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
            MyDBHandler dbHandler = new MyDBHandler(this);
            ArrayAdapter<String> data = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, hoursOfTheDay);
            data.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerET.setAdapter(data);
            spinnerST.setAdapter(data);
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

            availabilities.addAll(dbHandler.getAllAvailabilitiesFromSP(spID));

            //Concatenate the availability
            for(Availability availability: availabilities)
                myAvailabilities.add(getDayName(availability.getDayID()) + " at " +
                                                    availability.getStartDate() + " to " +
                                                    availability.getEndDate());

            if(myAvailabilities.size()==0) {
                myAvailabilities.add(EMPTY_TEXT_LV);
            }
            ArrayAdapter<String> data = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myAvailabilities);
            lv_availability.setAdapter(data);

        }catch(Exception ex){
            Toast.makeText(this,ex.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Add an availability
     */
    private void addAvailability(){
        try {
            MyDBHandler dbHandler = new MyDBHandler(this);
            Day day = getDay(spinner.getSelectedItem().toString());

            //Get the 2 spinner
            String[] spinner1 = spinnerST.getSelectedItem().toString().split(":");
            String[] spinner2 = spinnerET.getSelectedItem().toString().split(":");

            //Set the 2 hours
            int hourStart = Integer.parseInt(spinner1[0]);
            int hourEnd = Integer.parseInt(spinner2[0]);

            //Set the minutes
            int minuteStart = Integer.parseInt(spinner1[1]);
            int minuteEnd = Integer.parseInt(spinner2[1]);

            boolean valid = true;
            //Make sure that the end time is higher than the start time
            if(minuteStart >= minuteEnd && hourStart == hourEnd)
                valid = false;
            if(hourStart > hourEnd)
                valid = false;



            if (valid) {
                valid = isAvailabilityExist(day.getDay(),
                        spinnerST.getSelectedItem().toString(),
                        spinnerET.getSelectedItem().toString());
                if(valid) {
                    dbHandler.addAvailability(spID,
                            day.getDayID(),
                            spinnerST.getSelectedItem().toString(),
                            spinnerET.getSelectedItem().toString());
                    Toast.makeText(this, "Availability added.", Toast.LENGTH_LONG).show();
                    loadListView();
                }
                else
                    Toast.makeText(this, "This time already exist.", Toast.LENGTH_LONG).show();
            } else
                Toast.makeText(this, "Please make sure your end time is after your start time.", Toast.LENGTH_LONG).show();
        }
        catch(Exception ex){
            Log.d("ServiceAvailDebug","" + ex.getMessage());
        }
    }

    /**
     * Make sure to not have a duplicate,
     * @todo could be modify to make sure there no overlap
     * @return
     */
    private boolean isAvailabilityExist(String day, String startTime, String endTime){
        String full = day + " at " + startTime + " to " + endTime;

        for(Availability av : availabilities){
            if(full.equals(getDayName(av.getDayID()) + " at "
                    + av.getStartDate() + " to " + av.getEndDate()))
                return(false);
        }
        return(true);
    }

    /**
     * Modify an availability
     * @param dayToModify
     */
    private void modifyAvailability(String dayToModify){
        Intent intent = new Intent(ServiceProviderAvailability.this,AvailabilityModifier.class);

        Bundle extras = new Bundle();
        extras.putString(DAY_TO_MODIFY, dayToModify);
        extras.putInt(COL_SPID,spID);
        intent.putExtras(extras);

        startActivity(intent);
        loadListView();
    }
}
