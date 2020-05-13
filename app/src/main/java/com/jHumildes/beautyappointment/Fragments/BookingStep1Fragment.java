package com.jHumildes.beautyappointment.Fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beautyappointment.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jHumildes.beautyappointment.Adapter.MyServiceAdapter;
import com.jHumildes.beautyappointment.Common.Common;
import com.jHumildes.beautyappointment.Common.SpacesItemDecoration;
import com.jHumildes.beautyappointment.Interface.IAllServicesLoadListener;
import com.jHumildes.beautyappointment.Interface.IProceduresLoadListener;
import com.jHumildes.beautyappointment.Model.Services;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;

public class BookingStep1Fragment extends Fragment implements IAllServicesLoadListener, IProceduresLoadListener {

    //Variable
    CollectionReference allServicesRef;
    CollectionReference proceduresRef;

    IAllServicesLoadListener iAllServicesLoadListener;
    IProceduresLoadListener iProceduresLoadListener;

    @BindView(R.id.spinner)
    MaterialSpinner spinner;
    @BindView(R.id.recycler_services)
    RecyclerView recycler_services;

    Unbinder unbinder;
    AlertDialog dialog;

    static BookingStep1Fragment instance;

    public static BookingStep1Fragment getInstance(){
        if (instance == null)
            instance = new BookingStep1Fragment();
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        allServicesRef = FirebaseFirestore.getInstance().collection("AllServices");
        iAllServicesLoadListener =this;
        iProceduresLoadListener = this;

        dialog = new SpotsDialog.Builder().setContext(getActivity()).build();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

       View itemView = inflater.inflate(R.layout.fragment_booking_step_one,container,false);
       unbinder = ButterKnife.bind(this, itemView);

       initView();
       loadAllServices();

       
       return itemView;
    }

    private void initView() {

        recycler_services.setHasFixedSize(true);
        recycler_services.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recycler_services.addItemDecoration(new SpacesItemDecoration(4));
    }

    private void loadAllServices() {
        allServicesRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                            List<String > list = new ArrayList<>();
                            list.add("Please Choose a Service");
                            for (QueryDocumentSnapshot documentSnapshot:task.getResult())
                                list.add(documentSnapshot.getId());
                            iAllServicesLoadListener.onAllServicesLoadSuccess(list);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iAllServicesLoadListener.onAllServicesLoadFailed(e.getMessage());
            }
        });
    }

    @Override
    public void onAllServicesLoadSuccess(List<String> servNameList) {
        spinner.setItems(servNameList);
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if (position > 0)
                {
                    loadProcedureOfServices(item.toString());
                }
                else
                    recycler_services.setVisibility(View.GONE);
            }
        });

    }

    private void loadProcedureOfServices(String procedures) {
        dialog.show();

        Common.procedures1 = procedures;

        proceduresRef = FirebaseFirestore.getInstance()
                .collection("AllServices")
                .document(procedures)
                .collection("Procedures");

        proceduresRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Services> list = new ArrayList<>();
                if (task.isSuccessful())
                    for (QueryDocumentSnapshot documentSnapshot:task.getResult())
                    {
                        Services services = documentSnapshot.toObject(Services.class);
                        services.setServiceId(documentSnapshot.getId());
                        list.add(services);
                    }
                    iProceduresLoadListener.onProceduresLoadSuccess(list);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iProceduresLoadListener.onProceduresLoadFailed(e.getMessage());
            }
        });

    }

    @Override
    public void onAllServicesLoadFailed(String message) {

        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProceduresLoadSuccess(List<Services> procedNameList) {
        MyServiceAdapter adapter = new MyServiceAdapter(getActivity(),procedNameList);
        recycler_services.setAdapter(adapter);
        recycler_services.setVisibility(View.VISIBLE);

        dialog.dismiss();

    }

    @Override
    public void onProceduresLoadFailed(String message) {

        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }
}
