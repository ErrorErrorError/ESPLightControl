package com.errorerrorerror.esplightcontrol.viewmodel;

import com.errorerrorerror.esplightcontrol.devices.DevicesDataSource;
import com.errorerrorerror.esplightcontrol.di.DevicesScope;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
@DevicesScope
public class DevicesViewModelFactory implements ViewModelProvider.Factory {



    private DevicesDataSource devicesDataSource;

    public DevicesViewModelFactory(DevicesDataSource devicesDataSource) {
        this.devicesDataSource = devicesDataSource;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(DevicesCollectionViewModel.class))
            return (T) new DevicesCollectionViewModel(devicesDataSource);
        else if (modelClass.isAssignableFrom(DevicesConnectivityViewModel.class))
            return (T) new DevicesConnectivityViewModel(devicesDataSource);
        else {
            throw new IllegalArgumentException("ViewModel Not Found");
        }
    }
}
