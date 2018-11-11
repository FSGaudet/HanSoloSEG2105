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
    private static final String DEFAULT_TEXT2 = "New service name";
    private static final String NOTHING_TEXT = "-- NOTHING --";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_type_adder);

        //All the necessary for displaying as a "popup"
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.5));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);
        this.setTitle("Service Type");

        callListener();//Call all the listener
        loadSpinnerFromDB();
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
        Button btn = (Button)findViewById(R.id.ast_btnAdd);
        btn.setOnClickListener(
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
        Button btn = (Button)findViewById(R.id.ast_btnMod);
        btn.setOnClickListener(
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
        Button btn = (Button)findViewById(R.id.ast_btnDel);
        btn.setOnClickListener(
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
        Spinner spinner = (Spinner)findViewById(R.id.ast_spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                EditText modName = (EditText)findViewById(R.id.ast_textMod);
                Spinner spinner = (Spinner)findViewById(R.id.ast_spinner);
                String text = spinner.getSelectedItem().toString();

                //If the text is the default one just go with default
                if(text.equals(NOTHING_TEXT))
                    modName.setText(DEFAULT_TEXT2);
                else
                    modName.setText(text);
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
            Spinner mySpinner = (Spinner)findViewById(R.id.ast_spinner);
            MyDBHandler dbHandler = new MyDBHandler(this);

            ArrayList<String> myServices = new ArrayList<String>();
            myServices.add(NOTHING_TEXT);
            myServices.addAll(dbHandler.getAllServiceType());
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
        String serviceName = ((EditText)findViewById(R.id.ast_serviceName)).getText().toString();

        if(serviceName.equals("") || serviceName.equals(DEFAULT_TEXT))
            Toast.makeText(this, "Please write a existing type!", Toast.LENGTH_LONG).show();
        else{
            try {
                MyDBHandler dbHandler = new MyDBHandler(this);

                int result = dbHandler.insertServiceType(serviceName);//Try to insert the entry

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

    /**
     * Modify a service type
     */
    private void modifyServiceType(){
        Spinner mySpinner = (Spinner)findViewById(R.id.ast_spinner);//Get the spinner
        String serviceName = mySpinner.getSelectedItem().toString();//Get the text of the spinner
        String newServiceName = ((EditText)findViewById(R.id.ast_textMod)).getText().toString();

        try {
            MyDBHandler dbHandler = new MyDBHandler(this);

            boolean result = dbHandler.modifyServiceType(serviceName,newServiceName);

            if(result)//Look if there is a result
                Toast.makeText(this, "Modified successfully.", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(this, "Couldn't modify. Name probably exist.", Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(this, "There was a problem please try again.", Toast.LENGTH_LONG).show();
        } finally {
            loadSpinnerFromDB();
        }
    }

    /**
     * Delete a service type if person click YES
     */
    private void deleteServiceType(){

        Spinner mySpinner = (Spinner)findViewById(R.id.ast_spinner);//Get the spinner
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
        Spinner mySpinner = (Spinner)findViewById(R.id.ast_spinner);//Get the spinner
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
        Spinner mySpinner = (Spinner)findViewById(R.id.ast_spinner);//Get the spinner
        String serviceName = mySpinner.getSelectedItem().toString();//Get the text of the spinner
        String newServiceName = ((EditText)findViewById(R.id.ast_textMod)).getText().toString();

        if(serviceName.equals(NOTHING_TEXT))//Make sure the spinner isnt on default
            Toast.makeText(this, "Please select a service type.", Toast.LENGTH_LONG).show();
        else {
            if(serviceName.equals(newServiceName))//Make sure the user changed the actual name
                Toast.makeText(this, "Can't be the same service name.", Toast.LENGTH_LONG).show();
            else {
                /**
                 * Event of the dialog box (found on STACKOVERFLOW)
                 */
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.app_name);
                builder.setMessage("Do you really want to modify the service type?\n\"" + serviceName + "\" for \"" + newServiceName + "\"");
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
}
