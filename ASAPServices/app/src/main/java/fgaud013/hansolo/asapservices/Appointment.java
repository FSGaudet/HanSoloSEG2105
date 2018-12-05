package fgaud013.hansolo.asapservices;

public class Appointment {
    private int appointment_id, home_id, sp_id, day_id, serviceType_id;
    private String start_time, end_time;
    private Rating rating;
    private boolean completed;

    public Appointment() {
        appointment_id = -1;
        home_id = -1;
        sp_id = -1;
        day_id = -1;
        start_time = "";
        end_time = "";
        rating = new Rating();
        completed = false;
    }

    public Appointment(int Appointment_id,
                        int home_id,
                        int sp_id,
                        int day_id,
                        int serviceType_id,
                        String start_time,
                        String end_time,
                        Rating rating,
                        boolean completed) {
        this.appointment_id = Appointment_id;
        this.home_id = home_id;
        this.sp_id = sp_id;
        this.day_id = day_id;
        this.serviceType_id = serviceType_id;
        this.start_time = start_time;
        this.end_time = end_time;
        this.rating = rating;
        this.completed = completed;
    }

    public Appointment(Appointment appointment){
        this.appointment_id = appointment.getAppointment_id();
        this.home_id = appointment.getHome_id();
        this.sp_id = appointment.getSp_id();
        this.day_id = appointment.getDay_id();
        this.serviceType_id = appointment.getServiceType_id();
        this.start_time = appointment.getStart_time();
        this.end_time = appointment.getEnd_time();
        this.rating = appointment.getRating();
        this.completed = appointment.isCompleted();
    }

    public int getAppointment_id() {
        return appointment_id;
    }

    public void setAppointment_id(int Appointment_id) {
        this.appointment_id = Appointment_id;
    }

    public int getHome_id() {
        return home_id;
    }

    public void setHome_id(int home_id) {
        this.home_id = home_id;
    }

    public int getSp_id() {
        return sp_id;
    }

    public void setSp_id(int sp_id) {
        this.sp_id = sp_id;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public int getDay_id() {
        return day_id;
    }

    public void setDay_id(int day_id) {
        this.day_id = day_id;
    }

    public int getServiceType_id() {
        return serviceType_id;
    }

    public void setServiceType_id(int serviceType_id) {
        this.serviceType_id = serviceType_id;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public int getStartHour(){
        String[] chain = start_time.split(":");
        return(Integer.parseInt(chain[0]));
    }

    public int getEndHour(){
        String[] chain = end_time.split(":");
        return(Integer.parseInt(chain[0]));
    }

    public int getStartMinute(){
        String[] chain = start_time.split(":");
        return(Integer.parseInt(chain[1]));
    }

    public int getEndTimeMinute(){
        String[] chain = end_time.split(":");
        return(Integer.parseInt(chain[1]));
    }

}
