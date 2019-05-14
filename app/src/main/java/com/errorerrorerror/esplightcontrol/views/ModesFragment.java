package com.errorerrorerror.esplightcontrol.views;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.errorerrorerror.esplightcontrol.EspApp;
import com.errorerrorerror.esplightcontrol.adapter.ListModesAdapter;
import com.errorerrorerror.esplightcontrol.adapter.MusicModeAdapter;
import com.errorerrorerror.esplightcontrol.databinding.ModesFragmentBinding;
import com.errorerrorerror.esplightcontrol.viewmodel.DevicesCollectionViewModel;
import com.jakewharton.rxbinding3.view.RxView;
import com.trello.rxlifecycle3.components.support.RxFragment;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

public class ModesFragment extends RxFragment {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private DevicesCollectionViewModel viewModel;
    private ModesFragmentBinding binding;

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
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(DevicesCollectionViewModel.class);
        binding = ModesFragmentBinding.inflate(inflater, container, false);
        binding.setViewModel(viewModel);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.expandableMusic.setAdapter(new MusicModeAdapter());
        binding.expandableSolid.setAdapter(new ListModesAdapter(this));
        binding.expandableWaves.setAdapter(new ListModesAdapter(this));

        viewModel.addDisposable(RxView.clicks(binding.musicModeCardView)
                .throttleFirst(1000, TimeUnit.MICROSECONDS)
                .subscribe(onNext -> {
                            Log.d("ModesFragment", "music: ");
                            showMusicSheet();
                        }
                ));

        viewModel.addDisposable(RxView.clicks(binding.wavesModeCardView)
                .throttleFirst(1000, TimeUnit.MICROSECONDS)
                .subscribe(onNext -> {
                    Log.d("ModesFragment", "waves: ");
                    showWavesSheet();
                }));

        viewModel.addDisposable(RxView.clicks(binding.solidModeCardView)
                .throttleFirst(1000, TimeUnit.MICROSECONDS)
                .subscribe(onNext -> {
                    Log.d("ModesFragment", "solid: ");
                    showSolidSheet();
                })
        );

        

        //ListModesAdapter test = new ListModesAdapter();
       // binding.expandableMusic.setAdapter(test);
        //binding.expandableSolid.setAdapter(test);
        //binding.expandableWaves.setAdapter(test);

        //binding.expandableMusic.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        //binding.expandableSolid.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        //binding.expandableWaves.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
    }


    private void showMusicSheet() {
        FragmentTransaction ft = checkDialog();
        MusicBottomSheetDialogFragment bottomSheet = new MusicBottomSheetDialogFragment();
        bottomSheet.show(ft, "modaldialog");
    }

    private void showWavesSheet() {
        FragmentTransaction ft = checkDialog();
        WavesBottomSheetDialogFragment bottomSheet = new WavesBottomSheetDialogFragment();
        bottomSheet.show(ft, "modaldialog");
    }

    private void showSolidSheet() {
        FragmentTransaction ft = checkDialog();
        SolidBottomSheetDialogFragment bottomSheet = new SolidBottomSheetDialogFragment();
        bottomSheet.show(ft, "modaldialog");
    }


    private FragmentTransaction checkDialog() {
        FragmentTransaction ft = Objects.requireNonNull(getFragmentManager()).beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("modaldialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        return ft;
    }

}
