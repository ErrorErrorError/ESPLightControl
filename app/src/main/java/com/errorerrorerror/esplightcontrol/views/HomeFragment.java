package com.errorerrorerror.esplightcontrol.views;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.errorerrorerror.esplightcontrol.EspApp;
import com.errorerrorerror.esplightcontrol.R;
import com.errorerrorerror.esplightcontrol.adapter.RecyclerDeviceAdapter;
import com.errorerrorerror.esplightcontrol.databinding.DeviceDialogSettingsBinding;
import com.errorerrorerror.esplightcontrol.databinding.HomeFragmentBinding;
import com.errorerrorerror.esplightcontrol.devices.Devices;
import com.errorerrorerror.esplightcontrol.interfaces.OnClickedDevice;
import com.errorerrorerror.esplightcontrol.interfaces.OnClickedSwitch;
import com.errorerrorerror.esplightcontrol.utils.DialogCreateUtil;
import com.errorerrorerror.esplightcontrol.utils.DisplayUtils;
import com.errorerrorerror.esplightcontrol.utils.ValidationUtil;
import com.errorerrorerror.esplightcontrol.viewmodel.DevicesCollectionViewModel;
import com.jakewharton.rxbinding3.view.RxView;
import com.nightonke.jellytogglebutton.JellyToggleButton;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.functions.Consumer;

public class HomeFragment extends Fragment implements OnClickedDevice, OnClickedSwitch {
    //private static final String TAG = "HomeFragment";

    private final DialogCreateUtil createDialog = new DialogCreateUtil();

    //ViewModel Injector
    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private DevicesCollectionViewModel collectionViewModel;
    //Utils
    private ValidationUtil validationUtil;

    private RecyclerDeviceAdapter adapter;


    private HomeFragmentBinding homeBinding;
    private DeviceDialogSettingsBinding deviceDialogBinding;


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
        homeBinding = HomeFragmentBinding.inflate(inflater, container, false);

