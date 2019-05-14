package com.errorerrorerror.esplightcontrol.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.errorerrorerror.esplightcontrol.model.DevicesDataSource;
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
        else {
            throw new IllegalArgumentException("ViewModel Not Found");
        }
    }
}
