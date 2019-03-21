package com.errorerrorerror.esplightcontrol.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.errorerrorerror.esplightcontrol.Interface.OnClickedDevice;
import com.errorerrorerror.esplightcontrol.Interface.OnClickedSwitch;
import com.errorerrorerror.esplightcontrol.R;
import com.errorerrorerror.esplightcontrol.devices.Devices;
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

    public RecyclerDeviceAdapter(OnClickedDevice onClickedDevice, OnClickedSwitch onClickedSwitch) {
        super(DIFF_CALLBACK);
        this.onClickedDevice = onClickedDevice;
        this.onClickedSwitch = onClickedSwitch;
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public DevicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_devices_list, parent, false);
        return new DevicesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DevicesViewHolder holder, int position) {

        Devices devices = getItem(position);
        holder.deviceName.setText(devices.getDevice());
        holder.ipInfo.setText(devices.getIp());
        holder.port.setText(String.format("Port: %s", devices.getPort()));
        holder.connectionStatus.setText(devices.getConnectivity());
        holder.aSwitch.setCheckedImmediately(devices.isOn());
        holder.itemView.setTag(devices.getId());

        holder.aSwitch.setOnStateChangeListener((process, state, jtb) ->
                onClickedSwitch.OnSwitched(jtb.isChecked(), holder.getAdapterPosition(), jtb));

        holder.swipeRevealLayout.setOnIconClickListener(new OnIconClickListener() {
            @Override
            public void onLeftIconClick() {
                onClickedDevice.onEditDeviceClicked(holder.getAdapterPosition()); //Sends position to HomeFragment
                holder.swipeRevealLayout.close(true);
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
