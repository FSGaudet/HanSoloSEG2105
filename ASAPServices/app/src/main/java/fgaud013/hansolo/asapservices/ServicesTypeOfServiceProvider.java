package fgaud013.hansolo.asapservices;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ServicesTypeOfServiceProvider extends AppCompatActivity {

    //Table USER ACCOUNT
    private static final String COL_USERID = "userID";
    private static final String COL_SPID = "serviceProviderID";

    private static final String EMPTY_TEXT_LV = "Empty, please add a service type.";

    //Component
    private Spinner mySpinner;

    private Button btn_addServiceType;

    private ListView lv_services;

    //Global variables
    private ArrayList<ServiceType> allServices = new ArrayList<ServiceType>();
    private ArrayList<ServiceType> servicesFromSP = new ArrayList<ServiceType>();

    private int userID, spID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_type_of_service_provider);

        //Get value send from the login activity
        Intent i = getIntent();
        Bundle data = i.getExtras();

        spID = data.getInt(COL_SPID);


        if(spID < 0)
            finish();

        init();
    }

    /**
     * Initialise all component
     */
    private void init(){
        mySpinner = (Spinner)findViewById(R.id.spinner_stsp);

        btn_addServiceType = (Button)findViewById(R.id.btn_addService_stsp);

        lv_services = (ListView) findViewById(R.id.lv_serviceType_stsp);

        loadSpinnerFromDB();
        loadServiceTypeOfServiceProvider();
        callEventListener();
    }

    private void callEventListener() {
       //Listener for Button add service
        btn_addServiceType.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogAdd();
                    }
                }
        );

        //Listener for the list view
        lv_services.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                String actual = ((TextView)arg1).getText().toString();
                if(!actual.equals(EMPTY_TEXT_LV) )
                    dialogDelete(actual);
                return true;
            }
        });
    }

    /**
     * The dialog for the add button
     */
    private void dialogAdd(){

        String serviceName = mySpinner.getSelectedItem().toString();//Get the text of the spinner

        /**
         * Event of the dialog box (found on STACKOVERFLOW)
         */
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name);
        builder.setMessage("Do you really want to add this service type?\n" +
                            serviceName +
        "\nTo your services provided?");


        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                addServiceTypeToProvider();
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
     * Dialog for deleting a serviceType
     * @param serviceName
     */
    private void dialogDelete(final String serviceName){
        /**
         * Event of the dialog box (found on STACKOVERFLOW)
         */
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name);
        builder.setMessage("Do you really want to delete this service type?\n" +
                serviceName +
                "\nTo your services provided?");


        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteServiceTypeToProvider(serviceName);
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
     * Add a service type
     */
    private void addServiceTypeToProvider(){
        ServiceType st = new ServiceType(getServiceType(mySpinner.getSelectedItem().toString()));
        MyDBHandler db = new MyDBHandler(this);
        boolean exist = false;

        for(ServiceType serv: servicesFromSP)
            if (serv.getServiceName().equals(st.getServiceName()))
                exist = true;

        if(!exist) {
            boolean result = db.addServiceTypeToServiceProvider(st.getSTID(), this.spID);
            if (result) {
                Toast.makeText(this, "Service " + st.getServiceName() +
                                " successfully added!"
                        , Toast.LENGTH_LONG).show();
                loadServiceTypeOfServiceProvider();
            } else
                Toast.makeText(this, "Service " + st.getServiceName() +
                                " couldn't be added, try again!"
                        , Toast.LENGTH_LONG).show();
        }
        else
            Toast.makeText(this, "This service is already in your services."
                , Toast.LENGTH_LONG).show();
    }

    /**
     * Delete service type
     */
    private void deleteServiceTypeToProvider(String serviceName){
        ServiceType st = new ServiceType(getServiceType(serviceName));
        MyDBHandler db = new MyDBHandler(this);

        boolean result = db.deleteServiceTypeOfServiceProvider(st.getSTID(),this.spID);
        if(result){
            Toast.makeText(this,"Service " + st.getServiceName() +
                            " successfully deleted!"
                    ,Toast.LENGTH_LONG).show();
            loadServiceTypeOfServiceProvider();
        }
        else
            Toast.makeText(this,"Service " + st.getServiceName() +
                            " couldn't be deleted, try again!"
                    ,Toast.LENGTH_LONG).show();
    }

    /**
     * Load the spinner
     */
    private void loadSpinnerFromDB(){

        try {
            MyDBHandler dbHandler = new MyDBHandler(this);
            allServices.clear();
            ArrayList<String> myServices = new ArrayList<String>();

            allServices.addAll(dbHandler.getAllServiceType());

            for(ServiceType serviceType: allServices)
                myServices.add(serviceType.getServiceName() + " / " + serviceType.getHourlyRate() + " $/H");

            if(myServices.size()==0)
                Toast.makeText(this,"Cannot load the DB or DB empty",Toast.LENGTH_LONG).show();
            if (myServices.size()>0) {
                ArrayAdapter<String> data = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, myServices);
                data.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mySpinner.setAdapter(data);
            }


        }catch(Exception ex){
            Toast.makeText(this,ex.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
    /**
     * Load the list view
     */
    private void loadServiceTypeOfServiceProvider(){

        try {
            MyDBHandler dbHandler = new MyDBHandler(this);
            servicesFromSP.clear();
            ArrayList<String> myServices = new ArrayList<String>();

            servicesFromSP.addAll(dbHandler.getAllServiceTypeOfServiceProvider(spID));

            for(ServiceType serviceType: servicesFromSP)
                myServices.add(serviceType.getServiceName() + " / " + serviceType.getHourlyRate() + " $/H");

            if(myServices.size()==0) {
                Toast.makeText(this, "Cannot load the DB or DB empty", Toast.LENGTH_LONG).show();
                myServices.add(EMPTY_TEXT_LV);
            }
            ArrayAdapter<String> data = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myServices);
            lv_services.setAdapter(data);

        }catch(Exception ex){
            Toast.makeText(this,ex.getMessage(),Toast.LENGTH_LONG).show();
        }
    }


    /**
     * Return the service Type linked to the name
     * @param serviceTypeName
     * @return
     */
    private ServiceType getServiceType(String serviceTypeName){
        for(ServiceType serviceType: allServices)
            if(serviceTypeName.equals(serviceType.getServiceName() + " / " + serviceType.getHourlyRate() + " $/H"))
                return(serviceType);
        return(new ServiceType());
    }
}
