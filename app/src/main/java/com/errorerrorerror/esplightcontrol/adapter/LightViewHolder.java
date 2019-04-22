package com.errorerrorerror.esplightcontrol.adapter;


import androidx.recyclerview.widget.RecyclerView;

import com.errorerrorerror.esplightcontrol.databinding.LightRecyclerviewBinding;
import com.errorerrorerror.esplightcontrol.devices.Devices;

public class LightViewHolder extends RecyclerView.ViewHolder {

    final LightRecyclerviewBinding binding;

    public LightViewHolder(final LightRecyclerviewBinding light) {
        super(light.getRoot());
        this.binding = light;
    }

    public void bind(Devices devices){
        binding.setDevice(devices);
        binding.executePendingBindings();
    }
}
