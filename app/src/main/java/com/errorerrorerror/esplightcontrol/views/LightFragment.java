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

import io.reactivex.schedulers.Schedulers;

public class LightFragment extends RxFragment {

    private static final String TAG = "LightFragment";
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private DevicesCollectionViewModel collectionViewModel;

    private LightFragmentBinding binding;
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

        binding = LightFragmentBinding.inflate(inflater, container, false);
        binding.setViewModel(collectionViewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupRecyclerView();

        //progressChanged();

    }

    private void setupRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                RecyclerView.HORIZONTAL, true);
        linearLayoutManager.setStackFromEnd(true);
        binding.lightRecyclerView.setLayoutManager(linearLayoutManager);
        binding.lightRecyclerView.setHasFixedSize(true);
        recyclerLightAdapter = new RecyclerLightAdapter(this);
        binding.lightRecyclerView.setAdapter(recyclerLightAdapter);
        binding.lightRecyclerView.setItemAnimator(null);

        Objects.requireNonNull(binding.lightRecyclerView.getAdapter())
                .registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                    @Override
                    public void onItemRangeInserted(int positionStart, int itemCount) {
                        if (positionStart > 0) {
                            binding.lightRecyclerView.smoothScrollToPosition(positionStart);
                        }
                    }
                });

    }

    /*
    This changes the value from the room's progress with the corresponding slider
     */
    public void progressChanged(int progress, long id){
        collectionViewModel.addDisposable(collectionViewModel.updateBrightnessLevel(progress, id)
                .compose(bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .subscribe(() -> {} , onError -> Log.e(TAG, "progressChanged: ", onError)));
    }
}
