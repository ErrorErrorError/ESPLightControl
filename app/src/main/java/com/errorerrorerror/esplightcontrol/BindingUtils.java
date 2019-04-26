package com.errorerrorerror.esplightcontrol;

import android.annotation.SuppressLint;

import androidx.databinding.BindingAdapter;
import androidx.lifecycle.LiveData;
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

    @SuppressWarnings("ignore")
    @BindingAdapter("app:data")
    public static void setRecyclerViewProperties(RecyclerView recyclerView, LiveData<List<Devices>> data) {
        if (recyclerView.getAdapter() instanceof BindableAdapter) {
            data.observeForever(devices -> ((BindableAdapter) recyclerView.getAdapter()).setData(devices));
        }
    }

}
