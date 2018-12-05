package fgaud013.hansolo.asapservices;


import java.util.ArrayList;

/**
 * @description : Class for service provider, extended of UserAccount
 * @Author      : Frédérick Gaudet - 8035208
 */


public class ServiceProvider extends UserAccount {

    //Service type is a string for the moment, but could be implement as class
    private ArrayList<ServiceType> serviceType = new ArrayList<ServiceType>();
    private int serviceProviderID;
    private String companyName, description;
    private boolean licensed;

    /**
     * Default Constructor
     */
    public ServiceProvider(){
        super();
        this.serviceType = new ArrayList<ServiceType>();
        this.serviceProviderID = -1;
        this.companyName = "";
        this.description = "";
        this.licensed = false;
    }


    public ServiceProvider(String userName,
                           int userID,
                           String password,
                           String address,
                           String phoneNumber,
                           int permLevel,
                           int serviceProviderID,
                           ArrayList<ServiceType> serviceType,
                           String companyName,
                           String description,
                           boolean licensed
    ){
        super(userName, userID, password, address, phoneNumber, permLevel);
        this.serviceType = serviceType;
        this.serviceProviderID = serviceProviderID;
        this.companyName = companyName;
        this.description = description;
        this.licensed = licensed;
    }

    /**
     * Constructor that take himself (serviceProvider) as param
     * @param sp ServiceProvider
     */
    public ServiceProvider(ServiceProvider sp){
        super(sp);
        this.serviceType = sp.getServiceType();
        this.serviceProviderID = sp.getServiceProviderID();
        this.companyName = sp.getCompanyName();
        this.description = sp.getDescription();
        this.licensed = sp.isLicensed();
    }

    /**
     * Useraccount with the rest
     * @param userAccount
     * @param serviceProviderID
     * @param serviceType
     * @param companyName
     * @param description
     * @param licensed
     */
    public ServiceProvider(UserAccount userAccount,
                           int serviceProviderID,
                           ArrayList<ServiceType> serviceType,
                           String companyName,
                           String description,
                           boolean licensed
                           ){
        super(userAccount);
        this.serviceType = serviceType;
        this.serviceProviderID = serviceProviderID;
        this.companyName = companyName;
        this.description = description;
        this.licensed = licensed;
    }

    /**
     * Constructor userAccount
     * @param userAccount
     */
    public ServiceProvider(UserAccount userAccount){
        super(userAccount);
        this.serviceProviderID = -1;
        this.serviceType = new ArrayList<ServiceType>();
        this.companyName = "";
        this.description = "";
        this.licensed = false;
    }



    /**
     * ===================================== GETTERS/SETTERS =====================================
     */
    public ArrayList<ServiceType> getServiceType() {
        return serviceType;
    }

    public void setServiceType(ArrayList<ServiceType> serviceType) {
        this.serviceType = serviceType;
    }

    public int getServiceProviderID() {
        return serviceProviderID;
    }

    public void setServiceProviderID(int serviceProviderID) {
        this.serviceProviderID = serviceProviderID;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isLicensed() {
        return licensed;
    }

    public void setLicensed(boolean licensed) {
        this.licensed = licensed;
    }

    public void addServiceType(ServiceType serviceType){
        this.serviceType.add(serviceType);
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
                this.getAddress() + "\n" +
                this.getPhoneNumber() + "\n" +
                this.getCompanyName() + "\n" +
                this.isLicensed() + "\n" +
                this.getDescription();

        return(chain);
    }
}


