package com.jHumildes.beautyappointment.Interface;

import com.jHumildes.beautyappointment.Model.Services;

import java.util.List;

public interface IProceduresLoadListener {

    void onProceduresLoadSuccess(List<Services> procedNameList);
    void onProceduresLoadFailed(String message);
}
