package com.errorerrorerror.esplightcontrol.views;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.errorerrorerror.esplightcontrol.EspApp;
import com.errorerrorerror.esplightcontrol.adapter.ListModesAdapter;
import com.errorerrorerror.esplightcontrol.base.BaseRoundedBottomSheetDialogFragment;
import com.errorerrorerror.esplightcontrol.databinding.MusicModeBinding;
import com.errorerrorerror.esplightcontrol.model.device.Device;
import com.errorerrorerror.esplightcontrol.model.device_music.DeviceMusic;
import com.errorerrorerror.esplightcontrol.viewmodel.DevicesCollectionViewModel;
import com.jakewharton.rxbinding3.view.RxView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MusicBottomSheetDialogFragment extends BaseRoundedBottomSheetDialogFragment {

    @Inject
    ViewModelProvider.Factory devicesViewModelFactory;


    private DevicesCollectionViewModel viewModel;
    private MusicModeBinding binding;

    private List<Device> temp = new ArrayList<>();

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
        viewModel = ViewModelProviders.of(this, devicesViewModelFactory).get(DevicesCollectionViewModel.class);
        binding = MusicModeBinding.inflate(inflater, container, false);
        binding.setMusicView(this);
        binding.setViewModel(viewModel);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerViews();
        viewModel.addDisposable(RxView.clicks(binding.addMusicDeviceButton).subscribe(v -> {
            for (int i = 0; i < temp.size(); i++) {
                viewModel.addDisposable(
                        viewModel.insertUpdateDeviceMusic(
                                new DeviceMusic(temp.get(i),
                                        1,
                                        100,
                                        500,
                                        3))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(() -> Toast.makeText(getContext(), "Added device successfully", Toast.LENGTH_LONG).show(), err -> Log.e("ModesFragment", "toast: ", err))
                );

                viewModel.addDisposable(
                        viewModel.deleteDevice(temp.get(i)).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe()
                );
            }
            dismiss();
        }));
    }

    private void initRecyclerViews() {
        binding.musicSelectDeviceRecycler.setNestedScrollingEnabled(true);
        setCancelable(false);
        binding.musicSelectDeviceRecycler.setAdapter(new ListModesAdapter(this));
    }


    public void toast(Device device) {
        if (temp.contains(device)) {
            temp.remove(device);
        } else {
            temp.add(device);
        }
    }
}