package fgaud013.hansolo.asapservices;

public class Rating {
    private int rating_id, appointment, rating;
    private String comment;

    public Rating() {
        appointment = -1;
        rating_id = -1;
        rating = -1;
        comment = "";
    }

    public Rating(int rating_id,
                  int Appointment_id,
                  int rating,
                  String comment) {
        this.rating_id = rating_id;
        this.appointment = Appointment_id;
        this.rating = rating;
        this.comment = comment;
    }

    public int getRating_id() {
        return rating_id;
    }

    public void setRating_id(int rating_id) {
        this.rating_id = rating_id;
    }

    public int getAppointment_id() {
        return appointment;
    }

    public void setAppointment_id(int appointment_id) {
        this.appointment = appointment_id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
