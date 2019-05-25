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
import com.errorerrorerror.esplightcontrol.databinding.WavesModeBinding;
import com.errorerrorerror.esplightcontrol.model.device.Device;
import com.errorerrorerror.esplightcontrol.model.device_waves.DeviceWaves;
import com.errorerrorerror.esplightcontrol.utils.ObservableList;
import com.errorerrorerror.esplightcontrol.viewmodel.DevicesCollectionViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.jakewharton.rxbinding3.view.RxView;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class WavesBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private static final String TAG = "WavesBottomSheetDialog";
    private WavesModeBinding binding;
    //private List<Device> listWaves = new ArrayList<>();
    @NonNull
    private ObservableList<Device> listWaves = new ObservableList<>();
    //private PublishSubject<List<Device>> listSubject = PublishSubject.create();

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private DevicesCollectionViewModel viewModel;
    @Nullable
    private Device device;

    public WavesBottomSheetDialogFragment(Device device) {
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

        binding.wavesSelectDeviceChip.setLayoutManager(chipsLayoutManager);
        binding.wavesSelectDeviceChip.setAdapter(new ListModesAdapter(this));
        binding.wavesSelectDeviceChip.addItemDecoration(new SpacingItemDecoration(getResources().getDimensionPixelOffset(R.dimen.item_space),
                getResources().getDimensionPixelOffset(R.dimen.item_space)));

        listWaves.getObservableList()
                .map(list -> !list.isEmpty())
                .doOnNext(bool -> binding.addWavesButton.setEnabled(bool))
                .switchMap(bool -> RxView.clicks(binding.addWavesButton))
                .take(1)
                .switchMap(unit -> Observable.fromIterable(listWaves.getList()))
                .flatMap((Function<Device, ObservableSource<?>>) device -> {
                    if (device instanceof DeviceWaves) {
                        ((DeviceWaves) device).setSpeed(100);
                        ((DeviceWaves) device).setPrimaryColor(binding.wavesColorPicker.getColor());
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
                                                        device.getId(),
                                                        device,
                                                        100,
                                                        binding.wavesColorPicker.getColor())
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


        binding.wavesColorPicker.setOnTouchListener((v, event) ->

        {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    binding.wavesColorPicker.requestDisallowInterceptTouchEvent(true);
                    return true;
                case MotionEvent.ACTION_MOVE:
                    binding.wavesColorPicker.requestDisallowInterceptTouchEvent(true);
                    return false;
                case MotionEvent.ACTION_UP:
                    binding.wavesColorPicker.requestDisallowInterceptTouchEvent(false);
                    return false;
            }
            return false;
        });

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

        final int[] count = {0};
        binding.wavesSelectDeviceChip.getViewTreeObserver().
                addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        count[0]++;
                        if (count[0] >= 3) {
                            if (device != null) {
                                setDataBeforeLoad(device);
                            }
                            listWaves.reInsertList();
                            binding.wavesSelectDeviceChip.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            device = null;
                        }
                    }
                });

    }

    private void setDataBeforeLoad(@NonNull Device t) {
        listWaves.add(t);
        for (int i = 0; i < binding.wavesSelectDeviceChip.getChildCount(); i++) {
            Chip child = (Chip) binding.wavesSelectDeviceChip.getChildAt(i);

            if (child.getText().equals(device.getDeviceName())) {
                child.setChecked(true);
            }
        }

        binding.wavesColorPicker.setColor(((DeviceWaves) t).getPrimaryColor(), true);
    }


    private void initRecyclerViews() {
        binding.wavesColorPicker.setColor(Color.WHITE, true);
    }


    public void devicesToChange(@NonNull View v, Device device) {
        if (listWaves.contains(device)) {
            listWaves.remove(device);
            ((Chip) v).setChecked(false);
        } else {
            listWaves.add(device);
            ((Chip) v).setChecked(true);
        }
    }
}
