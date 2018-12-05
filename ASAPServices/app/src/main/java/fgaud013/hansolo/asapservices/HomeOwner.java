package fgaud013.hansolo.asapservices;

public class HomeOwner extends UserAccount {
    private int homeID;
    private String firstName, lastName, dateOfBirth;

    /**
     * Empty constructor
     */
    public HomeOwner(){
        super();
        this.homeID = -1;
        this.firstName = "";
        this.lastName = "";
        this.dateOfBirth = "";
    }

    /**
     * Basic constructor
     * @param userName
     * @param userID
     * @param password
     * @param address
     * @param phoneNumber
     * @param permLevel
     * @param homeID
     * @param firstName
     * @param lastName
     */
    public HomeOwner(String userName,
                     int userID,
                     String password,
                     String address,
                     String phoneNumber,
                     int permLevel,
                     int homeID,
                     String firstName,
                     String lastName,
                     String dateOfBirth){
        super(userName, userID, password, address, phoneNumber, permLevel);
        this.homeID = homeID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Constructor taking himself
     * @param homeOwner
     */
    public HomeOwner(HomeOwner homeOwner){
        this.setUserAccount(homeOwner.getUserAccount());
        this.homeID = homeOwner.getHomeID();
        this.firstName = homeOwner.getFirstName();
        this.lastName = homeOwner.getLastName();
        this.dateOfBirth = homeOwner.getDateOfBirth();
    }

    /**
     * Regular constructor takin UserAccount
     * @param ua
     * @param homeID
     * @param firstName
     * @param lastName
     */
    public HomeOwner(UserAccount ua,
                     int homeID,
                     String firstName,
                     String lastName,
                     String dateOfBirth){
        super(ua);
        this.homeID = homeID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Empty constructor taking a UserAccount
     * @param ua
     */
    public HomeOwner(UserAccount ua){
        super(ua);
        this.homeID = -1;
        this.firstName = "";
        this.lastName = "";
        this.dateOfBirth = "";
    }

    public int getHomeID() {
        return homeID;
    }

    public void setHomeID(int homeID) {
        this.homeID = homeID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String toString(){
        String tmp = super.toString();
        tmp = tmp +
                "\n" + this.homeID +
                "\n" + this.firstName +
                "\n" + this.lastName +
                "\n" + this.dateOfBirth;
        return tmp;
    }
}
