package com.errorerrorerror.esplightcontrol.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.errorerrorerror.esplightcontrol.base.BaseRoundedBottomSheetDialogFragment;
import com.errorerrorerror.esplightcontrol.databinding.WavesModeBinding;
import com.errorerrorerror.esplightcontrol.model.device.Device;

public class WavesBottomSheetDialogFragment extends BaseRoundedBottomSheetDialogFragment {

    private WavesModeBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = WavesModeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void toast(Device device){
        Toast.makeText(getContext(), "Waves Test: " + device.getDeviceName(), Toast.LENGTH_LONG).show();
    }

}
