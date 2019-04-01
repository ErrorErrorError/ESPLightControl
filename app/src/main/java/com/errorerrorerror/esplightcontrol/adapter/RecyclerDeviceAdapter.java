package com.errorerrorerror.esplightcontrol.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.errorerrorerror.esplightcontrol.databinding.RecyclerDevicesListBinding;
import com.errorerrorerror.esplightcontrol.devices.Devices;
import com.errorerrorerror.esplightcontrol.interfaces.OnClickedDevice;
import com.errorerrorerror.esplightcontrol.interfaces.OnClickedSwitch;
import com.tenclouds.swipeablerecyclerviewcell.swipereveal.interfaces.OnIconClickListener;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import static com.tenclouds.swipeablerecyclerviewcell.metaball.MetaBallsKt.RIGHT_VIEW_TO_DELETE;

public class RecyclerDeviceAdapter extends ListAdapter<Devices, DevicesViewHolder> {

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

    private final OnClickedDevice onClickedDevice;
    private final OnClickedSwitch onClickedSwitch;
    private LayoutInflater layoutInflater;

    public RecyclerDeviceAdapter(OnClickedDevice onClickedDevice, OnClickedSwitch onClickedSwitch) {
        super(DIFF_CALLBACK);
        this.onClickedDevice = onClickedDevice;
        this.onClickedSwitch = onClickedSwitch;
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public DevicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        final RecyclerDevicesListBinding binding =
                RecyclerDevicesListBinding.inflate(layoutInflater, parent, false);

        return new DevicesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DevicesViewHolder holder, int position) {

        Devices device = getItem(position);
        holder.bind(device);


        holder.binding.connectionSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> onClickedSwitch.OnSwitched(isChecked, holder.getAdapterPosition(), null));
        
        /*holder.binding.connectionSwitch.setOnStateChangeListener((progress, state, jtb) ->
                onClickedSwitch.OnSwitched(jtb.isChecked(), holder.getAdapterPosition(), jtb));
        */
        holder.binding.swipeLayout.setOnIconClickListener(new OnIconClickListener() {
            @Override
            public void onLeftIconClick() {
                onClickedDevice.onEditDeviceClicked(device.getId());//Sends id of device to HomeFragment
                holder.binding.swipeLayout.close(true);
            }

            @Override
            public void onRightIconClick() {
                onClickedDevice.onRemoveDeviceClicked(holder.getAdapterPosition());
            }
        }, RIGHT_VIEW_TO_DELETE);

    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    protected Devices getItem(int position) {
        return getCurrentList().get(position);
    }
}
