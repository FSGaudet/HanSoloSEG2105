package fgaud013.hansolo.asapservices;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class HomeOwnerAppointment extends AppCompatActivity {

    private static final String APPOINTMENT_ID = "AppointmentID";
    private static final String COMPANY_ID = "CompanyID";
    private static final String SERVICE_ID = "ServiceID";
    private static final String DAY_NAME = "DayName";
    private static final int DEFAULT_RATING = 2;

    private Spinner spin_rating;

    private TextView tv_main;

    private EditText et_comment;

    private Button btn_rate, btn_cancel;


    private int appointment_id = -1, company_id = -1, service_id = -1;
    private String dayName;
    private Appointment appointment = new Appointment();
    private ServiceProvider company = new ServiceProvider();
    private ServiceType serviceType = new ServiceType();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_owner_appointment);

        Intent i = getIntent();
        Bundle data = i.getExtras();

        appointment_id = data.getInt(APPOINTMENT_ID);
        company_id = data.getInt(COMPANY_ID);
        service_id = data.getInt(SERVICE_ID);
        dayName = data.getString(DAY_NAME);

        if(appointment_id == -1 || company_id == -1 || service_id == -1)
            finish();

        init();
    }
    /**
     * =================================== INITIALISATION ======================================
     */

    private void init(){
        spin_rating = (Spinner)findViewById(R.id.spinner_rating_hoa);

        tv_main = (TextView)findViewById(R.id.tv_main_hoa);

        et_comment = (EditText)findViewById(R.id.et_comment_hoa);

        btn_cancel  = (Button)findViewById(R.id.btn_cancel_hoa);
        btn_rate  = (Button)findViewById(R.id.btn_rate_hoa);

        MyDBHandler dbHandler = new MyDBHandler(this);

        company = new ServiceProvider(dbHandler.getServiceProviderFromID(company_id));
        serviceType = new ServiceType(dbHandler.getServiceTypeFromID(service_id));
        appointment = new Appointment(dbHandler.getAppointmentFromID(appointment_id));

        String chain = "Service : " + serviceType.getServiceName() + "\n" +
                        "Company :" + company.getCompanyName() + "\n" +
                        dayName + " at " + appointment.getStart_time() + " to " +
                appointment.getEnd_time();
        tv_main.setText(chain);

        loadSpinnerRating();
        callEventListener();
    }

    private void callEventListener(){
        btn_cancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogCancel();
                    }
                }
        );

        btn_rate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogRating();
                    }
                }
        );
    }
    /**
     * Load the main spinner with ratings
     */
    private void loadSpinnerRating(){

        try {
            ArrayList<String> rating = new ArrayList<String>();
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
     * Dialog to cancel an appointment
     */
    private void dialogCancel(){
        /**
         * Event of the dialog box (found on STACKOVERFLOW)
         */
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Appointment");
        builder.setMessage("You sure you want to cancel this appointment?");


        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                cancelAppointment();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    private void dialogRating(){
        /**
         * Event of the dialog box (found on STACKOVERFLOW)
         */
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Appointment");
        builder.setMessage("Are you sure this appointment is completed?");


        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                rateAppointment();
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
     * Rate an appointment and complete it
     */
    private void rateAppointment(){
        Rating rating = new Rating();
        String spinnerText = spin_rating.getSelectedItem().toString();
        String comment = et_comment.getText().toString();

        rating.setRating(Integer.parseInt(spinnerText));
        rating.setComment(comment);
        rating.setAppointment_id(appointment_id);

        MyDBHandler myHandler = new MyDBHandler(this);

        boolean result = myHandler.completeAppointmentWithRating(rating);
        if(result) {
            Toast.makeText(this,"Appointment completed.",Toast.LENGTH_LONG).show();
            finish();
        }
    }

    /**
     * Cancel an appointment
     */
    private void cancelAppointment(){
        MyDBHandler dbHandler = new MyDBHandler(this);
        boolean canceled = dbHandler.cancelAppointment(appointment_id);
        if(canceled){
            Toast.makeText(this,"Canceled.",Toast.LENGTH_LONG).show();
            finish();
        }
        else
            Toast.makeText(this,"Couldn't cancel.",Toast.LENGTH_LONG).show();
    }
}
