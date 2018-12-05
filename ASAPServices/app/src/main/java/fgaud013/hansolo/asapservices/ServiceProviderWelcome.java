package fgaud013.hansolo.asapservices;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ServiceProviderWelcome extends AppCompatActivity {

    //Table USER ACCOUNT
    private static final String COL_USERID = "userID";

    //Table SERVICE PROVIDER
    private static final String COL_SPID = "serviceProviderID";
    private static final String COMPANY_NAME = "CompanyName";


    EditText et_company, et_phone, et_address, et_licensed, et_description;

    TextView tv_company, tv_phone, tv_address, tv_licensed, tv_description, tv_welcome;

    Button btn_availability, btn_serviceType, btn_appointment;

    ServiceProvider serviceProvider = new ServiceProvider();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider_welcome);

        //Get value send from the login activity
        Intent i = getIntent();
        Bundle data = i.getExtras();
        int userID = data.getInt(COL_USERID);

        boolean result = findServiceProvider(userID);

        if(!result) {
            Toast.makeText(this, "Can't connect to the user.", Toast.LENGTH_LONG).show();
            finish();
        }

        init();
    }

    /**
     * Initialise the text field and text view
     */
    private void init(){
        //Edit Text ======================================================
        et_address = (EditText)findViewById(R.id.address_et_spw);
        et_phone = (EditText)findViewById(R.id.phone_et_spw);
        et_company = (EditText)findViewById(R.id.company_et_spw);
        et_description = (EditText)findViewById(R.id.description_et_spw);
        et_licensed = (EditText)findViewById(R.id.licensed_et_spw);

        //Text View ======================================================
        tv_address = (TextView)findViewById(R.id.address_tv_spw);
        tv_phone = (TextView)findViewById(R.id.phone_tv_spw);
        tv_company = (TextView)findViewById(R.id.company_tv_spw);
        tv_description = (TextView)findViewById(R.id.description_tv_spw);
        tv_licensed = (TextView)findViewById(R.id.licensed_tv_spw);
        tv_welcome = (TextView)findViewById(R.id.welcomeText_spw);

        //Button ======================================================
        btn_availability = (Button)findViewById(R.id.btn_availability_spw);
        btn_serviceType = (Button)findViewById(R.id.btn_serviceType_spw);
        btn_appointment = (Button)findViewById(R.id.btn_appointment_spw);


        //Disable all the text Edit Text ==================================

        changeEditText(et_address, false);
        changeEditText(et_company, false);
        changeEditText(et_description, false);
        changeEditText(et_licensed, false);
        changeEditText(et_phone, false);
        callEventListener();
        fillTextField();
    }

    private void callEventListener(){
        btn_serviceType.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ServiceProviderWelcome.this,ServicesTypeOfServiceProvider.class);

                        Bundle extras = new Bundle();
                        extras.putInt(COL_SPID, serviceProvider.getServiceProviderID());
                        intent.putExtras(extras);

                        startActivity(intent);
                    }
                }
        );

        btn_availability.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ServiceProviderWelcome.this,ServiceProviderAvailability.class);

                        Bundle extras = new Bundle();
                        extras.putInt(COL_SPID, serviceProvider.getServiceProviderID());
                        intent.putExtras(extras);

                        startActivity(intent);
                    }
                }
        );

        btn_appointment.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ServiceProviderWelcome.this,ServiceProviderAppointmentList.class);

                        Bundle extras = new Bundle();
                        extras.putInt(COL_SPID, serviceProvider.getServiceProviderID());
                        extras.putString(COMPANY_NAME, serviceProvider.getCompanyName());
                        intent.putExtras(extras);

                        startActivity(intent);
                    }
                }
        );
    }

    /**
     * Fill or edit the text field with a home Owner
     */
    private void fillTextField(){

        et_company.setText(this.serviceProvider.getCompanyName());
        et_address.setText(this.serviceProvider.getAddress());
        et_description.setText(this.serviceProvider.getDescription());

        String licensed = ((this.serviceProvider.isLicensed())?"Yes":"No");
        et_licensed.setText(licensed);

        et_phone.setText(this.serviceProvider.getPhoneNumber());

        tv_welcome.setText("Welcome, " + this.serviceProvider.getUserName() + ", on the service provider page.");
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
     * Find the home Owner connected to the userID send to this activity
     * @param id
     * @return
     */
    private boolean findServiceProvider(int id){
        boolean result = false;
        MyDBHandler dbHandler = new MyDBHandler(this);

        UserAccount userAccount = new UserAccount(dbHandler.getUserAccountFromID(id));

        this.serviceProvider = new ServiceProvider(dbHandler.getServiceProvider(userAccount));

        if(this.serviceProvider.getServiceProviderID() >= 0)
            result = true;
        return(result);
    }
}
