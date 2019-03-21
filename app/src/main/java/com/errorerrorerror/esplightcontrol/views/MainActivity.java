package com.errorerrorerror.esplightcontrol.views;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.errorerrorerror.esplightcontrol.R;
import com.errorerrorerror.esplightcontrol.adapter.CustomViewPager;
import com.errorerrorerror.esplightcontrol.adapter.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivityTest";

    //Fragment items
    private final HomeFragment homeFragment = new HomeFragment();
    private final ModesFragment modesFragment = new ModesFragment();
    private final LightFragment lightFragment = new LightFragment();
    private final PresetsFragment presetsFragment = new PresetsFragment();
    private ViewPagerAdapter vpAdapter;

    private BindingViews.MainActivityViews bindings = new BindingViews.MainActivityViews();
    private CustomViewPager viewPager;
    private CurvedBubbleNavigation curvedBubbleNavigation;

    @TargetApi(Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(bindings, MainActivity.this);
        curvedBubbleNavigation = bindings.curvedBubbleNavigation;
        viewPager = bindings.viewPager;

        // View Pager Set up
        setupFrag();

        //Sets up transparent status bar
        transparentStatusBar();


    }


    private void setupFrag() {
        // used for ViewPager adapter
        List<Fragment> fragments = new ArrayList<>(4);

        //Add Fragments to adapter
        fragments.add(homeFragment);
        fragments.add(modesFragment);
        fragments.add(lightFragment);
        fragments.add(presetsFragment);

        //Set Adapter
        vpAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(vpAdapter);

        //navigation.setupWithViewPager(viewPager);
        curvedBubbleNavigation.setNavigationChangeListener((view, position) -> {
            int limit = vpAdapter.getCount();
            viewPager.setOffscreenPageLimit(limit);
            System.out.println("qwertyThis is position: " + position);
            viewPager.setCurrentItem(position, false);
        });
    }

    private void transparentStatusBar() //Allows transparent status bar//
    {
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

    }
}
