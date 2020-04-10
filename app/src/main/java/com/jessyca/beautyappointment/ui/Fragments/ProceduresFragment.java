package com.jessyca.beautyappointment.ui.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jessyca.beautyappointment.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProceduresFragment extends Fragment {


    public ProceduresFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_procedures, container, false);
    }

}
