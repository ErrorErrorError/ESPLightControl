package com.errorerrorerror.esplightcontrol.adapter;

import androidx.recyclerview.widget.RecyclerView;

import com.errorerrorerror.esplightcontrol.databinding.RecyclerDevicesListBinding;
import com.errorerrorerror.esplightcontrol.devices.Devices;


class DevicesViewHolder extends RecyclerView.ViewHolder {

    final RecyclerDevicesListBinding binding;

    DevicesViewHolder(final RecyclerDevicesListBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(Devices devices) {
        binding.setDevice(devices);
        binding.executePendingBindings();
    }
}