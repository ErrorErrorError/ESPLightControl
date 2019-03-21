package com.errorerrorerror.esplightcontrol.views;


import android.widget.LinearLayout;
import android.widget.TextView;

import com.errorerrorerror.esplightcontrol.R;
import com.errorerrorerror.esplightcontrol.adapter.CustomViewPager;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.nightonke.jellytogglebutton.JellyToggleButton;
import com.tenclouds.swipeablerecyclerviewcell.swipereveal.SwipeRevealLayout;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.BindViews;

public class BindingViews {

    public static class DialogInputViews {
        @BindViews({R.id.deviceName, R.id.IPAddressInput, R.id.portInput})
        public List<TextInputEditText> inputEditTexts;
    }

    public static class DialogInputLayoutInput {
        @BindViews({R.id.deviceNameTextLayout, R.id.ipAddressTextLayout, R.id.portTextLayout})
        public List<TextInputLayout> layoutsEditText;
    }

    public static class HomeBindingsViews {

        @BindView(R.id.recyclerviewAddDevice)
        public RecyclerView recyclerView;
        @BindView(R.id.addDeviceButton)
        public MaterialCardView materialCardView;
        @BindView(R.id.noDeviceConnectedText)
        public TextView notConnectedText;
        @BindView(R.id.linearLayoutAdddevice)
        public LinearLayout linearLayoutAddDeviceBackgroud;
    }


    static class MainActivityViews {
        @BindView(R.id.viewPager)
        CustomViewPager viewPager;
        @BindView(R.id.customBubbleBar)
        CurvedBubbleNavigation curvedBubbleNavigation;
    }

    public static class ViewHolderViews {
        @BindView(R.id.deviceNameRecycler)
        public TextView deviceName;


        @BindView(R.id.ipAddressRecycler)
        public TextView ipInfo;

        //@BindView(R.id.portNumInputSum)
        //public TextView port;
        @BindView(R.id.portName)
        public TextView port;

        @BindView(R.id.connectionStatus)
        public TextView connectionStatus;
        @BindView(R.id.swipe_layout)
        public SwipeRevealLayout swipeRevealLayout;
        @BindView(R.id.connectionSwitch)
        public JellyToggleButton aSwitch;
    }
}
