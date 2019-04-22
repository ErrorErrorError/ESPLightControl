package com.errorerrorerror.esplightcontrol.views;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.errorerrorerror.esplightcontrol.EspApp;
import com.errorerrorerror.esplightcontrol.adapter.RecyclerLightAdapter;
import com.errorerrorerror.esplightcontrol.databinding.LightFragmentBinding;
import com.errorerrorerror.esplightcontrol.viewmodel.DevicesCollectionViewModel;
import com.trello.rxlifecycle3.components.support.RxFragment;

import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LightFragment extends RxFragment {

    private static final String TAG = "LightFragment";
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private DevicesCollectionViewModel collectionViewModel;

    private LightFragmentBinding lightFragmentBinding;
    private RecyclerLightAdapter recyclerLightAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((EspApp) Objects.requireNonNull(getActivity()).getApplication())
                .getApplicationComponent()
                .inject(this);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //Implements ViewModel to HomeFragment
        collectionViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(DevicesCollectionViewModel.class);

        lightFragmentBinding = LightFragmentBinding.inflate(inflater, container, false);

        return lightFragmentBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupRecyclerView();

        lightDevicesListeners();
    }

    private void setupRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                RecyclerView.HORIZONTAL, true);
        linearLayoutManager.setStackFromEnd(true);
        lightFragmentBinding.lightRecyclerView.setLayoutManager(linearLayoutManager);
        lightFragmentBinding.lightRecyclerView.setHasFixedSize(true);
    }

    private void lightDevicesListeners() {

        recyclerLightAdapter = new RecyclerLightAdapter();
        lightFragmentBinding.lightRecyclerView.setAdapter(recyclerLightAdapter);

        collectionViewModel.addDisposable(collectionViewModel.getAllDevices()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(devicesList ->
                                recyclerLightAdapter.submitList(devicesList)
                        , onError -> Log.e(TAG, "lightDevicesListeners: ", onError))
        );

        collectionViewModel.addDisposable(
                recyclerLightAdapter.getProgressObserver()
                        .subscribe(progressId -> collectionViewModel.addDisposable(
                                collectionViewModel.updateBrightnessLevel(progressId.progress, progressId.id)
                                        .compose(bindToLifecycle())
                                        .subscribeOn(Schedulers.io())
                                        .subscribe(() -> Log.d(TAG, "accept: Successfully changed brightness!"),
                                                onError -> Log.e(TAG, "accept: ", onError))
                        ))
        );
    }
}
