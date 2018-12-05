package fgaud013.hansolo.asapservices;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

/**
 * @Description : Practicing with SQLite and lab 06
 * @Author      : Frédérick Gaudet - 8035208
 */

public class MyDBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ASAPService.db";

    //Table USER ACCOUNT
    private static final String TABLE_USER = "UserAccount";
    private static final String COL_USERID = "user_id";
    private static final String COL_USERNAME = "username";
    private static final String COL_PASSWORD = "password";
    private static final String COL_PHONE = "phone_number";
    private static final String COL_ADDRESS = "address";
    private static final String COL_PERMLVL = "account_level";

    //Table HOMEOWNER
    private static final String TABLE_HOMEOWNER = "HomeOwner";
    private static final String COL_HOMEID = "home_id";
    private static final String COL_FIRSTNAME = "first_name";
    private static final String COL_LASTNAME = "last_name";
    private static final String COL_DOB = "date_of_birth";

    //Table SERVICE PROVIDER
    private static final String TABLE_SP = "ServiceProvider";
    private static final String COL_SPID = "sp_id";
    private static final String COL_COMPANY = "company_name";
    private static final String COL_DESCRIPTION = "description";
    private static final String COL_LICENSED = "licensed";

    //Table SERVICE TYPE
    private static final String TABLE_SERVICE = "ServiceType";
    private static final String COL_SERVICE_ID = "service_id";
    private static final String COL_SERVICENAME = "service_name";
    private static final String COL_HOURLYRATE = "hourly_rate";

    //Table DAYOFTHEWEEK
    private static final String TABLE_DAYOFTHEWEEK = "DayOfTheWeek";
    private static final String COL_DAYID = "day_id";
    private static final String COL_DAYNAME = "day_name";

    //Table AVAILABILITY
    private static final String TABLE_AVAILABILITY = "Availability";
    private static final String COL_AVAILA_ID = "availability_id";
    private static final String COL_START_TIME = "start_time";
    private static final String COL_END_TIME = "end_time";


    //Table SERVICETYPESP
    private static final String TABLE_ST_SP = "ServiceTypeSP";
    private static final String COL_ST_SP_ID = "ServiceTypeSP_ID";

    //Table RATING
    private static final String TABLE_RATING = "Rating";
    private static final String COL_RATING_ID = "rating_id";
    private static final String COL_RATING = "rating";
    private static final String COL_COMMENT = "comment";

    //Table Appointment
    private static final String TABLE_APPOINTMENT = "Appointment";
    private static final String COL_APPOINTMENT_ID = "appointment_id";
    private static final String COL_COMPLETED_APPOINTMENT = "completed";


    private static final String NO_TIME = "No time";
    private static final String NO_RATING = "No rating";


    /**
     * Constructor using context
     *
     * @param context
     */
    public MyDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    /**
     * Creation of the database
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        //Creation of the table USER
        String CREATE_TABLE_USER = "CREATE TABLE " + TABLE_USER + "( " +
                COL_USERID + " INTEGER NOT NULL PRIMARY KEY, " +
                COL_USERNAME + " TEXT, " +
                COL_PASSWORD + " TEXT, " +
                COL_ADDRESS + " TEXT, " +
                COL_PHONE + " TEXT, " +
                COL_PERMLVL + " INTEGER" +
                ")";


        //Creation of the table SERVICE
        String CREATE_TABLE_SERVICE = "CREATE TABLE " + TABLE_SERVICE + "( " +
                COL_SERVICE_ID + " INTEGER NOT NULL PRIMARY KEY, " +
                COL_SERVICENAME + " TEXT, " +
                COL_HOURLYRATE + " REAL)";

        //Creation of table ServiceProvider
        String CREATE_TABLE_SP = "CREATE TABLE " + TABLE_SP + "( " +
                COL_SPID + " INTEGER NOT NULL PRIMARY KEY, " +
                COL_USERID + " INTEGER, " +
                COL_COMPANY + " TEXT, " +
                COL_DESCRIPTION + " TEXT, " +
                COL_LICENSED + " INTEGER)";

        //Creation of table AVAILABILITY
        String CREATE_TABLE_AVAILABILITY = "CREATE TABLE " + TABLE_AVAILABILITY + "( " +
                COL_AVAILA_ID + " INTEGER NOT NULL PRIMARY KEY, " +
                COL_SPID + " INTEGER, " +
                COL_DAYID + " INTEGER, " +
                COL_START_TIME + " TEXT, " +
                COL_END_TIME + " TEXT)";

        //Creation of table HomeOwner
        String CREATE_TABLE_HOMEOWNER = "CREATE TABLE " + TABLE_HOMEOWNER + "( " +
                COL_HOMEID + " INTEGER NOT NULL PRIMARY KEY, " +
                COL_USERID + " INTEGER, " +
                COL_FIRSTNAME + " TEXT, " +
                COL_LASTNAME + " TEXT, " +
                COL_DOB + " TEXT)";

        //Creation of table that link ServiceType and service provider
        String CREATE_TABLE_ST_SP = "CREATE TABLE " + TABLE_ST_SP + "( " +
                COL_ST_SP_ID + " INTEGER NOT NULL PRIMARY KEY, " +
                COL_SPID + " INTEGER, " +
                COL_SERVICE_ID + " INTEGER)";

        //Creation of table that link ServiceType and service provider
        String CREATE_TABLE_DAYS = "CREATE TABLE " + TABLE_DAYOFTHEWEEK + "( " +
                COL_DAYID + " INTEGER NOT NULL PRIMARY KEY, " +
                COL_DAYNAME + " TEXT)";

        //Creation of table RATING
        String CREATE_TABLE_RATING = "CREATE TABLE " + TABLE_RATING + "( " +
                COL_RATING_ID + " INTEGER NOT NULL PRIMARY KEY, " +
                COL_APPOINTMENT_ID + " INTEGER, " +
                COL_SPID + " INTEGER, " +
                COL_RATING + " INTEGER, " +
                COL_COMMENT + " TEXT)";

        //Creation of table RATING
        String CREATE_TABLE_Appointment = "CREATE TABLE " + TABLE_APPOINTMENT + "( " +
                COL_APPOINTMENT_ID + " INTEGER NOT NULL PRIMARY KEY, " +
                COL_HOMEID + " INTEGER, " +
                COL_SPID + " INTEGER, " +
                COL_DAYID + " INTEGER, " +
                COL_SERVICE_ID + " INTEGER, " +
                COL_START_TIME + " TEXT, " +
                COL_END_TIME + " TEXT, " +
                COL_COMPLETED_APPOINTMENT + " INTEGER)";

        try {
            db.execSQL(CREATE_TABLE_USER);
            db.execSQL(CREATE_TABLE_SERVICE);
            db.execSQL(CREATE_TABLE_SP);
            db.execSQL(CREATE_TABLE_HOMEOWNER);
            db.execSQL(CREATE_TABLE_ST_SP);
            db.execSQL(CREATE_TABLE_DAYS);
            db.execSQL(CREATE_TABLE_AVAILABILITY);
            db.execSQL(CREATE_TABLE_Appointment);
            db.execSQL(CREATE_TABLE_RATING);

            String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

            for (int i = 0; i < days.length; i++) {
                ContentValues values = new ContentValues();
                values.put(COL_DAYNAME, days[i]);
                db.insert(TABLE_DAYOFTHEWEEK, null, values);
            }
            //Creation of the admin account
            insertAdmin();

        } catch (Exception ex) {
            Log.d("BUGDATABASECREATION", ex.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVICE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOMEOWNER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ST_SP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAYOFTHEWEEK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AVAILABILITY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RATING);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APPOINTMENT);
        onCreate(db);
    }

    public void insertAdmin() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USERNAME, "admin");
        values.put(COL_PASSWORD, "admin");
        values.put(COL_PHONE, "No phone");
        values.put(COL_ADDRESS, "No address");
        values.put(COL_PERMLVL, 0);
        db.insert(TABLE_USER, null, values);
    }

    /**
     * Fill the table Day of The Week with all the day of the week.
     */
    public void fillDayOfWeek(SQLiteDatabase db) {
        db = this.getWritableDatabase();
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

        for (int i = 0; i < days.length; i++) {
            ContentValues values = new ContentValues();
            values.put(COL_DAYNAME, days[i]);
            db.insert(TABLE_DAYOFTHEWEEK, null, values);
        }
        db.close();
    }

    /**
     * USE WITH CARE, THIS DROP ALL THE DATA
     */
    public void clearAll() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_USER);
        db.execSQL("DELETE FROM " + TABLE_SP);
        db.execSQL("DELETE FROM " + TABLE_SERVICE);
        db.execSQL("DELETE FROM " + TABLE_ST_SP);
        db.execSQL("DELETE FROM " + TABLE_HOMEOWNER);
        db.execSQL("DELETE FROM " + TABLE_AVAILABILITY);
        db.execSQL("DELETE FROM " + TABLE_RATING);
        db.execSQL("DELETE FROM " + TABLE_APPOINTMENT);
        db.close();
    }

    /**
     * Return true if the database is empty
     *
     * @return
     */
    public boolean isDBEmpty() {
        boolean empty = true;
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USER;

        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.getCount() != 0)
                empty = false;
        } catch (Exception ex) {
            return empty;
        }
        return empty;
    }

    /**
     * Clean the table user from enemie
     */
    public void cleanUser() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_USER);
        db.close();
    }

    /**
     * =================================== SERVICE TYPE ======================================
     */

    /**
     * Insert a serviceType in the datbase
     *
     * @param serviceType
     * @return -1 = Inserted, -2 = Already exist, -3 = error
     */
    public int insertServiceType(ServiceType serviceType) {

        String query = "SELECT * FROM " + TABLE_SERVICE +
                " WHERE " + COL_SERVICENAME + " = \'" +
                serviceType.getServiceName() + "\'";

        try {
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst())//Look if there is at least one result
                return (-2);//If it exist

            cursor.close();
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COL_SERVICENAME, serviceType.getServiceName());
            values.put(COL_HOURLYRATE, serviceType.getHourlyRate());

            db.insert(TABLE_SERVICE, null, values);

            db.close();
            return (-1);
        } catch (Exception ex) {
            Log.d("BUGDATABASEHANDLER", "METHOD (insertServiceType)" + ex.getMessage());
            return (-3);
        }
    }

    /**
     * Get all the service type out from the database and return it into an array
     *
     * @return
     */
    public ArrayList<ServiceType> getAllServiceTypeOfServiceProvider(int spID) {
        ArrayList<ServiceType> myServices = new ArrayList<ServiceType>();//Create the array
        //The query to get all the service from the table service
        String query = "SELECT * FROM " + TABLE_SERVICE +
                " WHERE " + COL_SERVICE_ID + " IN (SELECT " + COL_SERVICE_ID +
                " FROM " + TABLE_ST_SP + " WHERE " + COL_SPID + " = " + spID + ")";

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String name = cursor.getString(cursor.getColumnIndex(COL_SERVICENAME));

                    Double hourlyRate = cursor.getDouble(cursor.getColumnIndex(COL_HOURLYRATE));

                    int id = cursor.getInt(cursor.getColumnIndex(COL_SERVICE_ID));

                    ServiceType st = new ServiceType(id, name, hourlyRate);

                    myServices.add(st);
                }
            }
            cursor.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d("BUGDATABASEHANDLER", "METHOD (getAllServiceTypeOfServiceProvider)" + ex.getMessage());
            return (myServices);
        } finally {
            db.close();
        }
        return (myServices);
    }

    /**
     * Get all the service type out from the database and return it into an array
     *
     * @return
     */
    public ArrayList<ServiceType> getAllServiceType() {
        ArrayList<ServiceType> myServices = new ArrayList<ServiceType>();//Create the array
        //The query to get all the serviceName
        String query = "Select * FROM " + TABLE_SERVICE;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String name = cursor.getString(cursor.getColumnIndex(COL_SERVICENAME));
                    Double hourlyRate = cursor.getDouble(cursor.getColumnIndex(COL_HOURLYRATE));
                    int id = cursor.getInt(cursor.getColumnIndex(COL_SERVICE_ID));
                    ServiceType st = new ServiceType(id, name, hourlyRate);
                    myServices.add(st);
                }
            }
            cursor.close();
        } catch (Exception ex) {
            Log.d("BUGDATABASEHANDLER", "METHOD (getAllServiceType)" + ex.getMessage());
            ex.printStackTrace();
            return (myServices);
        } finally {
            db.close();
        }

        return (myServices);
    }

    /**
     * Get service Type
     * @param id
     * @return
     */
    public ServiceType getServiceTypeFromID(int id) {
        ServiceType st = new ServiceType();

        String query = "SELECT * FROM " + TABLE_SERVICE +
                " WHERE " + COL_SERVICE_ID + " = " + id;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        //Try to get the ID
        try {
            //Get all the info of the Service type
            if (cursor.moveToFirst()) {
                st.setServiceName(cursor.getString(cursor.getColumnIndex(COL_SERVICENAME)));
                st.setSTID(cursor.getInt(cursor.getColumnIndex(COL_SERVICE_ID)));
                st.setHourlyRate(cursor.getDouble(cursor.getColumnIndex(COL_HOURLYRATE)));
            }

            cursor.close();//Closing cursor
            db.close();//Closing DB
        } catch (Exception ex) {
            Log.d("BUGDATABASEHANDLER", "METHOD (getServiceProvider)" + ex.getMessage());
            return (new ServiceType());
        }
        return (st);
    }

    /**
     * Modify a service type
     *
     * @param serviceType
     * @param newHourlyRate
     * @param newServiceName
     * @return
     */
    public boolean modifyServiceType(ServiceType serviceType, double newHourlyRate, String newServiceName) {
        SQLiteDatabase db = this.getWritableDatabase();//Create the connection to DB

        try {
            //New values
            ContentValues cv = new ContentValues();

            //New service name put in col servicename with same id
            cv.put(COL_SERVICENAME, newServiceName);
            cv.put(COL_HOURLYRATE, newHourlyRate);

            //Update the TABLE_SERVICE
            db.update(TABLE_SERVICE,
                    cv,
                    COL_SERVICE_ID + " = " + serviceType.getSTID(),
                    null);
            db.close();
            return (true);
        } catch (Exception ex) {
            Log.d("BUGDATABASEHANDLER", "METHOD (modifyServiceType)" + ex.getMessage());
            return (false);
        }
    }

    /**
     * Delete the service name
     *
     * @param serviceName
     * @return if it got deleted (boolean)
     */
    public boolean deleteServiceType(String serviceName) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(TABLE_SERVICE,
                    COL_SERVICENAME + "= \'" + serviceName + "\'",
                    null);
            db.close();
            return (true);
        } catch (Exception ex) {
            Log.d("BUGDATABASEHANDLER", "METHOD (deleteServiceType)" + ex.getMessage());
            return (false);
        }
    }

    /**
     * Get the service ID
     *
     * @param serviceName
     * @return
     */
    public int getServiceTypeID(String serviceName) {
        int id = -1;
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT " + COL_SERVICE_ID +
                " FROM " + TABLE_SERVICE +
                " WHERE " + COL_SERVICENAME + " = \'" + serviceName + "\'";

        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(COL_SERVICE_ID)));
            }
        } catch (Exception ex) {
            Log.d("BUGDATABASEHANDLER", "METHOD (getServiceTypeID)" + ex.getMessage());
        }
        return id;
    }

    /**
     * Get the service name from the ID
     *
     * @param id
     * @return
     */
    public String getServiceTypeName(int id) {
        String serviceName = "";
        String query = "SELECT " + COL_SERVICENAME +
                " FROM " + TABLE_SERVICE +
                " WHERE " + COL_SERVICE_ID + " = " + id;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        //Try to get the ID
        try {
            if (cursor.moveToFirst())
                serviceName = cursor.getString(cursor.getColumnIndex(COL_SERVICENAME));

            cursor.close();//Closing cursor
            db.close();//Closing DB
        } catch (Exception ex) {
            Log.d("BUGDATABASEHANDLER", "METHOD (getServiceTypeName)" + ex.getMessage());
            return serviceName;
        }
        return serviceName;//Return ID
    }

    /**
     * =================================== USER ACCOUNT ======================================
     */

    /**
     * Insert of user account
     *
     * @param userAccount
     * @return -1 = error getting ID, -2 = Already exist, -3 = error
     * Other wise return the ID of the userAccount after creating it
     */
    public UserAccount insertUserAccount(UserAccount userAccount) {
        //A query to look if the user exist.
        String query = "SELECT * FROM " + TABLE_USER +
                " WHERE " + COL_USERNAME + " = \"" +
                userAccount.getUserName() + "\"";
        int userID = -1;
        try {
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {//Look if there is at least one result
                userAccount.setUserID(-2);
                return (userAccount);//If it exist
                //return ("User exist");//If it exist
            }

            cursor.close();
            db = this.getWritableDatabase();

            ContentValues values = new ContentValues();

            //Setting values to insert in the table
            values.put(COL_USERNAME, userAccount.getUserName());
            values.put(COL_PASSWORD, userAccount.getPassword());
            values.put(COL_ADDRESS, userAccount.getAddress());
            values.put(COL_PHONE, userAccount.getPhoneNumber());
            values.put(COL_PERMLVL, userAccount.getPermLevel());

            db.insert(TABLE_USER, null, values);

            db.close();//Closing database
            //Getting the ID of the user
            userID = getUserAccountID(userAccount.getUserName(), userAccount.getPassword());

            if (userID == -1) {
                userAccount.setUserID(-1);
                return (userAccount);
                //return("Cant find the user");
            }
            if (userID == -2) {
                userAccount.setUserID(-8);
                return (userAccount);
                // return ("Error searching for USER ID");
            }
            userAccount.setUserID(userID);//Return the actual user with the good ID
            return (userAccount);
            //return ("User created");
        } catch (Exception ex) {
            Log.d("BUGDATABASEHANDLER", "METHOD (insertUserAccount)" + ex.getMessage());
            userAccount.setUserID(-3);//Return the ID at -3
            return (userAccount);
            //return (ex.getMessage());
        }
    }

    /**
     * Get the user ID with the username and password.
     *
     * @param username
     * @param password
     * @return -1 if not found
     */
    public int getUserAccountID(String username, String password) {
        int id = -1;
        String query = "SELECT " + COL_USERID +
                " FROM " + TABLE_USER +
                " WHERE " + COL_USERNAME + " = \"" +
                username + "\" AND " + COL_PASSWORD +
                " = \"" + password + "\"";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        //Try to get the ID
        try {
            if (cursor.moveToFirst())
                id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(COL_USERID)));

            cursor.close();//Closing cursor
            db.close();//Closing DB
        } catch (Exception ex) {
            Log.d("BUGDATABASEHANDLER", "METHOD (getUserAccountID)" + ex.getMessage());
            return -2;
        }
        return id;//Return ID
    }

    /**
     * @param username
     * @param password
     * @return
     */
    public UserAccount getUserAccount(String username, String password) {
        UserAccount userAccountTmp = new UserAccount();

        String query = "SELECT * FROM " + TABLE_USER +
                " WHERE " + COL_USERNAME + " = \'" +
                username + "\' AND " + COL_PASSWORD +
                " =\'" + password + "\'";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        //Try to get the ID
        try {
            //Get all the info of the userAccount
            if (cursor.moveToFirst()) {
                userAccountTmp.setUserID(
                        Integer.parseInt(cursor.getString(cursor.getColumnIndex(COL_USERID))));
                userAccountTmp.setUserName(
                        cursor.getString(cursor.getColumnIndex(COL_USERNAME)));
                userAccountTmp.setPassword(
                        cursor.getString(cursor.getColumnIndex(COL_PASSWORD)));
                userAccountTmp.setAddress(
                        cursor.getString(cursor.getColumnIndex(COL_ADDRESS)));
                userAccountTmp.setPhoneNumber(
                        cursor.getString(cursor.getColumnIndex(COL_PHONE)));
                userAccountTmp.setPermLevel(
                        Integer.parseInt(cursor.getString(cursor.getColumnIndex(COL_PERMLVL))));
            }
            cursor.close();//Closing cursor
            db.close();//Closing DB
        } catch (Exception ex) {
            Log.d("BUGDATABASEHANDLER", "METHOD (getUserAccount)" + ex.getMessage());
            return (new UserAccount());
        }
        return userAccountTmp;//Return ID
    }

    /**
     * Get user account
     *
     * @param userID
     * @return
     */
    public UserAccount getUserAccountFromID(int userID) {
        UserAccount userAccountTmp = new UserAccount();

        String query = "SELECT * FROM " + TABLE_USER +
                " WHERE " + COL_USERID + " = " + userID;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        //Try to get the ID
        try {
            //Get all the info of the userAccount
            if (cursor.moveToFirst()) {
                userAccountTmp.setUserID(
                        Integer.parseInt(cursor.getString(cursor.getColumnIndex(COL_USERID))));
                userAccountTmp.setUserName(
                        cursor.getString(cursor.getColumnIndex(COL_USERNAME)));
                userAccountTmp.setPassword(
                        cursor.getString(cursor.getColumnIndex(COL_PASSWORD)));
                userAccountTmp.setAddress(
                        cursor.getString(cursor.getColumnIndex(COL_ADDRESS)));
                userAccountTmp.setPhoneNumber(
                        cursor.getString(cursor.getColumnIndex(COL_PHONE)));
                userAccountTmp.setPermLevel(
                        Integer.parseInt(cursor.getString(cursor.getColumnIndex(COL_PERMLVL))));
            }
            cursor.close();//Closing cursor
            db.close();//Closing DB
        } catch (Exception ex) {
            Log.d("BUGDATABASEHANDLER", "METHOD (getUserAccountFromID)" + ex.getMessage());
            return (new UserAccount());
        }
        return userAccountTmp;//Return ID
    }

    /**
     * Delete a user
     *
     * @param userID
     * @return
     */
    public boolean deleteUserAccount(int userID) {

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_USER, COL_USERID + "=" + userID, null);
            db.close();

            return (true);
        } catch (Exception ex) {
            Log.d("BUGDATABASEHANDLER", "METHOD (deleteUserAccount)" + ex.getMessage());
        }

        return (false);
    }

    /**
     * ================================= SERVICE PROVIDER ====================================
     */

    /**
     * Some error will be returned as negative ID
     * ================== User Account ======================
     * If userID = -1 ==> Cant find the user PUSHED(problably error in the push)
     * Return ==>  serviceProviderID = -1
     * If userID = -2 ==> Already exist
     * Return ==>  serviceProviderID = -2
     * If userID = -3 ==> Error with inserting user
     * Return ==>  serviceProviderID = -3
     * If userID = -3 ==> Error with inserting user
     * Return ==>  serviceProviderID = -8
     * If userID >= 0 ==> User got pushed and returned the ID
     * <p>
     * ================= Service Type ======================
     * If service = -1 ==> Cant find the service Type
     * Return ==> serviceProviderID = -4
     * <p>
     * =============== Service PROVIDER ====================
     * If generic error
     * Return ==> serviceProviderID = -5
     * Cant find the SP
     * Return ==> serviceProviderID = -6
     */
    public ServiceProvider insertServiceProvider(ServiceProvider serviceProvider) {

        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(COL_USERID, serviceProvider.getUserID());
            values.put(COL_COMPANY, serviceProvider.getCompanyName());
            values.put(COL_DESCRIPTION, serviceProvider.getDescription());
            values.put(COL_LICENSED, (serviceProvider.isLicensed() ? 1 : 0));

            db.insert(TABLE_SP, null, values);//Insert everything in the table
            db.close();//Closing database
        } catch (Exception ex) {
            Log.d("BUGDATABASEHANDLER", "METHOD (insertServiceProvider)" + ex.getMessage());
            serviceProvider.setServiceProviderID(-5);//Error
        }

        int spID = getServiceProviderID(serviceProvider.getUserID());//Return -6 error finding, -1 cant find
        serviceProvider.setServiceProviderID(spID);

        return (serviceProvider);
    }

    /**
     * get the service provider ID linked to the UserID
     *
     * @param userID
     * @return
     */
    public int getServiceProviderID(int userID) {
        int spID = -1;
        String query = "SELECT " + COL_SPID +
                " FROM " + TABLE_SP +
                " WHERE " + COL_USERID + " = " + userID;

        SQLiteDatabase db = this.getReadableDatabase();


        //Try to get the ID of the ServiceProvider
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst())
                spID = Integer.parseInt(cursor.getString(cursor.getColumnIndex(COL_SPID)));

            cursor.close();//Closing cursor
            db.close();//Closing DB
        } catch (Exception ex) {
            Log.d("BUGDATABASEHANDLER", "METHOD (getServiceProviderID) " + ex.getMessage());
            spID = -6;
        }

        return (spID);
    }

    /**
     * Delete the service provider with the userID
     *
     * @param userID
     * @return
     */
    public boolean deleteServiceProvider(int userID) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_SP, COL_USERID + "=" + userID, null);
            db.delete(TABLE_USER, COL_USERID + "=" + userID, null);
            db.close();
            return (true);
        } catch (Exception ex) {
            Log.d("BUGDATABASEHANDLER", "METHOD (deleteServiceProvider)" + ex.getMessage());
        }

        return (false);
    }

    /**
     * Can delete a service type from a service provider
     *
     * @param stID
     * @param spID
     * @return
     */
    public boolean deleteServiceTypeOfServiceProvider(int stID, int spID) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_ST_SP, COL_SPID + "=" + spID + " AND "
                            + COL_SERVICE_ID + "=" + stID
                    , null);
            db.close();
            return (true);
        } catch (Exception ex) {
            Log.d("BUGDATABASEHANDLER", "METHOD (deleteServiceTypeOfServiceProvider)" + ex.getMessage());
        }

        return (false);
    }

    /**
     * Insert a service type linked to a serviceProvider
     *
     * @param stID
     * @param spID
     * @return
     */
    public boolean addServiceTypeToServiceProvider(int stID, int spID) {

        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(COL_SERVICE_ID, stID);
            values.put(COL_SPID, spID);

            db.insert(TABLE_ST_SP, null, values);//Insert everything in the table
            db.close();//Closing database
            return (true);
        } catch (Exception ex) {
            Log.d("BUGDATABASEHANDLER", "METHOD (insertServiceTypeToServiceProvider)" + ex.getMessage());

        }
        return (false);
    }

    /**
     * Get the service provider
     *
     * @param userAccount
     * @return
     */
    public ServiceProvider getServiceProvider(UserAccount userAccount) {
        ServiceProvider spTmp = new ServiceProvider(userAccount);

        int userID = userAccount.getUserID();

        String query = "SELECT * FROM " + TABLE_SP +
                " WHERE " + COL_USERID + " = " + userID;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        //Try to get the ID
        try {
            //Get all the info of the Service provider
            if (cursor.moveToFirst()) {
                spTmp.setServiceProviderID(
                        Integer.parseInt(cursor.getString(cursor.getColumnIndex(COL_SPID))));
                spTmp.setDescription(cursor.getString(cursor.getColumnIndex(COL_DESCRIPTION)));
                int licensed = cursor.getInt(cursor.getColumnIndex(COL_LICENSED));
                spTmp.setLicensed(licensed == 1);
                spTmp.setCompanyName(cursor.getString(cursor.getColumnIndex(COL_COMPANY)));
            }
            spTmp.setServiceType(getAllServiceTypeOfServiceProvider(spTmp.getServiceProviderID()));

            cursor.close();//Closing cursor
            db.close();//Closing DB
        } catch (Exception ex) {
            Log.d("BUGDATABASEHANDLER", "METHOD (getServiceProvider)" + ex.getMessage());
            return (new ServiceProvider());
        }
        return (spTmp);
    }

    /**
     * Get the service provider from the Id
     * @param id
     * @return
     */
    public ServiceProvider getServiceProviderFromID(int id) {
        ServiceProvider spTmp = new ServiceProvider();

        String query = "SELECT * FROM " + TABLE_SP +
                " WHERE " + COL_SPID + " = " + id;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        //Try to get the ID
        try {
            //Get all the info of the Service provider
            if (cursor.moveToFirst()) {
                spTmp.setServiceProviderID(
                        Integer.parseInt(cursor.getString(cursor.getColumnIndex(COL_SPID))));
                spTmp.setDescription(cursor.getString(cursor.getColumnIndex(COL_DESCRIPTION)));
                int licensed = cursor.getInt(cursor.getColumnIndex(COL_LICENSED));
                spTmp.setLicensed(licensed == 1);
                spTmp.setCompanyName(cursor.getString(cursor.getColumnIndex(COL_COMPANY)));
            }
            spTmp.setServiceType(new ArrayList<ServiceType>());

            cursor.close();//Closing cursor
            db.close();//Closing DB
        } catch (Exception ex) {
            Log.d("BUGDATABASEHANDLER", "METHOD (getServiceProviderFromID)" + ex.getMessage());
            return (new ServiceProvider());
        }
        return (spTmp);
    }

    /**
     * Return the name of the company from the ID
     * @param id
     * @return
     */
    public String getServiceProviderCompanyName(int id) {

        String companyName = "";
        String query = "SELECT " + COL_COMPANY +
                " FROM " + TABLE_SP +
                " WHERE " + COL_SPID + " = " + id;

        SQLiteDatabase db = this.getReadableDatabase();


        //Try to get the company name
        try
        {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst())
                companyName = cursor.getString(cursor.getColumnIndex(COL_COMPANY));

            cursor.close();//Closing cursor
            db.close();//Closing DB
        }
        catch(Exception ex)
        {
            Log.d("BUGDATABASEHANDLER", "METHOD (getServiceProviderID) " + ex.getMessage());
        }

            return(companyName);
    }
    /**
     * ================================= HOME OWNER ====================================
     */


    /**
     * Return the name of the company from the ID
     * @param id
     * @return
     */
    public String getHomeOwnerName(int id) {

        String firstName = "";
        String lastName = "";

        String query = "SELECT " + COL_FIRSTNAME + "," + COL_LASTNAME +
                " FROM " + TABLE_HOMEOWNER +
                " WHERE " + COL_HOMEID + " = " + id;

        SQLiteDatabase db = this.getReadableDatabase();


        //Try to get the company name
        try
        {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                firstName = cursor.getString(cursor.getColumnIndex(COL_FIRSTNAME));
                lastName = cursor.getString(cursor.getColumnIndex(COL_LASTNAME));
            }

            cursor.close();//Closing cursor
            db.close();//Closing DB
        }
        catch(Exception ex)
        {
            Log.d("BUGDATABASEHANDLER", "METHOD (getServiceProviderID) " + ex.getMessage());
        }

        return(firstName + " " + lastName);
    }
    /**
     * Home Owner
     * @param homeOwner
     * @return -5 = error inserting home owner, -6 error finding ID, -1 cant find Id
     */
    public HomeOwner insertHomeOwner(HomeOwner homeOwner){

        try{
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(COL_USERID, homeOwner.getUserID());
            values.put(COL_FIRSTNAME, homeOwner.getFirstName());
            values.put(COL_LASTNAME, homeOwner.getLastName());
            values.put(COL_DOB, homeOwner.getDateOfBirth());

            db.insert(TABLE_HOMEOWNER, null, values);//Insert everything in the table
            db.close();//Closing database
        }
        catch(Exception ex){
            Log.d("BUGDATABASEHANDLER","METHOD (insertHomeOwner)" + ex.getMessage());
            homeOwner.setHomeID(-5);//Error
        }

        int hoID = getHomeOwnerID(homeOwner.getUserID());//Return -6 error finding, -1 cant find
        homeOwner.setHomeID(hoID);

        return (homeOwner);
    }

    /**
     * Get the home owner ID from a USERID
     * @param userID
     * @return
     */
    public int getHomeOwnerID(int userID){
        int hoID = -1;
        String query =  "SELECT " + COL_HOMEID +
                " FROM " + TABLE_HOMEOWNER  +
                " WHERE " + COL_USERID + " = " + userID;

        SQLiteDatabase db = this.getReadableDatabase();

        //Try to get the ID of the ServiceProvider
        try{
            Cursor cursor = db.rawQuery(query, null);
            if(cursor.moveToFirst())
                hoID = cursor.getInt(cursor.getColumnIndex(COL_HOMEID));

            cursor.close();//Closing cursor
            db.close();//Closing DB
        }
        catch(Exception ex){
            Log.d("BUGDATABASEHANDLER","METHOD (getHomeOwnerID)" + ex.getMessage());
            hoID = -6;
        }

        return(hoID);
    }

    /**
     * Delete an homeowner from the USERID
     * @param userID
     * @return
     */
    public boolean deleteHomeOwner(int userID){
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_HOMEOWNER, COL_USERID + "=" + userID, null);
            db.delete(TABLE_USER, COL_USERID + "=" + userID, null);
            db.close();
            return(true);
        }
        catch(Exception ex)
        {
            Log.d("BUGDATABASEHANDLER","METHOD (deleteHomeOwner)" + ex.getMessage());
        }

        return (false);
    }

    /**
     * get a homeowner from an user account
     * @param userAccount
     * @return
     */
    public HomeOwner getHomeOwner(UserAccount userAccount){
        HomeOwner hoTmp = new HomeOwner(userAccount);

        int userID = userAccount.getUserID();

        String query =  "SELECT * FROM " + TABLE_HOMEOWNER +
                " WHERE " + COL_USERID + " = " + userID;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        //Try to get the ID
        try{
            //Get all the info of the Service provider

            if(cursor.moveToFirst()) {
                hoTmp.setHomeID(cursor.getInt(cursor.getColumnIndex(COL_HOMEID)));
                hoTmp.setFirstName(cursor.getString(cursor.getColumnIndex(COL_FIRSTNAME)));
                hoTmp.setLastName(cursor.getString(cursor.getColumnIndex(COL_LASTNAME)));
                hoTmp.setDateOfBirth(cursor.getString(cursor.getColumnIndex(COL_DOB)));

            }
            cursor.close();//Closing cursor
            db.close();//Closing DB
        }
        catch(Exception ex){
            Log.d("BUGDATABASEHANDLER","METHOD (getHomeOwner)" + ex.getMessage());
            return (new HomeOwner());
        }
        return(hoTmp);
    }

    /**
     * =================================== OTHERS ======================================
     */

    public ArrayList<Day> getDaysOfTheWeek(){
        ArrayList<Day> days = new ArrayList<Day>();//Create the array
        //The query to get all the serviceName
        String query = "Select * FROM " + TABLE_DAYOFTHEWEEK;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery(query, null);
            if(cursor.getCount() > 0) {
                while(cursor.moveToNext()) {
                    String name = cursor.getString(cursor.getColumnIndex(COL_DAYNAME));
                    int id = cursor.getInt(cursor.getColumnIndex(COL_DAYID));
                    days.add(new Day(id,name));
                }
            }
            cursor.close();
        }
        catch(Exception ex){
            Log.d("BUGDATABASEHANDLER","METHOD (getDaysOfTheWeek)" + ex.getMessage());
            return(days);
        }
        finally {
            db.close();
        }
        return(days);
    }

    /**
     * =================================== Availibity ======================================
     */

    /**
     * Add availability to database
     * @param spID
     * @param dayID
     * @param start
     * @param end
     * @return
     */
    public boolean addAvailability(int spID, int dayID, String start, String end){
        try{
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(COL_DAYID, dayID);
            values.put(COL_SPID, spID);
            values.put(COL_START_TIME, start);
            values.put(COL_END_TIME, end);

            db.insert(TABLE_AVAILABILITY, null, values);//Insert everything in the table
            db.close();//Closing database
        }
        catch(Exception ex){
            Log.d("BUGDATABASEHANDLER","METHOD (addAvailability)" + ex.getMessage());
        }
        return false;
    }

    /**
     * Get all the Availabilities from a service provider
     * @param spID
     * @return
     */
    public ArrayList<Availability> getAllAvailabilitiesFromSP(int spID){
        ArrayList<Availability> myAvailability = new ArrayList<Availability>();//Create the array
        //The query to get all the service from the table service
        String query = "SELECT * FROM " + TABLE_AVAILABILITY +
                " WHERE " + COL_SPID + " = " + spID +
                " ORDER BY " + COL_DAYID + " ASC";

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery(query, null);

            if(cursor.getCount() > 0) {
                while(cursor.moveToNext()) {
                    String startDate = cursor.getString(cursor.getColumnIndex(COL_START_TIME));
                    String endDate = cursor.getString(cursor.getColumnIndex(COL_END_TIME));
                    int dayID = cursor.getInt(cursor.getColumnIndex(COL_DAYID));
                    int availID = cursor.getInt(cursor.getColumnIndex(COL_AVAILA_ID));

                    Availability availability = new Availability(startDate,endDate,
                    dayID,availID);

                    myAvailability.add(availability);
                }
            }
            cursor.close();
        }
        catch(Exception ex){
            ex.printStackTrace();
            Log.d("BUGDATABASEHANDLER","METHOD (getAllAvailabilitiesFromSP)" + ex.getMessage());
            return(myAvailability);
        }
        finally {
            db.close();
        }
        return(myAvailability);
    }

    /**
     * Get the availability ID from the ServiceProvider ID, the Start time and the ENDTIME
     * @param spID
     * @param startTime
     * @param endTime
     * @return
     */
    public int getAvailabilityID(int spID, int dayID, String startTime, String endTime){
        int id = -1;
        String query =  "SELECT * FROM " + TABLE_AVAILABILITY +
                " WHERE " + COL_SPID + " = " + spID +
                " AND " + COL_DAYID + " = " + dayID +
                " AND " + COL_START_TIME + " = \'" + startTime + "\' " +
                " AND " + COL_END_TIME + " = \'" + endTime + "\' " ;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        //Try to get the ID
        try{
            //Get all the info of the Service provider

            if(cursor.moveToFirst())
                id = cursor.getInt(cursor.getColumnIndex(COL_AVAILA_ID));

            cursor.close();//Closing cursor
            db.close();//Closing DB
        }
        catch(Exception ex){
            Log.d("BUGDATABASEHANDLER","METHOD (getAvailabilityID)\n" + ex.getMessage());
            return (-2);
        }
        return id;
    }

    /**
     * Delete availability
     * @param availID
     * @return
     */
    public boolean deleteAvailability(int availID){
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_AVAILABILITY, COL_AVAILA_ID+ "=" + availID, null);
            db.close();
            return(true);
        }
        catch(Exception ex)
        {
            Log.d("BUGDATABASEHANDLER","METHOD (deleteAvailability)" + ex.getMessage());
        }

        return (false);
    }

    public boolean modifyAvailability(int availID, int dayID, String startTime, String endTime){

        SQLiteDatabase db = this.getWritableDatabase();
        try{
            //New values
            ContentValues cv = new ContentValues();

            //New service name put in col servicename with same id
            cv.put(COL_START_TIME, startTime);
            cv.put(COL_DAYID,dayID);
            cv.put(COL_END_TIME, endTime);

            //Update the TABLE_SERVICE
            db.update(TABLE_AVAILABILITY,
                    cv,
                    COL_AVAILA_ID + " = " + availID,
                    null);
            db.close();
            return(true);
        }
        catch(Exception ex){
            Log.d("BUGDATABASEHANDLER","METHOD (modifyAvailability)" + ex.getMessage());
            return(false);
        }
    }

    /**
     * =================================== Appointment ======================================
     */

    /**
     * Return all the Appointment of an home owner
     * @param id
     * @return
     */
    public ArrayList<Appointment> getAllAppointmentFromHomeOwner(int id){
        //Create the good query for Appointment
        String query = "SELECT * FROM " + TABLE_APPOINTMENT +
                " WHERE " + COL_HOMEID + " = " + id +
                " AND " + COL_COMPLETED_APPOINTMENT + " = 0" +
                " ORDER BY " + COL_DAYID + " ASC";
        return (getAllAppointment(query));
    }

    /**
     * Return all the Appointment for a service Provider
     * @param id
     * @return
     */
    public ArrayList<Appointment> getAllAppointmentFromServiceProvider(int id){
        //Create the good query for Appointment
        String query = "SELECT * FROM " + TABLE_APPOINTMENT +
                " WHERE " + COL_SPID + " = " + id +
                " AND " + COL_COMPLETED_APPOINTMENT + " = 0" +
                " ORDER BY " + COL_DAYID + " ASC";
        return (getAllAppointment(query));
    }

    /**
     * Return a list of all the appointements completed
     */
    public ArrayList<Appointment> getCompletedAppointmentFromServiceProvider(int id){
        //Create the good query for Appointment
        String query = "SELECT * FROM " + TABLE_APPOINTMENT +
                " WHERE " + COL_SPID + " = " + id +
                " AND " + COL_COMPLETED_APPOINTMENT + " = 1" +
                " ORDER BY " + COL_DAYID + " ASC";
        return (getAllAppointment(query));
    }

    /**
     * Get the list of all the Appointment linked to one home owner or SP
     * @param query
     * @return
     */
    public ArrayList<Appointment> getAllAppointment(String query){
        ArrayList<Appointment> myAppointment = new ArrayList<Appointment>();//Create the array

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery(query, null);

            if(cursor.getCount() > 0) {
                while(cursor.moveToNext()) {
                    String startTime = cursor.getString(cursor.getColumnIndex(COL_START_TIME));
                    String endTime = cursor.getString(cursor.getColumnIndex(COL_END_TIME));
                    int dayID = cursor.getInt(cursor.getColumnIndex(COL_DAYID));
                    int homeID = cursor.getInt(cursor.getColumnIndex(COL_HOMEID));
                    int spID = cursor.getInt(cursor.getColumnIndex(COL_SPID));
                    int appID = cursor.getInt(cursor.getColumnIndex(COL_APPOINTMENT_ID));
                    int serviceID = cursor.getInt(cursor.getColumnIndex(COL_SERVICE_ID));

                    int completedInt = cursor.getInt(cursor.getColumnIndex(COL_COMPLETED_APPOINTMENT));
                    boolean completed = (completedInt == 0)? false:true;

                    Rating rating = new Rating();
                    if(completed)
                        rating = getRatingFromAppointment(appID);

                    Appointment Appointment = new Appointment(appID,
                            homeID,
                            spID,
                            dayID,
                            serviceID,
                            startTime,
                            endTime,
                            rating,
                            completed);

                    myAppointment.add(Appointment);
                }
            }
            cursor.close();
        }
        catch(Exception ex){
            ex.printStackTrace();
            Log.d("BUGDATABASEHANDLER","METHOD (getAllAppointment)\n" + ex.getMessage());
            return(myAppointment);
        }
        finally {
            db.close();
        }
        return(myAppointment);
    }

    /**
     * Complete an appointment with ID
     * @param rating
     * @return
     */
    public boolean completeAppointmentWithRating(Rating rating){
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            //New values
            ContentValues cv = new ContentValues();


            //Change the appointment to completed
            cv.put(COL_COMPLETED_APPOINTMENT,1);

            //Update the APPOINTMENT
            db.update(TABLE_APPOINTMENT,
                    cv,
                    COL_APPOINTMENT_ID + " = " + rating.getAppointment_id(),
                    null);

            ContentValues values = new ContentValues();

            db.close();
            Appointment appointment = new Appointment(getAppointmentFromID(rating.getAppointment_id()));
            db = this.getWritableDatabase();

            values.put(COL_APPOINTMENT_ID, rating.getAppointment_id());
            values.put(COL_COMMENT, rating.getComment());
            values.put(COL_RATING, rating.getRating());
            values.put(COL_SPID, appointment.getSp_id());

            db.insert(TABLE_RATING, null, values);//Insert everything in the table
            db.close();
            return(true);
        }
        catch(Exception ex){
            Log.d("BUGDATABASEHANDLER","METHOD (completeAppointmentWithRating)\n" + ex.getMessage());
            return(false);
        }
    }

    /**
     * Cancel an appointment
     * @param appointment_id
     * @return
     */
    public boolean cancelAppointment(int appointment_id){
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.delete(TABLE_APPOINTMENT,
                    COL_APPOINTMENT_ID + "= " + appointment_id,
                    null);
            db.close();
            return (true);
        } catch (Exception ex) {
            Log.d("BUGDATABASEHANDLER", "METHOD (cancelAppointment)" + ex.getMessage());
            return (false);
        }
    }

    /**
     * =================================== RATING ======================================
     */
    public Rating getRatingFromAppointment(int id){
        Rating rating = new Rating();
        String query =  "SELECT * FROM " + TABLE_RATING +
                " WHERE " + COL_APPOINTMENT_ID + " = " + id ;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        //Try to get the ID
        try{
            //Get all the info of the Service provider

            if(cursor.moveToFirst()) {
                int idRating = cursor.getInt(cursor.getColumnIndex(COL_RATING_ID));
                String comment = cursor.getString(cursor.getColumnIndex(COL_COMMENT));
                int ratingPoint = cursor.getInt(cursor.getColumnIndex(COL_RATING));
                int appID = cursor.getInt(cursor.getColumnIndex(COL_APPOINTMENT_ID));
                if(appID == id)
                    rating = new Rating(idRating,appID,ratingPoint,comment);
            }

            cursor.close();//Closing cursor
            db.close();//Closing DB
        }
        catch(Exception ex){
            Log.d("BUGDATABASEHANDLER","METHOD (getRatingFromAppointment)\n" + ex.getMessage());
            return (rating);
        }
        return rating;
    }


    /**
     * ============================= SEARCH SERVICE PROVIDER ================================
     */

    /**
     * Get a list of service propvider
     * @param service_id
     * @param day_id
     * @param start
     * @param end
     * @param rating
     * @return
     */
    public ArrayList<ServiceProvider> searchResultServiceProvider(int service_id, int day_id,
                                            String start, String end, int rating){

        ArrayList<ServiceProvider> mySP = new ArrayList<ServiceProvider>();//Create the array

        String subQuery = "", subQuery2 = "", query = "";

        subQuery = "SELECT " + COL_SPID + " FROM " + TABLE_ST_SP +
                " WHERE " + COL_SERVICE_ID + " = " + service_id;

        if(rating >=0){
            String subQuery3 = "SELECT " + COL_SPID +
                    " FROM " + TABLE_RATING + " GROUP BY " + COL_SPID +
                    " HAVING AVG(" + COL_RATING + ") BETWEEN " + rating + " AND " + (rating + 1);

            subQuery = subQuery + " AND " + COL_SPID + " IN(" +subQuery3 + ")";
        }

        if(day_id<0){
            subQuery2 = "SELECT DISTINCT " + COL_SPID + " FROM " + TABLE_AVAILABILITY +
                    " WHERE " + COL_SPID + " IN(" + subQuery + ")";
        }

        //The query to get all the service from the table service
        if(day_id>=0) {
            subQuery2 = "SELECT DISTINCT " + COL_SPID + " FROM " + TABLE_AVAILABILITY +
                    " WHERE " + COL_DAYID + " = " + day_id +
                    " AND " + COL_SPID + " IN(" + subQuery + ") " +
                    " AND " + COL_START_TIME + " <=\'" + start + "\'" +
                    " AND " + COL_END_TIME + " >= \'" + end + "\'";

        }

        query = "SELECT * FROM " + TABLE_SP +
                " WHERE " + COL_SPID + " IN(" + subQuery2 + ")";


        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery(query, null);

            if(cursor.getCount() > 0) {
                while(cursor.moveToNext()) {
                    int sp_id = cursor.getInt(cursor.getColumnIndex(COL_SPID));
                    String company = cursor.getString(cursor.getColumnIndex(COL_COMPANY));
                    String description = cursor.getString(cursor.getColumnIndex(COL_DESCRIPTION));
                    int lic = cursor.getInt(cursor.getColumnIndex(COL_LICENSED));
                    boolean licensed = ((lic == 1)? true:false);

                    ArrayList<ServiceType> st = new ArrayList<ServiceType>();
                    UserAccount ua = new UserAccount();
                    ServiceProvider serviceProvider = new ServiceProvider(ua,sp_id,st,
                            company,description,licensed);

                    mySP.add(serviceProvider);
                }
            }
            cursor.close();
        }
        catch(Exception ex){
            ex.printStackTrace();
            Log.d("BUGDATABASEHANDLER","METHOD (searchResultServiceProvider)\n" + ex.getMessage());
            return(mySP);
        }
        finally {
            db.close();
        }
        return(mySP);
    }

    /**
     * Create an appointment
     * @param spID
     * @param homeID
     * @param serviceID
     * @param dayID
     * @param start
     * @param end
     * @return
     */
    public boolean createAppointment(int spID, int homeID, int serviceID, int dayID, String start, String end){
        try{
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(COL_DAYID, dayID);
            values.put(COL_SPID, spID);
            values.put(COL_START_TIME, start);
            values.put(COL_END_TIME, end);
            values.put(COL_SERVICE_ID, serviceID);
            values.put(COL_HOMEID, homeID);
            values.put(COL_COMPLETED_APPOINTMENT, 0);

            db.insert(TABLE_APPOINTMENT, null, values);//Insert everything in the table
            db.close();//Closing database
            return(true);
        }
        catch(Exception ex){
            Log.d("BUGDATABASEHANDLER","METHOD (createAppointment)\n" + ex.getMessage());
        }
        return false;
    }

    /**
     * Get an appointments from an ID
     * @param id
     * @return
     */
    public Appointment getAppointmentFromID(int id) {
        Appointment appointment = new Appointment();

        String query = "SELECT * FROM " + TABLE_APPOINTMENT +
                " WHERE " + COL_APPOINTMENT_ID + " = " + id;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        //Try to get the ID
        try {
            //Get all the info of the appointment
            if (cursor.moveToFirst()) {
                appointment.setAppointment_id(cursor.getInt(cursor.getColumnIndex(COL_APPOINTMENT_ID)));
                appointment.setHome_id(cursor.getInt(cursor.getColumnIndex(COL_HOMEID)));
                appointment.setSp_id(cursor.getInt(cursor.getColumnIndex(COL_SPID)));
                appointment.setDay_id(cursor.getInt(cursor.getColumnIndex(COL_DAYID)));
                appointment.setServiceType_id(cursor.getInt(cursor.getColumnIndex(COL_SERVICE_ID)));
                appointment.setStart_time(cursor.getString(cursor.getColumnIndex(COL_START_TIME)));
                appointment.setEnd_time(cursor.getString(cursor.getColumnIndex(COL_END_TIME)));
                int completed = cursor.getInt(cursor.getColumnIndex(COL_COMPLETED_APPOINTMENT));
                appointment.setCompleted((completed==1)?true:false);
            }

            cursor.close();//Closing cursor
            db.close();//Closing DB
        } catch (Exception ex) {
            Log.d("BUGDATABASEHANDLER", "METHOD (getServiceProvider)" + ex.getMessage());
            return (new Appointment());
        }
        return (appointment);
    }
}