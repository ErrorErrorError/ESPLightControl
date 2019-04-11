package com.errorerrorerror.esplightcontrol.views;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.errorerrorerror.esplightcontrol.EspApp;
import com.errorerrorerror.esplightcontrol.R;
import com.errorerrorerror.esplightcontrol.adapter.RecyclerDeviceAdapter;
import com.errorerrorerror.esplightcontrol.databinding.DevicesFragmentBinding;
import com.errorerrorerror.esplightcontrol.interfaces.OnClickedDevice;
import com.errorerrorerror.esplightcontrol.interfaces.OnClickedSwitch;
import com.errorerrorerror.esplightcontrol.utils.Constants;
import com.errorerrorerror.esplightcontrol.viewmodel.DevicesCollectionViewModel;
import com.jakewharton.rxbinding3.view.RxView;
import com.nightonke.jellytogglebutton.JellyToggleButton;
import com.trello.rxlifecycle3.components.support.RxFragment;

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
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class HomeFragment extends RxFragment implements OnClickedDevice, OnClickedSwitch {

    //ViewModel Injector
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private DevicesCollectionViewModel collectionViewModel;

    //Utils
    private RecyclerDeviceAdapter adapter;
    private DevicesFragmentBinding homeBinding;

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

        //Implements ViewModel to HomeFragment
        collectionViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(DevicesCollectionViewModel.class);

        //Inflates view and databinding
        homeBinding = DevicesFragmentBinding.inflate(inflater, container, false);

        return homeBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Listens for devices
        devicesListeners();

        collectionViewModel.addDisposable(RxView.clicks(homeBinding.addDeviceButton)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .compose(bindToLifecycle())
                .subscribe(unit -> addDeviceDialog(), error -> Log.e(Constants.HOME_TAG, "addDialog: " + error.toString())));


    }

    private void addDeviceDialog() {
        FragmentTransaction ft = checkDialog();

        androidx.fragment.app.DialogFragment newFragment = DialogFragment.newInstance("Add Device", "Add a device name, ip, and the port.",
                "Cancel",
                "Add",
                Constants.ADD_DEVICE);
        newFragment.show(ft, "dialog");
    }

    private FragmentTransaction checkDialog() {
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

        androidx.fragment.app.DialogFragment newFragment = DialogFragment.newInstance("Edit Device", "Change Device Settings",
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
            homeBinding.linearLayoutAdddevice.setBackgroundResource(R.drawable.cardview_background_gradient);
        } else {
            homeBinding.linearLayoutAdddevice.setBackgroundResource(R.drawable.ic_cardview_background_gradient);
        }
    }

    private void initRecyclerLayers() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                RecyclerView.VERTICAL, true);
        linearLayoutManager.setStackFromEnd(true);
        homeBinding.recyclerviewAddDevice.setLayoutManager(linearLayoutManager);
        homeBinding.noDeviceConnectedText.setVisibility(View.GONE);
        //Custom Height so the remove item animation works on every device screen
      // ViewGroup.LayoutParams params = homeBinding.recyclerviewAddDevice.getLayoutParams();
       // DisplayUtils displayUtils = new DisplayUtils(params,
       //         getContext());
        //homeBinding.recyclerviewAddDevice.setLayoutParams(displayUtils.getRecyclerViewHeight());
        //homeBinding.recyclerviewAddDevice.setLayoutManager(linearLayoutManager);

        //Set No Device Connected text based on height
        //int textHeight = (int) (params.height / 2.1);
        //LinearLayout.LayoutParams textL = (LinearLayout.LayoutParams) homeBinding.noDeviceConnectedText.getLayoutParams();
        //textL.setMargins(0, textHeight, 0, 0);
        //homeBinding.noDeviceConnectedText.setLayoutParams(textL);

        //Gains performance
        homeBinding.recyclerviewAddDevice.setHasFixedSize(true);

        //Removes onChange Animation
        Objects.requireNonNull(homeBinding.recyclerviewAddDevice.getItemAnimator())
                .setChangeDuration(0);
    }

    private void devicesListeners() {

        adapter = new RecyclerDeviceAdapter(HomeFragment.this, HomeFragment.this);
        homeBinding.recyclerviewAddDevice.setAdapter(adapter);

        collectionViewModel.addDisposable(collectionViewModel.getAllDevices()
                .compose(bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        devices -> {
                            adapter.submitList(devices);
                            Log.d(Constants.HOME_TAG, "devicesListeners: " + Thread.currentThread().getName());

                            if (devices.isEmpty()) {
                               // homeBinding.noDeviceConnectedText.setVisibility(View.VISIBLE);
                            } else {
                                //homeBinding.noDeviceConnectedText.setVisibility(View.GONE);
                            }

                        },
                        onError -> Log.e(Constants.HOME_TAG, "devicesListeners: " + onError)
                )
        );

        //Scroll to top on new item
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                if (positionStart > 0) {
                    Log.d(Constants.HOME_TAG, "onItemRangeInserted: " + positionStart);
                    homeBinding.recyclerviewAddDevice.smoothScrollToPosition(positionStart);
                }

            }
        });
    }

    @Override
    public void onRemoveDeviceClicked(int position) {
        if (position < 0) {
            return;
        }

        collectionViewModel.addDisposable(collectionViewModel.deleteDevice(adapter.getCurrentList().get(position))
                .compose(bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .subscribe(() -> Log.d(Constants.HOME_TAG, "onRemoveDeviceClicked: on Thread " + Thread.currentThread().getName()),
                        onError -> Log.e(Constants.HOME_TAG, "onRemoveDeviceClicked: ", onError))
        );
    }

    @Override
    public void OnSwitched(Boolean bool, int position, JellyToggleButton buttonView) {
        if (position < 0) {
            return;
        }

        collectionViewModel.addDisposable(
                collectionViewModel.setSwitch(bool, adapter.getCurrentList().get(position).getId())
                .compose(bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        homeBinding.unbind();
    }
}

