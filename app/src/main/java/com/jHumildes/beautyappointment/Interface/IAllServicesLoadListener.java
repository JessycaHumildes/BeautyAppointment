package com.jHumildes.beautyappointment.Interface;

import java.util.List;

public interface IAllServicesLoadListener {

    void onAllServicesLoadSuccess(List<String> servNameList);
    void onAllServicesLoadFailed(String message);
}
