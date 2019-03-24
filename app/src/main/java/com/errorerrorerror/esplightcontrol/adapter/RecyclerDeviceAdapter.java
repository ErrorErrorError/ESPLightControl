package com.errorerrorerror.esplightcontrol.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.errorerrorerror.esplightcontrol.databinding.RecyclerDevicesListBinding;
import com.errorerrorerror.esplightcontrol.interfaces.OnClickedDevice;
import com.errorerrorerror.esplightcontrol.interfaces.OnClickedSwitch;
import com.errorerrorerror.esplightcontrol.R;
import com.errorerrorerror.esplightcontrol.devices.Devices;
import com.tenclouds.swipeablerecyclerviewcell.swipereveal.interfaces.OnIconClickListener;
import com.tenclouds.swipeablerecyclerviewcell.swipereveal.interfaces.OnSwipeListener;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
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


    private LayoutInflater layoutInflater;
    private final OnClickedDevice onClickedDevice;
    private final OnClickedSwitch onClickedSwitch;

    public RecyclerDeviceAdapter(OnClickedDevice onClickedDevice, OnClickedSwitch onClickedSwitch) {
        super(DIFF_CALLBACK);
        this.onClickedDevice = onClickedDevice;
        this.onClickedSwitch = onClickedSwitch;
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public DevicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(layoutInflater == null)
        {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        final RecyclerDevicesListBinding binding =
                DataBindingUtil.inflate(layoutInflater,R.layout.recycler_devices_list, parent, false);

        return new DevicesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DevicesViewHolder holder, int position) {

        Devices devices = getItem(position);
        holder.bind(devices);

        holder.binding.connectionSwitch.setCheckedImmediately(devices.isOn());

        holder.binding.connectionSwitch.setOnStateChangeListener((process, state, jtb) ->
                onClickedSwitch.OnSwitched(jtb.isChecked(), holder.getAdapterPosition(), jtb));


        holder.binding.swipeLayout.setOnIconClickListener(new OnIconClickListener() {
            @Override
            public void onLeftIconClick() {
                onClickedDevice.onEditDeviceClicked(holder.getAdapterPosition()); //Sends position to HomeFragment
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
