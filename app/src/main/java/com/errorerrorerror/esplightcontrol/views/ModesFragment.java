package com.errorerrorerror.esplightcontrol.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration;
import com.errorerrorerror.esplightcontrol.EspApp;
import com.errorerrorerror.esplightcontrol.R;
import com.errorerrorerror.esplightcontrol.adapter.ListModesAdapter;
import com.errorerrorerror.esplightcontrol.databinding.ModesFragmentBinding;
import com.errorerrorerror.esplightcontrol.model.device.Device;
import com.errorerrorerror.esplightcontrol.model.device_music.DeviceMusic;
import com.errorerrorerror.esplightcontrol.model.device_solid.DeviceSolid;
import com.errorerrorerror.esplightcontrol.model.device_waves.DeviceWaves;
import com.errorerrorerror.esplightcontrol.viewmodel.DevicesCollectionViewModel;
import com.jakewharton.rxbinding3.view.RxView;
import com.trello.rxlifecycle3.components.support.RxFragment;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

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
        binding.setView(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();

        viewModel.addDisposable(RxView.clicks(binding.musicModeCardView)
                .throttleFirst(700, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .doOnNext(onNext -> showMusicSheet(null))
                .subscribe()
        );

        viewModel.addDisposable(RxView.clicks(binding.wavesModeCardView)
                .throttleFirst(700, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribe(onNext -> showWavesSheet(null)));

        viewModel.addDisposable(RxView.clicks(binding.solidModeCardView)
                .throttleFirst(700, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribe(onNext -> showSolidSheet(null))
        );
    }

    private void showMusicSheet(@Nullable Device device) {
        FragmentTransaction ft = checkDialog();
        MusicBottomSheetDialogFragment bottomSheet = device != null ? new MusicBottomSheetDialogFragment(device) : new MusicBottomSheetDialogFragment();
        bottomSheet.show(ft, "modaldialog");
    }

    private void showWavesSheet(@Nullable Device device) {
        FragmentTransaction ft = checkDialog();
        WavesBottomSheetDialogFragment bottomSheet;

        if (device != null) {
            bottomSheet = new WavesBottomSheetDialogFragment(device);
        } else {
            bottomSheet = new WavesBottomSheetDialogFragment();
        }
        bottomSheet.show(ft, "modaldialog");

    }

    private void initViews() {
        ChipsLayoutManager one = ChipsLayoutManager.newBuilder(getContext())
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .build();

        ChipsLayoutManager two = ChipsLayoutManager.newBuilder(getContext())
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .build();

        ChipsLayoutManager three = ChipsLayoutManager.newBuilder(getContext())
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .build();

        binding.expandableWaves.setLayoutManager(one);
        binding.expandableMusic.setLayoutManager(two);
        binding.expandableSolid.setLayoutManager(three);

        binding.expandableWaves.setAdapter(new ListModesAdapter(this));
        binding.expandableMusic.setAdapter(new ListModesAdapter(this));
        binding.expandableSolid.setAdapter(new ListModesAdapter(this));

        binding.expandableSolid.addItemDecoration(new SpacingItemDecoration(getResources().getDimensionPixelOffset(R.dimen.item_space_horizontal),
                getResources().getDimensionPixelOffset(R.dimen.item_space_vertical)));
        binding.expandableMusic.addItemDecoration(new SpacingItemDecoration(getResources().getDimensionPixelOffset(R.dimen.item_space_horizontal),
                getResources().getDimensionPixelOffset(R.dimen.item_space_vertical)));
        binding.expandableWaves.addItemDecoration(new SpacingItemDecoration(getResources().getDimensionPixelOffset(R.dimen.item_space_horizontal),
                getResources().getDimensionPixelOffset(R.dimen.item_space_vertical)));
    }

    private void showSolidSheet(@Nullable Device device) {
        FragmentTransaction ft = checkDialog();
        SolidBottomSheetDialogFragment bottomSheet;
        if (device != null) {
            bottomSheet = new SolidBottomSheetDialogFragment(device);
        } else {
            bottomSheet = new SolidBottomSheetDialogFragment();
        }

        bottomSheet.show(ft, "modaldialog");
    }


    @NonNull
    private FragmentTransaction checkDialog() {
        FragmentTransaction ft = Objects.requireNonNull(getFragmentManager()).beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("modaldialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        return ft;
    }

    public void showSetting(@NonNull CompoundButton v,@NotNull Device device) {
        if (device instanceof DeviceMusic) {
            v.setChecked(false);
            showMusicSheet(device);
        } else if (device instanceof DeviceWaves) {
            v.setChecked(false);
            showWavesSheet(device);
        } else if (device instanceof DeviceSolid) {
            v.setChecked(false);
            showSolidSheet(device);
        }
    }
}
