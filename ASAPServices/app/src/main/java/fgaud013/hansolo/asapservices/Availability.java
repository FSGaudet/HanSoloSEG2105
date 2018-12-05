package fgaud013.hansolo.asapservices;

public class Availability {
    private String startDate, endDate;
    private int dayID,availabilityID;

    public Availability(){
        this.startDate = "";
        this.endDate = "";
        this.dayID = -1;
        this.availabilityID = -1;
    }

    public Availability(String startDate, String endDate, int dayID, int availabilityID) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.dayID = dayID;
        this.availabilityID = availabilityID;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getDayID() {
        return dayID;
    }

    public void setDayID(int dayID) {
        this.dayID = dayID;
    }

    public int getAvailabilityID() {
        return availabilityID;
    }

    public void setAvailabilityID(int availabilityID) {
        this.availabilityID = availabilityID;
    }

    public int getStartHour(){
        String[] chain = startDate.split(":");
        return(Integer.parseInt(chain[0]));
    }

    public int getEndHour(){
        String[] chain = endDate.split(":");
        return(Integer.parseInt(chain[0]));
    }

    public int getStartMinute(){
        String[] chain = startDate.split(":");
        return(Integer.parseInt(chain[1]));
    }

    public int getEndTimeMinute(){
        String[] chain = endDate.split(":");
        return(Integer.parseInt(chain[1]));
    }
}
