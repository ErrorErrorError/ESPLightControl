package com.errorerrorerror.esplightcontrol.adapter;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.errorerrorerror.esplightcontrol.BR;

public class DeviceViewHolder<T> extends RecyclerView.ViewHolder {

    @NonNull
    public final ViewDataBinding binding;

    DeviceViewHolder(@NonNull ViewDataBinding view) {
        super(view.getRoot());
        binding = view;
    }

    public void bind(T device) {
        binding.setVariable(BR.device, device);
        binding.executePendingBindings();
    }
}
