package com.example.parkyeriharita;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MyViewPagerAdapter extends FragmentPagerAdapter {
    public MyViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new HistoryFragment();
            case 1:
                return new FavouriteFragment();
            case 2:
                return new MapsFragment_user();
            case 3:
                return new PaymentFragment();
            case 4:
                return new HelpFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 5;
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Geçmiş";
            case 1:
                return "Favori";
            case 2:
                return "Kullanıcı";
            case 3:
                return "Ödeme";
            case 4:
                return "Yardım";
            default:
                return null;
        }
    }

}
