package fgaud013.hansolo.asapservices;


/**
 * @description : Class that only being used to validate field
 * @Author      : Frédérick Gaudet - 8035208
 */

public class Validator {
    private static final int MIN_YEAR = 1900;
    private static final int MAX_YEAR = 2019;


    public Validator(){

    }

    /**
     * Validate the username
     * @param username
     * @return
     */
    public boolean isUsernameValid(String username){
        return !username.isEmpty() && !username.toLowerCase().equals("username");
    }

    /**
     * Validate the password
     * @param password
     * @return
     */
    public boolean isPasswordValid(String password){
        return !password.isEmpty() && !password.toLowerCase().equals("password");
    }

    /**
     * Validate the first name
     * @param firstname
     * @return
     */
    public boolean isFirstnameValid(String firstname){
        return !firstname.isEmpty() && !firstname.toLowerCase().equals("first name");
    }

    /**
     * Validate the last name
     * @param lastname
     * @return
     */
    public boolean isLastnameValid(String lastname){
        return !lastname.isEmpty() && !lastname.toLowerCase().equals("last name");
    }

    /**
     * Super self made method to confirm date of birth being yyyy-mm-dd
     * @param dateOfBirth
     * @return return true if yyyy-mm-dd
     */
    public boolean isDateOfBirthValid(String dateOfBirth){
        //Make sure that the date as a length of 10 (yyyy-mm-dd)
        if(dateOfBirth.length() != 10)
            return false;

        int year = -1;
        int month = -1;
        int day = -1;
        String year_st = "", month_st = "", day_st = "";

        char[] date = new char[10];
        dateOfBirth.getChars(0, 10, date, 0);

        //Validate the date has a format of ####-##-##
        for(int i =0;i<10;i++) {
            int a = (int)date[i];
            if(i != 4 && i != 7) {
                if((a - '0')>9 || (a - '0')<0)
                    return false;
            }
            else {
                if((a - '-') != 0)
                    return false;
            }
        }

        //Since we know that there is only number and dash we can do this directly
        for(int i=0;i<10;i++) {
            //The year
            if(i<4)
                year_st = year_st + date[i];
                //The month
            else if(i>4 && i<7)
                month_st = month_st + date[i];
                //The day
            else if(i>7)
                day_st = day_st + date[i];
        }

        //Put all the value into integer
        try {
            year = Integer.parseInt(year_st);
            month = Integer.parseInt(month_st);
            day = Integer.parseInt(day_st);
        }catch(Exception ex){
            return (false);
        }


        //Confirm the person is born between 1900 and 2019
        if(year < MIN_YEAR || year > MAX_YEAR)
            return false;

        //Confirm month is between 1 and 12
        if(month<1 || month>12)
            return false;

        //For 31 day's month
        if(month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
            return day >= 0 && day <= 31;
        }

        //For february
        else if(month == 2) {
            return day >= 0 && day <= 29;
        }

        //For 30 day's month
        else
            return day >= 0 && day <= 30;

    }

    /**
     * Confirm the phone number
     * @param phoneNumber
     * @return return true if ###-###-####
     */
    public boolean isPhoneNumberValid(String phoneNumber){
        //Make sure that the phone number as a length of 12 (###-###-####)
        if(phoneNumber.length() != 12)
            return false;


        char[] phone = new char[12];
        phoneNumber.getChars(0, 12, phone, 0);

        //Validate the phone number has a format of ###-###-####
        for(int i =0;i<10;i++) {
            int a = (int)phone[i];
            if(i != 3 && i != 7) {
                if((a - '0')>9 || (a - '0')<0)
                    return false;
            }
            else {
                if((a - '-')!=0)
                    return false;
            }
        }

        return true;
    }

    /**
     * Validate that hourlyRate is a double
     * @param hourlyRate
     * @return
     */
    public boolean isHourlyRateValid(String hourlyRate){
        //String is empty
        if(hourlyRate.isEmpty())
            return false;
        double d=-1;
        //Will try to parse the string into double
        try {
            d = Double.parseDouble(hourlyRate);
        }
        catch(Exception ex) {
            return false;
        }
        //If the value is smaller than 0
        return !(d < 0);
    }

    /**
     * Validate the service name
     * @param serviceName
     * @return
     */
    public boolean isServiceNameValid(String serviceName){
        return !serviceName.isEmpty();
    }

    public boolean isCompanyNameValid(String companyName){
        return !companyName.isEmpty() && !companyName.toLowerCase().equals("company name");
    }

    public boolean isAddressValid(String address){
        return !address.isEmpty() && !address.toLowerCase().equals("address");
    }
}
