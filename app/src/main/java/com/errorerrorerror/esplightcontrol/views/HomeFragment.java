package com.errorerrorerror.esplightcontrol.views;

import android.animation.Animator;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.errorerrorerror.esplightcontrol.EspApp;
import com.errorerrorerror.esplightcontrol.R;
import com.errorerrorerror.esplightcontrol.adapter.RecyclerDeviceAdapter;
import com.errorerrorerror.esplightcontrol.databinding.HomeFragmentBinding;
import com.errorerrorerror.esplightcontrol.interfaces.OnClickedDevice;
import com.errorerrorerror.esplightcontrol.interfaces.OnClickedSwitch;
import com.errorerrorerror.esplightcontrol.utils.DisplayUtils;
import com.errorerrorerror.esplightcontrol.viewmodel.DevicesCollectionViewModel;
import com.jakewharton.rxbinding3.view.RxView;
import com.nightonke.jellytogglebutton.JellyToggleButton;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.disposables.CompositeDisposable;


public class HomeFragment extends Fragment implements OnClickedDevice, OnClickedSwitch {
    private static final String TAG = "HomeFragment";

    //ViewModel Injector
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private DevicesCollectionViewModel collectionViewModel;

    private static final int ADD_DEVICE = -2;
    //Utils
    private RecyclerDeviceAdapter adapter;

    private HomeFragmentBinding homeBinding;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Implements ViewModel to HomeFragment
        collectionViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(DevicesCollectionViewModel.class);

        //Listens for devices
        devicesListeners();

        compositeDisposable.add(RxView.clicks(homeBinding.addDeviceButton)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(unit -> addDeviceDialog(), error -> Log.e(TAG, "addDialog: "+ error.toString())));
    }

    private void addDeviceDialog()
    {
        FragmentTransaction ft = checkDialog();

        androidx.fragment.app.DialogFragment newFragment = DialogFragment.newInstance("Add Device","Add a device name, ip, and the port.",
                "Cancel",
                "Add",
                ADD_DEVICE);
        newFragment.show(ft, "dialog");
    }

    private FragmentTransaction checkDialog()
    {
        assert getFragmentManager() != null;
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        return ft;
    }

    @Override
    public void onEditDeviceClicked(long id) {
        FragmentTransaction ft = checkDialog();

        androidx.fragment.app.DialogFragment newFragment = DialogFragment.newInstance("Edit Device","Edit the device",
                "Cancel",
                "Edit",
                id);
        newFragment.show(ft, "dialog");

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Changes Vector drawable to png if less than 24api. Vector uses gradient and is only supported 24 =< x
        setAddDeviceBackground();

        //sets up RecyclerView Listeners
        initRecyclerLayers();
    }

    //if lower than 24 api, uses png
    private void setAddDeviceBackground() {
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

    @Override
    public void onRemoveDeviceClicked(int position) {
        if (position < 0) {
            return;
        }

        collectionViewModel.deleteDevice(adapter.getCurrentList().get(position));
    }

    @Override
    public void OnSwitched(Boolean bool, int position, JellyToggleButton buttonView) {
        if (position < 0) {
            return;
        }
        collectionViewModel.setSwitchConnection(bool, adapter.getCurrentList().get(position).getId());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        homeBinding.unbind();
        compositeDisposable.dispose();
        Log.d(TAG, "composite disposed: " + compositeDisposable.isDisposed());
    }
}

