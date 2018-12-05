package fgaud013.hansolo.asapservices;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ServiceProviderFinder extends AppCompatActivity {

    private static final String COMPANY_NAME = "CompanyName";
    private static final String COMPANY_ID = "CompanyID";
    private static final String NO_TIME = "No time";
    private static final String NO_RATING = "No rating";
    private static final String EMPTY_TEXT_LV = "No service provider enter in your criteria.";
    private static final String HOME_ID = "homeID";

    private static final int DEFAULT_RATING = 0;
    private static final int DEFAULT_START_TIME = 32;
    private static final int DEFAULT_END_TIME = 48;


    private Spinner spin_type, spin_rating, spin_day, spin_start, spin_end;
    private Button btn_search;
    private ListView lv_availability;

    private ArrayList<Day> dayOfTheWeek = new ArrayList<Day>();
    private ArrayList<String> hoursOfTheDay = new ArrayList<String>();
    private ArrayList<ServiceType> allServices = new ArrayList<ServiceType>();
    private ArrayList<Availability> availabilities = new ArrayList<Availability>();
    private ArrayList<ServiceProvider> serviceProviderResult = new ArrayList<ServiceProvider>();

    private int homeID =-1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider_finder);
        Intent i = getIntent();
        Bundle data = i.getExtras();

        homeID = data.getInt(HOME_ID);
        if(homeID == -1)
            finish();
        init();
    }

    /**
     * Initialise
     */
    private void init(){
        //Spinner declaration
        spin_rating = (Spinner)findViewById(R.id.spinner_rating_spf);
        spin_type = (Spinner)findViewById(R.id.spinner_service_spf);
        spin_day = (Spinner)findViewById(R.id.spinner_day_spf);
        spin_start = (Spinner)findViewById(R.id.spinner_start_spf);
        spin_end = (Spinner)findViewById(R.id.spinner_end_spf);
        lv_availability = (ListView)findViewById(R.id.lv_availability_spf);

        btn_search = (Button)findViewById(R.id.btn_search_spf);

        eventListener();
        loadDays();
        loadHours();
        loadSpinnerHours();
        loadSpinnerDays();
        loadSpinnerRating();
        loadSpinnerService();
    }

    private void eventListener(){
        btn_search.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        search();
                    }
                }
        );
        //Listener for the list view
        lv_availability.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                String actual = ((TextView)arg1).getText().toString();
                Intent intent = new Intent(ServiceProviderFinder.this, HomeOwnerServiceProviderAvailability.class);

                Bundle extras = new Bundle();
                extras.putString(COMPANY_NAME, actual);
                extras.putInt(COMPANY_ID, getServiceProviderID(actual));
                extras.putInt(HOME_ID, homeID);
                intent.putExtras(extras);

                startActivity(intent);
            }
        });

    }

    /**
     * Do a search depending of the option type choosen by the user
     */
    private void search(){
        loadListView();
    }

    /**
     * Load the list view
     */
    private void loadListView(){
        try {
            MyDBHandler dbHandler = new MyDBHandler(this);
            serviceProviderResult.clear();//Clear the array list
            ArrayList<String> mySP = new ArrayList<String>();

            String service = spin_type.getSelectedItem().toString();
            String day = spin_day.getSelectedItem().toString();
            String start = spin_start.getSelectedItem().toString();
            String end = spin_end.getSelectedItem().toString();
            String rating = spin_rating.getSelectedItem().toString();

            int rat = -1;
            int day_id = -1;
            if(!rating.equals(NO_RATING))
                rat = Integer.parseInt(rating);
            if(!rating.equals(NO_TIME))
                day_id = getDay(day).getDayID();

            int st_id = getServiceID(service);


            serviceProviderResult.addAll(dbHandler.searchResultServiceProvider(st_id,day_id,start,end,rat));

            //Concatenate the availability
            for(ServiceProvider serviceProvider: serviceProviderResult)
                mySP.add(serviceProvider.getCompanyName());

            if(mySP.size()==0) {
                mySP.add(EMPTY_TEXT_LV);
            }
            ArrayAdapter<String> data = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mySP);
            lv_availability.setAdapter(data);

        }catch(Exception ex){
            Toast.makeText(this,ex.getMessage(),Toast.LENGTH_LONG).show();
        }
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
     * Load the spinner with days
     */
    private void loadSpinnerDays(){

        try {
            ArrayList<String> days = new ArrayList<String>();
            days.add(NO_TIME);
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
     * load the hours in the 2 other spin
     */
    private void loadSpinnerHours(){

        try {

            ArrayAdapter<String> data = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, hoursOfTheDay);
            data.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spin_start.setAdapter(data);
            spin_end.setAdapter(data);

            spin_start.setSelection(DEFAULT_START_TIME);
            spin_end.setSelection(DEFAULT_END_TIME);
        }
        catch(Exception ex){
            Toast.makeText(this,ex.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Load the main spinner with ratings
     */
    private void loadSpinnerRating(){

        try {
            ArrayList<String> rating = new ArrayList<String>();
            rating.add(NO_RATING);
            rating.add("1");
            rating.add("2");
            rating.add("3");
            rating.add("4");
            rating.add("5");
            ArrayAdapter<String> data = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, rating);
            data.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spin_rating.setAdapter(data);
            spin_rating.setSelection(DEFAULT_RATING);
        }
        catch(Exception ex){
            Toast.makeText(this,ex.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Load the spinner
     */
    private void loadSpinnerService(){

        try {
            MyDBHandler dbHandler = new MyDBHandler(this);
            allServices.clear();
            ArrayList<String> myServices = new ArrayList<String>();

            allServices.addAll(dbHandler.getAllServiceType());

            for(ServiceType serviceType: allServices)
                myServices.add(serviceType.getServiceName() + " / " + serviceType.getHourlyRate() + " $/H");

            if (myServices.size()>0) {
                ArrayAdapter<String> data = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, myServices);
                data.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spin_type.setAdapter(data);
            }


        }catch(Exception ex){
            Toast.makeText(this,ex.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Get the service ID from the string
     * @param chain
     * @return
     */
    private int getServiceID(String chain){
        for(ServiceType serviceType: allServices)
            if(chain.equals(serviceType.getServiceName() + " / " +
                    serviceType.getHourlyRate() + " $/H"))
                return(serviceType.getSTID());
        return(-1);
    }

    /**
     * Get the ID of a service Provider
     * @param companyName
     * @return service provider Id
     */
    private int getServiceProviderID(String companyName){
        for(ServiceProvider sp : serviceProviderResult)
            if(sp.getCompanyName().equals(companyName))
                return(sp.getServiceProviderID());
        return(-1);
    }
}
