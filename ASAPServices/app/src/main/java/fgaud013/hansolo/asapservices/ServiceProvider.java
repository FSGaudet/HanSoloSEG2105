package fgaud013.hansolo.asapservices;


/**
 * @description : Class for service provider, extended of UserAccount
 * @Author      : Frédérick Gaudet - 8035208
 */


public class ServiceProvider extends UserAccount {

    //Service type is a string for the moment, but could be implement as class
    private ServiceType serviceType;
    private int serviceProviderID;

    private double hourlyRate;

    /**
     * Default Constructor
     */
    public ServiceProvider(){
        super();
        this.serviceType = new ServiceType();
        this.serviceProviderID = -1;
        this.hourlyRate = -1;
    }

    /**
     * Full constructor
     * @param userName String
     * @param userID String
     * @param password String
     * @param serviceType String/serviceType
     * @param hourlyRate HourlyRate
     */
    public ServiceProvider(String userName,
                           int userID,
                           String password,
                           String firstName,
                           String lastName,
                           String dateOfbirth,
                           String phoneNumber,
                           int permLevel,
                           int serviceProviderID,
                           ServiceType serviceType,
                           double hourlyRate){
        super(userName, userID, password, firstName, lastName, dateOfbirth, phoneNumber, permLevel);
        this.serviceType = serviceType;
        this.serviceProviderID = serviceProviderID;
        this.hourlyRate = hourlyRate;

    }

    /**
     * Constructor that take himself (serviceProvider) as param
     * @param sp ServiceProvider
     */
    public ServiceProvider(ServiceProvider sp){
        super(sp);
        this.serviceType = sp.getServiceType();
        this.hourlyRate = sp.getHourlyRate();
        this.serviceProviderID = sp.getServiceProviderID();
    }

    /**
     * Constructor userAccount
     * @param userAccount
     * @param serviceProviderID
     * @param serviceType
     * @param hourlyRate
     */
    public ServiceProvider(UserAccount userAccount,
                           int serviceProviderID,
                           ServiceType serviceType,
                           double hourlyRate ){
        super(userAccount);
        this.serviceType = serviceType;
        this.serviceProviderID = serviceProviderID;
        this.hourlyRate = hourlyRate;
    }

    /**
     * Constructor userAccount
     * @param userAccount
     */
    public ServiceProvider(UserAccount userAccount){
        super(userAccount);
        this.serviceProviderID = -1;
        this.serviceType = new ServiceType();
        this.hourlyRate = -1;
    }

    public void setUserAccount(UserAccount ua){
        this.setUserName(ua.getUserName());
        this.setPassword(ua.getPassword());
        this.setUserID(ua.getUserID());
        this.setFirstName(ua.getFirstName());
        this.setLastName(getLastName());
        this.setDateOfBirth(getDateOfBirth());
        this.setPhoneNumber(getPhoneNumber());
        this.setPermLevel(ua.getPermLevel());
    }

    /**
     * ========================================== GETTERS ==========================================
     */

    public int getServiceProviderID() {
        return serviceProviderID;
    }

    /**
     * Return the service name
     * @return String
     */


    public ServiceType getServiceType() {
        return serviceType;
    }

    /**
     * Return the hourly rate
     * @return double
     */
    public double getHourlyRate(){
        return(this.hourlyRate);
    }




    /**
     * ========================================== SETTERS ==========================================
     */

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public void setHourlyRate(double hourlyRate){
        this.hourlyRate = hourlyRate;
    }

    public void setServiceProviderID(int serviceProviderID) {
        this.serviceProviderID = serviceProviderID;
    }

    /**
     * Return a string of all the component
     * @return
     */
    public String toString(){
        String chain;
        chain = this.getUserID() +"\n" +
                this.getServiceProviderID() + "\n" +
                this.getUserName() + "\n" +
                this.getPassword() + "\n" +
                this.getFirstName() + "\n" +
                this.getLastName() + "\n" +
                this.getDateOfBirth() + "\n" +
                this.getPhoneNumber() + "\n" +
                this.getServiceType().getSTID() + "\n" +
                this.getServiceType().getServiceName() + "\n" +
                this.getHourlyRate();

        return(chain);
    }
}


