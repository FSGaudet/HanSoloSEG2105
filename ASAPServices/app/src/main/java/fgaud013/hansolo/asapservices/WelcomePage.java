package fgaud013.hansolo.asapservices;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * @description : The welcome page for users
 * @Author      : Frédérick Gaudet - 8035208
 */
public class WelcomePage extends AppCompatActivity {

    //Table USER ACCOUNT
    private static final String COL_USERID = "userID";
    private static final String COL_USERNAME = "username";
    private static final String COL_PASSWORD = "password";
    private static final String COL_FIRSTNAME = "firstname";
    private static final String COL_LASTNAME = "lastname";
    private static final String COL_BIRTH = "dateOfBirth";
    private static final String COL_PHONE = "phoneNumber";
    private static final String COL_PERMLVL = "permLevel";

    //Table SERVICE PROVIDER
    private static final String COL_SPID = "serviceProviderID";
    private static final String COL_HOURLYRATE = "hourlyRate";
    private static final String COL_ST_ID = "serviceTypeID";
    private static final String COL_ST_NAME= "serviceTypeName";


    //Text views
    private TextView welcomeTV;

    //Text View left col
    private TextView firstNameTV;
    private TextView lastNameTV;
    private TextView dateOfBirthTV;
    private TextView phoneTV;
    private TextView usernameTV;
    private TextView serviceTypeTV;
    private TextView hourlyRateTV;

    //Text view right col
    private TextView firstNameTV2;
    private TextView lastNameTV2;
    private TextView dateOfBirthTV2;
    private TextView phoneTV2;
    private TextView usernameTV2;
    private TextView serviceTypeTV2;
    private TextView hourlyRateTV2;

    private Button btnNewService;

    private ServiceProvider serviceProvider = new ServiceProvider();
    private UserAccount userAccount = new UserAccount();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);


        //Get all the values of the user
        Intent i = getIntent();
        Bundle data = i.getExtras();

        ServiceType stTmp = new ServiceType(data.getInt(COL_ST_ID),data.getString(COL_ST_NAME));

        //Create user account with the bundle
        userAccount = new UserAccount(  data.getString(COL_USERNAME),
                data.getInt(COL_USERID),
                data.getString(COL_PASSWORD),
                data.getString(COL_FIRSTNAME),
                data.getString(COL_LASTNAME),
                data.getString(COL_BIRTH),
                data.getString(COL_PHONE),
                data.getInt(COL_PERMLVL));

        serviceProvider = new ServiceProvider();

        // Complete the service provider if the perm level = 1
        if(userAccount.getPermLevel() == 1){
            serviceProvider = new ServiceProvider(  userAccount,
                                                    data.getInt(COL_SPID),
                                                    stTmp,
                                                    data.getDouble(COL_HOURLYRATE));
        }

        //Toast.makeText(this,serviceProvider.toString(),Toast.LENGTH_LONG).show();

        //Init the page
        init(userAccount.getPermLevel());

        //Refresh the UI
        refreshUI();

        welcomeTV.setText("Welcome " + userAccount.getFirstName()+ ".\nYou are connected as " +
                ((userAccount.getPermLevel()==0)?"administrator.":
                        (userAccount.getPermLevel()==1)?"service provider":"home owner"));

    }

    /**
     * Listener for the button create service type
     */
    public void createServiceType(){
        Button btn = (Button)findViewById(R.id.btn_new_service);
        btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(WelcomePage.this,ServiceTypeAdder.class);
                        startActivity(intent);
                    }
                }
        );
    }

    /**
     * Initialisation of the component
     */
    public void init(int permLevel){
        //Init the text field
        //Welcome
        welcomeTV = (TextView)findViewById(R.id.welcome_tv);

        //Text View left col
        firstNameTV = (TextView)findViewById(R.id.firstName_tv);
        lastNameTV = (TextView)findViewById(R.id.lastName_tv);
        dateOfBirthTV = (TextView)findViewById(R.id.dateOfBirth_tv);
        phoneTV = (TextView)findViewById(R.id.phoneNumb_tv);
        usernameTV = (TextView)findViewById(R.id.userName_tv);
        serviceTypeTV = (TextView)findViewById(R.id.serviceType_tv);
        hourlyRateTV = (TextView)findViewById(R.id.hourlyRate_tv);

        //Text view right col
        firstNameTV2 = (TextView)findViewById(R.id.firstName_tv2);
        lastNameTV2 = (TextView)findViewById(R.id.lastName_tv2);
        dateOfBirthTV2 = (TextView)findViewById(R.id.dateOfBirth_tv2);
        phoneTV2 = (TextView)findViewById(R.id.phoneNumb_tv2);
        usernameTV2 = (TextView)findViewById(R.id.userName_tv2);
        serviceTypeTV2 = (TextView)findViewById(R.id.serviceType_tv2);
        hourlyRateTV2 = (TextView)findViewById(R.id.hourlyRate_tv2);

        btnNewService = (Button)findViewById(R.id.btn_new_service);

        if(permLevel != 1){
            serviceTypeTV.setVisibility(View.GONE);
            serviceTypeTV2.setVisibility(View.GONE);
            hourlyRateTV.setVisibility(View.GONE);
            hourlyRateTV2.setVisibility(View.GONE);
        }

        if(permLevel != 0){
            btnNewService.setVisibility(View.GONE);
        }
        createServiceType();
    }

    /**
     * Refresh the UI with the actual provider/user/admin
     */
    public void refreshUI(){

        usernameTV2.setText(userAccount.getUserName());
        firstNameTV2.setText(userAccount.getFirstName());
        lastNameTV2.setText(userAccount.getLastName());
        dateOfBirthTV2.setText(userAccount.getDateOfBirth());
        phoneTV2.setText(userAccount.getPhoneNumber());

        if(userAccount.getPermLevel() == 1) {
            serviceTypeTV2.setText(serviceProvider.getServiceType().getServiceName());
            hourlyRateTV2.setText(Double.toString(serviceProvider.getHourlyRate()));
        }
    }
}
