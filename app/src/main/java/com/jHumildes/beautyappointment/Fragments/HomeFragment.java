package com.jHumildes.beautyappointment.Fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.beautyappointment.R;
import com.facebook.accountkit.AccountKit;

import com.jHumildes.beautyappointment.Adapter.MyRowAdapter;
import com.jHumildes.beautyappointment.BookingActivity;
import com.jHumildes.beautyappointment.Common.Common;
import com.jHumildes.beautyappointment.Model.ModelRow;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ss.com.bannerslider.Slider;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    RecyclerView recyclerView;
    MyRowAdapter rowAdapter;

    private Unbinder unbinder;

    @BindView(R.id.layout_user_information)
    LinearLayout layout_user_information;

    @BindView(R.id.txt_user_name)
    TextView txt_user_name;



    @OnClick(R.id.card_view_booking)
            void booking()
    {
        startActivity(new Intent(getActivity(), BookingActivity.class));
    }


    public HomeFragment() {


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);



        //check if its logged
       // if (AccountKit.getCurrentAccessToken() !=null)
      //  {
         //   setUserInformation();

      //  }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView = getActivity().findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        rowAdapter = new MyRowAdapter(this.getActivity(),getMyList());
        recyclerView.setAdapter(rowAdapter);
    }

    private ArrayList<ModelRow>getMyList(){

        ArrayList<ModelRow>modelRows = new ArrayList<>();

        ModelRow m = new ModelRow();
        m.setTitle("Hair Removal");
        m.setImg(R.drawable.armwax);
        modelRows.add(m);

        m = new ModelRow();
        m.setTitle("Face Care");
        m.setImg(R.drawable.eyebrows);
        modelRows.add(m);

        m = new ModelRow();
        m.setTitle("Nails");
        m.setImg(R.drawable.manicurepedicure);
        modelRows.add(m);

        m = new ModelRow();
        m.setTitle("Massage");
        m.setImg(R.drawable.massage);
        modelRows.add(m);

        return modelRows;

    }




    private void setUserInformation() {
        layout_user_information.setVisibility(View.VISIBLE);
        txt_user_name.setText(Common.currentUser.getName());
    }





}
