package com.creativodevelopers.fwmadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabsAccessorAdapter extends FragmentPagerAdapter {

    public TabsAccessorAdapter(FragmentManager fm) {

        super(fm);
    }

    @Override
    public Fragment getItem(int i) {

        switch (i){
            case 0:
                return  new ShowEventFragment();
            case 1:
                return  new myeventsfragment();
            case 2:
                return new user_upcoming_fragment();
            case 3:
                return new UserFragment();
            default:
                return  null;
        }

    }

    @Override
    public int getCount() {

        return 4;
    }



        @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return  "Show Event";
            case 1:
                return  "Show Report";
            case 2:
                return  "Guest User";
            case 3:
                return  "All User";
            default:
                return  null;
        }
    }
}
