package com.jHumildes.beautyappointment.Model;

public class InformationBooking {
    private String customerName, customerPhone, professionalId,time, professionalName, serviceAddres;
    private Long slot;

    public InformationBooking() {
    }

    public InformationBooking(String customerName, String customerPhone, String professionalId, String time, String professionalName, String serviceAddres, Long slot) {
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.professionalId = professionalId;
        this.time = time;
        this.professionalName = professionalName;
        this.serviceAddres = serviceAddres;
        this.slot = slot;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getProfessionalId() {
        return professionalId;
    }

    public void setProfessionalId(String professionalId) {
        this.professionalId = professionalId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getProfessionalName() {
        return professionalName;
    }

    public void setProfessionalName(String professionalName) {
        this.professionalName = professionalName;
    }

    public String getServiceAddres() {
        return serviceAddres;
    }

    public void setServiceAddres(String serviceAddres) {
        this.serviceAddres = serviceAddres;
    }

    public Long getSlot() {
        return slot;
    }

    public void setSlot(Long slot) {
        this.slot = slot;
    }
}
