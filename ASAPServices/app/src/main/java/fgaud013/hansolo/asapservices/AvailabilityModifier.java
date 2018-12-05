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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AvailabilityModifier extends AppCompatActivity {

    //Constant
    private static final String DAY_TO_MODIFY = "dayToChange";
    private static final String COL_SPID = "serviceProviderID";

    //Component
    private Spinner spinnerST, spinnerET, spinnerDay;
    private Button btn_mod, btn_delete;

    private TextView tv_dayToChange;


    //global variable
    private ArrayList<Day> dayOfTheWeek = new ArrayList<Day>();
    private ArrayList<String> hoursOfTheDay = new ArrayList<String>();
    private ArrayList<Availability> availabilities = new ArrayList<Availability>();

    private String dateToChange, startTimeTC, endTimeTC, dayTC;
    private int spID, availID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_availability_modifier);

        //Get value send from the login activity
        Intent i = getIntent();
        Bundle data = i.getExtras();

        this.dateToChange = data.getString(DAY_TO_MODIFY);
        this.spID = data.getInt(COL_SPID);

        if(dateToChange.equals(""))
            finish();
        init();
    }

    /**
     * Initialise
     */
    private void init(){

        spinnerST = (Spinner)findViewById(R.id.spinnerStartTime_am);
        spinnerET = (Spinner)findViewById(R.id.spinnerEndTime_am);
        spinnerDay = (Spinner)findViewById(R.id.spinnerDay_am);

        btn_mod = (Button)findViewById(R.id.btn_modifyDate_am);
        btn_delete = (Button)findViewById(R.id.btn_deleteDate_am);

        tv_dayToChange = (TextView)findViewById(R.id.dateToChange_tv_am);

        tv_dayToChange.setText("You are modifying/deleting the availability\n\"" + dateToChange + "\".");

        try {
            eventListener();
            loadHours();
            loadDays();
            loadSpinnerDays();
            loadSpinnerHours();
            splitTime();
            setAvailabilityID();
            loadAvailabilities();
            removeCurrentAvailability();
        }catch(Exception ex){
            Log.d("MODAVAILDEBUG","Error initing\n" + ex.getMessage());
        }
    }

    /**
     * Split the time
     */
    private void splitTime(){
        //Code work but still give an error if no try catch.
        try {
            String[] split1 = dateToChange.split(" at ");//Get the day [0]
            String[] split2 = split1[1].split(" to ");

            //Get the day to change, the start time to change and the end time to change
            dayTC = split1[0];
            startTimeTC = split2[0];
            endTimeTC = split2[1];

            ArrayAdapter myAdap = (ArrayAdapter) spinnerDay.getAdapter();
            int position = myAdap.getPosition(dayTC);

            spinnerDay.setSelection(position);//Set the day to the day

            //Set the start time to the spinner
            myAdap = (ArrayAdapter) spinnerST.getAdapter();
            position = myAdap.getPosition(startTimeTC);
            spinnerST.setSelection(position);

            //Set the endTime to the spinner
            myAdap = (ArrayAdapter) spinnerET.getAdapter();
            position = myAdap.getPosition(endTimeTC);
            spinnerET.setSelection(position);

        }catch(Exception ex){
            Log.d("MODAVAILDEBUG","Bug \n" + ex.getMessage());
        }
    }

    /**
     * If the person doesnt change the day you call this one
     * @param first
     * @param second
     * @return
     */
    private boolean compareTwoAvailability(String first, String second){
        String[] splitFirst = first.split(":");
        String[] splitSecond = second.split(":");
        int firstHour,secondHour,firstMin,secondMin;

        firstHour = Integer.parseInt(splitFirst[0]);
        firstMin = Integer.parseInt(splitFirst[1]);

        secondHour = Integer.parseInt(splitSecond[0]);
        secondMin = Integer.parseInt(splitSecond[1]);

        if(firstHour == secondHour && firstMin == secondMin)
            return (false);//Return false if the same exact time
        return true;
    }
    /**
     * Event listener
     */
    private void eventListener(){
       //Event button modify
        btn_mod.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogModify();
                    }
                }
        );
        //event button delete
        btn_delete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogDelete();
                    }
                }
        );
    }

    private void loadAvailabilities(){
        MyDBHandler dbHandler = new MyDBHandler(this);
        availabilities.clear();//Clear the array list
        availabilities.addAll(dbHandler.getAllAvailabilitiesFromSP(spID));
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
     * Load the spinner with hours
     */
    private void loadSpinnerHours(){
        try {
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

            if(days.size()==0)
                Toast.makeText(this,"Cannot load the DB or DB empty",Toast.LENGTH_LONG).show();
            if (days.size()>0) {
                ArrayAdapter<String> data = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, days);
                data.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerDay.setAdapter(data);
            }
        }catch(Exception ex){
            Toast.makeText(this,ex.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Delete a availability
     */
    private void deleteAvailability(){
        MyDBHandler myHandler = new MyDBHandler(this);
        if(myHandler.deleteAvailability(this.availID))
            finish();
        else
            Toast.makeText(this,"Couldn't be deleted.",Toast.LENGTH_LONG).show();

    }

    /**
     * Modify a availability
     */
    private void modifyAvailability(){
        MyDBHandler myHandler = new MyDBHandler(this);
        Day day = getDay(spinnerDay.getSelectedItem().toString());

        String newST = spinnerST.getSelectedItem().toString();
        String newET = spinnerET.getSelectedItem().toString();

        //Get the 2 spinner
        String[] spinner1 = newST.split(":");
        String[] spinner2 = newET.split(":");

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

            //Look if exist
            if (valid) {
                if (myHandler.getAvailabilityID(this.spID, day.getDayID(), newST, newET) < 0) {
                    if (myHandler.modifyAvailability(this.availID,
                            day.getDayID(),
                            spinnerST.getSelectedItem().toString(),
                            spinnerET.getSelectedItem().toString())) {
                        Toast.makeText(this, "Availability modified.", Toast.LENGTH_LONG).show();
                        finish();
                    } else
                        Toast.makeText(this, "Couldn't modify.",
                                Toast.LENGTH_LONG).show();

                } else
                    Toast.makeText(this, "There an availability with the same time/day.",
                            Toast.LENGTH_LONG).show();
            } else
                Toast.makeText(this, "There is overlapping with an set availability.", Toast.LENGTH_LONG).show();
        }else
            Toast.makeText(this, "Availability not good.",
                Toast.LENGTH_LONG).show();
    }

    /**
     * Get the ID of availability
     */
    private void setAvailabilityID(){
        MyDBHandler myHandler = new MyDBHandler(this);
        availID = myHandler.getAvailabilityID(this.spID,getDay(dayTC).getDayID(), this.startTimeTC,this.endTimeTC);
    }

    /**
     * ======================================== DIALOG BOX =======================================
     */

    /**
     * The dialog for the modify
     */
    private void dialogModify(){

        String day = spinnerDay.getSelectedItem().toString();//Get the text of the spinner
        String time = spinnerST.getSelectedItem().toString()+ " to " +
                spinnerET.getSelectedItem().toString();//Get the text of the spinner
        String fullTime = day + " at " + time;

        /**
         * Event of the dialog box (found on STACKOVERFLOW)
         */
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Availability");
        builder.setMessage("Do you really want to modify this availability?\n" +
                dateToChange + " for \n" +
                fullTime +
                "\nTo your services provided?");


        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                modifyAvailability();
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
     */
    private void dialogDelete(){
        /**
         * Event of the dialog box (found on STACKOVERFLOW)
         */

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Availability");
        builder.setMessage("Do you really want to delete this availability\n" +
                dateToChange +
                "\nfrom your services provided?");


        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteAvailability();
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

            //Set the 2 hours
            int hourStartAV = av.getStartHour();
            int hourEndAV = av.getEndHour();

            //Set the minutes
            int minuteStartAV = av.getStartMinute();
            int minuteEndAV = av.getEndTimeMinute();

            //If it exactly the same
            if(full.equals(getDayName(av.getDayID()) + " at "
                    + av.getStartDate() + " to " + av.getEndDate()))
                return(false);

            //If the same day, make sure it doesnt overlap
            if(dayID == dayIDAV){
                //If the new hour is between the hourStart and hourEnd of existing availability
                if(hourStart < hourEndAV && hourStart > hourStartAV)
                    return(false);
                //If the new hour is between the hourStart and hourEnd of existing availability
                if(hourEnd > hourStartAV && hourEnd < hourEndAV)
                    return(false);
                //If the new end is the same as the start but the minute overlap
                if(hourEnd == hourStartAV && minuteStartAV < minuteEnd)
                    return(false);

                if(hourStart == hourEndAV && minuteEndAV > minuteStart)
                    return(false);

                if(hourStart == hourStartAV)
                    return(false);

                if(hourEnd == hourEndAV)
                    return(false);
            }
        }
        return(true);
    }

    /**
     * remove current availability so when we look if it exist it doesnt give an error
     */
    private void removeCurrentAvailability(){
        ArrayList<Availability> availTMP = new ArrayList<Availability>();

        for(Availability av : availabilities) {
            String day = getDayName(av.getDayID());//Get the text of the spinner
            String time = av.getStartDate() + " to " +
                    av.getEndDate();//Get the text of the spinner
            String fullTime = day + " at " + time;
            if(!dateToChange.equals(fullTime))
                availTMP.add(av);
        }
        availabilities.clear();
        availabilities.addAll(availTMP);
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

}
