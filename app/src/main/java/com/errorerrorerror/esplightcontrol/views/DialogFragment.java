package com.errorerrorerror.esplightcontrol.views;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.animation.CycleInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.errorerrorerror.esplightcontrol.EspApp;
import com.errorerrorerror.esplightcontrol.R;
import com.errorerrorerror.esplightcontrol.databinding.DialogFragmentDevicesBinding;
import com.errorerrorerror.esplightcontrol.model.device.Device;
import com.errorerrorerror.esplightcontrol.utils.Constants;
import com.errorerrorerror.esplightcontrol.utils.DisplayUtils;
import com.errorerrorerror.esplightcontrol.utils.ValidationUtil;
import com.errorerrorerror.esplightcontrol.viewmodel.DevicesCollectionViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.jakewharton.rxbinding3.view.RxView;
import com.trello.rxlifecycle3.components.support.RxDialogFragment;

import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DialogFragment extends RxDialogFragment {

    private static final String TAG = "DialogFragment";
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private DialogFragmentDevicesBinding devicesBinding;
    private DevicesCollectionViewModel collectionViewModel;
    private ValidationUtil validationUtil = new ValidationUtil();
    private String title;
    private String negative;
    private String positive;
    private long mode;

    static DialogFragment newInstance(String title,
                                      String negative,
                                      String positive,
                                      long mode) {
        DialogFragment f = new DialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("negative", negative);
        args.putString("positive", positive);
        args.putLong("mode", mode);
        f.setArguments(args);
        return f;
    }

    private void shakeAnim() //Shakes dialog animation
    {
        Objects.requireNonNull(Objects.requireNonNull(this.getDialog())
                .getWindow())
                .getDecorView()
                .animate()
                .translationX(16f)
                .setInterpolator(new CycleInterpolator(7f));
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
        devicesBinding = DialogFragmentDevicesBinding.inflate(LayoutInflater.from(getContext()), null, false);
        devicesBinding.positiveButton.setText(positive);
        devicesBinding.negativeButton.setText(negative);
        devicesBinding.addTitle.setText(title);
        return new MaterialAlertDialogBuilder(Objects.requireNonNull(getContext()), DialogFragment.STYLE_NO_TITLE)
                .setView(devicesBinding.getRoot()).show();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setBackground();
        Objects.requireNonNull(getDialog()).setCancelable(false);

        collectionViewModel.addDisposable(
                RxView.clicks(devicesBinding.negativeButton)
                        .subscribe(test -> dismiss())
        );

        validationUtil.setColorError(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.redDelete));

        collectionViewModel.addDisposable(
                collectionViewModel.getAllDevices()
                        .compose(bindToLifecycle())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(devicesList -> {
                                validationUtil.updateDeviceList(devicesList);
                            if (mode == -2) {
                                addDevice();
                            } else {
                                editDevice(mode);
                            }
                        }, onError -> Log.e(TAG, "onCreate: ",onError )));


    }

    private void addDevice() {
        collectionViewModel.addDisposable(RxView.clicks(devicesBinding.positiveButton)
                .compose(bindToLifecycle())
                .subscribe(unit -> {
                    boolean test = validationUtil
                            .testAllAdd(devicesBinding);

                    if (!test) {
                        shakeAnim();
                    } else {
                        // Add input to Database if there is input
                        // dismiss the dialog
                        collectionViewModel.addDisposable(
                                collectionViewModel.insertDevice(new Device(Objects.requireNonNull(devicesBinding.deviceName.getText()).toString(),
                                        Objects.requireNonNull(devicesBinding.IPAddressInput.getText()).toString(),
                                        Objects.requireNonNull(devicesBinding.portInput.getText()).toString(),
                                        "",
                                        true,
                                        false,
                                        100))
                                        .compose(bindToLifecycle())
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(() -> Log.d(Constants.DIALOG_TAG, "Added Device Successfully on thread: " + Thread.currentThread().getName()),
                                                onError -> Log.e(Constants.DIALOG_TAG, "addDevice: ", onError))
                        );
                        dismiss();
                    }

                }));
    }

    private void editDevice(long mode) {

        final boolean[] testBoolean = new boolean[2];
        final int[] testInt = new int[1];
        collectionViewModel.addDisposable(collectionViewModel.getDeviceWithId(mode)
                .compose(bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(devices -> {
                    devicesBinding.deviceName.setText(devices.getDeviceName());
                    devicesBinding.IPAddressInput.setText(devices.getIp());
                    devicesBinding.portInput.setText(devices.getPort());
                    testBoolean[0] = devices.getOn();
                    testBoolean[1] = devices.isOpen();
                    testInt[0] = devices.getBrightness();
                }, onError -> Log.e(TAG, "editDevice: ", onError))
        );

        collectionViewModel.addDisposable(RxView.clicks(devicesBinding.positiveButton)
                .compose(bindToLifecycle())
                .subscribe(s -> {
                    boolean test = validationUtil.testAllEdit(devicesBinding, mode);
                    if (!test) {
                        shakeAnim();
                    } else {
                        // Edit input to Database if there is input
                        // dismiss the dialog


                            Device device = new Device(Objects.requireNonNull(devicesBinding.deviceName.getText()).toString(),
                                Objects.requireNonNull(devicesBinding.IPAddressInput.getText()).toString(),
                                Objects.requireNonNull(devicesBinding.portInput.getText()).toString(),
                                "",
                                testBoolean[0],
                                testBoolean[1],
                                testInt[0]);

                        device.setId(mode);

                        collectionViewModel.addDisposable(
                                collectionViewModel.updateDevice(device)
                                        .compose(bindToLifecycle())
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(() -> Log.d(TAG, "editDevice: Edited Device Successfully"), onError -> Log.e(TAG, "editDevice: ", onError))
                        );

                        dismiss();
                    }
                }, onError -> Log.e(TAG, "editDevice: ", onError)));
    }

    private void setBackground() {
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().setLayout((int) DisplayUtils.convertDpToPixel(375, Objects.requireNonNull(getContext())),
                    (int) DisplayUtils.convertDpToPixel(375, Objects.requireNonNull(getContext())));
        }
    }
}
