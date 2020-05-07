package com.jHumildes.beautyappointment.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beautyappointment.R;
import com.jHumildes.beautyappointment.Adapter.MyProfessionalAdapter;
import com.jHumildes.beautyappointment.Common.Common;
import com.jHumildes.beautyappointment.Common.SpacesItemDecoration;
import com.jHumildes.beautyappointment.Model.Professional;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BookingStep2Fragment extends Fragment {

    Unbinder unbinder;
    LocalBroadcastManager localBroadcastManager;

    @BindView(R.id.recycler_professional)
    RecyclerView recycler_professional;

    private BroadcastReceiver professionalDoneReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<Professional> professionalArrayList = intent.getParcelableArrayListExtra(Common.PROFESSIONAL_LOAD_DONE_KEY);
            //create adapter later
            MyProfessionalAdapter adapter = new MyProfessionalAdapter(getContext(),professionalArrayList);
            recycler_professional.setAdapter(adapter);

        }
    };

    static BookingStep2Fragment instance;
    public static BookingStep2Fragment getInstance(){
        if (instance == null)
            instance = new BookingStep2Fragment();
        return instance;

    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(professionalDoneReceiver,new IntentFilter(Common.PROFESSIONAL_LOAD_DONE_KEY));
    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(professionalDoneReceiver);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_booking_step_two,container,false);

        unbinder = ButterKnife.bind(this,view);

        initView();

        

        return view;
    }

    private void initView() {
        recycler_professional.setHasFixedSize(true);
        recycler_professional.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recycler_professional.addItemDecoration(new SpacesItemDecoration(4));
    }

}
