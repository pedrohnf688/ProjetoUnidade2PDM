package com.example.phnf2.projeto2unidade.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.phnf2.projeto2unidade.fragment.Camera;
import com.example.phnf2.projeto2unidade.fragment.FragmentConversa;
import com.example.phnf2.projeto2unidade.fragment.FragmentMapa;

public class FixedTabsPageAdapter extends FragmentPagerAdapter {

    Fragment f1 = new Camera();
    Fragment f2 = new FragmentConversa();
    Fragment f3 = new FragmentMapa();


    public FixedTabsPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int posicao) {

        switch (posicao){
            case 0:
                return f1;
            case 1:
                return f2;
            case 2:
                return f3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position){
            case 0:
                return "";
            case 1:
                return "Conversa";
            case 2:
                return "Mapa";
            default:
                return null;
        }
    }
}
