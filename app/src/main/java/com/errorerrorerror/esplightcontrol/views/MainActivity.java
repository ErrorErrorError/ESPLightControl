package com.errorerrorerror.esplightcontrol.views;

import android.animation.ValueAnimator;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.errorerrorerror.esplightcontrol.R;
import com.errorerrorerror.esplightcontrol.adapter.ViewPagerAdapter;
import com.errorerrorerror.esplightcontrol.databinding.ActivityMainBinding;
import com.errorerrorerror.esplightcontrol.rxobservable.RxBubbleNavigation;
import com.errorerrorerror.esplightcontrol.utils.Constants;
import com.jakewharton.rxbinding3.viewpager.RxViewPager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.CompositeDisposable;


public class MainActivity extends AppCompatActivity {

    //Fragment items
    private final HomeFragment homeFragment = new HomeFragment();
    private final ModesFragment modesFragment = new ModesFragment();
    private final LightFragment lightFragment = new LightFragment();
    private final PresetsFragment presetsFragment = new PresetsFragment();
    private ActivityMainBinding binding;
    @NonNull
    private CompositeDisposable disposable = new CompositeDisposable();
    private static final String TAG = "MainActivityApp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        int defaultValue = prefs.getInt("mode",AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

        AppCompatDelegate.setDefaultNightMode(defaultValue);
        super.onCreate(savedInstanceState);


        //Binding Activity
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // View Pager Set up
        setupFrag();

        //Sets up transparent status bar
        transparentStatusBar();

        //Top bar Animation
        wavesAnimation();
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

        disposable.add(RxViewPager.pageSelections(binding.viewPager)
                .doOnNext(i -> binding.customBubbleBar.setCurrentActiveItem(i))
                .flatMap(integer -> RxBubbleNavigation.bubbleSelections(binding.customBubbleBar))
                .doOnNext(position -> {
                    binding.viewPager.setOffscreenPageLimit(Objects.requireNonNull(binding.viewPager.getAdapter()).getCount());
                    binding.viewPager.setCurrentItem(position, false);
                })
                .subscribe());


    }


    private void transparentStatusBar() //Allows transparent status bar//
    {
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    private void wavesAnimation() {
        //Animation
        ValueAnimator velocity = ValueAnimator.ofFloat(150, 20);
        velocity.addUpdateListener(animation ->
                binding.topPanelWave.setVelocity((Float) animation.getAnimatedValue()));

        binding.topPanelWave.setStartColor(ContextCompat.getColor(getApplicationContext(), R.color.gradientColorStart));
        binding.topPanelWave.setCloseColor(ContextCompat.getColor(getApplicationContext(), R.color.gradientColorEnd));
        binding.topPanelWave.setColorAlpha(1f);
        binding.topPanelWave.setWaves(Constants.WAVES);
        binding.topPanelWave.setProgress(1f);
        binding.topPanelWave.start();

        //Starts animation
        velocity.setDuration(TimeUnit.SECONDS.toMillis(5));
        velocity.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
        binding.unbind();
    }

    @Override
    protected void onPause() {
        super.onPause();
        binding.topPanelWave.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.topPanelWave.start();
    }
}
