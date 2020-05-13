package com.jHumildes.beautyappointment.Fragments;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beautyappointment.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jHumildes.beautyappointment.Adapter.MyTSlotAdapter;
import com.jHumildes.beautyappointment.Common.Common;
import com.jHumildes.beautyappointment.Common.SpacesItemDecoration;
import com.jHumildes.beautyappointment.Interface.ITimeSlotLoadListener;
import com.jHumildes.beautyappointment.Model.CalendarTimeSlot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;
import dmax.dialog.SpotsDialog;

public class BookingStep3Fragment extends Fragment implements ITimeSlotLoadListener {

    //variable
    DocumentReference documentReference;
    ITimeSlotLoadListener iTimeSlotLoadListener;
    AlertDialog dialog;

    Unbinder unbinder;
    LocalBroadcastManager localBroadcastManager;


    @BindView(R.id.recycler_timeslot)
    RecyclerView recycler_timeslot;
    @BindView(R.id.calendar_v)
    HorizontalCalendarView calendar_v;
    SimpleDateFormat simpleDateFormat;

    BroadcastReceiver displayTSlot = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Calendar day = Calendar.getInstance();
                    day.add(Calendar.DATE,0);//Add current date
            loadAvailableTSlotOfProfessional(Common.currentProfessional.getProfessionalId(),
            simpleDateFormat.format(day.getTime()));
        }
    };

    private void loadAvailableTSlotOfProfessional(String professionalId,final String bookday) {
        dialog.show();

        ///AllServices/HairRemoval/Procedures/OhXuhxBe926ceq2rAQ0x/Professionals/wxJ65xCz1nHmbObc0rWF
        documentReference = FirebaseFirestore.getInstance()
                .collection("AllServices")
                .document(Common.procedures1)
                .collection("Procedures")
                .document(Common.currentService.getServiceId())
                .collection("Professional")
                .document(Common.currentProfessional.getProfessionalId());

        //get info of this professional
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful())//is professional available?
                {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists())
                    {
                        //GET INF OF BOOKING
                        CollectionReference date =FirebaseFirestore.getInstance()
                                .collection("AllServices")
                                .document(Common.procedures1)
                                .collection("Procedures")
                                .document(Common.currentService.getServiceId())
                                .collection("Professional")
                                .document(Common.currentProfessional.getProfessionalId())
                                .collection(bookday);

                        date.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful())
                                {
                                    QuerySnapshot querySnapshot = task.getResult();
                                    if (querySnapshot.isEmpty())//if there is no appointment
                                        iTimeSlotLoadListener.onTSlotLoadEmpty();
                                    else {
                                        //if exist appointment
                                        List<CalendarTimeSlot> timeSlots = new ArrayList<>();
                                        for (QueryDocumentSnapshot documentSnapshot1:task.getResult())
                                            timeSlots.add(documentSnapshot1.toObject(CalendarTimeSlot.class));
                                        iTimeSlotLoadListener.onTSlotLoadSuccess(timeSlots);
                                    }
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                iTimeSlotLoadListener.onTSlotLoadFailed(e.getMessage());
                            }
                        });
                    }
                }
            }
        });
    }

    static BookingStep3Fragment instance;
    public static BookingStep3Fragment getInstance(){
        if (instance == null)
            instance = new BookingStep3Fragment();
        return instance;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        iTimeSlotLoadListener = this;

        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(displayTSlot,new IntentFilter(Common.TIME_SLOT_KEY));

        simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy");//14/08/2020

        dialog = new SpotsDialog.Builder().setContext(getContext()).setCancelable(false).build();

    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(displayTSlot);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

       View view = inflater.inflate(R.layout.fragment_booking_step_three,container,false);
       unbinder = ButterKnife.bind(this, view);

       init(view);
       

       return view;
    }

    private void init(View view) {
        recycler_timeslot.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),3);
        recycler_timeslot.setLayoutManager(gridLayoutManager);
        recycler_timeslot.addItemDecoration(new SpacesItemDecoration(8));

        //Calendar
        Calendar startDay = Calendar.getInstance();
        startDay.add(Calendar.DATE,0);
        Calendar endDay = Calendar.getInstance();
        endDay.add(Calendar.DATE,2);//2 days left

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(view,R.id.calendar_v)
                .range(startDay,endDay)
                .datesNumberOnScreen(1)
                .mode(HorizontalCalendar.Mode.DAYS)
                .defaultSelectedDate(startDay)
                .build();
        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                if (Common.currentDay.getTimeInMillis() != date.getTimeInMillis())
                {
                    Common.currentDay = date;//this code will not load again if selected new day same with day selected
                    loadAvailableTSlotOfProfessional(Common.currentProfessional.getProfessionalId(),
                            simpleDateFormat.format(date.getTime()));
                }
            }
        });
    }

    @Override
    public void onTSlotLoadSuccess(List<CalendarTimeSlot> calendarTimeSlotList) {

        MyTSlotAdapter adapter = new MyTSlotAdapter(getContext(),calendarTimeSlotList);
        recycler_timeslot.setAdapter(adapter);

        dialog.dismiss();

    }

    @Override
    public void onTSlotLoadFailed(String message) {
        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
        dialog.dismiss();

    }

    @Override
    public void onTSlotLoadEmpty() {
        MyTSlotAdapter adapter = new MyTSlotAdapter(getContext());
        recycler_timeslot.setAdapter(adapter);

        dialog.dismiss();

    }
}
