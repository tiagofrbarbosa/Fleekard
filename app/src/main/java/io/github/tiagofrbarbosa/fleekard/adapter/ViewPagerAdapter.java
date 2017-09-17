package io.github.tiagofrbarbosa.fleekard.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import io.github.tiagofrbarbosa.fleekard.fragment.FragmentChat;
import io.github.tiagofrbarbosa.fleekard.fragment.FragmentUser;
import io.github.tiagofrbarbosa.fleekard.fragment.FragmentNotification;
import io.github.tiagofrbarbosa.fleekard.fragment.FragmentFavorite;

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
            fragment = new FragmentUser();

        }else if(position == 1){
            fragment = new FragmentNotification();

        }else if(position == 2){
            fragment = new FragmentChat();

        } else if(position == 3){
        fragment = new FragmentFavorite();
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
