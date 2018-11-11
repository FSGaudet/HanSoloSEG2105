package fgaud013.hansolo.asapservices;

/**
 * @description : Class service type
 * @Author      : Frédérick Gaudet - 8035208
 */

public class ServiceType{
    private int id;
    private String serviceName;

    public ServiceType() {
        this.id = -1;
        this. serviceName = "";
    }

    public ServiceType(int id, String serviceName) {
        this.id = id;
        this.serviceName = serviceName;
    }

    public ServiceType(ServiceType st){
        this.id = new Integer(st.getSTID());
        this.serviceName = new String(st.getServiceName());
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
}
