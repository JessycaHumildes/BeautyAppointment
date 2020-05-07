package com.jHumildes.beautyappointment.Fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.beautyappointment.R;
import com.facebook.accountkit.AccountKit;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import com.jHumildes.beautyappointment.Adapter.HomeSliderAdapter;
import com.jHumildes.beautyappointment.BookingActivity;
import com.jHumildes.beautyappointment.Common.Common;
import com.jHumildes.beautyappointment.Interface.IBannerLoadListener;
import com.jHumildes.beautyappointment.Model.Banner;
import com.jHumildes.beautyappointment.Service.PicassoImageLoadingService;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ss.com.bannerslider.Slider;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements IBannerLoadListener {

    private Unbinder unbinder;

    @BindView(R.id.layout_user_information)
    LinearLayout layout_user_information;

    @BindView(R.id.txt_user_name)
    TextView txt_user_name;

    @BindView(R.id.banner_slider)
    Slider banner_slider;

    @OnClick(R.id.card_view_booking)
            void booking()
    {
        startActivity(new Intent(getActivity(), BookingActivity.class));
    }

    //Firestore
    CollectionReference bannerRef;

    //Interface
    IBannerLoadListener iBannerLoadListener;

    public HomeFragment() {

        bannerRef = FirebaseFirestore.getInstance().collection("Banner");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);

        //init
        Slider.init(new PicassoImageLoadingService());
        iBannerLoadListener = this;

        //check if its logged
        if (AccountKit.getCurrentAccessToken() !=null)
        {
            setUserInformation();
            loadBanner();
        }

        return view;
    }

    private void loadBanner() {

        bannerRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<Banner> banners = new ArrayList<>();
                        if (task.isSuccessful())
                        {
                            for (QueryDocumentSnapshot bannerSnapShot:task.getResult())
                            {
                                Banner banner = bannerSnapShot.toObject(Banner.class);
                                banners.add(banner);
                            }
                            iBannerLoadListener.OnBannerLoadSuccess(banners);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iBannerLoadListener.OnBannerLoadFail(e.getMessage());
            }
        });
    }

    private void setUserInformation() {
        layout_user_information.setVisibility(View.VISIBLE);
        txt_user_name.setText(Common.currentUser.getName());
    }



    @Override
    public void OnBannerLoadSuccess(List<Banner> banners) {
        banner_slider.setAdapter(new HomeSliderAdapter(banners));
    }

    @Override
    public void OnBannerLoadFail(String message) {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();

    }
}
