package com.jHumildes.beautyappointment.Common;


import com.jHumildes.beautyappointment.Model.Services;
import com.jHumildes.beautyappointment.Model.User;

public class Common {


    public static final String IS_LOGIN = "IsLogin";
    public static final String ENABLE_NEXT_BUTTON_KEY ="ENABLE_BUTTON_NEXT" ;
    public static final String SERVICES_STORE_KEY ="SERVICE_SAVE" ;
    public static final String PROFESSIONAL_LOAD_DONE_KEY =  "PROFESSIONAL_LOAD_DONE";
    public static User currentUser;
    public static Services currentService;
    public static int fase = 0; //init first fase

    public static String treatment="";
}
