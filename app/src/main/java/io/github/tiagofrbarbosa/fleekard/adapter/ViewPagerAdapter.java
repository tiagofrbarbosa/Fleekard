package io.github.tiagofrbarbosa.fleekard.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import io.github.tiagofrbarbosa.fleekard.fragment.FragmentChat;
import io.github.tiagofrbarbosa.fleekard.fragment.FragmentFavorite;
import io.github.tiagofrbarbosa.fleekard.fragment.FragmentNotification;
import io.github.tiagofrbarbosa.fleekard.fragment.FragmentUser;

/**
 * Created by tfbarbosa on 16/09/17.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public static final int TAB_USER = 0;
    public static final int TAB_NOTIFICATION = 1;
    public static final int TAB_CHAT = 2;
    public static final int TAB_FAVORITE = 3;
    public static final int TABS = 4;

    private int rotate = 0;

    public ViewPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        if(position == TAB_USER){
            fragment = new FragmentUser();

        }else if(position == TAB_NOTIFICATION){
            fragment = new FragmentNotification();

        }else if(position == TAB_CHAT){
            fragment = new FragmentChat();

        } else if(position == TAB_FAVORITE){
            fragment = new FragmentFavorite();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return TABS;
    }

    @Override
    public CharSequence getPageTitle(int position){
       return null;
    }

    public void setRotate(int rotate){
        this.rotate = rotate;
    }
}
