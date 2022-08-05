package com.quickinstasaver.app.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.quickinstasaver.app.R;


public class FragmentHome extends Fragment implements BottomNavigationView.OnNavigationItemSelectedListener {


    private FrameLayout fl_bottom_bar;
    String CopyIntent;
    private BottomNavigationView bottomNavigationView;

    public FragmentHome(String CopyIntent) {
        // Required empty public constructor
        this.CopyIntent = CopyIntent;    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initView(view);
        setFragment(new FragmentHomeBottom(CopyIntent));

        bottomNavigationView = view.findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        return view;
    }

    private void initView(View view) {
        fl_bottom_bar = view.findViewById(R.id.fl_bottom_bar);
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(fl_bottom_bar.getId(), fragment);
        fragmentTransaction.commit();
    }

    public void changeFragment(Fragment fragment, String tagFragmentName) {

        FragmentManager mFragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        Fragment currentFragment = mFragmentManager.getPrimaryNavigationFragment();
        if (currentFragment != null) {
            fragmentTransaction.hide(currentFragment);
        }

        Fragment fragmentTemp = mFragmentManager.findFragmentByTag(tagFragmentName);
        if (fragmentTemp == null) {
            fragmentTemp = fragment;
            fragmentTransaction.add(R.id.fl_bottom_bar, fragmentTemp, tagFragmentName);
        } else {
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            fragmentTransaction.show(fragmentTemp);
        }

        fragmentTransaction.setPrimaryNavigationFragment(fragmentTemp);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commitNowAllowingStateLoss();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                changeFragment(new FragmentHomeBottom(CopyIntent),FragmentHomeBottom.class.getName());
                return true;

            case R.id.downlaod:
                changeFragment(new FragmentDownload(),FragmentDownload.class.getName());
                return true;
        }
        return false;
    }
}