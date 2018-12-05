package fgaud013.hansolo.asapservices;

/**
 * @description : Class service type
 * @Author      : Frédérick Gaudet - 8035208
 */

public class ServiceType{
    private int id;
    private String serviceName;
    private double hourlyRate;

    public ServiceType() {
        this.id = -1;
        this.serviceName = "";
        this.hourlyRate = -1;
    }

    public ServiceType(int id, String serviceName, double hourlyRate) {
        this.id = id;
        this.serviceName = serviceName;
        this.hourlyRate = hourlyRate;
    }

    public ServiceType(ServiceType st){
        this.id = new Integer(st.getSTID());
        this.serviceName = new String(st.getServiceName());
        this.hourlyRate = new Double(st.getHourlyRate());
    }

    public int getSTID() {
        return id;
    }

    public void setSTID(int id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }
}
