package com.errorerrorerror.esplightcontrol.views;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.errorerrorerror.esplightcontrol.EspApp;
import com.errorerrorerror.esplightcontrol.databinding.DialogFragmentDevicesBinding;
import com.errorerrorerror.esplightcontrol.model.device.Device;
import com.errorerrorerror.esplightcontrol.model.device_music.DeviceMusic;
import com.errorerrorerror.esplightcontrol.utils.ValidationUtil;
import com.errorerrorerror.esplightcontrol.viewmodel.DevicesCollectionViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.jakewharton.rxbinding3.view.RxView;
import com.jakewharton.rxbinding3.widget.RxTextView;
import com.trello.rxlifecycle3.components.support.RxDialogFragment;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import kotlin.Unit;

public class DialogFragment extends RxDialogFragment {

    private static final String TAG = "DialogFragment";
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private DialogFragmentDevicesBinding binding;
    private DevicesCollectionViewModel collectionViewModel;
    @Nullable
    private String title;
    @Nullable
    private String negative;
    @Nullable
    private String positive;
    private long mode;

    @NonNull
    static DialogFragment newInstance(String title,
                                      String positive,
                                      long mode) {
        DialogFragment f = new DialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("negative", "Cancel");
        args.putString("positive", positive);
        args.putLong("mode", mode);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((EspApp) Objects.requireNonNull(getActivity()).getApplication())
                .getAppComponent()
                .inject(this);

        assert getArguments() != null;
        title = getArguments().getString("title");
        negative = getArguments().getString("negative");
        positive = getArguments().getString("positive");
        mode = getArguments().getLong("mode");

        collectionViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(DevicesCollectionViewModel.class);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DialogFragmentDevicesBinding.inflate(LayoutInflater.from(getContext()), null, false);
        binding.positiveButton.setText(positive);
        binding.negativeButton.setText(negative);
        binding.addTitle.setText(title);
        return new MaterialAlertDialogBuilder(Objects.requireNonNull(getContext()))
                .setView(binding.getRoot()).show();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //setBackground();
        Objects.requireNonNull(getDialog()).setCancelable(false);

        collectionViewModel.addDisposable(
                RxView.clicks(binding.negativeButton)
                        .subscribe(test -> dismiss())
        );

        collectionViewModel.addDisposable(
                collectionViewModel.getAllDevices()
                        .compose(bindToLifecycle())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(devicesList -> {
                            if (mode == -2) {
                                binding.positiveButton.setEnabled(false);
                                add(devicesList);
                            } else {
                                binding.positiveButton.setEnabled(true);
                                edit(devicesList);
                            }
                        }, onError -> Log.e(TAG, "onCreate: ", onError)));

        getDialog().setOnKeyListener((dialog, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                dismiss();
            }
            return false;
        });
    }

    private void add(@NonNull List<Device> devicesList) {

        collectionViewModel.addDisposable(
                observableValidation(devicesList, 1)
                        .flatMap(completable -> RxView.clicks(binding.positiveButton))
                        .map(unit -> new Device(Objects.requireNonNull(binding.deviceName.getText()).toString(),
                                Objects.requireNonNull(binding.ipAddressInput.getText()).toString(),
                                Objects.requireNonNull(binding.portInput.getText()).toString(),
                                "",
                                true,
                                100))
                        .map(device -> new DeviceMusic(
                                device,
                                Color.RED,
                                Color.RED,
                                Color.RED,
                                5))
                        .flatMapCompletable(deviceMusic -> collectionViewModel.insertDevice(deviceMusic)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .ignoreElement()
                                .doOnComplete(this::dismiss)
                        )
                        .subscribe(() -> Log.d(TAG, "add: SUCCESS"), error -> Log.e(TAG, "add: ", error))
        );
    }

    private Observable<Boolean> observableValidation(@NonNull List<Device> devices, int skip) {
        Observable<String> deviceNameObservable = RxTextView.afterTextChangeEvents(binding.deviceName)
                .skip(skip)
                .map(i -> i.getView().getText().toString())
                .map(CharSequence::toString)
                .map(String::trim);

        Observable<String> ipAddressObservable = RxTextView.afterTextChangeEvents(binding.ipAddressInput)
                .skip(skip)
                .map(i -> i.getView().getText().toString())
                .map(CharSequence::toString);

        Observable<Boolean> isPortValid = RxTextView.afterTextChangeEvents(binding.portInput)
                .skip(skip)
                .map(i -> i.getView().getText().toString())
                .map(CharSequence::toString)
                .map(String::trim)
                .map(ValidationUtil::portValid)
                .map(i -> ValidationUtil.validation(i, false, binding.portTextLayout, null, ValidationUtil.INVALID_PORT));

        Observable<Boolean> canAddName = deviceNameObservable
                .map(ValidationUtil::nameValid)
                .zipWith(deviceNameObservable
                        .map(i -> ValidationUtil.nameRepeated(devices, i, mode)), (valid, repeated) -> ValidationUtil.validation(valid, repeated, binding.deviceNameTextLayout, ValidationUtil.USED_NAME, ValidationUtil.INVALID_NAME));
        Observable<Boolean> canAddIp = ipAddressObservable
                .map(ValidationUtil::ipValid)
                .zipWith(ipAddressObservable
                        .map(i -> ValidationUtil.ipRepeated(devices, i, mode)), (valid, repeated) -> ValidationUtil.validation(valid, repeated, binding.ipAddressTextLayout, ValidationUtil.USED_IP, ValidationUtil.INVALID_IP));


        return ValidationUtil.isAllValid(canAddName, canAddIp, isPortValid).map(aBoolean -> {
            binding.positiveButton.setEnabled(aBoolean);
            return aBoolean;
        });
    }


    private void edit(@NonNull List<Device> devicesList) {
        final boolean[] hasSet = {false};
        collectionViewModel.addDisposable(
                observableValidation(devicesList, 0)
                        .flatMap((Function<Boolean, ObservableSource<Device>>) aBoolean -> Observable.fromIterable(devicesList))
                        .filter(i -> {
                            if (!hasSet[0]) {
                                if (i.getId() == mode) {
                                    hasSet[0] = true;
                                    return true;
                                } else {
                                    return false;
                                }
                            } else {
                                return false;
                            }
                        })
                        .map(i -> {
                            binding.deviceName.setText(i.getDeviceName());
                            binding.ipAddressInput.setText(i.getIp());
                            binding.portInput.setText(i.getPort());
                            return i;
                        })
                        .switchMap((Function<Device, ObservableSource<Unit>>) device -> RxView.clicks(binding.positiveButton))
                        .flatMap((Function<Unit, ObservableSource<Device>>) unit -> Observable.fromIterable(devicesList))
                        .filter(i -> i.getId() == mode)
                        .map(device -> {
                            device.setDeviceName(Objects.requireNonNull(binding.deviceName.getText()).toString());
                            device.setIp(Objects.requireNonNull(binding.ipAddressInput.getText()).toString());
                            device.setPort(Objects.requireNonNull(binding.portInput.getText()).toString());
                            return device;
                        })
                        .switchMapCompletable(device -> collectionViewModel.updateDevice(device)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .doOnComplete(this::dismiss))
                        .subscribe(() -> Log.d(TAG, "edit: Deleted Successfully"), error -> Log.e(TAG, "edit: ", error))
        );
    }
}
