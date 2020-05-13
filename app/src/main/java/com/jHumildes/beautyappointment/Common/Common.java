package com.jHumildes.beautyappointment.Common;


import com.jHumildes.beautyappointment.Model.CalendarTimeSlot;
import com.jHumildes.beautyappointment.Model.Professional;
import com.jHumildes.beautyappointment.Model.Services;
import com.jHumildes.beautyappointment.Model.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class  Common {


    public static final String IS_LOGIN = "IsLogin";
    public static final String ENABLE_NEXT_BUTTON_KEY = "ENABLE_BUTTON_NEXT";
    public static final String SERVICES_STORE_KEY = "SERVICE_SAVE";
    public static final String PROFESSIONAL_LOAD_DONE_KEY = "PROFESSIONAL_LOAD_DONE";
    public static final String TIME_SLOT_KEY = "DISPLAY_TIMESLT";
    public static final String KEY_FASE = "FASE";
    public static final String KEY_PROFESSIONAL_SELECTED = "PROFESSIONAL_SELECTED";
    public static final int CALENDAR_TSLOT_TOTAL = 20;
    public static final Object DISABLE_TAG = "DISABLE";
    public static final String KEY_TIME_SLOT ="TIME_SLOT" ;
    public static final String KEY_CONFIRMATION_BOOKING = "CONFIRM_BOOKING";
    public static User currentUser;
    public static Services currentService;
    public static int fase = 0; //init first fase

    public static String procedures1 = "";
    public static Professional currentProfessional;
    public static int currentTSlot= -1;
    public static Calendar currentDay = Calendar.getInstance();
    public static SimpleDateFormat simpleFormatDay = new SimpleDateFormat("dd_MM_yyyy");//only use when need format key


    public static String convertTSlotToString(int slot) {
        switch (slot) {
            case 0:
                return "9:00 - 9:30";
            case 1:
                return "9:30 - 10:00";
            case 2:
                return "10:00 - 10:30";
            case 3:
                return "10:30 - 11:00";
            case 4:
                return "11:00 - 11:30";
            case 5:
                return "11:30 - 12:00";
            case 6:
                return "12:00 - 12:30";
            case 7:
                return "12:30 - 13:00";
            case 8:
                return "13:00 - 13:30";
            case 9:
                return "13:30 - 14:00";
            case 10:
                return "14:00 - 14:30";
            case 11:
                return "14:30 - 15:00";
            case 12:
                return "15:00 - 15:30";
            case 13:
                return "15:30 - 16:00";
            case 14:
                return "16:00 - 16:30";
            case 15:
                return "16:30 - 17:00";
            case 16:
                return "17:00 - 17:30";
            case 17:
                return "17:30 - 18:00";
            case 18:
                return "18:00 - 18:30";
            case 19:
                return "18:30 - 19:00";
            default:
                return "Closed";
        }
    }
}
