package com.errorerrorerror.esplightcontrol.views;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.errorerrorerror.esplightcontrol.EspApp;
import com.errorerrorerror.esplightcontrol.adapter.AllDevicesRecyclerAdapter;
import com.errorerrorerror.esplightcontrol.databinding.LightFragmentBinding;
import com.errorerrorerror.esplightcontrol.model.device.Device;
import com.errorerrorerror.esplightcontrol.viewmodel.DevicesCollectionViewModel;

import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LightFragment extends Fragment {

    private static final String TAG = "LightFragment";
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private DevicesCollectionViewModel collectionViewModel;

    private LightFragmentBinding binding;
    private AllDevicesRecyclerAdapter allDevicesRecyclerAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((EspApp) Objects.requireNonNull(getActivity()).getApplication())
                .getAppComponent()
                .inject(this);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //Implements ViewModel to HomeFragment
        collectionViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(DevicesCollectionViewModel.class);

        binding = LightFragmentBinding.inflate(inflater, container, false);
        binding.setViewModel(collectionViewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupRecyclerView();

        allDevicesRecyclerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                binding.lightRecyclerView.smoothScrollToPosition(0);
            }
        });

    }

    private void setupRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                RecyclerView.HORIZONTAL, false);
        linearLayoutManager.setStackFromEnd(false);
        binding.lightRecyclerView.setLayoutManager(linearLayoutManager);
        binding.lightRecyclerView.setHasFixedSize(true);
        allDevicesRecyclerAdapter = new AllDevicesRecyclerAdapter(this);
        binding.lightRecyclerView.setAdapter(allDevicesRecyclerAdapter);
        binding.lightRecyclerView.setItemAnimator(null);

    }

    /*
    This changes the value from the room's progress with the corresponding slider
     */
    public void progressChanged(int progress, Device device) {
        collectionViewModel.addDisposable(collectionViewModel.updateBrightnessLevel(progress, device)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                }, onError -> Log.e(TAG, "progressChanged: ", onError)));
    }

}
