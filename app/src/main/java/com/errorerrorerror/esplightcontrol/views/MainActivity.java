package com.errorerrorerror.esplightcontrol.views;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.errorerrorerror.esplightcontrol.R;
import com.errorerrorerror.esplightcontrol.adapter.ViewPagerAdapter;
import com.errorerrorerror.esplightcontrol.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivityTest";

    //Fragment items
    private final HomeFragment homeFragment = new HomeFragment();
    private final ModesFragment modesFragment = new ModesFragment();
    private final LightFragment lightFragment = new LightFragment();
    private final PresetsFragment presetsFragment = new PresetsFragment();
    ActivityMainBinding binding;


    @TargetApi(Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

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
        binding.viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragments));

        binding.customBubbleBar.setNavigationChangeListener((view, position) -> {
            int limit = Objects.requireNonNull(binding.viewPager.getAdapter()).getCount();
            binding.viewPager.setOffscreenPageLimit(limit);
            binding.viewPager.setCurrentItem(position, false);
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
