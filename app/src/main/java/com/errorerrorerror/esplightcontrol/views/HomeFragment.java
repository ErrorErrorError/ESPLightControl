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
import com.errorerrorerror.esplightcontrol.adapter.AllDevicesRecyclerAdapter;
import com.errorerrorerror.esplightcontrol.databinding.DevicesFragmentBinding;
import com.errorerrorerror.esplightcontrol.model.device.Device;
import com.errorerrorerror.esplightcontrol.utils.Constants;
import com.errorerrorerror.esplightcontrol.viewmodel.DevicesCollectionViewModel;
import com.jakewharton.rxbinding3.view.RxView;
import com.tenclouds.swipeablerecyclerviewcell.swipereveal.SwipeRevealLayout;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import kotlin.Unit;


public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    //ViewModel Injector
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private DevicesCollectionViewModel collectionViewModel;

    //Utils
    private DevicesFragmentBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((EspApp) Objects.requireNonNull(getActivity()).getApplication())
                .getAppComponent()
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
        binding.setVersion(Build.VERSION.SDK_INT);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Add device button click listener
        collectionViewModel.addDisposable(RxView.clicks(binding.addDeviceButton)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(unit -> addDeviceDialog(),
                        error -> Log.e(Constants.HOME_TAG, "addDialog: " + error.toString())));

        collectionViewModel.addDisposable(
                RxView.longClicks(binding.addDeviceButton)
                .throttleFirst(2000, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Unit>() {
                    @Override
                    public void accept(Unit unit) throws Exception {

                    }
                })
                .subscribe()
        );
    }

    //This changes switch from room, is implemented in recycler_devices_list.xml
    public void setSwitch(Boolean bool, Device device) {
        collectionViewModel.addDisposable(
                collectionViewModel.setSwitch(bool, device)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                        }, onError -> Log.e(TAG, "onActivityCreated: ", onError)));
    }

    private void addDeviceDialog() {
        FragmentTransaction ft = checkDialog();

        androidx.fragment.app.DialogFragment newFragment = DialogFragment.newInstance(
                "Add device",
                "Add",
                Constants.ADD_DEVICE);
        newFragment.show(ft, "dialog");
    }

    @NonNull
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

        //sets up RecyclerView Listeners
        initRecyclerLayers();

        Objects.requireNonNull(binding.recyclerviewAddDevice.getAdapter()).registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                binding.recyclerviewAddDevice.smoothScrollToPosition(positionStart);
            }
        });

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initRecyclerLayers() {
        binding.recyclerviewAddDevice.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerviewAddDevice.setAdapter(new AllDevicesRecyclerAdapter(this));
        binding.recyclerviewAddDevice.setHasFixedSize(true);
        Objects.requireNonNull(binding.recyclerviewAddDevice.getItemAnimator()).setChangeDuration(0);
    }

    //This method gets called when the delete button is clicked
    public void removeDevice(Device device) {
        Log.d(TAG, "removeDevice: " + device);
        collectionViewModel.addDisposable(collectionViewModel.deleteDevice(device)
                .subscribeOn(Schedulers.io())
                .subscribe(
                        () -> {
                        }, onError -> Log.e(TAG, "removeDevice: ", onError)));

    }

    /*
    This method gets called when the edit button is clicked, which passes the id of the device
     */
    public void editDevice(@NonNull SwipeRevealLayout swiper, long id) {
        FragmentTransaction ft = checkDialog();

        androidx.fragment.app.DialogFragment newFragment = DialogFragment.newInstance(
                "Edit device",
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

