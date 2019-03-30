package com.errorerrorerror.esplightcontrol.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.CycleInterpolator;

import com.errorerrorerror.esplightcontrol.EspApp;
import com.errorerrorerror.esplightcontrol.R;
import com.errorerrorerror.esplightcontrol.databinding.DialogFragmentDevicesBinding;
import com.errorerrorerror.esplightcontrol.devices.Devices;
import com.errorerrorerror.esplightcontrol.utils.ValidationUtil;
import com.errorerrorerror.esplightcontrol.viewmodel.DevicesCollectionViewModel;
import com.jakewharton.rxbinding3.view.RxView;

import java.util.Objects;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import io.reactivex.disposables.CompositeDisposable;

public class DialogFragment extends androidx.fragment.app.DialogFragment {
    //private static final String TAG = "DialogFragment";
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private DialogFragmentDevicesBinding devicesBinding;
    private DevicesCollectionViewModel collectionViewModel;
    private ValidationUtil validationUtil;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    static DialogFragment newInstance(String title,
                                      String message,
                                      String negative,
                                      String positive,
                                      long mode) {
        DialogFragment f = new DialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("message", message);
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

        collectionViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(DevicesCollectionViewModel.class);

        assert getArguments() != null;
        String title = getArguments().getString("title");
        String message = getArguments().getString("message");
        String negative = getArguments().getString("negative");
        String positive = getArguments().getString("positive");
        long mode = getArguments().getLong("mode");

        //Sets background
        Objects.requireNonNull(Objects.requireNonNull(getDialog())
                .getWindow()).setBackgroundDrawable(ContextCompat
                .getDrawable(Objects.requireNonNull(getContext()), R.drawable.dialog_shape));

        //Sets curved corners on dialog
        getDialog().getWindow().setLayout(
                (int) getContext().getResources().getDisplayMetrics().density * 475,
                Objects.requireNonNull(getDialog().getWindow()).getAttributes().height
        );

        devicesBinding.addTitle.setText(title);
        devicesBinding.addMessage.setText(message);
        devicesBinding.positiveButton.setText(positive);
        devicesBinding.negativeButton.setText(negative);

        devicesBinding.negativeButton.setOnClickListener(v -> dismiss());
        collectionViewModel.getAllDevices().observe(getViewLifecycleOwner(),
                devicesList ->
                        validationUtil = new ValidationUtil(devicesList, getContext())
        );

        if (mode == -2) {

            compositeDisposable.add(RxView.clicks(devicesBinding.positiveButton).subscribe(unit -> {
                boolean test = validationUtil.testAllAdd(Objects.requireNonNull(devicesBinding.deviceName.getText()).toString(),
                        Objects.requireNonNull(devicesBinding.IPAddressInput.getText()).toString(),
                        Objects.requireNonNull(devicesBinding.portInput.getText()).toString(),
                        devicesBinding.deviceNameTextLayout,
                        devicesBinding.ipAddressTextLayout,
                        devicesBinding.portTextLayout);

                if (!test) {
                    shakeAnim();
                } else {
                    // Add input to Database if there is input
                    // dismiss the dialog

                    collectionViewModel.addDevice(
                            new Devices(devicesBinding.deviceName.getText().toString(),
                                    devicesBinding.IPAddressInput.getText().toString(),
                                    devicesBinding.portInput.getText().toString(),
                                    "",
                                    true));
                    dismiss();
                }
            }));

        } else {

            Devices temp = collectionViewModel.getDeviceWithId(mode);
            devicesBinding.deviceName.setText(temp.getDevice());
            devicesBinding.IPAddressInput.setText(temp.getIp());
            devicesBinding.portInput.setText(temp.getPort());

            compositeDisposable.add(RxView.clicks(devicesBinding.positiveButton).subscribe(s -> {
                boolean test = validationUtil.testAllEdit(Objects.requireNonNull(devicesBinding.deviceName.getText()).toString(),
                        Objects.requireNonNull(devicesBinding.IPAddressInput.getText()).toString(),
                        Objects.requireNonNull(devicesBinding.portInput.getText()).toString(),
                        mode,
                        devicesBinding.deviceNameTextLayout,
                        devicesBinding.ipAddressTextLayout,
                        devicesBinding.portTextLayout);
                if (!test) {
                    shakeAnim();
                } else {
                    // Edit input to Database if there is input
                    // dismiss the dialog
                    Devices device = new Devices(devicesBinding.deviceName.getText().toString(),
                            devicesBinding.IPAddressInput.getText().toString(),
                            devicesBinding.portInput.getText().toString(),
                            "",
                            temp.isOn());

                    device.setId(mode);
                    collectionViewModel.editDevice(device);
                    dismiss();
                }
            }));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        compositeDisposable.dispose();
    }
}
