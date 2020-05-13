package com.jHumildes.beautyappointment.Fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.beautyappointment.R;
import com.jHumildes.beautyappointment.Adapter.MySlideAdapter;
import com.jHumildes.beautyappointment.Model.SliderItem;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProceduresFragment extends Fragment{


    private ViewPager2 viewPager2;

    private Handler sliderHandler = new Handler();

    public ProceduresFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_procedures, container, false);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        viewPager2 = getActivity().findViewById(R.id.viewPager);

        List<SliderItem> sliderItems = new ArrayList<>();
        sliderItems.add(new SliderItem(R.drawable.eyebrowss));
        sliderItems.add(new SliderItem(R.drawable.eyebrowsss));
        sliderItems.add(new SliderItem(R.drawable.eyelashees));
        sliderItems.add(new SliderItem(R.drawable.massagem));

        viewPager2.setAdapter(new MySlideAdapter(sliderItems, viewPager2));

        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(50));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.95f * r * 1.35f);
            }
        });


        viewPager2.setPageTransformer(compositePageTransformer);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 3000);
            }
        });
    }
    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };
}