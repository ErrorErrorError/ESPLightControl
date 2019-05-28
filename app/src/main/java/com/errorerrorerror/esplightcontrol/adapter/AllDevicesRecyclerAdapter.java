package com.errorerrorerror.esplightcontrol.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DiffUtil;

import com.errorerrorerror.esplightcontrol.databinding.LightRecyclerviewBinding;
import com.errorerrorerror.esplightcontrol.databinding.RecyclerDevicesListBinding;
import com.errorerrorerror.esplightcontrol.model.device.Device;
import com.errorerrorerror.esplightcontrol.views.HomeFragment;
import com.errorerrorerror.esplightcontrol.views.LightFragment;

public class AllDevicesRecyclerAdapter extends DataBindingAdapter<Device> {

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
                    return oldItem.equals(newItem) && oldItem.isOn().equals(newItem.isOn());
                }
            };
    private LayoutInflater layoutInflater;
    private Fragment view;

    public AllDevicesRecyclerAdapter(Fragment view) {
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

        final ViewDataBinding binding;
        if (view instanceof LightFragment) {
            binding = LightRecyclerviewBinding.inflate(layoutInflater, parent, false);
            ((LightRecyclerviewBinding) binding).setView((LightFragment) view);
        } else if (view instanceof HomeFragment) {
            binding =
                    RecyclerDevicesListBinding.inflate(layoutInflater, parent, false);
            ((RecyclerDevicesListBinding) binding).setView((HomeFragment) view);
        } else {
            binding = null;
        }

        assert binding != null;
        return new DeviceViewHolder<>(binding);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }
}
