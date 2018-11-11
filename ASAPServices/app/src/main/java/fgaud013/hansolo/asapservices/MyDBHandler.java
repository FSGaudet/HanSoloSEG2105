package fgaud013.hansolo.asapservices;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;

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
    private static final String COL_FIRSTNAME = "first_name";
    private static final String COL_LASTNAME = "last_name";
    private static final String COL_DOB = "date_of_birth";
    private static final String COL_PHONE = "phone_number";
    private static final String COL_PERMLVL = "account_level";

    //Table SERVICE PROVIDER
    private static final String TABLE_SP = "ServiceProvider";
    private static final String COL_SPID = "sp_id";
    private static final String COL_HOURLYRATE = "hourly_rate";

    //Table SERVICE TYPE
    private static final String TABLE_SERVICE = "ServiceType";
    private static final String COL_SERVICE_ID = "service_id";
    private static final String COL_SERVICENAME = "service_name";


    /** Constructor using context
     * @param context
     */
    public MyDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    /**
     * Creation of the database
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        //Creation of the table USER
        String CREATE_TABLE_USER = "CREATE TABLE " + TABLE_USER + "( " +
                COL_USERID + " INTEGER NOT NULL PRIMARY KEY, " +
                COL_USERNAME + " TEXT, " +
                COL_PASSWORD + " TEXT, " +
                COL_FIRSTNAME + " TEXT, " +
                COL_LASTNAME + " TEXT, " +
                COL_DOB + " TEXT, " +
                COL_PHONE + " TEXT, " +
                COL_PERMLVL + " INTEGER" +
                ")";


        //Creation of the table SERVICE
        String CREATE_TABLE_SERVICE = "CREATE TABLE " + TABLE_SERVICE + "( " +
                COL_SERVICE_ID + " INTEGER NOT NULL PRIMARY KEY, " +
                COL_SERVICENAME + " TEXT" +
                ")";

        //Creation of table ServiceProvider
        String CREATE_TABLE_SP = "CREATE TABLE " + TABLE_SP + "( " +
                COL_SPID + " INTEGER NOT NULL PRIMARY KEY, " +
                COL_USERID + " INTEGER, "+
                COL_SERVICE_ID+ " INTEGER,"+
                COL_HOURLYRATE + " REAL)";

        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_SERVICE);
        db.execSQL(CREATE_TABLE_SP);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVICE);
        onCreate(db);
    }

    /**
     * USE WITH CARE, THIS DROP ALL THE DATA
     */
    public void clearAll(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_USER);
        db.execSQL("DELETE FROM " + TABLE_SP);
        db.execSQL("DELETE FROM " + TABLE_SERVICE);
        db.close();
    }

    /**
     * Return true if the database is empty
     * @return
     */
    public boolean isDBEmpty(){
        boolean empty = true;
        SQLiteDatabase db = getReadableDatabase();
        String query =  "SELECT * FROM " + TABLE_USER;

        try{
            Cursor cursor = db.rawQuery(query, null);
            if(cursor.getCount()!=0)
                    empty = false;
        }
        catch(Exception ex){
            return empty;
        }
        return empty;
    }

    /**
     * Clean the table user from enemie
     */
    public void cleanUser(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_USER);
        db.close();

    }

    /**
     * =================================== SERVICE TYPE ======================================
     */

    /**
     * Insert a serviceType in the datbase
     * @param serviceType
     * @return -1 = Inserted, -2 = Already exist, -3 = error
     */
    public int insertServiceType(String serviceType){

        String query = "SELECT * FROM " + TABLE_SERVICE +
                " WHERE " + COL_SERVICENAME + " = \'" +
                serviceType +"\'";

        try{
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = db.rawQuery(query, null);

            if(cursor.moveToFirst())//Look if there is at least one result
                return(-2);//If it exist

            cursor.close();
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COL_SERVICENAME, serviceType);

            db.insert(TABLE_SERVICE, null, values);

            db.close();
            return(-1);
        }
        catch(Exception ex){
            return(-3);
        }
    }

    /**
     * Get all the service type out from the database and return it into an array
     * @return
     */
    public ArrayList<String> getAllServiceType(){
        ArrayList<String> myServices = new ArrayList<String>();//Create the array
        //The query to get all the serviceName
        String query = "Select " + COL_SERVICENAME + " FROM " + TABLE_SERVICE;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery(query, null);
            if(cursor.getCount() > 0) {
                while(cursor.moveToNext()) {
                    String name = cursor.getString(cursor.getColumnIndex(COL_SERVICENAME));
                    myServices.add(name);
                }
            }
            cursor.close();
        }
        catch(Exception ex){
            ex.printStackTrace();
            return(myServices);
        }
        finally {
            db.close();
        }

        return(myServices);
    }

    /**
     * Modify the service name
     * @param serviceName
     * @param newServiceName
     * @return modified worked (boolean)
     */
    public boolean modifyServiceType(String serviceName, String newServiceName){
        SQLiteDatabase db = this.getWritableDatabase();//Create the connection to DB
        String query = "SELECT * FROM " + TABLE_SERVICE +
                " WHERE " + COL_SERVICENAME + " = \'" +
                newServiceName +"\'";
        try{
            Cursor cursor = db.rawQuery(query, null);

            if(cursor.moveToFirst()) {//Look if there is at least one result
                cursor.close();
                db.close();
                return (false);//If it exist
            }
            cursor.close();
        }
        catch(Exception ex){
            return(false);
        }

        try{
            //New values
            ContentValues cv = new ContentValues();

            //New service name put in col servicename with same id
            cv.put(COL_SERVICENAME,newServiceName);
            //Update the TABLE_SERVICE
            db.update(TABLE_SERVICE,
                    cv,
                    COL_SERVICENAME + "= \'" + serviceName + "\'",
                    null);
            db.close();
            return(true);
        }
        catch(Exception ex){
            return(false);
        }
    }

    /**
     * Delete the service name
     * @param serviceName
     * @return if it got deleted (boolean)
     */
    public boolean deleteServiceType(String serviceName){
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            db.delete(TABLE_SERVICE,
                    COL_SERVICENAME + "= \'" + serviceName + "\'",
                    null);
            db.close();
            return(true);
        }
        catch(Exception ex){
            return(false);
        }
    }

    /**
     * Get the service ID
     * @param serviceName
     * @return
     */
    public int getServiceTypeID(String serviceName){
        int id = -1;
        SQLiteDatabase db = getReadableDatabase();
        String query =  "SELECT " + COL_SERVICE_ID +
                        " FROM " + TABLE_SERVICE +
                        " WHERE " + COL_SERVICENAME + " = \'" + serviceName +"\'";

        try{
            Cursor cursor = db.rawQuery(query, null);
            if(cursor.moveToFirst()){
                id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(COL_SERVICE_ID)));
            }
        }
        catch(Exception ex){

        }
        return id;
    }

    /**
     * Get the service name from the ID
     * @param id
     * @return
     */
    public String getServiceTypeName(int id){
        String serviceName = "";
        String query =  "SELECT " + COL_SERVICENAME +
                        " FROM " + TABLE_SERVICE +
                        " WHERE " + COL_SERVICE_ID + " = " + id;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        //Try to get the ID
        try{
            if(cursor.moveToFirst())
                serviceName = cursor.getString(cursor.getColumnIndex(COL_SERVICENAME));

            cursor.close();//Closing cursor
            db.close();//Closing DB
        }
        catch(Exception ex){
            return serviceName;
        }
        return serviceName;//Return ID
    }

    /**
     * =================================== USER ACCOUNT ======================================
     */

    /**
     * Insert of user account
     * @param userAccount
     * @return -1 = error getting ID, -2 = Already exist, -3 = error
     *          Other wise return the ID of the userAccount after creating it
     */
    public UserAccount insertUserAccount(UserAccount userAccount){
        //A query to look if the user exist.
        String query =  "SELECT * FROM " + TABLE_USER+
                        " WHERE " + COL_USERNAME+ " = \"" +
                        userAccount.getUserName() +"\"";
        int userID = -1;
        try{
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = db.rawQuery(query, null);

            if(cursor.moveToFirst()) {//Look if there is at least one result
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
            values.put(COL_FIRSTNAME, userAccount.getFirstName());
            values.put(COL_LASTNAME, userAccount.getLastName());
            values.put(COL_DOB, userAccount.getDateOfBirth());
            values.put(COL_PHONE, userAccount.getPhoneNumber());
            values.put(COL_PERMLVL, userAccount.getPermLevel());

            db.insert(TABLE_USER, null, values);

            db.close();//Closing database
            //Getting the ID of the user
            userID = getUserAccountID(userAccount.getUserName(), userAccount.getPassword());

            if(userID == -1){
                userAccount.setUserID(-1);
                return(userAccount);
                //return("Cant find the user");
            }
            if(userID == -2) {
                userAccount.setUserID(-8);
                return (userAccount);
               // return ("Error searching for USER ID");
            }
            userAccount.setUserID(userID);//Return the actual user with the good ID
            return(userAccount);
            //return ("User created");
        }
        catch(Exception ex){
            userAccount.setUserID(-3);//Return the ID at -3
            return(userAccount);
            //return (ex.getMessage());
        }
    }

    /**
     * Get the user ID with the username and password.
     * @param username
     * @param password
     * @return -1 if not found
     */
    public int getUserAccountID(String username, String password){
        int id = -1;
        String query = "SELECT " + COL_USERID +
                " FROM " + TABLE_USER +
                " WHERE " + COL_USERNAME + " = \"" +
                username + "\" AND " + COL_PASSWORD +
                " = \"" + password +"\"";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        //Try to get the ID
        try{
            if(cursor.moveToFirst())
                id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(COL_USERID)));

            cursor.close();//Closing cursor
            db.close();//Closing DB
        }
        catch(Exception ex){
            return -2;
        }
        return id;//Return ID
    }

    public UserAccount getUserAccount(String username, String password){
        UserAccount userAccountTmp = new UserAccount();

        String query = "SELECT * FROM " + TABLE_USER +
                " WHERE " + COL_USERNAME + " = \'" +
                username + "\' AND " + COL_PASSWORD +
                " =\'" + password +"\'";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        //Try to get the ID
        try{
            //Get all the info of the userAccount
            if(cursor.moveToFirst()) {
                userAccountTmp.setUserID(
                        Integer.parseInt(cursor.getString(cursor.getColumnIndex(COL_USERID))));
                userAccountTmp.setUserName(
                        cursor.getString(cursor.getColumnIndex(COL_USERNAME)));
                userAccountTmp.setPassword(
                        cursor.getString(cursor.getColumnIndex(COL_PASSWORD)));
                userAccountTmp.setFirstName(
                        cursor.getString(cursor.getColumnIndex(COL_FIRSTNAME)));
                userAccountTmp.setLastName(
                        cursor.getString(cursor.getColumnIndex(COL_LASTNAME)));
                userAccountTmp.setDateOfBirth(
                        cursor.getString(cursor.getColumnIndex(COL_DOB)));
                userAccountTmp.setPhoneNumber(
                        cursor.getString(cursor.getColumnIndex(COL_PHONE)));
                userAccountTmp.setPermLevel(
                        Integer.parseInt(cursor.getString(cursor.getColumnIndex(COL_PERMLVL))));
            }
            cursor.close();//Closing cursor
            db.close();//Closing DB
        }
        catch(Exception ex){
            return (new UserAccount());
        }
        return userAccountTmp;//Return ID
    }

    /**
     * Delete a user
     * @param userID
     * @return
     */
    public boolean deleteUserAccount(int userID){

        try{
            SQLiteDatabase db = this.getWritableDatabase();
            deleteServiceProvider(userID);
            db.delete(TABLE_USER, COL_USERID + "=" + userID, null);
            db.close();

            return(true);
        }
        catch(Exception ex)
        {
        }

        return (false);
    }

    /**
     * ================================= SERVICE PROVIDER ====================================
     */

    /** Some error will be returned as negative ID
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
     *
     * ================= Service Type ======================
     * If service = -1 ==> Cant find the service Type
     * Return ==> serviceProviderID = -4
     *
     * =============== Service PROVIDER ====================
     * If generic error
     * Return ==> serviceProviderID = -5
     * Cant find the SP
     * Return ==> serviceProviderID = -6
     */
    public ServiceProvider insertServiceProvider(ServiceProvider serviceProvider){
        /**
         * Service type part here
         */

        ServiceType st = serviceProvider.getServiceType();

        int serviceTypeID = st.getSTID();

        //If no STID set, search for one.
        if(st.getSTID() == -1)
            serviceTypeID = getServiceTypeID(st.getServiceName());

        if(serviceTypeID == -1){//Couldnt get the ID
            serviceProvider.setServiceProviderID(-4);
            return(serviceProvider);
        }
        st.setSTID(serviceTypeID);
        serviceProvider.setServiceType(st);//Get the service ID

        /**
         * Service provider part start here
         */

        try{
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();

            //Prepare the USER ID related to the SP
            values.put(COL_USERID, serviceProvider.getUserID());
            //Prepare ID ServiceType ID
            values.put(COL_SERVICE_ID, st.getSTID());
            //Prepare hourly rate
            values.put(COL_HOURLYRATE, serviceProvider.getHourlyRate());

            db.insert(TABLE_SP, null, values);//Insert everything in the table
            db.close();//Closing database
        }
        catch(Exception ex){
            serviceProvider.setServiceProviderID(-5);//Error
        }

        int spID = getServiceProviderID(serviceProvider.getUserID());//Return -6 error finding, -1 cant find
        serviceProvider.setServiceProviderID(spID);

        return (serviceProvider);
    }

    public int getServiceProviderID(int userID){
        int spID = -1;
        String query =  "SELECT " + COL_SPID +
                        " FROM " + TABLE_SP  +
                        " WHERE " + COL_USERID + " = " + userID;

        SQLiteDatabase db = this.getReadableDatabase();


        //Try to get the ID of the ServiceProvider
        try{
            Cursor cursor = db.rawQuery(query, null);
            if(cursor.moveToFirst())
                spID = Integer.parseInt(cursor.getString(cursor.getColumnIndex(COL_SPID)));

            cursor.close();//Closing cursor
            db.close();//Closing DB
        }
        catch(Exception ex){
            spID = -6;
        }

        return(spID);
    }

    /**
     * Delete the service provider with the userID
     * @param userID
     * @return
     */
    public boolean deleteServiceProvider(int userID){
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_SP, COL_USERID + "=" + userID, null);
            db.close();
            return(true);
        }
        catch(Exception ex)
        {
        }

        return (false);
    }


    /**
     * Get the service provider
     * @param userAccount
     * @return
     */
    public ServiceProvider getServiceProvider(UserAccount userAccount){
        ServiceProvider spTmp = new ServiceProvider(userAccount);
        ServiceType stTmp = new ServiceType();

        int userID = userAccount.getUserID();

        String query =  "SELECT * FROM " + TABLE_SP  +
                " WHERE " + COL_USERID + " = " + userID;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        //Try to get the ID
        try{
            //Get all the info of the userAccount
            if(cursor.moveToFirst()) {
                spTmp.setServiceProviderID(
                        Integer.parseInt(cursor.getString(cursor.getColumnIndex(COL_SPID))));
                spTmp.setHourlyRate(
                        Double.parseDouble(cursor.getString(cursor.getColumnIndex(COL_HOURLYRATE))));
                stTmp.setSTID(
                        Integer.parseInt(cursor.getString(cursor.getColumnIndex(COL_SERVICE_ID))));
            }

            stTmp.setServiceName(getServiceTypeName(stTmp.getSTID()));
            spTmp.setServiceType(stTmp);

            cursor.close();//Closing cursor
            db.close();//Closing DB
        }
        catch(Exception ex){
            return (new ServiceProvider());
            //return(ex.getMessage());
        }
        return(spTmp);
        //return("Did it!");
    }
    /**
     * =================================== OTHERS ======================================
     */

}