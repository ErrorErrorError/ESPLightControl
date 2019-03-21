package com.errorerrorerror.esplightcontrol.adapter;

import android.view.View;
import android.widget.TextView;

import com.errorerrorerror.esplightcontrol.views.BindingViews;
import com.nightonke.jellytogglebutton.JellyToggleButton;
import com.tenclouds.swipeablerecyclerviewcell.swipereveal.SwipeRevealLayout;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.ButterKnife;

class DevicesViewHolder extends RecyclerView.ViewHolder {

    BindingViews.ViewHolderViews bindings = new BindingViews.ViewHolderViews();

    TextView deviceName;
    TextView ipInfo;
    TextView port;
    TextView connectionStatus;
    SwipeRevealLayout swipeRevealLayout;
    JellyToggleButton aSwitch;

    DevicesViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(bindings, itemView);
        deviceName = bindings.deviceName;
        ipInfo = bindings.ipInfo;
        port = bindings.port;
        connectionStatus = bindings.connectionStatus;
        swipeRevealLayout = bindings.swipeRevealLayout;
        aSwitch = bindings.aSwitch;
    }

}