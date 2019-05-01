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

import com.errorerrorerror.esplightcontrol.EspApp;
import com.errorerrorerror.esplightcontrol.databinding.ModesFragmentBinding;
import com.errorerrorerror.esplightcontrol.viewmodel.DevicesCollectionViewModel;
import com.jakewharton.rxbinding3.view.RxView;
import com.trello.rxlifecycle3.components.support.RxFragment;

import java.util.Objects;

import javax.inject.Inject;

public class ModesFragment extends RxFragment {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    DevicesCollectionViewModel viewModel;

    private ModesFragmentBinding binding;

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
        viewModel= ViewModelProviders.of(this, viewModelFactory).get(DevicesCollectionViewModel.class);
        binding = ModesFragmentBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel.addDisposable(RxView.clicks(binding.musicModeCardView)
                .subscribe(onNext ->

                        {
                            Log.d("ModesFragment", "music: ");
                        }
        ));

        viewModel.addDisposable(RxView.clicks(binding.wavesModeCardView)
                .subscribe(onNext -> Log.d("ModesFragment", "waves: "))
        );

        viewModel.addDisposable(RxView.clicks(binding.solidModeCardView)
                .subscribe(onNext -> Log.d("ModesFragment", "solid: "))
        );
    }
}
