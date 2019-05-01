package com.errorerrorerror.esplightcontrol.views;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.errorerrorerror.esplightcontrol.EspApp;
import com.errorerrorerror.esplightcontrol.R;
import com.errorerrorerror.esplightcontrol.adapter.RecyclerDeviceAdapter;
import com.errorerrorerror.esplightcontrol.databinding.DevicesFragmentBinding;
import com.errorerrorerror.esplightcontrol.devices.Devices;
import com.errorerrorerror.esplightcontrol.utils.Constants;
import com.errorerrorerror.esplightcontrol.viewmodel.DevicesCollectionViewModel;
import com.jakewharton.rxbinding3.view.RxView;
import com.tenclouds.swipeablerecyclerviewcell.swipereveal.SwipeRevealLayout;
import com.trello.rxlifecycle3.components.support.RxFragment;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class HomeFragment extends RxFragment {

    private static final String TAG = "HomeFragment";
    //ViewModel Injector
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private DevicesCollectionViewModel collectionViewModel;

    //Utils
    private RecyclerDeviceAdapter adapter;
    private DevicesFragmentBinding binding;

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
        binding = DevicesFragmentBinding.inflate(inflater, container, false);
        binding.setViewModel(collectionViewModel);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Add device button click listener
        collectionViewModel.addDisposable(RxView.clicks(binding.addDeviceButton)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .compose(bindToLifecycle())
                .subscribe(unit -> addDeviceDialog(),
                        error -> Log.e(Constants.HOME_TAG, "addDialog: " + error.toString())));

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                if (positionStart > 0) {
                    binding.recyclerviewAddDevice.smoothScrollToPosition(positionStart);
                }
            }
        });
    }

    //This changes switch from room, is implemented in recycler_devices_list.xml
    public void setSwitch(boolean bool, long id) {
        collectionViewModel.addDisposable(
                collectionViewModel.setSwitch(bool, id)
                        .compose(bindToLifecycle())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                        }, onError -> Log.e(TAG, "onActivityCreated: ", onError)));
    }

    private void addDeviceDialog() {
        FragmentTransaction ft = checkDialog();

        androidx.fragment.app.DialogFragment newFragment = DialogFragment.newInstance(
                "Add device",
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
            binding.linearLayoutAdddevice.setBackgroundResource(R.drawable.cardview_background_gradient);
        } else {
            binding.linearLayoutAdddevice.setBackgroundResource(R.drawable.ic_cardview_background_gradient);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initRecyclerLayers() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                RecyclerView.VERTICAL, true);
        linearLayoutManager.setStackFromEnd(true);
        binding.recyclerviewAddDevice.setLayoutManager(linearLayoutManager);

        //Gains performance
        binding.recyclerviewAddDevice.setHasFixedSize(true);

        //Removes onChange Animation
        Objects.requireNonNull(binding.recyclerviewAddDevice.getItemAnimator())
                .setChangeDuration(0);

        adapter = new RecyclerDeviceAdapter(this);
        binding.recyclerviewAddDevice.setAdapter(adapter);

    }

    //This method gets called when the delete button is clicked
    public void removeDevice(Devices device) {
        collectionViewModel.addDisposable(collectionViewModel.deleteDevice(device)
                .compose(bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        () -> {
                        }, onError -> Log.e(TAG, "removeDevice: ", onError)));
    }

    /*
    This method gets called when the edit button is clicked, which passes the id of the device
     */
    public void editDevice(SwipeRevealLayout swiper, long id) {
        FragmentTransaction ft = checkDialog();

        androidx.fragment.app.DialogFragment newFragment = DialogFragment.newInstance(
                "Edit device",
                "Cancel",
                "Edit",
                id);

        newFragment.show(ft, "dialog");

        swiper.close(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.unbind();
    }
}

