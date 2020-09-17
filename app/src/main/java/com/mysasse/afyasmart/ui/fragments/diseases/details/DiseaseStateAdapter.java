package com.mysasse.afyasmart.ui.fragments.diseases.details;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DiseaseStateAdapter extends FragmentStateAdapter {

    private List<Fragment> fragmentList;

    public DiseaseStateAdapter(@NonNull Fragment fragment) {
        super(fragment);

        fragmentList = new ArrayList<>();
    }

    public void addFragment(Fragment fragment) {
        fragmentList.add(fragment);
    }

    public void addFragments(Fragment... fragments) {

        fragmentList.addAll(Arrays.asList(fragments));

    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getItemCount() {
        return fragmentList.size();
    }


}
