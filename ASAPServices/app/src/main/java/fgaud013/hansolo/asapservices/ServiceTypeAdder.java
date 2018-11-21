package fgaud013.hansolo.asapservices;

/**
 * @Description : Here is all the necessary for a user to create, modify and delete a service.
 * @author      : Frédérick Gaudet - 8035208
 * @team        : Han Solo
 */

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class ServiceTypeAdder extends AppCompatActivity {
    //Constant
    private static final String DEFAULT_TEXT = "Service name desired";
    private static final String DEFAULT_TEXT_STMOD = "New service name";
    private static final String DEFAULT_TEXT_HRMOD = "New hourly rate";
    private static final String NOTHING_TEXT = "-- NOTHING --";

    Button btn_modify, btn_add, btn_delete;

    EditText et_newST, et_modST, et_newHR, et_modHR;

    Spinner mySpinner;

    ArrayList<ServiceType> services = new ArrayList<ServiceType>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_type_adder);

        //All the necessary for displaying as a "popup"
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.8));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);
        this.setTitle("Service Type");

        init();
        callListener();//Call all the listener
        loadSpinnerFromDB();
    }

    /**
     * Initialise the interface
     */
    private void init(){
        //Button
        btn_modify = (Button)findViewById(R.id.ast_btnMod);
        btn_add = (Button)findViewById(R.id.ast_btnAdd);
        btn_delete = (Button)findViewById(R.id.ast_btnDel);

        //Edit Text
        et_modHR = (EditText)findViewById(R.id.ast_hourlyRate2);
        et_newHR = (EditText)findViewById(R.id.ast_hourlyRate);
        et_newST = (EditText)findViewById(R.id.ast_serviceName);
        et_modST = (EditText)findViewById(R.id.ast_textMod);

        mySpinner = (Spinner)findViewById(R.id.ast_spinner);
    }

    /**
     * =================================== LISTENERS ======================================
     */

    /**
     * All the listener
     */
    private void callListener(){
        btnAddClick();
        btnModifyClick();
        btnDeleteClick();
        spinnerItemChange();
    }

    /**
     * Event for the BUTTON ADD
     */
    private void btnAddClick(){
        btn_add.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addServiceType();
                    }
                }
        );
    }

    /**
     * Event for the button MODIFY
     */
    private void btnModifyClick(){
        btn_modify.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertModifyDialog();
                    }
                }
        );
    }

    /**
     * Event for button DELETE
     */
    private void btnDeleteClick(){
        btn_delete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDeleteDialog();
                    }
                }
        );
    }

    /**
     * Event for spinner item change
     */
    private void spinnerItemChange(){
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                String text = mySpinner.getSelectedItem().toString();
                double hourlyRate = 0;

                //Get the hourly rate of the service type
                for(ServiceType serviceType: services)
                    if(serviceType.getServiceName().equals(text))
                        hourlyRate = serviceType.getHourlyRate();

                String hourlyRateText = "" + hourlyRate;

                //If the text is the default one just go with default
                if(text.equals(NOTHING_TEXT)) {
                    et_modST.setText(DEFAULT_TEXT_STMOD);
                    et_modHR.setText(DEFAULT_TEXT_HRMOD);
                }
                else {//Set the actual hourlyRate and Text
                    et_modST.setText(text);
                    et_modHR.setText(hourlyRateText);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }


    /**
     * =================================== DB HANDLER ======================================
     */

    /**
     * Load the spinner
     */
    private void loadSpinnerFromDB(){

        try {
            MyDBHandler dbHandler = new MyDBHandler(this);
            services.clear();
            ArrayList<String> myServices = new ArrayList<String>();

            myServices.add(NOTHING_TEXT);
            services.addAll(dbHandler.getAllServiceType());

            for(ServiceType serviceType: services)
                myServices.add(serviceType.getServiceName());

            if(myServices.size()==1)
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
     * Add a service type to the database
     */
    private void addServiceType(){
        String serviceName = et_newST.getText().toString();
        Validator validator = new Validator();
        if(validator.isHourlyRateValid(et_newHR.getText().toString())){
            double hourlyRate = Double.parseDouble(et_newHR.getText().toString());

            if(serviceName.equals("") || serviceName.equals(DEFAULT_TEXT))
                Toast.makeText(this, "Please write a existing type!", Toast.LENGTH_LONG).show();
            else{
                try {
                    MyDBHandler dbHandler = new MyDBHandler(this);
                    ServiceType service = new ServiceType(-1,serviceName,hourlyRate);
                    int result = dbHandler.insertServiceType(service);//Try to insert the entry

                    //Depending of the result, different toast. Just read toast to know what they worth
                    if(result == -1)
                        Toast.makeText(this, "Service type added!", Toast.LENGTH_LONG).show();
                    else if(result == -2)
                        Toast.makeText(this, "Service type already exist!", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(this, "Something went wrong, please try again!", Toast.LENGTH_LONG).show();
                } catch (Exception ex) {
                    Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
                }
                finally {
                    loadSpinnerFromDB();
                }
            }
        }
        else
            Toast.makeText(this, "The hourly rate is not a number. I.E: 20.25", Toast.LENGTH_LONG).show();
    }

    /**
     * Modify a service type
     */
    private void modifyServiceType(){
        String serviceNameSpinner = mySpinner.getSelectedItem().toString();//Get the text of the spinner
        String newServiceName = ((EditText)findViewById(R.id.ast_textMod)).getText().toString();
        ServiceType serviceTypeSpinner = new ServiceType();

        double newRate = -1;

        //Get the service type from the spinner
        serviceTypeSpinner = getServiceType(serviceNameSpinner);

        //Valid the text field hourly rate
        Validator valid = new Validator();
        if(valid.isHourlyRateValid(et_modHR.getText().toString())) {
            newRate = Double.parseDouble(et_modHR.getText().toString());

            double actualRate = serviceTypeSpinner.getHourlyRate();//Get the hourly rate from the ServiceType

            String actualServiceName = serviceTypeSpinner.getServiceName();

            if (actualRate == newRate && newServiceName.equals(actualServiceName))//Compare field to see if something changed
                Toast.makeText(this, "There is no change made.", Toast.LENGTH_LONG).show();
             else {
                try {
                    MyDBHandler dbHandler = new MyDBHandler(this);

                    boolean result = dbHandler.modifyServiceType(serviceTypeSpinner, newRate, newServiceName);

                    if (result)//Look if there is a result
                        Toast.makeText(this, "Modified successfully.", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(this, "Couldn't modify. Name probably exist.", Toast.LENGTH_LONG).show();
                } catch (Exception ex) {
                    Toast.makeText(this, "There was a problem please try again.", Toast.LENGTH_LONG).show();
                } finally {
                    loadSpinnerFromDB();
                }
            }
        }
        else
            Toast.makeText(this, "Hourly rate need to be a number $$ EX: 20.40.", Toast.LENGTH_LONG).show();

    }

    /**
     * Return the service Type linked to the name
     * @param serviceTypeName
     * @return
     */
    private ServiceType getServiceType(String serviceTypeName){
        for(ServiceType serviceType: services)
            if(serviceType.getServiceName().equals(serviceTypeName))
                return(serviceType);
        return(new ServiceType());
    }

    /**
     * Delete a service type if person click YES
     */
    private void deleteServiceType(){

        String serviceName = mySpinner.getSelectedItem().toString();//Get the text of the spinner

        try {
            MyDBHandler dbHandler = new MyDBHandler(this);

            boolean result = dbHandler.deleteServiceType(serviceName);

            if (result)
                Toast.makeText(this, "Deleted successfully.", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(this, "Couldn't delete.", Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(this, "There was a problem please try again.", Toast.LENGTH_LONG).show();
        } finally {
            loadSpinnerFromDB();
        }

    }

    /**
     * Popup for deleting
     */
    private void alertDeleteDialog(){
        String serviceName = mySpinner.getSelectedItem().toString();//Get the text of the spinner


        if(serviceName.equals(NOTHING_TEXT))//Make sure that the spinner isn't on the default
            Toast.makeText(this, "Please select a service type.", Toast.LENGTH_LONG).show();
        else {
            /**
             * Event of the dialog box (found on STACKOVERFLOW)
             */
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.app_name);
            builder.setMessage("Do you really want to delete the service type\"" + serviceName +"\"?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    deleteServiceType();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    /**
     * Popup for Modifying
     */
    private void alertModifyDialog(){

        String serviceName = mySpinner.getSelectedItem().toString();//Get the text of the spinner
        String newServiceName = et_modST.getText().toString();
        String hourlyRate = et_modHR.getText().toString();
        ServiceType serviceType = getServiceType(serviceName);//Get the service type

        if(serviceName.equals(NOTHING_TEXT))//Make sure the spinner isnt on default
            Toast.makeText(this, "Please select a service type.", Toast.LENGTH_LONG).show();
        else {

            /**
             * Event of the dialog box (found on STACKOVERFLOW)
             */
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.app_name);
            builder.setMessage("Do you really want to modify the service type?\n\""
                    + serviceName + "\" for \"" + newServiceName + "\"" +
            "\n" + serviceType.getHourlyRate() + "$ for " + hourlyRate + "$");


            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    modifyServiceType();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }
}
