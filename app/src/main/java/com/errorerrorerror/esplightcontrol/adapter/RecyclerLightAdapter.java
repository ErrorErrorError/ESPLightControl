package com.errorerrorerror.esplightcontrol.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.errorerrorerror.esplightcontrol.databinding.LightRecyclerviewBinding;
import com.errorerrorerror.esplightcontrol.devices.Devices;
import com.errorerrorerror.esplightcontrol.views.LightFragment;

import java.util.List;

public class RecyclerLightAdapter extends ListAdapter<Devices, DeviceViewHolder> implements BindableAdapter{

    private static final DiffUtil.ItemCallback<Devices> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Devices>() {
                @Override
                public boolean areItemsTheSame(
                        @NonNull Devices oldItem, @NonNull Devices newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull Devices oldItem,
                                                  @NonNull Devices newItem) {
                    return oldItem.equals(newItem);
                }
            };
    private LayoutInflater layoutInflater;
    private LightFragment view;
    public RecyclerLightAdapter(LightFragment view) {
        super(DIFF_CALLBACK);
        this.view = view;
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        final LightRecyclerviewBinding binding =
                LightRecyclerviewBinding.inflate(layoutInflater, parent, false);
        binding.setLightView(view);

        return new DeviceViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public void setData(List<Devices> data) {
        submitList(data);
    }
}
