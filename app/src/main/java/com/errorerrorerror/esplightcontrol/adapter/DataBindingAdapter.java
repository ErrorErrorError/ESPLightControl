package com.errorerrorerror.esplightcontrol.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import java.util.List;

public abstract class DataBindingAdapter<T> extends ListAdapter<T, DeviceViewHolder<T>> implements BindableAdapter<T> {

    DataBindingAdapter(@NonNull DiffUtil.ItemCallback<T> diffCallback) {
        super(diffCallback);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder<T> holder, int position) {
        holder.bind(getItem(position));
    }

    @Override
    public void setData(List<T> data) {
        submitList(data);
    }
}
