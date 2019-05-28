package com.errorerrorerror.esplightcontrol.utils;

import android.annotation.SuppressLint;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.errorerrorerror.esplightcontrol.adapter.BindableAdapter;
import com.errorerrorerror.esplightcontrol.model.device.Device;
import com.errorerrorerror.esplightcontrol.model.device_music.DeviceMusic;
import com.errorerrorerror.esplightcontrol.model.device_solid.DeviceSolid;
import com.errorerrorerror.esplightcontrol.model.device_waves.DeviceWaves;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class BindingUtils {

    @SuppressLint("CheckResult")
    @BindingAdapter("data")
    public static void setRecyclerViewProperties(RecyclerView recyclerView, @NonNull Observable<List<Device>> data) {
        if (recyclerView.getAdapter() instanceof BindableAdapter) {
            data
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(devices -> ((BindableAdapter) recyclerView.getAdapter()).setData(devices)
                            , error -> Log.e("HomeFragment", "setRecyclerViewProperties: ", error));
        }
    }

    @SuppressLint("CheckResult")
    @BindingAdapter("data")
    public static void dataTextView(@NonNull TextView view, Observable<List<Device>> data) {
        data.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(devices -> {
                    if (devices.isEmpty()) {
                        view.animate().alpha(1f)
                                .setDuration(500)
                                .start();
                    } else {
                        view.animate().alpha(0f)
                                .start();
                    }
                });
    }


    @SuppressLint("CheckResult")
    @BindingAdapter("data")
    public static void setRecyclerViewPropertiesW(RecyclerView recyclerView, @NonNull Observable<List<DeviceWaves>> data) {
        if (recyclerView.getAdapter() instanceof BindableAdapter) {
            data
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(devices -> ((BindableAdapter) recyclerView.getAdapter()).setData(devices)
                            , error -> Log.e("HomeFragment", "setRecyclerViewProperties: ", error));
        }
    }
    @SuppressLint("CheckResult")
    @BindingAdapter("data")
    public static void setRecyclerViewPropertiesS(RecyclerView recyclerView, @NonNull Observable<List<DeviceSolid>> data) {
        if (recyclerView.getAdapter() instanceof BindableAdapter) {
            data
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(devices -> ((BindableAdapter) recyclerView.getAdapter()).setData(devices)
                            , error -> Log.e("HomeFragment", "setRecyclerViewProperties: ", error));
        }
    }
    @SuppressLint("CheckResult")
    @BindingAdapter("data")
    public static void setRecyclerViewPropertiesM(RecyclerView recyclerView, @NonNull Observable<List<DeviceMusic>> data) {
        if (recyclerView.getAdapter() instanceof BindableAdapter) {
            data
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(devices -> ((BindableAdapter) recyclerView.getAdapter()).setData(devices)
                            , error -> Log.e("HomeFragment", "setRecyclerViewProperties: ", error));
        }
    }
}
