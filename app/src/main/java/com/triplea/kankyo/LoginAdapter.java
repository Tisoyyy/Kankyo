package com.triplea.kankyo;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class LoginAdapter extends FragmentPagerAdapter {

    private Context context;
    int totalTabs;

    public LoginAdapter(@NonNull FragmentManager fm, Context context, int totalTabs) {
        super(fm);
        this.context = context;
        this.totalTabs = totalTabs;
    }

    @Override
    public int getCount() {
        return totalTabs;
    }

    public Fragment getItem(int position) {

        Fragment fragment = null;
        switch(position) {
            case 0:
                fragment = new LoginTabFragment();
                return fragment;
            case 1:
                fragment = new SignupTabFragment();
                return fragment;
            default:
                return null;
        }
    }

}
