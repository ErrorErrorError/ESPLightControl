package com.errorerrorerror.esplightcontrol.views;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
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
import com.errorerrorerror.esplightcontrol.utils.NetworkUtils;
import com.errorerrorerror.esplightcontrol.viewmodel.DevicesCollectionViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.jakewharton.rxbinding3.view.RxView;
import com.tenclouds.swipeablerecyclerviewcell.swipereveal.SwipeRevealLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    //ViewModel Injector
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private DevicesCollectionViewModel collectionViewModel;

    //Utils
    private DevicesFragmentBinding binding;
    private List<NetworkUtils> sockets = new ArrayList<>();
    private AllDevicesRecyclerAdapter adapter;

    private void onClick(DialogInterface dialog) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = sharedPref.edit();

        int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
        Log.d(TAG, "onClick: " + selectedPosition);

        switch (selectedPosition) {
            case 0:
                editor.putInt("mode", AppCompatDelegate.MODE_NIGHT_NO);
                editor.apply();
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                dialog.dismiss();
                break;
            case 1:
                editor.putInt("mode", AppCompatDelegate.MODE_NIGHT_YES);
                editor.apply();
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                dialog.dismiss();
                break;
            case 2:
                editor.putInt("mode", AppCompatDelegate.MODE_NIGHT_AUTO_TIME);
                editor.apply();
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_TIME);
                dialog.dismiss();
                break;
            case 3:
                editor.putInt("mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                editor.apply();
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                dialog.dismiss();
                break;
        }
    }

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

    @SuppressLint("CheckResult")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Add device button click listener
        collectionViewModel.addDisposable(RxView.clicks(binding.addDeviceButton)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(unit -> addDeviceDialog(),
                        error -> Log.e(Constants.HOME_TAG, "addDialog: " + error.toString())));


        CharSequence[] mTestArray = {"Day", "Night", "Automatic", "Follow System"};
        collectionViewModel.addDisposable(
                RxView.longClicks(binding.addDeviceButton)
                        .throttleFirst(3000, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                        .doOnNext(unit -> new MaterialAlertDialogBuilder(Objects.requireNonNull(getContext()))
                                .setTitle("Select Theme")
                                .setSingleChoiceItems(mTestArray, 0, null)
                                .setPositiveButton("Set", (dialog1, whichButton) -> HomeFragment.this.onClick(dialog1))
                                .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel())
                                .show())
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

    @SuppressLint("CheckResult")
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
        adapter = new AllDevicesRecyclerAdapter(this);
        binding.recyclerviewAddDevice.setAdapter(adapter);
        binding.recyclerviewAddDevice.setHasFixedSize(true);
        //Objects.requireNonNull(binding.recyclerviewAddDevice.getItemAnimator()).setChangeDuration(0);
    }

    //This method gets called when the delete button is clicked
    public void removeDevice(Device device) {
        Runnable t = () -> {
            for (int i = 0; i < sockets.size(); i++) {
                if (device.getId() == sockets.get(i).getId()) {
                    sockets.remove(sockets.get(i));
                    break;
                }
            }
        };
        t.run();
        collectionViewModel.addDisposable(collectionViewModel.deleteDevice(device)
                .observeOn(AndroidSchedulers.mainThread())
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

