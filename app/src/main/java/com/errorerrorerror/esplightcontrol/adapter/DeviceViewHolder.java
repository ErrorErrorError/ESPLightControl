package com.errorerrorerror.esplightcontrol.adapter;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.errorerrorerror.esplightcontrol.BR;
import com.errorerrorerror.esplightcontrol.devices.Devices;

public class DeviceViewHolder extends RecyclerView.ViewHolder {

    public final ViewDataBinding binding;

    DeviceViewHolder(@NonNull ViewDataBinding itemView) {
        super(itemView.getRoot());
        binding = itemView;
    }

    public void bind(Devices devices) {
        binding.setVariable(BR.device, devices);
        binding.executePendingBindings();
    }
}
