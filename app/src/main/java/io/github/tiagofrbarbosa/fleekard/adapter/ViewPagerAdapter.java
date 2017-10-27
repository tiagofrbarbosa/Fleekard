package io.github.tiagofrbarbosa.fleekard.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.Spannable;
import android.text.SpannableString;

import io.github.tiagofrbarbosa.fleekard.R;
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

    private Context context;

    private int[] tabIcon = {
            R.drawable.ic_gps_fixed_white_24dp,
            R.drawable.ic_notifications_white_24dp,
            R.drawable.ic_question_answer_white_24dp,
            R.drawable.ic_favorite_white_24dp
    };

    private int[] tabText = {
            R.string.tabtext_find,
            R.string.tabtext_notification,
            R.string.tabtext_chat,
            R.string.tabtext_favorite
    };

    public ViewPagerAdapter(FragmentManager fm, Context context){
        super(fm);
        this.context = context;
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
        Drawable drawable = context.getResources().getDrawable(tabIcon[position]);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        SpannableString spannableString = new SpannableString(" " + context.getResources().getString(tabText[position]));
        spannableString.setSpan(drawable, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
}
