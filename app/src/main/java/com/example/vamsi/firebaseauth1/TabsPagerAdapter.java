package com.example.vamsi.firebaseauth1;




//import android.app.Fragment;
//import android.app.FragmentManager;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;
import android.app.Fragment;


import android.support.v4.app.FragmentStatePagerAdapter;


import com.example.vamsi.firebaseauth1.CouponsTabClasses.FoodTab;
import com.example.vamsi.firebaseauth1.CouponsTabClasses.MoviesTab;
import com.example.vamsi.firebaseauth1.CouponsTabClasses.ShoppingTab;
/**
 * Created by FALCONS on 3/14/2017.
 */

public class TabsPagerAdapter extends FragmentPagerAdapter {
    public TabsPagerAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Top Rated fragment activity
                return new FoodTab();
            case 1:
                // Games fragment activity
                return new MoviesTab();
            case 2:
                // Movies fragment activity
                return new ShoppingTab();
        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }

}
