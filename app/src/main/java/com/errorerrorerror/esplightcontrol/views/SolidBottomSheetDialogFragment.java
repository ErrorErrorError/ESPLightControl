package com.errorerrorerror.esplightcontrol.views;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration;
import com.errorerrorerror.esplightcontrol.EspApp;
import com.errorerrorerror.esplightcontrol.R;
import com.errorerrorerror.esplightcontrol.adapter.ListModesAdapter;
import com.errorerrorerror.esplightcontrol.databinding.SolidModeBinding;
import com.errorerrorerror.esplightcontrol.model.device.Device;
import com.errorerrorerror.esplightcontrol.model.device_solid.DeviceSolid;
import com.errorerrorerror.esplightcontrol.utils.ObservableList;
import com.errorerrorerror.esplightcontrol.viewmodel.DevicesCollectionViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.jakewharton.rxbinding3.view.RxView;

import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class SolidBottomSheetDialogFragment extends BottomSheetDialogFragment {
    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private DevicesCollectionViewModel viewModel;

    private SolidModeBinding binding;
    @NonNull
    private ObservableList<Device> listSolid = new ObservableList<>();

    private Device device;

    public SolidBottomSheetDialogFragment(Device device) {
        this.device = device;
    }

    public SolidBottomSheetDialogFragment() {
    }

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
        binding = SolidModeBinding.inflate(inflater, container, false);
        binding.setViewModel(viewModel);
        binding.setSolidView(this);
        return binding.getRoot();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerViews();

        ChipsLayoutManager chipsLayoutManager = ChipsLayoutManager.newBuilder(getContext())
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .build();

        binding.solidSelectDeviceChip.setLayoutManager(chipsLayoutManager);
        binding.solidSelectDeviceChip.setAdapter(new ListModesAdapter(this));
        binding.solidSelectDeviceChip.addItemDecoration(new SpacingItemDecoration(getResources().getDimensionPixelOffset(R.dimen.item_space),
                getResources().getDimensionPixelOffset(R.dimen.item_space)));

        listSolid.getObservableList()
                .map(list -> !list.isEmpty())
                .doOnNext(bool -> binding.addSolidButton.setEnabled(bool))
                .switchMap(bool -> RxView.clicks(binding.addSolidButton))
                .take(1)
                .switchMap(unit -> Observable.fromIterable(listSolid.getList()))
                .flatMap((Function<Device, ObservableSource<?>>) device -> {
                    if (device instanceof DeviceSolid) {
                        ((DeviceSolid) device).setColor(binding.solidColorPicker.getColor());
                        return viewModel.updateDevice(device)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .toObservable();
                    } else {
                        return viewModel.deleteDevice(device)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .andThen(
                                        Observable.just(
                                                new DeviceSolid(
                                                        device,
                                                        device.getId(),
                                                        binding.solidColorPicker.getColor())
                                        ))
                                .flatMap(deviceSolid -> viewModel.insertDevice(deviceSolid)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .toObservable()
                                );
                    }
                })
                .doOnComplete(this::dismiss)
                .subscribe();

        binding.solidColorPicker.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    binding.solidColorPicker.requestDisallowInterceptTouchEvent(true);
                    return true;
                case MotionEvent.ACTION_MOVE:
                    binding.solidColorPicker.requestDisallowInterceptTouchEvent(true);
                    return false;
                case MotionEvent.ACTION_UP:
                    binding.solidColorPicker.requestDisallowInterceptTouchEvent(false);
                    return false;
            }
            return false;
        });

        binding.solidSelectDeviceChip.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    binding.solidSelectDeviceChip.requestDisallowInterceptTouchEvent(true);
                    return true;
                case MotionEvent.ACTION_MOVE:
                    binding.solidSelectDeviceChip.requestDisallowInterceptTouchEvent(true);
                    return false;
                case MotionEvent.ACTION_UP:
                    binding.solidSelectDeviceChip.requestDisallowInterceptTouchEvent(false);
                    return false;
            }
            return false;
        });
    }

    private void initRecyclerViews() {
        binding.solidColorPicker.setColor(Color.WHITE, true);
    }

    private void setDataBeforeLoad(@NonNull Device t) {
        listSolid.add(t);
        for (int i = 0; i < binding.solidSelectDeviceChip.getChildCount(); i++) {
            Chip child = (Chip) binding.solidColorPicker.getChildAt(i);

            if (child.getText().equals(device.getDeviceName())) {
                child.setChecked(true);
            }
        }

        binding.solidColorPicker.setColor(((DeviceSolid) t).getColor(), true);
    }

    public void devicesToChange(@NonNull View v, Device device) {
        if (listSolid.contains(device)) {
            listSolid.remove(device);
            ((Chip) v).setChecked(false);
        } else {
            listSolid.add(device);
            ((Chip) v).setChecked(true);
        }
    }
}
