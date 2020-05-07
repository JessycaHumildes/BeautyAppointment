package com.jHumildes.beautyappointment.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Services implements Parcelable {

    private String name, serviceId;

    public Services() {

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public static Creator<Services> getCREATOR() {
        return CREATOR;
    }


    protected Services(Parcel in) {
        name = in.readString();
        serviceId = in.readString();
    }

    public static final Creator<Services> CREATOR = new Creator<Services>() {
        @Override
        public Services createFromParcel(Parcel in) {
            return new Services(in);
        }

        @Override
        public Services[] newArray(int size) {
            return new Services[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(serviceId);
    }
}
