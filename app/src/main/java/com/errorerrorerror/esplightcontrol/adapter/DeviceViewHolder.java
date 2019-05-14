package com.errorerrorerror.esplightcontrol.adapter;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.errorerrorerror.esplightcontrol.BR;
import com.errorerrorerror.esplightcontrol.model.device.Device;
import com.errorerrorerror.esplightcontrol.model.device_music.DeviceMusic;

public class DeviceViewHolder<T> extends RecyclerView.ViewHolder {

    public final ViewDataBinding binding;

    DeviceViewHolder(@NonNull ViewDataBinding itemView) {
        super(itemView.getRoot());
        binding = itemView;
    }

    public void bind(T device) {
        if(device instanceof DeviceMusic)
            binding.setVariable(BR.deviceMusic, device);
        else if(device instanceof Device)
            binding.setVariable(BR.device, device);
        binding.executePendingBindings();
    }
}
