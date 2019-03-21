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
import android.widget.TextView;

import com.errorerrorerror.esplightcontrol.EspApp;
import com.errorerrorerror.esplightcontrol.Interface.OnClickedDevice;
import com.errorerrorerror.esplightcontrol.Interface.OnClickedSwitch;
import com.errorerrorerror.esplightcontrol.R;
import com.errorerrorerror.esplightcontrol.adapter.RecyclerDeviceAdapter;
import com.errorerrorerror.esplightcontrol.devices.Devices;
import com.errorerrorerror.esplightcontrol.utils.DialogCreateUtil;
import com.errorerrorerror.esplightcontrol.utils.DisplayUtils;
import com.errorerrorerror.esplightcontrol.utils.ValidationUtil;
import com.errorerrorerror.esplightcontrol.viewmodel.DevicesCollectionViewModel;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.nightonke.jellytogglebutton.JellyToggleButton;

import java.util.Objects;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment implements OnClickedDevice, OnClickedSwitch {
    private static final String TAG = "HomeFragment";

    private final DialogCreateUtil createDialog = new DialogCreateUtil();

    //ViewModel Injector
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private DevicesCollectionViewModel collectionViewModel;
    //Utils
    private ValidationUtil validationUtil;
    //Recyclerview
    private RecyclerView recyclerView;
    private LinearLayout linearLayoutAddDeviceBackgroud;
    private RecyclerDeviceAdapter adapter;

    //Views In HomeFragment
    private TextView notConnectedText;
    private MaterialCardView materialCardView;


    //Text Input
    private TextInputLayout textInputLayoutName;
    private TextInputLayout textInputLayoutIP;
    private TextInputLayout textInputLayoutPort;
    private TextInputEditText devName;
    private TextInputEditText devIp;
    private TextInputEditText devPort;


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
        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Implements ViewModel to HomeFragment
        collectionViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(DevicesCollectionViewModel.class);

        //Listens for devices
        devicesListeners();
        materialCardView.setOnClickListener(v -> showAddDialog());

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Bind views
        BindingViews.HomeBindingsViews homeBindingsViews = new BindingViews.HomeBindingsViews();
        ButterKnife.bind(homeBindingsViews, view);
        recyclerView = homeBindingsViews.recyclerView;
        materialCardView = homeBindingsViews.materialCardView;
        notConnectedText = homeBindingsViews.notConnectedText;
        linearLayoutAddDeviceBackgroud = homeBindingsViews.linearLayoutAddDeviceBackgroud;

        //Changes Vector drawable to png if less than 24api. Vector uses gradient and is only supported 24 =< x
        setAddDeviceBackground();

        //sets up Recyclerview 7 Listeners
        initRecyclerLayers();
    }

    private void setAddDeviceBackground() //if lower than 24 api, usees png
    {
        if (Build.VERSION.SDK_INT < 24) {
            linearLayoutAddDeviceBackgroud.setBackgroundResource(R.drawable.cardview_background_gradient_for_lowerend_devices);
        } else {
            linearLayoutAddDeviceBackgroud.setBackgroundResource(R.drawable.ic_cardview_background_gradient);
        }
    }

    private void initRecyclerLayers() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                RecyclerView.VERTICAL, true);
        linearLayoutManager.setStackFromEnd(true);

        //Custom Height so the remove item animation works on every device screen
        ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
        DisplayUtils displayUtils = new DisplayUtils(params,
                getContext());
        recyclerView.setLayoutParams(displayUtils.getRecyclerViewHeight());
        recyclerView.setLayoutManager(linearLayoutManager);

        //Set No Device Connected text based on height
        int textHeight = (int) (params.height / 2.1);
        LinearLayout.LayoutParams textL = (LinearLayout.LayoutParams) notConnectedText.getLayoutParams();
        textL.setMargins(0, textHeight, 0, 0);
        notConnectedText.setLayoutParams(textL);

        //Gains performance
        recyclerView.setHasFixedSize(true);
        Objects.requireNonNull(recyclerView.getItemAnimator())
                .setChangeDuration(0); //Removes onChange Animation
    }

    private void devicesListeners() {

        adapter = new RecyclerDeviceAdapter(HomeFragment.this, HomeFragment.this);
        recyclerView.setAdapter(adapter);



        collectionViewModel.getAllDevices().observe(getViewLifecycleOwner(),
                devicesList -> {
                    //Adds to adapter
                    adapter.submitList(devicesList);

                    //Validation tool
                    validationUtil = new ValidationUtil(devicesList, getContext());

                    //Hides text if not empty
                    if (devicesList.isEmpty()) {
                        notConnectedText.animate().alpha(1.0f).setStartDelay(200)
                                .setListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {
                                        notConnectedText.setVisibility(View.VISIBLE);
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
                        notConnectedText.setVisibility(View.GONE);
                    }
         });


        //Scroll to top on new item
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                recyclerView.smoothScrollToPosition(positionStart);
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

        // Replace the "Add" button's click listener.
        alertDialog
                .getButton(DialogInterface.BUTTON_POSITIVE)
                .setOnClickListener(view -> {
                    //Tests input
                    boolean test = validationUtil.testAllAdd(Objects.requireNonNull(devName.getText()).toString(),
                            Objects.requireNonNull(devIp.getText()).toString(),
                            Objects.requireNonNull(devPort.getText()).toString(),
                            textInputLayoutName,
                            textInputLayoutIP,
                            textInputLayoutPort);
                    if (!test) {
                        createDialog.shakeAnim(alertDialog);
                    } else {
                        // Add input to Database if there is input
                        // dismiss the dialog
                        Devices devices = new Devices(devName.getText().toString(),
                                devIp.getText().toString(),
                                devPort.getText().toString(),
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

        //BindTextViewInput
        BindingViews.DialogInputViews input = new BindingViews.DialogInputViews();
        ButterKnife.bind(input, dialogView);

        devName = input.inputEditTexts.get(0);
        devIp = input.inputEditTexts.get(1);
        devPort = input.inputEditTexts.get(2);

        //BindTextInputLayout
        BindingViews.DialogInputLayoutInput inputLayout = new BindingViews.DialogInputLayoutInput();
        ButterKnife.bind(inputLayout, dialogView);

        textInputLayoutName = inputLayout.layoutsEditText.get(0);
        textInputLayoutIP = inputLayout.layoutsEditText.get(1);
        textInputLayoutPort = inputLayout.layoutsEditText.get(2);
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

        devName.setText(adapter.getCurrentList().get(position).getDevice());
        devIp.setText(adapter.getCurrentList().get(position).getIp());
        devPort.setText(adapter.getCurrentList().get(position).getPort());

        // Replace the "Edit" button's click listener.
        AlertDialog alertDialog = createDialog.getDialogCreated();
        alertDialog
                .getButton(DialogInterface.BUTTON_POSITIVE)
                .setOnClickListener(view -> {
                    boolean test = validationUtil.testAllEdit(Objects.requireNonNull(devName.getText()).toString(),
                            Objects.requireNonNull(devIp.getText()).toString(),
                            Objects.requireNonNull(devPort.getText()).toString(),
                            position,
                            textInputLayoutName,
                            textInputLayoutIP,
                            textInputLayoutPort);
                    if (!test) {
                        createDialog.shakeAnim(alertDialog);
                    } else {
                        // Edit input to Database if there is input
                        // dismiss the dialog
                        Devices device = new Devices(devName.getText().toString(),
                                devIp.getText().toString(),
                                devPort.getText().toString(),
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

