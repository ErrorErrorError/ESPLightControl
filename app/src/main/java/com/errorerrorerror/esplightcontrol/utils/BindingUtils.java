package com.errorerrorerror.esplightcontrol.utils;

import android.annotation.SuppressLint;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.errorerrorerror.esplightcontrol.adapter.BindableAdapter;
import com.errorerrorerror.esplightcontrol.devices.Devices;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class BindingUtils {

    @SuppressLint("CheckResult")
    @BindingAdapter("app:data")
    public static void setRecyclerViewProperties(RecyclerView recyclerView, Flowable<List<Devices>> data) {
        if (recyclerView.getAdapter() instanceof BindableAdapter) {
            data
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(devices -> ((BindableAdapter) recyclerView.getAdapter()).setData(devices));
        }
    }

    @SuppressLint("CheckResult")
    @BindingAdapter("app:data")
    public static void dataTextView(TextView view, Flowable<List<Devices>> data) {
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

}
