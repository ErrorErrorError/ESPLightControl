package com.errorerrorerror.esplightcontrol.views;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.CycleInterpolator;

import com.errorerrorerror.esplightcontrol.EspApp;
import com.errorerrorerror.esplightcontrol.R;
import com.errorerrorerror.esplightcontrol.databinding.DialogFragmentDevicesBinding;
import com.errorerrorerror.esplightcontrol.devices.Devices;
import com.errorerrorerror.esplightcontrol.utils.Constants;
import com.errorerrorerror.esplightcontrol.utils.ValidationUtil;
import com.errorerrorerror.esplightcontrol.viewmodel.DevicesCollectionViewModel;
import com.jakewharton.rxbinding3.view.RxView;
import com.trello.rxlifecycle3.components.support.RxDialogFragment;

import java.util.Objects;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
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
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        devicesBinding = DialogFragmentDevicesBinding.inflate(inflater, container, false);
        setCancelable(false);

        return devicesBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setBackground();

        collectionViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(DevicesCollectionViewModel.class);

        assert getArguments() != null;
        String title = getArguments().getString("title");
        String negative = getArguments().getString("negative");
        String positive = getArguments().getString("positive");
        long mode = getArguments().getLong("mode");

        devicesBinding.addTitle.setText(title);
        devicesBinding.positiveButton.setText(positive);
        devicesBinding.negativeButton.setText(negative);

        devicesBinding.negativeButton.setOnClickListener(v -> dismiss());
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
        //Sets background
        Objects.requireNonNull(Objects.requireNonNull(getDialog())
                .getWindow()).setBackgroundDrawable(ContextCompat
                .getDrawable(Objects.requireNonNull(getContext()), R.drawable.dialog_shape));

        //Sets curved corners on dialog
        getDialog().getWindow().setLayout(
                (int) getContext().getResources().getDisplayMetrics().density * 475,
                Objects.requireNonNull(getDialog().getWindow()).getAttributes().height);
    }
}
