package fgaud013.hansolo.asapservices;

public class Day {
    private int dayID;
    private String day;

    public Day() {
        this.day = "";
        this.dayID = -1;
    }

    public Day(int dayID, String day) {
        this.dayID = dayID;
        this.day = day;
    }

    public int getDayID() {
        return dayID;
    }

    public void setDayID(int dayID) {
        this.dayID = dayID;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
