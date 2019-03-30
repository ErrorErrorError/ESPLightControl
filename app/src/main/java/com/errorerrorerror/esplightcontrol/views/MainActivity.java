package com.errorerrorerror.esplightcontrol.views;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.errorerrorerror.esplightcontrol.R;
import com.errorerrorerror.esplightcontrol.adapter.ViewPagerAdapter;
import com.errorerrorerror.esplightcontrol.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivityTest";

    private static final String waves = "0,0,1,0.4,5";

    //Fragment items
    private final HomeFragment homeFragment = new HomeFragment();
    private final ModesFragment modesFragment = new ModesFragment();
    private final LightFragment lightFragment = new LightFragment();
    private final PresetsFragment presetsFragment = new PresetsFragment();
    ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        ValueAnimator velocity = ValueAnimator.ofFloat(100, 20);
        velocity.addUpdateListener(animation -> {
            Log.d(TAG, "transparentStatusBar: " + animation.getAnimatedValue());
            binding.topPanelWave.setVelocity((Float) animation.getAnimatedValue());
        });
        velocity.setDuration(TimeUnit.SECONDS.toMillis(5));

        // View Pager Set up
        setupFrag();

        //Sets up transparent status bar
        transparentStatusBar(velocity);
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

    private void transparentStatusBar(ValueAnimator velocity) //Allows transparent status bar//
    {
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        binding.topPanelWave.setStartColor(ContextCompat.getColor(getApplicationContext(), R.color.gradientColorStartWithout75Alpha));
        binding.topPanelWave.setCloseColor(ContextCompat.getColor(getApplicationContext(), R.color.gradientColorEndWithout75Alpha));
        binding.topPanelWave.setColorAlpha(1f);
        binding.topPanelWave.setWaves(waves);
        binding.topPanelWave.setProgress(1f);
        binding.topPanelWave.start();

        //velocity.start();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.unbind();
    }
}
