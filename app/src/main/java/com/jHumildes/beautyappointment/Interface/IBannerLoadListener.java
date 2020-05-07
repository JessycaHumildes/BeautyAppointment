package com.jHumildes.beautyappointment.Interface;



import com.jHumildes.beautyappointment.Model.Banner;

import java.util.List;

public interface IBannerLoadListener {

    void OnBannerLoadSuccess(List<Banner> banners);
    void OnBannerLoadFail(String message);
}
