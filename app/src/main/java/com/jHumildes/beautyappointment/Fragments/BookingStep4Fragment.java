package com.jHumildes.beautyappointment.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.beautyappointment.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.protobuf.StringValue;
import com.jHumildes.beautyappointment.Common.Common;
import com.jHumildes.beautyappointment.Model.InformationBooking;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class BookingStep4Fragment extends Fragment {

    SimpleDateFormat simpleDateFormat;
    LocalBroadcastManager localBroadcastManager;
    Unbinder unbinder;

    @BindView(R.id.txt_salon_website)
    TextView txt_salon_website;
    @BindView(R.id.booking_professional_text)
    TextView booking_professional_text;
    @BindView(R.id.txt_booking_time_text)
    TextView txt_booking_time_text;
    @BindView(R.id.txt_salon_address)
    TextView txt_salon_address;
    @BindView(R.id.txt_Service_name)
    TextView txt_Service_name;
    @BindView(R.id.txt_salon_open_hours)
    TextView txt_salon_open_hours;
    @BindView(R.id.txt_salon_phone)
    TextView txt_salon_phone;

    @OnClick(R.id.btn_confirm)
    void confirmBtn(){

        //create booking Information
        InformationBooking informationBooking = new InformationBooking();

        informationBooking.setProfessionalId(Common.currentProfessional.getProfessionalId());
        informationBooking.setProfessionalName(Common.currentProfessional.getName());
        informationBooking.setCustomerName(Common.currentUser.getPhoneNumber());
        informationBooking.setCustomerPhone(Common.currentUser.getPhoneNumber());
        informationBooking.setServiceAddres(Common.currentService.getAddress());
        informationBooking.setTime(new StringBuilder(Common.convertTSlotToString(Common.currentTSlot))
                .append(" at ")
                .append(simpleDateFormat.format(Common.currentDay.getTime())).toString());
        informationBooking.setSlot(Long.valueOf(Common.currentTSlot));

        //submit to professional
        DocumentReference bookingDay =   FirebaseFirestore.getInstance()
                .collection("AllServices")
                .document(Common.procedures1)
                .collection("Procedures")
                .document(Common.currentService.getServiceId())
                .collection("Professional")
                .document(Common.currentProfessional.getProfessionalId())
                .collection(Common.simpleFormatDay.format(Common.currentDay.getTime()))
        .document(String.valueOf(Common.currentTSlot));

        //write data
        bookingDay.set(informationBooking)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        resetStaticData();
                        getActivity().finish();//close activity
                        Toast.makeText(getContext(), "WellDone!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resetStaticData() {
        Common.fase = 0;
        Common.currentTSlot = -1;
        Common.currentService = null;
        Common.currentProfessional = null;
        Common.currentDay.add(Calendar.DATE,0);//current date added
    }


    BroadcastReceiver confirmBookingReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setData();
        }
    };

    private void setData() {
        booking_professional_text.setText(Common.currentProfessional.getName());
        txt_booking_time_text.setText(new StringBuilder(Common.convertTSlotToString(Common.currentTSlot))
        .append(" at ")
        .append(simpleDateFormat.format(Common.currentDay.getTime())));

        txt_salon_address.setText(Common.currentService.getAddress());
        txt_salon_open_hours.setText(Common.currentService.getOpenHours());
        txt_salon_website.setText(Common.currentService.getWebsite());
        txt_Service_name.setText(Common.currentService.getName());



    }

    static BookingStep4Fragment instance;
    public static BookingStep4Fragment getInstance(){
        if (instance == null)
            instance = new BookingStep4Fragment();
        return instance;

    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //applying the way date will be displayed on confirming appt
        simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy");

        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(confirmBookingReceiver,new IntentFilter(Common.KEY_CONFIRMATION_BOOKING));
    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(confirmBookingReceiver);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_booking_step_four,container,false);
        unbinder = ButterKnife.bind(this,view);

        return view;
    }
}
