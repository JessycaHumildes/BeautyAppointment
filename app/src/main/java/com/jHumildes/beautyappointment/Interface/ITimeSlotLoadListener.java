package com.jHumildes.beautyappointment.Interface;

import com.jHumildes.beautyappointment.Model.CalendarTimeSlot;

import java.util.List;

public interface ITimeSlotLoadListener {
    void onTSlotLoadSuccess (List<CalendarTimeSlot> calendarTimeSlotList);
    void onTSlotLoadFailed(String message);
    void onTSlotLoadEmpty();
}
