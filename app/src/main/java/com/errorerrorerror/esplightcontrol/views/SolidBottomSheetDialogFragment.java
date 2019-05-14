package com.errorerrorerror.esplightcontrol.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.errorerrorerror.esplightcontrol.base.BaseRoundedBottomSheetDialogFragment;
import com.errorerrorerror.esplightcontrol.databinding.SolidModeBinding;
import com.errorerrorerror.esplightcontrol.model.device.Device;

public class SolidBottomSheetDialogFragment extends BaseRoundedBottomSheetDialogFragment {

    private SolidModeBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = SolidModeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void toast(Device device){
        Toast.makeText(getContext(), "Solid Test: " + device.getDeviceName(), Toast.LENGTH_LONG).show();
    }

}
