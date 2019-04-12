package com.errorerrorerror.esplightcontrol.views;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.animation.CycleInterpolator;

import com.errorerrorerror.esplightcontrol.EspApp;
import com.errorerrorerror.esplightcontrol.R;
import com.errorerrorerror.esplightcontrol.databinding.DialogFragmentDevicesBinding;
import com.errorerrorerror.esplightcontrol.devices.Devices;
import com.errorerrorerror.esplightcontrol.utils.Constants;
import com.errorerrorerror.esplightcontrol.utils.ValidationUtil;
import com.errorerrorerror.esplightcontrol.viewmodel.DevicesCollectionViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.jakewharton.rxbinding3.view.RxView;
import com.trello.rxlifecycle3.components.support.RxDialogFragment;

import java.util.Objects;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DialogFragment extends RxDialogFragment {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private DialogFragmentDevicesBinding devicesBinding;
    private DevicesCollectionViewModel collectionViewModel;
    private ValidationUtil validationUtil;
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
                .getApplicationComponent()
                .inject(this);

        assert getArguments() != null;
        title = getArguments().getString("title");
        negative = getArguments().getString("negative");
        positive = getArguments().getString("positive");
        mode = getArguments().getLong("mode");

        collectionViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(DevicesCollectionViewModel.class);

        collectionViewModel.addDisposable(
                collectionViewModel.getAllDevices()
                        .compose(bindToLifecycle())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(devicesList -> {
                            validationUtil = new ValidationUtil(devicesList,
                                    Objects.requireNonNull(getContext()).getResources().getColor(R.color.colorAccent));

                            if (mode == -2) {
                                addDevice();
                            } else {
                                editDevice(mode);
                            }
                        }));

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
                        collectionViewModel.insertEditDevice(new Devices(Objects.requireNonNull(devicesBinding.deviceName.getText()).toString(),
                                Objects.requireNonNull(devicesBinding.IPAddressInput.getText()).toString(),
                                Objects.requireNonNull(devicesBinding.portInput.getText()).toString(),
                                "",
                                true,
                                false))
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
        collectionViewModel.addDisposable(collectionViewModel.getDeviceWithId(mode)
                .compose(bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(devices -> {
                    devicesBinding.deviceName.setText(devices.getDevice());
                    devicesBinding.IPAddressInput.setText(devices.getIp());
                    devicesBinding.portInput.setText(devices.getPort());
                    testBoolean[0] = devices.isOn();
                    testBoolean[1] = devices.isOpen();
                })
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
                Devices device = new Devices(Objects.requireNonNull(devicesBinding.deviceName.getText()).toString(),
                        Objects.requireNonNull(devicesBinding.IPAddressInput.getText()).toString(),
                        Objects.requireNonNull(devicesBinding.portInput.getText()).toString(),
                        "",
                        testBoolean[0],
                        testBoolean[1]);

                device.setId(mode);

                collectionViewModel.addDisposable(
                        collectionViewModel.insertEditDevice(device)
                                .compose(bindToLifecycle())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe()
                );

                dismiss();
            }
        }));
    }



    private void setBackground() {
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }
}
