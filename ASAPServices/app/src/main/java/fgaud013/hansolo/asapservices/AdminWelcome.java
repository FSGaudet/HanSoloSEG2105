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

public class AdminWelcome extends AppCompatActivity {
    //Table USER ACCOUNT
    private static final String COL_USERID = "userID";

    private EditText et_username, et_userid;

    private TextView tv_username, tv_userid, tv_welcome;

    private Button btn_serviceType;

    private UserAccount userAccount = new UserAccount();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_welcome);
        init();

        //Get value send from the login activity
        Intent i = getIntent();
        Bundle data = i.getExtras();
        int userID = data.getInt(COL_USERID);

        boolean result = findAdminAccount(userID);

        if (!result) {
            Toast.makeText(this, "Can't connect to the user.", Toast.LENGTH_LONG).show();
            finish();
        }
        else
            fillTextField();
    }

    /**
     * Initialise the text field and text view
     */
    private void init() {
        //Edit Text ======================================================
        et_username = (EditText)findViewById(R.id.username_et_aw);
        et_userid = (EditText)findViewById(R.id.userid_et_aw);


        //Text View ======================================================
        tv_username = (TextView)findViewById(R.id.username_tv_aw);
        tv_userid = (TextView)findViewById(R.id.userid_tv_aw);
        tv_welcome = (TextView)findViewById(R.id.welcomeText_aw);

        //Button    ======================================================
        btn_serviceType = (Button)findViewById(R.id.btn_serviceTypeModifier_aw);

        //Disable all the text Edit Text ==================================
        changeEditText(et_username, false);
        changeEditText(et_userid, false);
    }

    /**
     * Fill or edit the text field with a admin account
     */
    private void fillTextField() {

        et_username.setText(userAccount.getUserName());
        et_userid.setText("" + userAccount.getUserID());

        tv_welcome.setText("Welcome, " + this.userAccount.getUserName() + ", on the administrator page.");

        createServiceType();
    }

    /**
     * change the EditText
     *
     * @param et
     */
    private void changeEditText(EditText et, boolean active) {
        et.setEnabled(active);
        et.setFocusable(active);
        et.setCursorVisible(active);
    }

    /**
     * Find the home Owner connected to the userID send to this activity
     *
     * @param id
     * @return
     */
    private boolean findAdminAccount(int id) {
        boolean result = false;
        MyDBHandler dbHandler = new MyDBHandler(this);

        this.userAccount = new UserAccount(dbHandler.getUserAccountFromID(id));
        Log.d("ADMINDEBUG", "METHOD (getting user ID)" + this.userAccount.getUserID());

        if(userAccount.getUserID()>=0)
            result = true;

        return (result);
    }

    /**
     * Listener for the button create service type
     */
    public void createServiceType(){
        btn_serviceType.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(AdminWelcome.this,ServiceTypeAdder.class);
                        startActivity(intent);
                    }
                }
        );
    }

}
