package com.jHumildes.beautyappointment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.beautyappointment.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jHumildes.beautyappointment.Adapter.MyViewPagerAdapter;
import com.jHumildes.beautyappointment.Common.Common;
import com.jHumildes.beautyappointment.Common.NonSwipeViewPager;
import com.jHumildes.beautyappointment.Model.Professional;
import com.shuhart.stepview.StepView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;

public class BookingActivity extends AppCompatActivity {

    LocalBroadcastManager localBroadcastManager;
    AlertDialog dialog;
    CollectionReference professionalRef;

    @BindView(R.id.step_view)
    StepView stepView;
    @BindView(R.id.view_pager)
    NonSwipeViewPager viewPager;
    @BindView(R.id.btn_previous_step)
    Button btn_previous_step;
    @BindView(R.id.btn_next_step)
    Button btn_next_step;

    //event
    @OnClick(R.id.btn_previous_step)
    void previousFase(){
        if (Common.fase == 3 || Common.fase > 0 )
        {
            Common.fase--;
            viewPager.setCurrentItem(Common.fase);
        }
    }
    @OnClick(R.id.btn_next_step)
    void nextClick(){

        //Toast.makeText(this,""+Common.currentService.getServiceId(),Toast.LENGTH_SHORT).show();
        if (Common.fase < 3 || Common.fase == 0)
        {
            Common.fase++;//increase
            if (Common.fase ==1)//after choosing service
            {
                if (Common.currentService !=null)
                    loadProfessionalByService(Common.currentService.getServiceId());
            }
            else if ( Common.fase == 2 )//pick time slot
            {
                if (Common.currentProfessional != null)
                    loadTimeSlotOfProfessional(Common.currentProfessional.getProfessionalId());
            }
            else if ( Common.fase == 3 )//Confirm booking
            {
                if (Common.currentTSlot != -1)
                    confirmBooking();
            }
            viewPager.setCurrentItem(Common.fase);
        }
    }

    private void confirmBooking() {
        //send Broadcast to fragment fase 4
        Intent intent = new Intent(Common.KEY_CONFIRMATION_BOOKING);
        localBroadcastManager.sendBroadcast(intent);

    }

    private void loadTimeSlotOfProfessional(String professionalId) {
        //send local Broadcast to fragment fase 3
        Intent intent = new Intent(Common.TIME_SLOT_KEY);
        localBroadcastManager.sendBroadcast(intent);
    }

    private void loadProfessionalByService(String serviceId) {

        dialog.show();
        //select all professionals of services
        ///AllServices/FaceCare/Procedures/6cdfTqHbf2z4PrI6tjYg/Professional
        if (!TextUtils.isEmpty(Common.procedures1))
        {
            professionalRef = FirebaseFirestore.getInstance()
                    .collection("AllServices")
                    .document(Common.procedures1)
                    .collection("Procedures")
                    .document(serviceId)
                    .collection("Professional");

            professionalRef.get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            ArrayList<Professional> professionals = new ArrayList<>();
                            for (QueryDocumentSnapshot professionalSnapshot:task.getResult())
                            {
                                Professional professional = professionalSnapshot.toObject(Professional.class);
                                professional.setPassword("");//remove password because in client app
                                professional.setProfessionalId(professionalSnapshot.getId());//get Id Of Professional

                                professionals.add(professional);
                            }
                            //send Broadcast to BookingStep2 to load Recycler
                            Intent intent = new Intent(Common.PROFESSIONAL_LOAD_DONE_KEY);
                            intent.putParcelableArrayListExtra(Common.PROFESSIONAL_LOAD_DONE_KEY,professionals);
                            localBroadcastManager.sendBroadcast(intent);

                            dialog.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                }
            });

        }

    }

    //Broadcast Receiver
    private BroadcastReceiver buttonNextReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int fase = intent.getIntExtra(Common.KEY_FASE,0);
            if (fase == 1)
                Common.currentService = intent.getParcelableExtra(Common.SERVICES_STORE_KEY);
            else if (fase ==2)
                Common.currentProfessional = intent.getParcelableExtra(Common.KEY_PROFESSIONAL_SELECTED);

            btn_next_step.setEnabled(true);
            setColorButton();
        }
    };

    @Override
    protected void onDestroy() {
        localBroadcastManager.unregisterReceiver(buttonNextReceiver );
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        ButterKnife.bind(BookingActivity.this);

        dialog = new SpotsDialog.Builder().setContext(this).build();

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(buttonNextReceiver,new IntentFilter(Common.ENABLE_NEXT_BUTTON_KEY));

        setupStepView();
        setColorButton();

        //Viewww

        viewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(4);//I have 7 screen Page
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                //show fases
                stepView.go(position,true);
                if (position == 0)

                    btn_previous_step.setEnabled(false);
                else
                    btn_previous_step.setEnabled(true);

                //set disable button next
                btn_next_step.setEnabled(false  );
                setColorButton();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void setColorButton() {
        if (btn_next_step.isEnabled())
        {
            btn_next_step.setBackgroundResource(R.color.colorBlue);
        }
        else
        {
            btn_next_step.setBackgroundResource(android.R.color.darker_gray);
        }
        if (btn_previous_step.isEnabled())
        {
            btn_previous_step.setBackgroundResource(R.color.colorBlue);
        }
        else
        {
            btn_previous_step.setBackgroundResource(android.R.color.darker_gray);
        }
    }

    private void setupStepView() {

        List<String> stepList = new ArrayList<>();
        stepList.add("Services");
        stepList.add("Procedure");
        stepList.add("Time");
        stepList.add("Confirm");
        stepView.setSteps(stepList);
    }
}
