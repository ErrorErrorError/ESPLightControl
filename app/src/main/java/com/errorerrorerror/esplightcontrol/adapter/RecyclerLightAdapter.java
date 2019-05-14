package com.errorerrorerror.esplightcontrol.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.errorerrorerror.esplightcontrol.databinding.LightRecyclerviewBinding;
import com.errorerrorerror.esplightcontrol.model.device.Device;
import com.errorerrorerror.esplightcontrol.views.LightFragment;

public class RecyclerLightAdapter extends DataBindingAdapter<Device>{

    private static final DiffUtil.ItemCallback<Device> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Device>() {
                @Override
                public boolean areItemsTheSame(
                        @NonNull Device oldItem, @NonNull Device newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull Device oldItem,
                                                  @NonNull Device newItem) {
                    return oldItem.equals(newItem) && oldItem.getOn().equals(newItem.getOn());
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
    public DeviceViewHolder<Device> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        final LightRecyclerviewBinding binding =
                LightRecyclerviewBinding.inflate(layoutInflater, parent, false);
        binding.setLightView(view);

        return new DeviceViewHolder<>(binding);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }
}
