package fgaud013.hansolo.asapservices;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class HomeOwnerWelcome extends AppCompatActivity {
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
    private static final String COL_ST_NAME = "serviceTypeName";

    //Textfield
    EditText et_firstName, et_lastName, et_birthDate, et_phone, et_address;

    //TextView
    TextView tv_welcome, tv_firstName, tv_lastName, tv_birthDate, tv_phone, tv_address;

    private HomeOwner homeOwner = new HomeOwner();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_owner_welcome);

        init();

        //Get value send from the login activity
        Intent i = getIntent();
        Bundle data = i.getExtras();
        int userID = data.getInt(COL_USERID);

        boolean result = findHomeOwner(userID);

        if(!result) {
            Toast.makeText(this, "Can't connect to the user.", Toast.LENGTH_LONG).show();
            finish();
        }
        else
            fillTextField();
    }

    /**
     * Initialise the text field and text view
     */
    private void init(){
        //Edit Text ======================================================
        et_firstName = (EditText)findViewById(R.id.firstName_et_how);
        et_lastName = (EditText)findViewById(R.id.lastName_et_how);
        et_address = (EditText)findViewById(R.id.address_et_how);
        et_birthDate = (EditText)findViewById(R.id.dateBirth_et_how);
        et_phone = (EditText)findViewById(R.id.phone_et_how);

        //Text View ======================================================
        tv_firstName = (TextView)findViewById(R.id.firstName_tv_how);
        tv_lastName = (TextView)findViewById(R.id.lastName_tv_how);
        tv_address = (TextView)findViewById(R.id.address_tv_how);
        tv_birthDate = (TextView)findViewById(R.id.dateBirth_tv_how);
        tv_phone = (TextView)findViewById(R.id.phone_tv_how);
        tv_welcome = (TextView)findViewById(R.id.welcomeText_how);

        //Disable all the text Edit Text ==================================
        changeEditText(et_firstName, false);
        changeEditText(et_lastName, false);
        changeEditText(et_address, false);
        changeEditText(et_phone, false);
        changeEditText(et_birthDate, false);
    }

    /**
     * Fill or edit the text field with a home Owner
     */
    private void fillTextField(){

        et_firstName.setText(this.homeOwner.getFirstName());
        et_lastName.setText(this.homeOwner.getLastName());
        et_birthDate.setText(this.homeOwner.getDateOfBirth());
        et_phone.setText(this.homeOwner.getPhoneNumber());
        et_address.setText(this.homeOwner.getAddress());

        tv_welcome.setText("Welcome, " + this.homeOwner.getUserName() + ", on the home owner page.");
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
    private boolean findHomeOwner(int id){
        boolean result = false;
        MyDBHandler dbHandler = new MyDBHandler(this);

        UserAccount userAccount = new UserAccount(dbHandler.getUserAccountFromID(id));
        Log.d("HOMEOWNERDEBUG","METHOD (getting user ID)" + userAccount.getUserID());

        this.homeOwner = new HomeOwner(dbHandler.getHomeOwner(userAccount));
        Log.d("HOMEOWNERDEBUG","METHOD (GETTING HOME OWNER)" + homeOwner.toString());

        if(this.homeOwner.getHomeID() >= 0)
            result = true;
        return(result);
    }

}
