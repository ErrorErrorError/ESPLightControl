package com.errorerrorerror.esplightcontrol.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.errorerrorerror.esplightcontrol.model.MainRepository;

import javax.inject.Inject;

public class DevicesViewModelFactory implements ViewModelProvider.Factory {

    private MainRepository devicesDataSource;

    @Inject
    public DevicesViewModelFactory(MainRepository devicesDataSource) {
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
