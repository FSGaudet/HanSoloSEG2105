package fgaud013.hansolo.asapservices;

/**
 * @description : Class UserAccount
 * @Author      : Frédérick Gaudet - 8035208
 */

public class UserAccount {

    private int userID;
    private String  userName, password, address, phoneNumber;
    private int permLevel;



    /**
     * Default constructor
     */
    public UserAccount(){
        this.userName = "";
        this.password = "";
        this.address = "";
        this.phoneNumber = "";
		this.permLevel = -1;
		this.userID = -1;
    }

    /**
     * Constructor taking Username, ID, userLevel
     * @param userName String
     * @param ID int
     * @param password string
     */
    public UserAccount(String userName,
                       int ID,
                       String password,
                       String address,
					   String phoneNumber,
					   int permLevel){
        this.userName = userName;
        this.userID = ID;
        this.password = password;
        this.address = address;
        this.phoneNumber = phoneNumber;
		this.permLevel = permLevel;
    }

    /**
     * Constructor taking himself as param.
     * @param ua
     */
    public UserAccount(UserAccount ua){
        this.userName = ua.getUserName();
        this.password = ua.getPassword();
        this.userID = ua.getUserID();
        this.address = ua.getAddress();
        this.phoneNumber = ua.getPhoneNumber();
		this.permLevel = ua.getPermLevel();
    }



    /**
     * ========================================== GETTERS ==========================================
     */
    public String getAddress() {
        return address;
    }

    public String getUserName(){
        return this.userName;
    }

    public String getPassword(){
        return this.password;
    }

    public int getUserID(){
        return this.userID;
    }

	public int getPermLevel(){
		return(this.permLevel);
	}

    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * ========================================== SETTERS ==========================================
     * This class is about setter, but theses setter are made from the database
     */


    public void setUserName(String userName){
        this.userName = userName;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setAddress(String address ) {
        this.address = address;
    }

    public void setUserID(int id){
        this.userID = id;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPermLevel(int permLevel){
		this.permLevel = permLevel;
	}
    public UserAccount getUserAccount(){
        UserAccount tmp = new UserAccount(this.getUserName(),
                this.getUserID(),
                this.getPassword(),
                this.getAddress(),
                this.getPhoneNumber(),
                this.getPermLevel());
        return(tmp);
    }

    public void setUserAccount(UserAccount ua){
        this.setUserName(ua.getUserName());
        this.setPassword(ua.getPassword());
        this.setUserID(ua.getUserID());
        this.setAddress(ua.getAddress());
        this.setPhoneNumber(ua.getPhoneNumber());
        this.setPermLevel(ua.getPermLevel());
    }

    /**
     * Return a string of all the component
     * @return
     */
    public String toString(){
        String chain;
        chain = this.getUserID() + "\n" +
                this.getUserName() + "\n" +
                this.getPassword() + "\n" +
                this.getAddress() + "\n" +
                this.getPhoneNumber();

        return(chain);
    }
}
