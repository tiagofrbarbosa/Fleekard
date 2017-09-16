package io.github.tiagofrbarbosa.fleekard.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by tfbarbosa on 16/09/17.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        if(position == 0){
            fragment = new FragmentA();

        }else if(position == 1){
            fragment = new FragmentB();

        }else if(position == 2){
            fragment = new FragmentC();

        } else if(position == 3){
        fragment = new FragmentD();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position){
       return null;
    }
}
