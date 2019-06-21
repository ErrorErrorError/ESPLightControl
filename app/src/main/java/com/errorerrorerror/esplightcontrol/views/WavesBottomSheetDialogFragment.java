package com.errorerrorerror.esplightcontrol.views;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration;
import com.errorerrorerror.esplightcontrol.EspApp;
import com.errorerrorerror.esplightcontrol.R;
import com.errorerrorerror.esplightcontrol.adapter.ListModesAdapter;
import com.errorerrorerror.esplightcontrol.databinding.WavesModeBinding;
import com.errorerrorerror.esplightcontrol.model.device.Device;
import com.errorerrorerror.esplightcontrol.model.device_waves.DeviceWaves;
import com.errorerrorerror.esplightcontrol.utils.ObservableList;
import com.errorerrorerror.esplightcontrol.viewmodel.DevicesCollectionViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.jakewharton.rxbinding3.recyclerview.RxRecyclerView;
import com.jakewharton.rxbinding3.view.RxView;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import top.defaults.colorpicker.ColorWheelSelector;

public class WavesBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private static final String TAG = "WavesBottomSheetDialog";
    private WavesModeBinding binding;
    @NonNull
    private ObservableList<Device> listWaves = new ObservableList<>();
    private ObservableList<Integer> colors = new ObservableList<>(6);

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private DevicesCollectionViewModel viewModel;
    @Nullable
    private DeviceWaves device;

    public WavesBottomSheetDialogFragment(@NotNull DeviceWaves device) {
        this.device = device;
    }

    public WavesBottomSheetDialogFragment() {
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
        binding = WavesModeBinding.inflate(inflater, container, false);

        binding.setViewModel(viewModel);
        binding.setWavesView(this);
        colors.reInsertList();
        return binding.getRoot();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listWaves.reInsertList();
        colors.reInsertList();

        ChipsLayoutManager chipsLayoutManager = ChipsLayoutManager.newBuilder(getContext())
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .build();

        //First Palette
        createFirstPalette();

        binding.wavesSelectDeviceChip.setLayoutManager(chipsLayoutManager);
        binding.wavesSelectDeviceChip.setAdapter(new ListModesAdapter(this));
        binding.wavesSelectDeviceChip.addItemDecoration(new SpacingItemDecoration(getResources().getDimensionPixelOffset(R.dimen.item_space_horizontal),
                getResources().getDimensionPixelOffset(R.dimen.item_space_vertical)));

        colors.getObservableList()
                .map(list -> {
                    binding.palleteText.setText(String.format(Locale.ENGLISH, "Palettes: %d", list.size()));
                    return list;
                })
                .map(List::size)
                .map(size -> size < 6)
                .doOnNext(aBoolean -> binding.addSelectorColor.setEnabled(aBoolean))
                .subscribe();

        RxView.clicks(binding.addSelectorColor)
                .doOnNext(unit -> addView(Color.WHITE))
                .subscribe();

        listWaves.getObservableList()
                .map(list -> !list.isEmpty())
                .doOnNext(bool -> binding.addWavesButton.setEnabled(bool))
                .switchMap(bool -> RxView.clicks(binding.addWavesButton))
                .take(1)
                .switchMap(unit -> Observable.fromIterable(listWaves.getList()))
                .flatMap((Function<Device, ObservableSource<?>>) device -> {
                    if (device instanceof DeviceWaves) {
                        ((DeviceWaves) device).setSpeed(100);
                        ((DeviceWaves) device).setColors(colors.getList());
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
                                                new DeviceWaves(
                                                        device,
                                                        100,
                                                        colors.getList())
                                        ))
                                .flatMap(deviceWaves -> viewModel.insertDevice(deviceWaves)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .toObservable()
                                );
                    }
                })
                .doOnComplete(this::dismiss)
                .subscribe();

        binding.wavesSelectDeviceChip.setOnTouchListener((v, event) ->
        {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    binding.wavesSelectDeviceChip.requestDisallowInterceptTouchEvent(true);
                    return true;
                case MotionEvent.ACTION_MOVE:
                    binding.wavesSelectDeviceChip.requestDisallowInterceptTouchEvent(true);
                    return false;
                case MotionEvent.ACTION_UP:
                    binding.wavesSelectDeviceChip.requestDisallowInterceptTouchEvent(false);
                    return false;
            }
            return false;
        });

        binding.wavesColorPicker.setOnTouchListener((v, event) ->
        {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    binding.wavesSelectDeviceChip.requestDisallowInterceptTouchEvent(true);
                    return true;
                case MotionEvent.ACTION_MOVE:
                    binding.wavesSelectDeviceChip.requestDisallowInterceptTouchEvent(true);
                    return false;
                case MotionEvent.ACTION_UP:
                    binding.wavesSelectDeviceChip.requestDisallowInterceptTouchEvent(false);
                    return false;
            }
            return false;
        });

        viewModel.addDisposable(RxRecyclerView.childAttachStateChangeEvents(binding.wavesSelectDeviceChip)
                .subscribe(recyclerViewChildAttachStateChangeEvent -> {
                    Chip t = (Chip) recyclerViewChildAttachStateChangeEvent.getChild();
                    if (device != null && t.getText().toString().equals(device.getDeviceName())) {
                        setDataBeforeLoad(device, t);
                    }
                    listWaves.reInsertList();
                    colors.reInsertList();
                }));
    }

    private void setDataBeforeLoad(@NonNull DeviceWaves t, Chip d) {
        listWaves.add(t);
        colors.remove(0);
        colors.addAll(t.getColors());
        d.setChecked(true);
        binding.wavesColorPicker.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                binding.wavesColorPicker.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                binding.wavesColorPicker.setColor(t.getColors().get(0), null);
                for (int i = 1; i < colors.getList().size(); i++) {
                    addView(colors.getList().get(i));
                }
            }
        });
    }

    public void devicesToChange(@NonNull CompoundButton v, boolean isChecked, Device device) {
        if (isChecked) {
            if (listWaves.contains(device)) {
                return;
            }
            listWaves.add(device);
        } else {
            if (listWaves.contains(device)) {
                listWaves.remove(device);
            }
        }
    }

    private void createFirstPalette() {
        View p = new View(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1);
        params.width = params.width / 6;
        params.setMargins(10, 10, 10, 10);
        p.setLayoutParams(params);
        binding.colorPallete.addView(p);
        binding.wavesColorPicker.getMainSelector().setColorListener(color -> {
            p.setBackgroundColor(color);
            if (colors.getList().size() == 0) {
                colors.add(0, color);
            } else {
                colors.set(0, color);
            }
        });
    }

    private void addView(int colorz) {
        View p = new View(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1);
        params.width = params.width / 6;
        params.setMargins(10, 10, 10, 10);

        ColorWheelSelector selector = binding.wavesColorPicker.addSelector(colorz, getContext());
        int latestSelector = binding.wavesColorPicker.getSelectorList().size();
        selector.setColorListener(color -> {
            p.setBackgroundColor(color);
            if (colors.getList().size() == latestSelector) {
                colors.add(latestSelector, color);
            } else {
                colors.set(latestSelector, color);
            }
        });

        p.setOnLongClickListener(v -> {
            binding.colorPallete.removeView(v);
            colors.remove(Integer.valueOf(selector.getColor()));
            binding.wavesColorPicker.removeView(selector);
            return true;
        });

        binding.colorPallete.addView(p, params);
    }
}
