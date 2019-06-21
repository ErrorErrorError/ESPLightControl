package com.errorerrorerror.esplightcontrol.views;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration;
import com.errorerrorerror.esplightcontrol.EspApp;
import com.errorerrorerror.esplightcontrol.R;
import com.errorerrorerror.esplightcontrol.adapter.ListModesAdapter;
import com.errorerrorerror.esplightcontrol.databinding.MusicModeBinding;
import com.errorerrorerror.esplightcontrol.model.device.Device;
import com.errorerrorerror.esplightcontrol.model.device_music.DeviceMusic;
import com.errorerrorerror.esplightcontrol.utils.ObservableList;
import com.errorerrorerror.esplightcontrol.viewmodel.DevicesCollectionViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.jakewharton.rxbinding3.recyclerview.RxRecyclerView;
import com.jakewharton.rxbinding3.view.RxView;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MusicBottomSheetDialogFragment extends BottomSheetDialogFragment {

    @Inject
    ViewModelProvider.Factory devicesViewModelFactory;
    private DevicesCollectionViewModel viewModel;
    private MusicModeBinding binding;
    @Nullable
    private DeviceMusic device;

    @NonNull
    private ObservableList<Device> listMusic = new ObservableList<>();

    public MusicBottomSheetDialogFragment(DeviceMusic device) {
        super();
        this.device = device;
    }

    public MusicBottomSheetDialogFragment() {
        super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((EspApp) Objects.requireNonNull(getActivity()).getApplication())
                .getAppComponent()
                .inject(this);
    }

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        dialog.setOnShowListener(dialog1 -> {
            BottomSheetDialog d = (BottomSheetDialog) dialog1;

            FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            assert bottomSheet != null;
            BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
        });
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(this, devicesViewModelFactory).get(DevicesCollectionViewModel.class);
        binding = MusicModeBinding.inflate(inflater, container, false);
        binding.setMusicView(this);
        binding.setViewModel(viewModel);

        return binding.getRoot();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ChipsLayoutManager chipsLayoutManager = ChipsLayoutManager.newBuilder(getContext())
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .build();
        listMusic.reInsertList();


        binding.musicSelectDeviceChip.setLayoutManager(chipsLayoutManager);
        binding.musicSelectDeviceChip.setAdapter(new ListModesAdapter(this));
        binding.musicSelectDeviceChip.addItemDecoration(new SpacingItemDecoration(getResources().getDimensionPixelOffset(R.dimen.item_space_horizontal),
                getResources().getDimensionPixelOffset(R.dimen.item_space_vertical)));


        listMusic.getObservableList()
                .map(list -> !list.isEmpty())
                .doOnNext(bool -> binding.addMusicDeviceButton.setEnabled(bool))
                .switchMap(bool -> RxView.clicks(binding.addMusicDeviceButton))
                .take(1)
                .switchMap(unit -> Observable.fromIterable(listMusic.getList()))
                .flatMap((Function<Device, ObservableSource<?>>) device -> {
                    if (device instanceof DeviceMusic) {
                        ((DeviceMusic) device).setHigh(binding.highColorSlider.getColor());
                        ((DeviceMusic) device).setMed(binding.medColorSlider.getColor());
                        ((DeviceMusic) device).setLow(binding.lowColorSlider.getColor());
                        ((DeviceMusic) device).setIntensity(binding.thresholdSlider.getProgress());
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
                                                new DeviceMusic(
                                                        device,
                                                        binding.lowColorSlider.getColor(),
                                                        binding.medColorSlider.getColor(),
                                                        binding.highColorSlider.getColor(),
                                                        binding.thresholdSlider.getProgress()
                                                )
                                        ))
                                .flatMap(deviceMusic -> viewModel.insertDevice(deviceMusic)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .toObservable()
                                );
                    }
                })
                .doOnComplete(this::dismiss)
                .subscribe();

        binding.musicSelectDeviceChip.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    binding.musicSelectDeviceChip.requestDisallowInterceptTouchEvent(true);
                    return true;
                case MotionEvent.ACTION_MOVE:
                    binding.musicSelectDeviceChip.requestDisallowInterceptTouchEvent(true);
                    return false;
                case MotionEvent.ACTION_UP:
                    binding.musicSelectDeviceChip.requestDisallowInterceptTouchEvent(false);
                    return false;
            }
            return false;
        });

        viewModel.addDisposable(RxRecyclerView.childAttachStateChangeEvents(binding.musicSelectDeviceChip)
                .subscribe(recyclerViewChildAttachStateChangeEvent -> {
                    Chip t = (Chip) recyclerViewChildAttachStateChangeEvent.getChild();
                    if (device != null && t.getText().toString().equals(device.getDeviceName())) {
                        setDataBeforeLoad(device, t);
                    }

                    listMusic.reInsertList();
                }));

    }


    /**
     * This sets the data of the device if the device is clicked on
     *
     * @param d - Adds the device to the list
     */
    private void setDataBeforeLoad(DeviceMusic device, Chip d) {
        listMusic.add(device);
        d.setChecked(true);

        binding.lowColorSlider.setInitialColor(device.getLow());
        binding.medColorSlider.setInitialColor(device.getMed());
        binding.highColorSlider.setInitialColor(device.getHigh());
        binding.thresholdSlider.setProgress(device.getIntensity());
    }


    /**
     * This is used for DataBinding.
     *
     * @param v      is The view
     * @param device is the device clicked
     */
    public void devicesToChange(@NonNull CompoundButton v, boolean isChecked, Device device) {
        if (isChecked) {
            if (listMusic.contains(device)) {
                return;
            }
            listMusic.add(device);
        } else {
            if (listMusic.contains(device)) {
                listMusic.remove(device);
            }
        }
    }
}