        return homeBinding.getRoot();
    }

    @SuppressLint("CheckResult")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Implements ViewModel to HomeFragment
        collectionViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(DevicesCollectionViewModel.class);

        //Listens for devices
        devicesListeners();

        RxView.clicks(homeBinding.addDeviceButton)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe((Consumer<Object>) o -> showAddDialog());
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Changes Vector drawable to png if less than 24api. Vector uses gradient and is only supported 24 =< x
        setAddDeviceBackground();

        //sets up RecyclerView Listeners
        initRecyclerLayers();
    }

    private void setAddDeviceBackground() //if lower than 24 api, usees png
    {
        if (Build.VERSION.SDK_INT < 24) {
            homeBinding.linearLayoutAdddevice.setBackgroundResource(R.drawable.cardview_background_gradient_for_lowerend_devices);
        } else {
            homeBinding.linearLayoutAdddevice.setBackgroundResource(R.drawable.ic_cardview_background_gradient);
        }
    }

    private void initRecyclerLayers() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                RecyclerView.VERTICAL, true);
        linearLayoutManager.setStackFromEnd(true);

        //Custom Height so the remove item animation works on every device screen
        ViewGroup.LayoutParams params = homeBinding.recyclerviewAddDevice.getLayoutParams();
        DisplayUtils displayUtils = new DisplayUtils(params,
                getContext());
        homeBinding.recyclerviewAddDevice.setLayoutParams(displayUtils.getRecyclerViewHeight());
        homeBinding.recyclerviewAddDevice.setLayoutManager(linearLayoutManager);

        //Set No Device Connected text based on height
        int textHeight = (int) (params.height / 2.1);
        LinearLayout.LayoutParams textL = (LinearLayout.LayoutParams) homeBinding.noDeviceConnectedText.getLayoutParams();
        textL.setMargins(0, textHeight, 0, 0);
        homeBinding.noDeviceConnectedText.setLayoutParams(textL);

        //Gains performance
        homeBinding.recyclerviewAddDevice.setHasFixedSize(true);
        Objects.requireNonNull(homeBinding.recyclerviewAddDevice.getItemAnimator())
                .setChangeDuration(0); //Removes onChange Animation
    }

    private void devicesListeners() {

        adapter = new RecyclerDeviceAdapter(HomeFragment.this, HomeFragment.this);
        homeBinding.recyclerviewAddDevice.setAdapter(adapter);

        collectionViewModel.getAllDevices().observe(getViewLifecycleOwner(),
                devicesList -> {
                    //Adds to adapter
                    adapter.submitList(devicesList);

                    //Validation tool
                    validationUtil = new ValidationUtil(devicesList, getContext());

                    //Hides text if not empty
                    if (devicesList.isEmpty()) {
                        homeBinding.noDeviceConnectedText.animate().alpha(1.0f).setStartDelay(200)
                                .setListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {
                                        homeBinding.noDeviceConnectedText.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animation) {

                                    }
                                })
                                .setDuration(800);
                    } else {
                        homeBinding.noDeviceConnectedText.setVisibility(View.GONE);
                    }
                });


        //Scroll to top on new item
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                homeBinding.recyclerviewAddDevice.smoothScrollToPosition(positionStart);
            }
        });
    }

    private void showAddDialog() //Shows Dialog to add device information
    {

        createDialog.setTitle("Add Device Info");
        createDialog.setPositiveButtonText("Add");
        createDialog.setNegativeButtonText("Cancel");
        createDialog.setViewDialog(initDialogView());
        createDialog.setContext(getContext());

        AlertDialog alertDialog = createDialog.getDialogCreated();
        alertDialog
                .getButton(DialogInterface.BUTTON_POSITIVE)
                .setOnClickListener(view -> {
                    //Tests input
                    boolean test = validationUtil.testAllAdd(Objects.requireNonNull(deviceDialogBinding.deviceName.getText()).toString(),
                            Objects.requireNonNull(deviceDialogBinding.IPAddressInput.getText()).toString(),
                            Objects.requireNonNull(deviceDialogBinding.portInput.getText()).toString(),
                            deviceDialogBinding.deviceNameTextLayout,
                            deviceDialogBinding.ipAddressTextLayout,
                            deviceDialogBinding.portTextLayout);
                    if (!test) {
                        createDialog.shakeAnim(alertDialog);
                    } else {
                        // Add input to Database if there is input
                        // dismiss the dialog
                        Devices devices = new Devices(deviceDialogBinding.deviceName.getText().toString(),
                                deviceDialogBinding.IPAddressInput.getText().toString(),
                                deviceDialogBinding.portInput.getText().toString(),
                                "",
                                true);

                        collectionViewModel.addDevice(devices);
                        alertDialog.dismiss();
                    }
                });
    }

    private View initDialogView() {
        @SuppressLint("InflateParams") View dialogView = LayoutInflater.from(HomeFragment.this.getActivity())
                .inflate(R.layout.device_dialog_settings, null);
        deviceDialogBinding = DeviceDialogSettingsBinding.bind(dialogView);

        return dialogView;
    }

    @Override
    public void onRemoveDeviceClicked(int position) {
        if (position < 0) {
            return;
        }

        collectionViewModel.deleteDevice(adapter.getCurrentList().get(position));
    }

    @Override
    public void onEditDeviceClicked(int position) {


        //Edits dialog
        createDialog.setTitle("Edit Device Info");
        createDialog.setPositiveButtonText("Edit");
        createDialog.setNegativeButtonText("Cancel");
        createDialog.setViewDialog(initDialogView());
        createDialog.setContext(getContext());
        deviceDialogBinding.deviceName.setText(adapter.getCurrentList().get(position).getDevice());
        deviceDialogBinding.IPAddressInput.setText(adapter.getCurrentList().get(position).getIp());
        deviceDialogBinding.portInput.setText(adapter.getCurrentList().get(position).getPort());

        // Replace the "Edit" button's click listener.

        AlertDialog alertDialog = createDialog.getDialogCreated();
        alertDialog
                .getButton(DialogInterface.BUTTON_POSITIVE)
                .setOnClickListener(view -> {
                    boolean test = validationUtil.testAllEdit(Objects.requireNonNull(deviceDialogBinding.deviceName.getText()).toString(),
                            Objects.requireNonNull(deviceDialogBinding.IPAddressInput.getText()).toString(),
                            Objects.requireNonNull(deviceDialogBinding.portInput.getText()).toString(),
                            position,
                            deviceDialogBinding.deviceNameTextLayout,
                            deviceDialogBinding.ipAddressTextLayout,
                            deviceDialogBinding.portTextLayout);
                    if (!test) {
                        createDialog.shakeAnim(alertDialog);
                    } else {
                        // Edit input to Database if there is input
                        // dismiss the dialog
                        Devices device = new Devices(deviceDialogBinding.deviceName.getText().toString(),
                                deviceDialogBinding.IPAddressInput.getText().toString(),
                                deviceDialogBinding.portInput.getText().toString(),
                                "",
                                adapter.getCurrentList().get(position).isOn());

                        device.setId(adapter.getItemId(position));
                        collectionViewModel.editDevice(device);
                        alertDialog.dismiss();
                    }
                });
    }

    @Override
    public void OnSwitched(Boolean bool, int position, JellyToggleButton buttonView) {
        if (position < 0) {
            return;
        }
        collectionViewModel.setSwitchConnection(bool, adapter.getCurrentList().get(position).getId());
    }
}

