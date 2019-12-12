package com.example.jigsawpuzzle.ui.main;

import android.content.Context;
import android.content.res.AssetManager;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.jigsawpuzzle.MainActivity;
import com.example.jigsawpuzzle.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[MainActivity.PICTURECS_COUNT];
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
        for(int i = 0; i < MainActivity.PICTURECS_COUNT; i++)
            TAB_TITLES[i] = R.string.tab_text;
    }

    /* 显示图片 */
    @Override
    public Fragment getItem(int position) {
        PictureFragment pictureFragment = new PictureFragment(position, mContext);
        return pictureFragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return MainActivity.PICTURECS_COUNT;
    }
}